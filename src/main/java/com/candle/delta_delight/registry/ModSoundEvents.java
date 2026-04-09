package com.candle.delta_delight.registry;

import com.candle.delta_delight.DeltaDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DeltaDelight.MODID);

    public static final RegistryObject<SoundEvent> MUSIC_DISC_DAWN = registerMusicDisc("dawn");
    public static final RegistryObject<SoundEvent> MUSIC_DISC_IN_THE_SUMMER = SOUND_EVENTS.register(
            "music_disc.in_the_summer",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(DeltaDelight.MODID, "music_disc.in_the_summer")
            )
    );
    public static final RegistryObject<SoundEvent> MUSIC_DISC_KING_OF_THE_RING = registerMusicDisc("king_of_the_ring");
    public static final RegistryObject<SoundEvent> MUSIC_DISC_MAKING_LEGENDS = registerMusicDisc("making_legends");
    public static final RegistryObject<SoundEvent> MUSIC_DISC_MENU1 = registerMusicDisc("menu1");
    public static final RegistryObject<SoundEvent> MUSIC_DISC_MENU2 = registerMusicDisc("menu2");
    public static final RegistryObject<SoundEvent> MUSIC_DISC_PLAY_WITH_FIRE = registerMusicDisc("play_with_fire");

    private ModSoundEvents() {
    }

    private static RegistryObject<SoundEvent> registerMusicDisc(String name) {
        return SOUND_EVENTS.register(
                "music_disc." + name,
                () -> SoundEvent.createVariableRangeEvent(
                        ResourceLocation.fromNamespaceAndPath(DeltaDelight.MODID, "music_disc." + name)
                )
        );
    }
}
