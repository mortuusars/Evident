package io.github.mortuusars.evident.setup;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ForgeTags {
    public static final TagKey<Item> TOOLS_AXES = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", "tools/axes"));

}
