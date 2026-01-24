package com.candle.delta_delight.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.candle.delta_delight.Delta_delight;
import com.candle.delta_delight.registry.ModItems;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Delta_delight.MODID);

    public static final RegistryObject<CreativeModeTab> FDADDITIONS = TABS.register(
            "fdadditions",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.fdadditions"))
                    .icon(() -> new ItemStack(ModItems.AFRICAHEART.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.AFRICAHEART.get());
                        output.accept(ModItems.CHAMPAGNE.get());
                        output.accept(ModItems.CAVIAR.get());
                        output.accept(ModItems.TEQUILA.get());
                        output.accept(ModItems.COFFEE.get());
                        output.accept(ModItems.SEAFOODCAN.get());
                        output.accept(ModItems.NOURISHCAN.get());
                        output.accept(ModItems.LEMONTEA.get());
                        output.accept(ModItems.GINGERBREADMAN.get());
                        output.accept(ModItems.ORANGEL.get());
                        output.accept(ModItems.ENGLISHTEA.get());
                        output.accept(ModItems.ARMYCAN.get());
                        output.accept(ModItems.YUMMYNOODLES.get());
                        output.accept(ModItems.VITABLETS.get());
                        output.accept(ModItems.YOGURT.get());
                        output.accept(ModItems.FASTNOODLES.get());
                    })
                    .build()
    );
}