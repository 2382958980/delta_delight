package com.candle.delta_delight.system;

import com.candle.delta_delight.cocktail.CocktailBrewer;
import com.candle.delta_delight.content.ShakerInventory;
import com.candle.delta_delight.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
    private static final Map<UUID, ActiveShake> ACTIVE_SHAKES = new HashMap<>();

    private ShakerSystem() {
    }

    public static void setShaking(ServerPlayer player, boolean shaking) {
        if (shaking) {
            ItemStack stack = player.getMainHandItem();
            if (stack.is(ModItems.SHAKER.get()) && ShakerInventory.canShake(stack)) {
                ACTIVE_SHAKES.put(player.getUUID(), new ActiveShake(InteractionHand.MAIN_HAND));
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
    }

    private static void finalizeShake(ServerPlayer player, boolean produceResult) {
        ActiveShake activeShake = ACTIVE_SHAKES.remove(player.getUUID());
        if (activeShake == null || !produceResult) {
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
        inventory.setStackInSlot(ShakerInventory.OUTPUT_SLOT, result);
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

        private float getSeconds() {
            return ticks / 20.0F;
        }
    }
}
