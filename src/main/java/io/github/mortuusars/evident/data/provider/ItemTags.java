package io.github.mortuusars.evident.data.provider;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.setup.ModItems;
import io.github.mortuusars.evident.setup.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ItemTags extends ItemTagsProvider {
    public ItemTags(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, Evident.ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.IGNITER)
                .add(Items.TORCH)
                .add(Items.BLAZE_POWDER)
                .add(Items.BLAZE_ROD)
                .add(Items.FLINT_AND_STEEL);

        tag(ModTags.CHOPPING_BLOCK_ITEM)
                .add(ModItems.CHOPPING_BLOCK.get())
                .add(ModItems.CHIPPED_CHOPPING_BLOCK.get())
                .add(ModItems.DAMAGED_CHOPPING_BLOCK.get());

        tag(ModTags.CHOPPING_BLOCK_WEDGEABLE)
                .add(Items.WOODEN_AXE)
                .add(Items.IRON_AXE)
                .add(Items.GOLDEN_AXE)
                .add(Items.DIAMOND_AXE)
                .add(Items.NETHERITE_AXE);
    }
}