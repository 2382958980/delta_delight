package com.candle.delta_delight.registry;

import com.candle.delta_delight.Delta_delight;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Delta_delight.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM_WITH_CHANCE =
            LOOT_MODIFIERS.register("add_item_with_chance", () -> AddItemWithChanceModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIERS.register(eventBus);
    }
}