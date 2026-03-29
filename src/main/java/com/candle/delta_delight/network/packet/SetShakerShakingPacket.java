package com.candle.delta_delight.network.packet;

import com.candle.delta_delight.system.ShakerSystem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SetShakerShakingPacket(boolean shaking) {
    public static void encode(SetShakerShakingPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.shaking);
    }

    public static SetShakerShakingPacket decode(FriendlyByteBuf buffer) {
        return new SetShakerShakingPacket(buffer.readBoolean());
    }

    public static void handle(SetShakerShakingPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ShakerSystem.setShaking(player, packet.shaking);
            }
        });
        context.setPacketHandled(true);
    }
}
