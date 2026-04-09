package com.candle.delta_delight.registry;

import com.candle.delta_delight.DeltaDelight;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ModLootModifiers {
    public static class AddItemWithChanceModifier extends LootModifier {
        public static final Codec<ModLootModifiers.AddItemWithChanceModifier> CODEC = RecordCodecBuilder.create(inst ->
                codecStart(inst).and(inst.group(
                        ItemStack.CODEC.fieldOf("item").forGetter(m -> m.itemToAdd),
                        Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)
                )).apply(inst, ModLootModifiers.AddItemWithChanceModifier::new)
        );

        private final ItemStack itemToAdd;
        private final float chance; // 0.0 到 1.0

        public AddItemWithChanceModifier(LootItemCondition[] conditions, ItemStack itemToAdd, float chance) {
            super(conditions);
            this.itemToAdd = itemToAdd;
            this.chance = chance;
        }

        @Override
        protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
            // 使用随机数判断是否掉落
            if (context.getRandom().nextFloat() < chance) {
                generatedLoot.add(itemToAdd.copy());
            }
            return generatedLoot;
        }

        @Override
        public Codec<? extends IGlobalLootModifier> codec() {
            return CODEC;
        }
    }

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, DeltaDelight.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM_WITH_CHANCE =
            LOOT_MODIFIERS.register("add_item_with_chance", () -> AddItemWithChanceModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIERS.register(eventBus);
    }
}