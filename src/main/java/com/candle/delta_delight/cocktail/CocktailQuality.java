package com.candle.delta_delight.cocktail;

public enum CocktailQuality {
    COMMON("common"),
    UNCOMMON("uncommon"),
    EPIC("epic");

    private final String serializedName;

    CocktailQuality(String serializedName) {
        this.serializedName = serializedName;
    }

    public String getSerializedName() {
        return serializedName;
    }
}
