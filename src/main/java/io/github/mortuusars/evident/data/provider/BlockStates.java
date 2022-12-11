package io.github.mortuusars.evident.data.provider;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.content.chopping_block.ChoppingBlockBlock;
import io.github.mortuusars.evident.content.chopping_block.Damage;
import io.github.mortuusars.evident.content.cobweb.CobwebCornerBlock;
import io.github.mortuusars.evident.setup.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Evident.ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(ModBlocks.COBWEB_CORNER.get())
                .forAllStates(state ->
                    ConfiguredModel.builder()
                            .modelFile(models().getExistingFile(modLoc("block/cobweb_corner")))
                            .rotationY((int)state.getValue(CobwebCornerBlock.FACING).toYRot())
                            .rotationX(state.getValue(CobwebCornerBlock.VERTICAL_DIRECTION) == Direction.DOWN ? 90 : 0)
                            .build());

        getVariantBuilder(ModBlocks.CHOPPING_BLOCK.get())
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                        .modelFile(models().getExistingFile(modLoc("block/" + ModBlocks.CHOPPING_BLOCK.get().getRegistryName().getPath())))
                        .rotationY((int) state.getValue(ChoppingBlockBlock.FACING).toYRot())
                        .build(), BlockStateProperties.WATERLOGGED);

        getVariantBuilder(ModBlocks.CHIPPED_CHOPPING_BLOCK.get())
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                        .modelFile(models().getExistingFile(modLoc("block/" + ModBlocks.CHIPPED_CHOPPING_BLOCK.get().getRegistryName().getPath())))
                        .rotationY((int) state.getValue(ChoppingBlockBlock.FACING).toYRot())
                        .build(), BlockStateProperties.WATERLOGGED);

        getVariantBuilder(ModBlocks.DAMAGED_CHOPPING_BLOCK.get())
                .forAllStatesExcept(state -> ConfiguredModel.builder()
                        .modelFile(models().getExistingFile(modLoc("block/" + ModBlocks.DAMAGED_CHOPPING_BLOCK.get().getRegistryName().getPath())))
                        .rotationY((int) state.getValue(ChoppingBlockBlock.FACING).toYRot())
                        .build(), BlockStateProperties.WATERLOGGED);
    }
}
