package com.candle.delta_delight.client.renderer;

import com.candle.delta_delight.content.ReconArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ReconArrowRenderer extends ArrowRenderer<ReconArrow> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("minecraft", "textures/entity/projectiles/spectral_arrow.png");

    public ReconArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ReconArrow arrow) {
        return TEXTURE;
    }
}
