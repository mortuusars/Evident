package io.github.mortuusars.evident.datagen.providers;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.setup.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, Evident.ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.IGNITER)
                .add(Items.TORCH)
                .add(Items.BLAZE_POWDER)
                .add(Items.BLAZE_ROD)
                .add(Items.FLINT_AND_STEEL);
    }
}