package com.candle.delta_delight.menu;

import com.candle.delta_delight.content.ShakerInventory;
import com.candle.delta_delight.registry.ModItems;
import com.candle.delta_delight.registry.ModMenuTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ShakerMenu extends AbstractContainerMenu {
    public static final int MIXING_SLOT_COUNT = ShakerInventory.SLOT_COUNT;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int VANILLA_SLOT_COUNT = PLAYER_INVENTORY_SLOT_COUNT + HOTBAR_SLOT_COUNT;
    private static final int MIXING_SLOT_START = 0;
    private static final int MIXING_SLOT_END = MIXING_SLOT_START + MIXING_SLOT_COUNT;
    private static final int PLAYER_SLOT_START = MIXING_SLOT_END;
    private static final int PLAYER_SLOT_END = PLAYER_SLOT_START + VANILLA_SLOT_COUNT;

    private final ShakerInventory shakerInventory;
    private final InteractionHand hand;

    public ShakerMenu(int containerId, Inventory playerInventory, InteractionHand hand) {
        this(containerId, playerInventory, new ShakerInventory(playerInventory.player, hand), hand);
    }

    public ShakerMenu(int containerId, Inventory playerInventory, ShakerInventory shakerInventory, InteractionHand hand) {
        super(ModMenuTypes.SHAKER.get(), containerId);
        this.shakerInventory = shakerInventory;
        this.hand = hand;

        addSlot(new SlotItemHandler(shakerInventory, ShakerInventory.BASE_SLOT, 27, 34));
        addSlot(new SlotItemHandler(shakerInventory, ShakerInventory.INGREDIENT_SLOT_1, 61, 34));
        addSlot(new SlotItemHandler(shakerInventory, ShakerInventory.INGREDIENT_SLOT_2, 79, 34));
        addSlot(new SlotItemHandler(shakerInventory, ShakerInventory.OUTPUT_SLOT, 133, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < MIXING_SLOT_END) {
            if (!moveItemStackTo(sourceStack, PLAYER_SLOT_START, PLAYER_SLOT_END, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (shakerInventory.isItemValid(ShakerInventory.BASE_SLOT, sourceStack)
                    && !slots.get(ShakerInventory.BASE_SLOT).hasItem()
                    && moveItemStackTo(sourceStack, ShakerInventory.BASE_SLOT, ShakerInventory.BASE_SLOT + 1, false)) {
                // todo moved to base slot
            } else if (moveItemStackTo(sourceStack, ShakerInventory.INGREDIENT_SLOT_1, ShakerInventory.OUTPUT_SLOT, false)) {
                // todo moved to ingredient slots
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getItemInHand(hand).is(ModItems.SHAKER.get()) && shakerInventory.isBoundToValidShaker();
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        shakerInventory.save();
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int slot = 0; slot < 9; slot++) {
            addSlot(new Slot(playerInventory, slot, 8 + slot * 18, 142));
        }
    }

    public ShakerInventory getShakerInventory() {
        return shakerInventory;
    }

    public InteractionHand getHand() {
        return hand;
    }
}
