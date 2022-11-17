package io.github.mortuusars.evident.client;

import io.github.mortuusars.evident.setup.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public class Rendering {
    public static void setRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COBWEB_CORNER.get(), RenderType.cutout());
    }
}
