package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.Evident;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Block> BURNABLE = BlockTags.create(new ResourceLocation(Evident.ID, "burnable"));
    public static final TagKey<Item> IGNITER = ItemTags.create(new ResourceLocation(Evident.ID, "igniter"));
}
