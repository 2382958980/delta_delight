package com.candle.delta_delight.system;

import com.candle.delta_delight.cocktail.CocktailBrewer;
import com.candle.delta_delight.content.ShakerInventory;
import com.candle.delta_delight.registry.ModItems;
import com.candle.delta_delight.registry.ModSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public final class ShakerSystem {
    private static final int SHAKE_SOUND_INTERVAL_TICKS = 27;
    private static final double SHAKE_SOUND_STOP_RADIUS = 32.0D;
    private static final Map<UUID, ActiveShake> ACTIVE_SHAKES = new HashMap<>();

    private ShakerSystem() {
    }

    public static void setShaking(ServerPlayer player, boolean shaking) {
        if (shaking) {
            ItemStack stack = player.getMainHandItem();
            if (stack.is(ModItems.SHAKER.get()) && ShakerInventory.canShake(stack)) {
                if (!ACTIVE_SHAKES.containsKey(player.getUUID())) {
                    ACTIVE_SHAKES.put(player.getUUID(), new ActiveShake(InteractionHand.MAIN_HAND));
                    playShakeSound(player);
                }
            }
            return;
        }

        finalizeShake(player, true);
    }

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }

        ActiveShake activeShake = ACTIVE_SHAKES.get(event.player.getUUID());
        if (activeShake == null) {
            return;
        }

        if (!(event.player instanceof ServerPlayer serverPlayer)) {
            ACTIVE_SHAKES.remove(event.player.getUUID());
            return;
        }

        ItemStack stack = serverPlayer.getItemInHand(activeShake.hand());
        if (!stack.is(ModItems.SHAKER.get()) || !ShakerInventory.canShake(stack)) {
            finalizeShake(serverPlayer, false);
            return;
        }

        activeShake.tick();
        if (activeShake.shouldPlaySound()) {
            playShakeSound(serverPlayer);
        }
    }

    private static void playShakeSound(ServerPlayer player) {
        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                ModSoundEvents.SHAKER_SHAKE.get(),
                SoundSource.PLAYERS,
                0.7F,
                0.95F + player.getRandom().nextFloat() * 0.1F
        );
    }

    private static void stopShakeSound(ServerPlayer player) {
        ClientboundStopSoundPacket packet = new ClientboundStopSoundPacket(
                ModSoundEvents.SHAKER_SHAKE.getId(),
                SoundSource.PLAYERS
        );
        double maxDistanceSqr = SHAKE_SOUND_STOP_RADIUS * SHAKE_SOUND_STOP_RADIUS;
        for (ServerPlayer listener : player.serverLevel().players()) {
            if (listener.distanceToSqr(player) <= maxDistanceSqr) {
                listener.connection.send(packet);
            }
        }
    }

    private static void finalizeShake(ServerPlayer player, boolean produceResult) {
        ActiveShake activeShake = ACTIVE_SHAKES.remove(player.getUUID());
        if (activeShake == null) {
            return;
        }

        stopShakeSound(player);
        if (!produceResult) {
            return;
        }

        ShakerInventory inventory = new ShakerInventory(player, activeShake.hand());
        if (!inventory.hasInputItems() || inventory.hasOutput()) {
            return;
        }

        ItemStack result = CocktailBrewer.brew(inventory, activeShake.getSeconds(), player.getRandom());
        if (result.isEmpty()) {
            return;
        }

        inventory.returnInputReminingItems();
        inventory.setStackInSlot(ShakerInventory.BREWED_SLOT, result);
        inventory.save();
        player.displayClientMessage(Component.translatable("actionbar.delta_delight.shaker_complete"), true);
    }

    private static final class ActiveShake {
        private final InteractionHand hand;
        private int ticks;

        private ActiveShake(InteractionHand hand) {
            this.hand = hand;
        }

        private InteractionHand hand() {
            return hand;
        }

        private void tick() {
            ticks++;
        }

        private boolean shouldPlaySound() {
            return ticks > 0 && ticks % SHAKE_SOUND_INTERVAL_TICKS == 0;
        }

        private float getSeconds() {
            return ticks / 20.0F;
        }
    }
}
