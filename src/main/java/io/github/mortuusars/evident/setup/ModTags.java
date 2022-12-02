package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.Evident;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("ConstantConditions")
public class ModTags {
    public static final TagKey<Block> BURNABLE = BlockTags.create(new ResourceLocation(Evident.ID, "burnables"));
    public static final TagKey<Item> IGNITER = ItemTags.create(new ResourceLocation(Evident.ID, "igniters"));

    public static final TagKey<Item> TORCH = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation("forge:torches"));
    public static final TagKey<Item> SOUL_TORCH = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation("forge:soul_torches"));
    public static final TagKey<Item> REDSTONE_TORCH = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation("forge:redstone_torches"));

    public static final TagKey<Block> COBWEB = ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation("forge", "cobwebs"));

    public static final TagKey<Item> CHOPPING_BLOCK_WEDGEABLE = ItemTags.create(Evident.resource("chopping_block/wedgeables"));
}
