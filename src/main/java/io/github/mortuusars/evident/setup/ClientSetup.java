package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.client.Rendering;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        Rendering.setRenderLayers();
    }
}