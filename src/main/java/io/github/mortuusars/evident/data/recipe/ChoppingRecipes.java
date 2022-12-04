package io.github.mortuusars.evident.data.recipe;

import io.github.mortuusars.evident.data.builder.ChoppingBlockRecipeBuilder;
import io.github.mortuusars.evident.setup.ForgeTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Map;
import java.util.function.Consumer;

public class ChoppingRecipes {
    public static void register(Consumer<FinishedRecipe> consumer) {
        woodChopping(consumer);
    }

    private static void woodChopping(Consumer<FinishedRecipe> consumer) {
        Map<ItemLike, ItemLike> woods = Map.of(
                Items.OAK_WOOD, Items.OAK_PLANKS,
                Items.DARK_OAK_WOOD, Items.DARK_OAK_PLANKS,
                Items.BIRCH_WOOD, Items.BIRCH_PLANKS,
                Items.SPRUCE_WOOD, Items.SPRUCE_PLANKS,
                Items.JUNGLE_WOOD, Items.JUNGLE_PLANKS
        );

        for (var entry : woods.entrySet()) {
            ChoppingBlockRecipeBuilder.chopping(Ingredient.of(entry.getKey()), Ingredient.of(ForgeTags.TOOLS_AXES), entry.getValue(), 4)
                    .addResultWithChance(entry.getValue(), 0.4F, 2)
                    .build(consumer);
        }
    }
}
