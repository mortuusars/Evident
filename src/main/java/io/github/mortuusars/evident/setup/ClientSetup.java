package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.client.Rendering;
import io.github.mortuusars.evident.client.renderer.ChoppingBlockRenderer;
import io.github.mortuusars.evident.client.renderer.entity.TorchArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        Rendering.setRenderLayers();
        registerItemProperties(event);
        registerEntityRenderers(event);
    }

    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypes.CHOPPING_BLOCK.get(), ChoppingBlockRenderer::new);
    }

    private static void registerItemProperties(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(Items.BOW, new ResourceLocation("torch"),
                    ((pStack, pLevel, pEntity, pSeed) -> pEntity != null && pEntity.getProjectile(pStack).is(Items.TORCH) ? 1F : 0F));
            ItemProperties.register(Items.BOW, new ResourceLocation("torch_pulled_animation"),
                    ((pStack, pLevel, pEntity, pSeed) -> {
                        if (pLevel == null) return 0F;

                        float v = pLevel.getGameTime() % 20 / 20F;
                        return v;
                    }));
        });
    }

    private static void registerEntityRenderers(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.TORCH_ARROW.get(), TorchArrowRenderer::new);
        });
    }
}