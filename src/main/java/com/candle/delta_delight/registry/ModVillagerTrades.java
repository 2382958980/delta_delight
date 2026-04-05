package com.candle.delta_delight.registry;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public final class ModVillagerTrades {
    private static final int LIBRARIAN_NOVICE_LEVEL = 1;
    private static final float HEART_OF_AFRICA_TRADE_CHANCE = 0.10F;
    private static final int MAX_USES = 1;
    private static final int VILLAGER_XP = 10;
    private static final float PRICE_MULTIPLIER = 0F;

    private ModVillagerTrades() {
    }

    @SubscribeEvent
    public static void addLibrarianTrades(VillagerTradesEvent event) {
        if (event.getType() != VillagerProfession.LIBRARIAN) {
            return;
        }

        var noviceTrades = event.getTrades().get(LIBRARIAN_NOVICE_LEVEL);
        if (noviceTrades == null) {
            return;
        }

        noviceTrades.add((trader, random) -> {
            if (random.nextFloat() >= HEART_OF_AFRICA_TRADE_CHANCE) {
                return null;
            }

            return new MerchantOffer(
                    new ItemStack(ModItems.HEART_OF_AFRICA.get()),
                    new ItemStack(Items.EMERALD_BLOCK, 64),
                    MAX_USES,
                    VILLAGER_XP,
                    PRICE_MULTIPLIER
            );
        });
    }
}
