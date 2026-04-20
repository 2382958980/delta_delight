package com.candle.delta_delight.registry;

import com.candle.delta_delight.DeltaDelight;
import com.candle.delta_delight.content.ReconArrow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DeltaDelight.MODID);

    public static final RegistryObject<EntityType<ReconArrow>> RECON_ARROW = ENTITY_TYPES.register(
            "recon_arrow",
            () -> EntityType.Builder.<ReconArrow>of(ReconArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("recon_arrow")
    );

    private ModEntityTypes() {
    }
}
