package io.github.mortuusars.evident.data.recipe;

import io.github.mortuusars.evident.data.builder.ChoppingBlockRecipeBuilder;
import io.github.mortuusars.evident.setup.ForgeTags;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChoppingRecipes {
    public static void register(Consumer<FinishedRecipe> consumer) {
        woodChopping(consumer);
    }

    private static void woodChopping(Consumer<FinishedRecipe> consumer) {
        BiConsumer<ItemLike, ItemLike> wood = (input, output) ->
                ChoppingBlockRecipeBuilder.chopping(Ingredient.of(input), Ingredient.of(ForgeTags.TOOLS_AXES), output, 4)
                    .addResultWithChance(output, 0.4F, 2)
                    .build(consumer);

        wood.accept(Items.OAK_WOOD, Items.OAK_PLANKS);
        wood.accept(Items.SPRUCE_WOOD, Items.SPRUCE_PLANKS);
        wood.accept(Items.BIRCH_WOOD, Items.BIRCH_PLANKS);
        wood.accept(Items.JUNGLE_WOOD, Items.JUNGLE_PLANKS);
        wood.accept(Items.ACACIA_WOOD, Items.ACACIA_PLANKS);
        wood.accept(Items.DARK_OAK_WOOD, Items.DARK_OAK_PLANKS);
        wood.accept(Items.CRIMSON_HYPHAE, Items.CRIMSON_PLANKS);
        wood.accept(Items.WARPED_HYPHAE, Items.WARPED_PLANKS);

        wood.accept(Items.OAK_LOG, Items.OAK_PLANKS);
        wood.accept(Items.SPRUCE_LOG, Items.SPRUCE_PLANKS);
        wood.accept(Items.BIRCH_LOG, Items.BIRCH_PLANKS);
        wood.accept(Items.JUNGLE_LOG, Items.JUNGLE_PLANKS);
        wood.accept(Items.ACACIA_LOG, Items.ACACIA_PLANKS);
        wood.accept(Items.DARK_OAK_LOG, Items.DARK_OAK_PLANKS);
        wood.accept(Items.CRIMSON_STEM, Items.CRIMSON_PLANKS);
        wood.accept(Items.WARPED_STEM, Items.WARPED_PLANKS);

        wood.accept(Items.STRIPPED_OAK_LOG, Items.OAK_PLANKS);
        wood.accept(Items.STRIPPED_SPRUCE_LOG, Items.SPRUCE_PLANKS);
        wood.accept(Items.STRIPPED_BIRCH_LOG, Items.BIRCH_PLANKS);
        wood.accept(Items.STRIPPED_JUNGLE_LOG, Items.JUNGLE_PLANKS);
        wood.accept(Items.STRIPPED_ACACIA_LOG, Items.ACACIA_PLANKS);
        wood.accept(Items.STRIPPED_DARK_OAK_LOG, Items.DARK_OAK_PLANKS);
        wood.accept(Items.STRIPPED_CRIMSON_STEM, Items.CRIMSON_PLANKS);
        wood.accept(Items.STRIPPED_WARPED_STEM, Items.WARPED_PLANKS);
    }
}
