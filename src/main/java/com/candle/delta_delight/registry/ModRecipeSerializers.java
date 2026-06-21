package com.candle.delta_delight.registry;

import com.candle.delta_delight.DeltaDelight;
import com.candle.delta_delight.recipe.ZongziRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DeltaDelight.MODID);

    public static final RegistryObject<RecipeSerializer<ZongziRecipe>> ZONGZI =
            RECIPE_SERIALIZERS.register("zongzi", ZongziRecipe.Serializer::new);

    private ModRecipeSerializers() {
    }
}
