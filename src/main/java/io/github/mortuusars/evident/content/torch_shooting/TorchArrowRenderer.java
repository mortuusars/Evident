package io.github.mortuusars.evident.content.torch_shooting;

import io.github.mortuusars.evident.content.torch_shooting.arrow.TorchArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TorchArrowRenderer extends ArrowRenderer<TorchArrow> {
    public static final ResourceLocation TORCH_ARROW_LOCATION = new ResourceLocation("evident:textures/entity/projectiles/torch_arrow.png");
    public static final ResourceLocation SOUL_TORCH_ARROW_LOCATION = new ResourceLocation("evident:textures/entity/projectiles/soul_torch_arrow.png");
    public static final ResourceLocation REDSTONE_TORCH_ARROW_LOCATION = new ResourceLocation("evident:textures/entity/projectiles/redstone_torch_arrow.png");

    private final TorchType type;

    public TorchArrowRenderer(EntityRendererProvider.Context context, TorchType type) {
        super(context);
        this.type = type;
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull TorchArrow entity) {
        return switch (type) {
            case NONE, TORCH -> TORCH_ARROW_LOCATION;
            case SOUL -> SOUL_TORCH_ARROW_LOCATION;
            case REDSTONE -> REDSTONE_TORCH_ARROW_LOCATION;
        };
    }
}
