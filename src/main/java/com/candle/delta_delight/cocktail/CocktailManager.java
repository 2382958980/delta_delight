package com.candle.delta_delight.cocktail;

import com.candle.delta_delight.DeltaDelight;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class CocktailManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String RECIPES_PATH = "data/" + DeltaDelight.MODID + "/cocktails/recipes.json";
    private static volatile List<CocktailDefinition> definitions = List.of();
    private static volatile boolean loaded;

    private CocktailManager() {
    }

    public static synchronized void ensureLoaded() {
        if (loaded) {
            return;
        }

        try (InputStream stream = CocktailManager.class.getClassLoader().getResourceAsStream(RECIPES_PATH)) {
            if (stream == null) {
                definitions = List.of();
                LOGGER.warn("Cocktail recipes not found at {}", RECIPES_PATH);
                loaded = true;
                return;
            }

            try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                List<CocktailDefinition> parsed = new ArrayList<>();
                for (JsonElement element : GsonHelper.getAsJsonArray(root, "recipes")) {
                    JsonObject recipeJson = GsonHelper.convertToJsonObject(element, "recipe");
                    ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(recipeJson, "id"));
                    String nameKey = GsonHelper.getAsString(recipeJson, "name_key");
                    CocktailQuality quality = CocktailQuality.valueOf(GsonHelper.getAsString(recipeJson, "quality").toUpperCase());
                    String baseKey = GsonHelper.getAsString(recipeJson, "base");

                    Set<String> ingredientKeys = new LinkedHashSet<>();
                    for (JsonElement ingredientElement : GsonHelper.getAsJsonArray(recipeJson, "ingredients")) {
                        ingredientKeys.add(ingredientElement.getAsString());
                    }

                    float minShake = GsonHelper.getAsFloat(recipeJson, "min_shake_seconds", 0.0F);
                    float maxShake = GsonHelper.getAsFloat(recipeJson, "max_shake_seconds", minShake);
                    parsed.add(new CocktailDefinition(id, nameKey, quality, baseKey, ingredientKeys, minShake, maxShake));
                }

                definitions = Collections.unmodifiableList(parsed);
                LOGGER.info("Loaded {} cocktail definitions", parsed.size());
            }
        } catch (IOException exception) {
            LOGGER.error("Failed to load cocktail definitions", exception);
            definitions = List.of();
        } finally {
            loaded = true;
        }
    }

    public static Optional<CocktailDefinition> findMatch(CocktailQuality quality, String baseKey, Set<String> ingredientKeys) {
        ensureLoaded();
        return definitions.stream()
                .filter(definition -> definition.quality() == quality)
                .filter(definition -> definition.matches(baseKey, ingredientKeys))
                .findFirst();
    }

    @SuppressWarnings("unused")
    public static List<CocktailDefinition> getDefinitions() {
        ensureLoaded();
        return definitions;
    }
}
