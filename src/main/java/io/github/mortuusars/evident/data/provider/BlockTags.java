package io.github.mortuusars.evident.data.provider;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.setup.ModBlocks;
import io.github.mortuusars.evident.setup.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BlockTags extends BlockTagsProvider {
    public BlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Evident.ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.BURNABLE)
                .add(Blocks.COBWEB)
                .add(Blocks.DEAD_BUSH)
                .add(ModBlocks.COBWEB_CORNER.get());

        tag(ModTags.CHOPPING_BLOCK)
                .add(ModBlocks.CHOPPING_BLOCK.get());
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.CHOPPING_BLOCK.get());

        tag(ModTags.COBWEB)
                .add(Blocks.COBWEB)
                .add(ModBlocks.COBWEB_CORNER.get());
    }
}
