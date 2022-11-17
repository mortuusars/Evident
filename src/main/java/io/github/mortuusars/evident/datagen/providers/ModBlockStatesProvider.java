package io.github.mortuusars.evident.datagen.providers;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.blocks.CobwebCornerBlock;
import io.github.mortuusars.evident.setup.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class ModBlockStatesProvider extends BlockStateProvider {

    public ModBlockStatesProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
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
    }
}
