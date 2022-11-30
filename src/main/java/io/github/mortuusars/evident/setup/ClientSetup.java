package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.client.Rendering;
import io.github.mortuusars.evident.client.renderer.ChoppingBlockRenderer;
import io.github.mortuusars.evident.client.renderer.entity.TorchArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BowItem;
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
                    (stack, level, livingEntity, seed) ->
                            livingEntity != null
                            && stack.getItem() instanceof BowItem
                            && livingEntity.isUsingItem()
                            && livingEntity.getProjectile(stack).is(ModTags.TORCH) ? 1F : 0F);
            ItemProperties.register(Items.BOW, new ResourceLocation("torch_pulled_animation"),
                    (stack, level, livingEntity, seed) -> level != null ? level.getGameTime() % 20 / 20F : 0F);

            ItemProperties.register(Items.CROSSBOW, new ResourceLocation("torch"),
                    (stack, level, livingEntity, seed) -> livingEntity != null
                            && stack.getItem() instanceof BowItem
                            && livingEntity.getProjectile(stack).is(ModTags.TORCH) ? 1F : 0F);
            ItemProperties.register(Items.CROSSBOW, new ResourceLocation("torch_pulled_animation"),
                    (stack, level, livingEntity, seed) -> level != null ? level.getGameTime() % 20 / 20F : 0F);
        });
    }

    private static void registerEntityRenderers(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.TORCH_ARROW.get(), TorchArrowRenderer::new);
        });
    }
}