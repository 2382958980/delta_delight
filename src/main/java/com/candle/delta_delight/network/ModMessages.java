package com.candle.delta_delight.network;

import com.candle.delta_delight.Delta_delight;
import com.candle.delta_delight.network.packet.SetShakerShakingPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ModMessages {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(Delta_delight.MODID, "messages"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private ModMessages() {
    }

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(
                id++,
                SetShakerShakingPacket.class,
                SetShakerShakingPacket::encode,
                SetShakerShakingPacket::decode,
                SetShakerShakingPacket::handle
        );
    }
}
