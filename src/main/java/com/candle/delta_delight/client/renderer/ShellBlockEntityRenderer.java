package com.candle.delta_delight.client.renderer;

import com.candle.delta_delight.content.ShellBlock;
import com.candle.delta_delight.content.ShellBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ShellBlockEntityRenderer implements BlockEntityRenderer<ShellBlockEntity> {
    public ShellBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull ShellBlockEntity shell, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = shell.getBlockState();
        if (!state.getValue(ShellBlock.OPEN)) {
            return;
        }

        ItemStack stack = shell.getStoredItem();
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.075D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-ShellBlock.getRotationDegrees(state)));
        poseStack.translate(0.0D, 0.0D, 0.0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(0.25F, 0.25F, 0.25F);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, shell.getLevel(), 0);
        poseStack.popPose();
    }
}
