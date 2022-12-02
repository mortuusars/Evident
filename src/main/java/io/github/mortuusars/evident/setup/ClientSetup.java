package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.behaviour.torch_shooting.TorchShootingItemOverrides;
import io.github.mortuusars.evident.behaviour.torch_shooting.TorchType;
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
            TorchShootingItemOverrides.registerItemProperties();
        });
    }

    private static void registerEntityRenderers(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.TORCH_ARROW.get(), context -> new TorchArrowRenderer(context, TorchType.TORCH));
            EntityRenderers.register(ModEntities.SOUL_TORCH_ARROW.get(), context -> new TorchArrowRenderer(context, TorchType.SOUL));
            EntityRenderers.register(ModEntities.REDSTONE_TORCH_ARROW.get(), context -> new TorchArrowRenderer(context, TorchType.REDSTONE));
        });
    }
}