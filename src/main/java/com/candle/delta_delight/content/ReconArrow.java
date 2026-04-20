package com.candle.delta_delight.content;

import com.candle.delta_delight.registry.ModEntityTypes;
import com.candle.delta_delight.registry.ModItems;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class ReconArrow extends AbstractArrow {
    private static final double SCAN_RADIUS = 6.0D;
    private static final double SCAN_RADIUS_SQR = SCAN_RADIUS * SCAN_RADIUS;
    private static final int GLOWING_DURATION_TICKS = 20 * 15;
    private static final int EFFECT_REFRESH_THRESHOLD_TICKS = 20;
    private static final int MIN_TRAIL_PARTICLE_COUNT = 2;
    private static final int MAX_TRAIL_PARTICLE_COUNT = 4;
    private static final int MIN_FALLING_PARTICLE_COUNT = 1;
    private static final int MAX_FALLING_PARTICLE_COUNT = 3;
    private static final int MIN_TRAIL_PARTICLE_DELAY_TICKS = 1;
    private static final int MAX_TRAIL_PARTICLE_DELAY_TICKS = 2;
    private static final int MIN_FALLING_PARTICLE_DELAY_TICKS = 1;
    private static final int MAX_FALLING_PARTICLE_DELAY_TICKS = 4;
    private static final BlockParticleOption FALLING_REDSTONE_DUST = new BlockParticleOption(
            ParticleTypes.FALLING_DUST,
            Blocks.REDSTONE_BLOCK.defaultBlockState()
    );
    private int trailParticleDelay;
    private int fallingParticleDelay;

    public ReconArrow(EntityType<? extends ReconArrow> entityType, Level level) {
        super(entityType, level);
    }

    public ReconArrow(Level level, LivingEntity owner) {
        super(ModEntityTypes.RECON_ARROW.get(), owner, level);
    }

    public ReconArrow(Level level, double x, double y, double z) {
        super(ModEntityTypes.RECON_ARROW.get(), x, y, z, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            if (!inGround && !isRemoved()) {
                spawnRedstoneTrail();
            }
            return;
        }

        if (!inGround && !isRemoved()) {
            revealVisibleHostileMobsBelow();
        }
    }

    private void spawnRedstoneTrail() {
        if (trailParticleDelay-- <= 0) {
            spawnTrailParticles(randomBetween(MIN_TRAIL_PARTICLE_COUNT, MAX_TRAIL_PARTICLE_COUNT));
            trailParticleDelay = randomBetween(MIN_TRAIL_PARTICLE_DELAY_TICKS, MAX_TRAIL_PARTICLE_DELAY_TICKS);
        }

        if (fallingParticleDelay-- <= 0) {
            spawnFallingParticles(randomBetween(MIN_FALLING_PARTICLE_COUNT, MAX_FALLING_PARTICLE_COUNT));
            fallingParticleDelay = randomBetween(MIN_FALLING_PARTICLE_DELAY_TICKS, MAX_FALLING_PARTICLE_DELAY_TICKS);
        }
    }

    private void spawnTrailParticles(int count) {
        double dx = getX() - xo;
        double dy = getY() - yo;
        double dz = getZ() - zo;

        for (int i = 0; i < count; i++) {
            double progress = (i + 1.0D) / count;
            double particleX = getX() - dx * progress;
            double particleY = getY() - dy * progress;
            double particleZ = getZ() - dz * progress;

            level().addParticle(
                    DustParticleOptions.REDSTONE,
                    particleX,
                    particleY,
                    particleZ,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }

    private void spawnFallingParticles(int count) {
        for (int i = 0; i < count; i++) {
            level().addParticle(
                    FALLING_REDSTONE_DUST,
                    getX() + randomOffset(0.08D),
                    getY() + randomOffset(0.04D),
                    getZ() + randomOffset(0.08D),
                    randomOffset(0.01D),
                    -0.02D,
                    randomOffset(0.01D)
            );
        }
    }

    private double randomOffset(double range) {
        return (random.nextDouble() - 0.5D) * range;
    }

    private int randomBetween(int minInclusive, int maxInclusive) {
        return minInclusive + random.nextInt(maxInclusive - minInclusive + 1);
    }

    private void revealVisibleHostileMobsBelow() {
        Vec3 arrowPosition = position();
        AABB scanArea = new AABB(
                arrowPosition.x - SCAN_RADIUS,
                level().getMinBuildHeight(),
                arrowPosition.z - SCAN_RADIUS,
                arrowPosition.x + SCAN_RADIUS,
                arrowPosition.y,
                arrowPosition.z + SCAN_RADIUS
        );

        for (Mob mob : level().getEntitiesOfClass(Mob.class, scanArea, this::isValidTarget)) {
            if (isInsideHorizontalRadius(arrowPosition, mob) && hasLineOfSightTo(mob)) {
                refreshGlowing(mob);
            }
        }
    }

    private boolean isValidTarget(Mob mob) {
        return mob.isAlive() && mob.getType().getCategory() == MobCategory.MONSTER;
    }

    private boolean isInsideHorizontalRadius(Vec3 arrowPosition, Entity entity) {
        double dx = entity.getX() - arrowPosition.x;
        double dz = entity.getZ() - arrowPosition.z;
        return dx * dx + dz * dz <= SCAN_RADIUS_SQR;
    }

    private boolean hasLineOfSightTo(LivingEntity target) {
        HitResult hitResult = level().clip(new ClipContext(
                position(),
                target.getEyePosition(),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this
        ));
        return hitResult.getType() == HitResult.Type.MISS;
    }

    private void refreshGlowing(LivingEntity target) {
        MobEffectInstance existing = target.getEffect(MobEffects.GLOWING);
        if (existing != null && existing.getDuration() > EFFECT_REFRESH_THRESHOLD_TICKS) {
            return;
        }

        target.addEffect(new MobEffectInstance(
                MobEffects.GLOWING,
                GLOWING_DURATION_TICKS,
                0,
                false,
                false
        ));
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(ModItems.RECON_ARROW.get());
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
