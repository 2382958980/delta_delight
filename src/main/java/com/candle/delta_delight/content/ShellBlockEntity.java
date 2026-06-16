package com.candle.delta_delight.content;

import com.candle.delta_delight.registry.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShellBlockEntity extends BlockEntity implements Clearable {
    private static final String ITEM_TAG = "Item";
    private static final String ITEM_EMPTY_TAG = "ItemEmpty";
    private ItemStack item = ItemStack.EMPTY;

    public ShellBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SHELL.get(), pos, state);
    }

    public ItemStack getStoredItem() {
        return item.copy();
    }

    public boolean isEmpty() {
        return item.isEmpty();
    }

    public void setStoredItem(ItemStack stack) {
        item = stack.copy();
        item.setCount(Math.min(1, item.getCount()));
        sync();
    }

    public ItemStack removeStoredItem() {
        ItemStack removed = item.copy();
        item = ItemStack.EMPTY;
        sync();
        return removed;
    }

    @Override
    public void clearContent() {
        item = ItemStack.EMPTY;
        sync();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (!item.isEmpty()) {
            tag.put(ITEM_TAG, item.save(new CompoundTag()));
        } else {
            tag.putBoolean(ITEM_EMPTY_TAG, true);
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        item = tag.contains(ITEM_TAG) && !tag.getBoolean(ITEM_EMPTY_TAG)
                ? ItemStack.of(tag.getCompound(ITEM_TAG))
                : ItemStack.EMPTY;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}
