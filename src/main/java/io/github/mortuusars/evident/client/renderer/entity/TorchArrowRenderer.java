package io.github.mortuusars.evident.client.renderer.entity;

import io.github.mortuusars.evident.entity.TorchArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TorchArrowRenderer extends ArrowRenderer<TorchArrow> {
    public static final ResourceLocation TORCH_ARROW_LOCATION = new ResourceLocation("evident:textures/entity/projectile/torch_arrow.png");

    public TorchArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull TorchArrow entity) {
        return new ResourceLocation("evident:textures/entity/projectiles/torch_arrow.png");
//        return TORCH_ARROW_LOCATION;
    }
}
