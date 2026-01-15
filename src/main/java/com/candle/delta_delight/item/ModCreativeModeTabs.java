package com.candle.delta_delight.item;

import com.candle.delta_delight.Delta_delight;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.registry.ModItems;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Delta_delight.MODID);

    public static final RegistryObject<CreativeModeTab> DELTA_FOOD_TAB =
            CREATIVE_MODE_TABS.register("delta_food_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.APPLE_PIE.get()))
                    .title(Component.translatable("itemGroup.delta_food_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.APPLE_CIDER.get());
                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
