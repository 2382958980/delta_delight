package com.candle.delta_delight.network;

import com.candle.delta_delight.DeltaDelight;
import com.candle.delta_delight.network.packet.SetShakerShakingPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ModMessages {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(DeltaDelight.MODID, "messages"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

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
