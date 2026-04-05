package com.candle.delta_delight.cocktail;

import com.candle.delta_delight.DeltaDelight;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class CocktailAppearanceManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String COLORS_PATH = "data/" + DeltaDelight.MODID + "/cocktails/ingredient_colors.json";
    private static final int BOTTOM_TINT_INDEX = 0;
    private static final int TOP_TINT_INDEX = 1;
    private static final int GLASS_TINT_INDEX = 2;
    private static volatile Map<String, Integer> ingredientColors = Map.of();
    private static volatile Map<String, Integer> ingredientOrder = Map.of();
    private static volatile boolean loaded;

    private CocktailAppearanceManager() {
    }

    public static synchronized void ensureLoaded() {
        if (loaded) {
            return;
        }

        try (InputStream stream = CocktailAppearanceManager.class.getClassLoader().getResourceAsStream(COLORS_PATH)) {
            if (stream == null) {
                ingredientColors = Map.of();
                ingredientOrder = Map.of();
                LOGGER.warn("Cocktail ingredient colors not found at {}", COLORS_PATH);
                loaded = true;
                return;
            }

            try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                Map<String, Integer> colors = new HashMap<>();
                Map<String, Integer> order = new HashMap<>();
                int index = 0;
                for (JsonElement element : GsonHelper.getAsJsonArray(root, "ingredients")) {
                    JsonObject ingredientJson = GsonHelper.convertToJsonObject(element, "ingredient");
                    String key = GsonHelper.getAsString(ingredientJson, "key").toLowerCase(Locale.ROOT);
                    String color = GsonHelper.getAsString(ingredientJson, "color").trim();
                    colors.put(key, parseColor(color));
                    order.put(key, index++);
                }

                ingredientColors = Collections.unmodifiableMap(colors);
                ingredientOrder = Collections.unmodifiableMap(order);
                LOGGER.info("Loaded {} cocktail ingredient colors", colors.size());
            }
        } catch (IOException exception) {
            LOGGER.error("Failed to load cocktail ingredient colors", exception);
            ingredientColors = Map.of();
            ingredientOrder = Map.of();
        } finally {
            loaded = true;
        }
    }

    public static int getTintColor(ItemStack stack, int tintIndex) {
        ensureLoaded();

        if (tintIndex == GLASS_TINT_INDEX) {
            return -1;
        }

        List<String> orderedKeys = getOrderedIngredientKeys(stack);
        if (orderedKeys.isEmpty()) {
            return 0xFFFFFF;
        }

        String bottomKey = orderedKeys.get(0);
        String topKey = orderedKeys.size() > 1 ? orderedKeys.get(1) : bottomKey;

        if (tintIndex == BOTTOM_TINT_INDEX) {
            return ingredientColors.getOrDefault(bottomKey, 0xFFFFFF);
        }
        if (tintIndex == TOP_TINT_INDEX) {
            return ingredientColors.getOrDefault(topKey, 0xFFFFFF);
        }
        return -1;
    }

    private static List<String> getOrderedIngredientKeys(ItemStack stack) {
        List<String> keys = CocktailItem.getStoredIngredientKeys(stack).stream()
                .map(key -> key.toLowerCase(Locale.ROOT))
                .filter(ingredientOrder::containsKey)
                .limit(2)
                .toList();
        if (keys.size() < 2) {
            return keys;
        }

        List<String> sorted = new ArrayList<>(keys);
        sorted.sort((left, right) -> Integer.compare(
                ingredientOrder.getOrDefault(left, Integer.MAX_VALUE),
                ingredientOrder.getOrDefault(right, Integer.MAX_VALUE)
        ));
        return sorted;
    }

    private static int parseColor(String color) {
        String normalized = color.startsWith("#") ? color.substring(1) : color;
        if (normalized.length() != 6) {
            throw new IllegalArgumentException("Invalid cocktail ingredient color: " + color);
        }
        return Integer.parseInt(normalized, 16);
    }
}
