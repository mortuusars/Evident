package io.github.mortuusars.evident.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.mortuusars.evident.block.entity.ChoppingBlockBlockEntity;
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

            if (choppingBlockEntity.isStoringTool()) {
                renderStoredTool(poseStack, choppingBlockEntity.getItemAngle());
            }
            else if (isBlockItem)
                renderBlock(poseStack);
            else {
                renderItem(poseStack);
            }

            itemRenderer.renderStatic(storedItemStack, ItemTransforms.TransformType.FIXED, packedLight,
                    packedOverlay, poseStack, bufferSource, (int)choppingBlockEntity.getBlockPos().asLong());
            poseStack.popPose();
        }
    }

    private void renderStoredTool(PoseStack poseStack, double angle) {
        poseStack.translate(0.5D, 1.17D, 0.5D);

        poseStack.mulPose(Vector3f.XP.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees((float)angle - 110));

        poseStack.scale(0.8F, 0.8F, 0.8F);
    }

    private void renderBlock(PoseStack poseStack) {
        poseStack.translate(0.5D, 1.05D, 0.5D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(8));
        poseStack.scale(0.7F, 0.7F, 0.7F);
    }

    private void renderItem(PoseStack poseStack) {
        poseStack.translate(0.5D, 0.96D, 0.5D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(8));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
        poseStack.scale(0.6F, 0.6F, 0.6F);
    }
}
