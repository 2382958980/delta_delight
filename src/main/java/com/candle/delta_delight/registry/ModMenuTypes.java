package com.candle.delta_delight.registry;

import com.candle.delta_delight.DeltaDelight;
import com.candle.delta_delight.menu.ShakerMenu;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, DeltaDelight.MODID);

    public static final RegistryObject<MenuType<ShakerMenu>> SHAKER =
            MENU_TYPES.register("shaker", () -> IForgeMenuType.create((windowId, inventory, data) -> {
                InteractionHand hand = data == null ? InteractionHand.MAIN_HAND : data.readEnum(InteractionHand.class);
                return new ShakerMenu(windowId, inventory, hand);
            }));
}
