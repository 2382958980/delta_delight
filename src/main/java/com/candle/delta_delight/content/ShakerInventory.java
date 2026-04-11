package com.candle.delta_delight.content;

import com.candle.delta_delight.cocktail.CocktailBase;
import com.candle.delta_delight.cocktail.CocktailIngredient;
import com.candle.delta_delight.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class ShakerInventory extends ItemStackHandler {
    public static final int BASE_SLOT = 0;
    public static final int INGREDIENT_SLOT_1 = 1;
    public static final int INGREDIENT_SLOT_2 = 2;
    public static final int OUTPUT_SLOT = 3;
    public static final int SLOT_COUNT = 4;
    private static final String INVENTORY_TAG = "ShakerInventory";

    private final Player player;
    private final InteractionHand hand;
    private boolean loading;

    public ShakerInventory(Player player, InteractionHand hand) {
        super(SLOT_COUNT);
        this.player = player;
        this.hand = hand;
        load();
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (!loading) {
            save();
        }
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return switch (slot) {
            case BASE_SLOT -> CocktailBase.fromStack(stack).isPresent();
            case INGREDIENT_SLOT_1, INGREDIENT_SLOT_2 -> CocktailIngredient.fromStack(stack).isPresent();
            default -> false;
        };
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    public ItemStack getShakerStack() {
        return player.getItemInHand(hand);
    }

    @SuppressWarnings("unused")
    public InteractionHand getHand() {
        return hand;
    }

    public boolean isBoundToValidShaker() {
        return getShakerStack().is(ModItems.SHAKER.get());
    }

    public boolean hasInputItems() {
        return hasValidShakeInputs(this);
    }

    public boolean hasOutput() {
        return !getStackInSlot(OUTPUT_SLOT).isEmpty();
    }

    public List<ItemStack> getIngredientStacks() {
        return List.of(getStackInSlot(INGREDIENT_SLOT_1), getStackInSlot(INGREDIENT_SLOT_2));
    }

    private static void returnCraftReminingItemToSlot(ShakerInventory inventory, int slotId) {
        ItemStack stack = inventory.getStackInSlot(slotId);
        inventory.setStackInSlot(slotId, stack.getCraftingRemainingItem());
    }

    public void returnInputReminingItems() {
        Stream.of(
                ShakerInventory.BASE_SLOT,
                ShakerInventory.INGREDIENT_SLOT_1,
                ShakerInventory.INGREDIENT_SLOT_2
        ).forEach(slot -> returnCraftReminingItemToSlot(this, slot));
    }

    public void load() {
        loading = true;
        deserializeNBT(getInventoryTag());
        loading = false;
    }

    public void save() {
        ItemStack shaker = getShakerStack();
        if (!shaker.is(ModItems.SHAKER.get())) {
            return;
        }
        shaker.getOrCreateTag().put(INVENTORY_TAG, serializeNBT());
    }

    private CompoundTag getInventoryTag() {
        ItemStack shaker = getShakerStack();
        if (!shaker.is(ModItems.SHAKER.get()) || !shaker.hasTag()) {
            return new CompoundTag();
        }
        CompoundTag root = shaker.getOrCreateTag();
        if (!root.contains(INVENTORY_TAG, Tag.TAG_COMPOUND)) {
            return new CompoundTag();
        }
        return root.getCompound(INVENTORY_TAG);
    }

    public static boolean canShake(ItemStack shakerStack) {
        if (!shakerStack.is(ModItems.SHAKER.get())) {
            return false;
        }

        ItemStackHandler handler = new ItemStackHandler(SLOT_COUNT);
        CompoundTag root = shakerStack.getTag();
        if (root == null || !root.contains(INVENTORY_TAG, Tag.TAG_COMPOUND)) {
            return false;
        }

        handler.deserializeNBT(root.getCompound(INVENTORY_TAG));
        return hasValidShakeInputs(handler);
    }

    private static boolean hasValidShakeInputs(ItemStackHandler handler) {
        if (handler.getSlots() <= OUTPUT_SLOT || !handler.getStackInSlot(OUTPUT_SLOT).isEmpty()) {
            return false;
        }

        if (CocktailBase.fromStack(handler.getStackInSlot(BASE_SLOT)).isEmpty()) {
            return false;
        }

        boolean hasIngredient = false;
        for (int slot : new int[]{INGREDIENT_SLOT_1, INGREDIENT_SLOT_2}) {
            ItemStack stack = handler.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }

            if (CocktailIngredient.fromStack(stack).isEmpty()) {
                return false;
            }
            hasIngredient = true;
        }

        return hasIngredient;
    }
}
