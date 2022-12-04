package io.github.mortuusars.evident.events;

import io.github.mortuusars.evident.content.torch_shooting.TorchShootingItemOverrides;
import io.github.mortuusars.evident.content.torch_shooting.TorchType;
import io.github.mortuusars.evident.content.chopping_block.ChoppingBlockRenderer;
import io.github.mortuusars.evident.content.torch_shooting.TorchArrowRenderer;
import io.github.mortuusars.evident.setup.ModBlockEntityTypes;
import io.github.mortuusars.evident.setup.ModBlocks;
import io.github.mortuusars.evident.setup.ModEntities;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        setRenderLayers();
        registerItemProperties(event);
        registerEntityRenderers(event);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypes.CHOPPING_BLOCK.get(), ChoppingBlockRenderer::new);
    }

    public static void setRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COBWEB_CORNER.get(), RenderType.cutout());
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