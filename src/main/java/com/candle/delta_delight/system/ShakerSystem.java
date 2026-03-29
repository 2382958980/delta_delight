package com.candle.delta_delight.system;

import com.candle.delta_delight.cocktail.CocktailBrewer;
import com.candle.delta_delight.content.ShakerInventory;
import com.candle.delta_delight.network.ModMessages;
import com.candle.delta_delight.network.packet.SetShakerShakingPacket;
import com.candle.delta_delight.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = com.candle.delta_delight.Delta_delight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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

        inventory.clearInputs();
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

    @Mod.EventBusSubscriber(modid = com.candle.delta_delight.Delta_delight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static final class ClientEvents {
        private static boolean localShaking;
        private static int localShakeTicks;

        private ClientEvents() {
        }

        @SubscribeEvent
        public static void onAttackInput(InputEvent.InteractionKeyMappingTriggered event) {
            if (!event.isAttack()) {
                return;
            }

            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.getMainHandItem().is(ModItems.SHAKER.get())) {
                event.setSwingHand(false);
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) {
                return;
            }

            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            if (player == null) {
                return;
            }

            boolean shouldShake = minecraft.screen == null
                    && player.getMainHandItem().is(ModItems.SHAKER.get())
                    && ShakerInventory.canShake(player.getMainHandItem());
            boolean attackDown = minecraft.options.keyAttack.isDown();

            if (attackDown && shouldShake && !localShaking) {
                localShaking = true;
                localShakeTicks = 0;
                ModMessages.CHANNEL.sendToServer(new SetShakerShakingPacket(true));
            }

            if (localShaking && attackDown && shouldShake) {
                localShakeTicks++;
                if (localShakeTicks % 5 == 0) {
                    player.swing(InteractionHand.MAIN_HAND);
                }
                player.displayClientMessage(Component.translatable(
                        "actionbar.delta_delight.shaker_time",
                        String.format(Locale.ROOT, "%.1f", localShakeTicks / 20.0F)
                ), true);
                return;
            }

            if (localShaking) {
                localShaking = false;
                localShakeTicks = 0;
                ModMessages.CHANNEL.sendToServer(new SetShakerShakingPacket(false));
            }
        }
    }
}
