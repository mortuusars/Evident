package io.github.mortuusars.evident.content.chopping_block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.mortuusars.evident.setup.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

public class ChoppingBlockRenderer implements BlockEntityRenderer<ChoppingBlockBlockEntity> {

    public ChoppingBlockRenderer(BlockEntityRendererProvider.Context pContext) { }

    @Override
    public void render(ChoppingBlockBlockEntity choppingBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack storedItemStack = choppingBlockEntity.getStoredItem();

        if (!storedItemStack.isEmpty()) {

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

            boolean isBlockItem = itemRenderer.getModel(storedItemStack, choppingBlockEntity.getLevel(), null, 0)
                    .isGui3d();

            poseStack.pushPose();

            float itemAngle = choppingBlockEntity.getItemAngle();

            if (choppingBlockEntity.isStoringTool()) {
                renderStoredTool(poseStack, itemAngle);
            }
            else if (isBlockItem)
                renderBlock(poseStack, itemAngle);
            else {
                renderItem(poseStack, itemAngle, storedItemStack.is(ModTags.RENDER_UPRIGHT));
            }

            itemRenderer.renderStatic(storedItemStack, ItemTransforms.TransformType.NONE, packedLight,
                    packedOverlay, poseStack, bufferSource, (int)choppingBlockEntity.getBlockPos().asLong());
            poseStack.popPose();
        }
    }

    private void renderStoredTool(PoseStack poseStack, double angle) {
        float scale = 0.7F;
        // Position on top face, slightly sticking into block:
        poseStack.translate(0.5D, ChoppingBlockBlockEntity.BLOCK_HEIGHT + scale / 2 - 0.1f, 0.5D);
        // Rotate upside down:
        poseStack.mulPose(Vector3f.XP.rotationDegrees(180));
        // Rotate Y towards player:
        poseStack.mulPose(Vector3f.YP.rotationDegrees((float)angle - 90));
        poseStack.scale(scale, scale, scale);
    }

    private void renderBlock(PoseStack poseStack, double angle) {
        float scale = 0.35F;
        // Position on top face
        poseStack.translate(0.5D, ChoppingBlockBlockEntity.BLOCK_HEIGHT + scale / 2, 0.5D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees((float) angle));
        poseStack.scale(scale, scale, scale);
    }

    private void renderItem(PoseStack poseStack, double angle, boolean isUpright) {
        float scale = 0.45F;
        if (isUpright) {
            // Position on top face, standing
            poseStack.translate(0.5D, ChoppingBlockBlockEntity.BLOCK_HEIGHT + scale / 2, 0.5D);
            // Rotate Y, should be before X rotation
            poseStack.mulPose(Vector3f.YP.rotationDegrees((float) angle));
        }
        else {
            // Position on top face
            poseStack.translate(0.5D, ChoppingBlockBlockEntity.BLOCK_HEIGHT + scale / 32, 0.5D);
            // Rotate Y, should be before X rotation
            poseStack.mulPose(Vector3f.YP.rotationDegrees((float) angle));
            // Rotate laying down
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
        }
        poseStack.scale(scale, scale, scale);
    }
}
