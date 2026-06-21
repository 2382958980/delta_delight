package com.candle.delta_delight.recipe;

import com.candle.delta_delight.content.ZongziItem;
import com.candle.delta_delight.registry.ModRecipeSerializers;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModItems;

public class ZongziRecipe extends CustomRecipe {
    public ZongziRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer container, @NotNull Level level) {
        return computeScore(container) >= 0;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        return ZongziItem.create(computeScore(container));
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return ZongziItem.create(0);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ZONGZI.get();
    }

    private static int computeScore(CraftingContainer container) {
        int seaweedCount = 0;
        int riceCount = 0;
        int optionalCount = 0;
        int score = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.is(Items.KELP)) {
                seaweedCount++;
                if (seaweedCount > 1) {
                    return -1;
                }
                continue;
            }

            if (stack.is(ModItems.COOKED_RICE.get())) {
                riceCount++;
                if (riceCount > 1) {
                    return -1;
                }
                continue;
            }

            Integer ingredientScore = ingredientScore(stack);
            if (ingredientScore == null) {
                return -1;
            }

            optionalCount++;
            if (optionalCount > 4) {
                return -1;
            }
            score += ingredientScore;
        }

        return seaweedCount == 1 && riceCount == 1 ? score : -1;
    }

    @Nullable
    private static Integer ingredientScore(ItemStack stack) {
        if (stack.is(ModItems.PUMPKIN_SLICE.get())) {
            return 2;
        }
        if (stack.is(Items.COCOA_BEANS)) {
            return 1;
        }
        if (stack.is(Items.SWEET_BERRIES)) {
            return 1;
        }
        if (stack.is(Items.HONEY_BOTTLE)) {
            return 2;
        }
        if (stack.is(ModItems.COOKED_BACON.get())) {
            return 3;
        }
        if (stack.is(ModItems.COOKED_CHICKEN_CUTS.get())) {
            return 3;
        }
        if (stack.is(ModItems.COOKED_SALMON_SLICE.get())) {
            return 3;
        }
        if (stack.is(Items.EGG)) {
            return 1;
        }
        return null;
    }

    public static class Serializer implements RecipeSerializer<ZongziRecipe> {
        @Override
        public @NotNull ZongziRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            return new ZongziRecipe(id, CraftingBookCategory.MISC);
        }

        @Override
        public @Nullable ZongziRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buffer) {
            return new ZongziRecipe(id, CraftingBookCategory.MISC);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull ZongziRecipe recipe) {
        }
    }
}
