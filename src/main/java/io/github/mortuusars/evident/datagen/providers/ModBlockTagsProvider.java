package io.github.mortuusars.evident.datagen.providers;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.setup.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Evident.ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.BURNABLE).add(Blocks.COBWEB);
        tag(ModTags.BURNABLE).add(Blocks.DEAD_BUSH);
    }
}
