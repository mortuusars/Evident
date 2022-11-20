package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.client.Rendering;
import io.github.mortuusars.evident.client.renderer.ChoppingBlockRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        Rendering.setRenderLayers();
    }

    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypes.CHOPPING_BLOCK.get(), ChoppingBlockRenderer::new);
    }
}