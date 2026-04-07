package com.candle.delta_delight.client;

import com.candle.delta_delight.DeltaDelight;
import com.candle.delta_delight.content.ShakerInventory;
import com.candle.delta_delight.network.ModMessages;
import com.candle.delta_delight.network.packet.SetShakerShakingPacket;
import com.candle.delta_delight.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Locale;

@EventBusSubscriber(modid = DeltaDelight.MODID, value = Dist.CLIENT)
public final class ShakerClientEvents {
    private static boolean localShaking;
    private static int localShakeTicks;

    private ShakerClientEvents() {
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
