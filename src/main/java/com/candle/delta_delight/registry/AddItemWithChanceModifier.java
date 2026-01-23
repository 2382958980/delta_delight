package com.candle.delta_delight.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class AddItemWithChanceModifier extends LootModifier {

    public static final Codec<AddItemWithChanceModifier> CODEC = RecordCodecBuilder.create(inst ->
            codecStart(inst).and(inst.group(
                    ItemStack.CODEC.fieldOf("item").forGetter(m -> m.itemToAdd),
                    Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)
            )).apply(inst, AddItemWithChanceModifier::new)
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
