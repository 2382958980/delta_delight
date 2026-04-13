package com.candle.delta_delight.client.screen;

import com.candle.delta_delight.DeltaDelight;
import com.candle.delta_delight.menu.ShakerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ShakerScreen extends AbstractContainerScreen<ShakerMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DeltaDelight.MODID, "textures/gui/shaker_ui.png");
    private static final int LABEL_COLOR = 0x404040;

    public ShakerScreen(ShakerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = Integer.MAX_VALUE;
        titleLabelY = Integer.MAX_VALUE;
        inventoryLabelX = Integer.MAX_VALUE;
        inventoryLabelY = Integer.MAX_VALUE;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawCenteredLabel(guiGraphics, Component.translatable("container.delta_delight.shaker"), 89, 6);
        drawCenteredLabel(guiGraphics, Component.translatable("container.delta_delight.shaker.inventory"), 20, 72);
    }

    private void drawCenteredLabel(GuiGraphics guiGraphics, Component text, int centerX, int y) {
        guiGraphics.drawString(font, text, centerX - font.width(text) / 2, y, LABEL_COLOR, false);
    }
}
