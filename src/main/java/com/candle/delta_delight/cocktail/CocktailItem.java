package com.candle.delta_delight.cocktail;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CocktailItem extends Item {
    public static final String NAME_KEY_TAG = "CocktailNameKey";
    public static final String QUALITY_TAG = "CocktailQuality";
    public static final String EFFECTS_TAG = "CocktailEffects";
    public static final String INGREDIENT_KEYS_TAG = "CocktailIngredientKeys";
    private static final String EFFECT_ID_TAG = "Id";
    private static final String DURATION_TAG = "Duration";
    private static final String AMPLIFIER_TAG = "Amplifier";

    public CocktailItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(NAME_KEY_TAG, Tag.TAG_STRING)) {
            return Component.translatable(tag.getString(NAME_KEY_TAG));
        }
        return super.getName(stack);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(QUALITY_TAG, Tag.TAG_STRING)) {
            return super.getRarity(stack);
        }

        return switch (CocktailQuality.valueOf(tag.getString(QUALITY_TAG))) {
            case COMMON -> Rarity.COMMON;
            case UNCOMMON -> Rarity.UNCOMMON;
            case EPIC -> Rarity.EPIC;
        };
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            for (MobEffectInstance effectInstance : getStoredEffects(stack)) {
                entity.addEffect(new MobEffectInstance(effectInstance));
            }
        }

        if (entity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
        }

        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            stack.shrink(1);
            ItemStack bottle = new ItemStack(net.minecraft.world.item.Items.GLASS_BOTTLE);
            if (stack.isEmpty()) {
                return bottle;
            }
            if (!player.getInventory().add(bottle)) {
                player.drop(bottle, false);
            }
        }

        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(QUALITY_TAG, Tag.TAG_STRING)) {
            String qualityName = tag.getString(QUALITY_TAG).toLowerCase();
            tooltip.add(Component.translatable("cocktail.delta_delight.quality." + qualityName).withStyle(ChatFormatting.GRAY));
        }

        for (MobEffectInstance effectInstance : getStoredEffects(stack)) {
            tooltip.add(Component.translatable(effectInstance.getDescriptionId()).withStyle(ChatFormatting.BLUE));
        }
    }

    public static ItemStack create(String nameKey, CocktailQuality quality, List<MobEffectInstance> effects,
                                   List<String> ingredientKeys, Item item) {
        ItemStack stack = new ItemStack(item);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(NAME_KEY_TAG, nameKey);
        tag.putString(QUALITY_TAG, quality.name());

        ListTag effectList = new ListTag();
        for (MobEffectInstance effectInstance : effects) {
            CompoundTag effectTag = new CompoundTag();
            effectTag.putString(EFFECT_ID_TAG, net.minecraftforge.registries.ForgeRegistries.MOB_EFFECTS.getKey(effectInstance.getEffect()).toString());
            effectTag.putInt(DURATION_TAG, effectInstance.getDuration());
            effectTag.putInt(AMPLIFIER_TAG, effectInstance.getAmplifier());
            effectList.add(effectTag);
        }
        tag.put(EFFECTS_TAG, effectList);

        ListTag ingredientList = new ListTag();
        for (String ingredientKey : ingredientKeys) {
            ingredientList.add(StringTag.valueOf(ingredientKey));
        }
        tag.put(INGREDIENT_KEYS_TAG, ingredientList);
        return stack;
    }

    public static List<MobEffectInstance> getStoredEffects(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(EFFECTS_TAG, Tag.TAG_LIST)) {
            return List.of();
        }

        List<MobEffectInstance> effects = new ArrayList<>();
        ListTag effectList = tag.getList(EFFECTS_TAG, Tag.TAG_COMPOUND);
        for (Tag tagElement : effectList) {
            CompoundTag effectTag = (CompoundTag) tagElement;
            MobEffect effect = net.minecraftforge.registries.ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.parse(effectTag.getString(EFFECT_ID_TAG)));
            if (effect != null) {
                effects.add(new MobEffectInstance(
                        effect,
                        effectTag.getInt(DURATION_TAG),
                        effectTag.getInt(AMPLIFIER_TAG)
                ));
            }
        }
        return effects;
    }

    public static List<String> getStoredIngredientKeys(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(INGREDIENT_KEYS_TAG, Tag.TAG_LIST)) {
            return List.of();
        }

        List<String> ingredientKeys = new ArrayList<>();
        ListTag ingredientList = tag.getList(INGREDIENT_KEYS_TAG, Tag.TAG_STRING);
        for (Tag tagElement : ingredientList) {
            ingredientKeys.add(tagElement.getAsString());
        }
        return ingredientKeys;
    }
}
