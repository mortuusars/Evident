package io.github.mortuusars.evident.data.provider;


import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.setup.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {
    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Evident.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture(ModBlocks.COBWEB_CORNER.get().getRegistryName().getPath(),
                mcLoc("item/generated"), "layer0", modLoc("block/cobweb_corner"));

        withExistingParent(ModBlocks.CHOPPING_BLOCK.get().getRegistryName().getPath(), modLoc("block/chopping_block"));
        withExistingParent(ModBlocks.CHIPPED_CHOPPING_BLOCK.get().getRegistryName().getPath(), modLoc("block/chipped_chopping_block"));
        withExistingParent(ModBlocks.DAMAGED_CHOPPING_BLOCK.get().getRegistryName().getPath(), modLoc("block/damaged_chopping_block"));
    }
}
