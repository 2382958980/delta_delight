package com.candle.delta_delight.network;

import com.candle.delta_delight.DeltaDelight;
import com.candle.delta_delight.network.packet.SetShakerShakingPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ModMessages {
    private static int id = 0;

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(DeltaDelight.MODID, "messages"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SuppressWarnings("SameParameterValue")
    private static <MSG> void registerMessage(
            Class<MSG> messageType,
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, MSG> decoder,
            BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer
    ) {
        CHANNEL.registerMessage(
                id++,
                messageType,
                encoder,
                decoder,
                messageConsumer
        );
    }

    public static void register() {
        registerMessage(
                SetShakerShakingPacket.class,
                SetShakerShakingPacket::encode,
                SetShakerShakingPacket::decode,
                SetShakerShakingPacket::handle
        );
    }
}
