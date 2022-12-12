package io.github.mortuusars.evident.data.provider;

import io.github.mortuusars.evident.data.recipe.ChoppingRecipes;
import io.github.mortuusars.evident.setup.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {

        ShapelessRecipeBuilder.shapeless(ModItems.COBWEB_CORNER.get(), 9)
                .requires(Items.COBWEB, 3)
                .unlockedBy("has_cobweb", has(Items.COBWEB))
                .save(consumer);

        ChoppingRecipes.register(consumer);

        ShapedRecipeBuilder.shaped(ModItems.CHOPPING_BLOCK.get(), 1)
                .pattern("LL")
                .pattern("NN")
                .define('L', ItemTags.LOGS)
                .define('N', Tags.Items.NUGGETS_IRON)
                .unlockedBy("has_logs", has(ItemTags.LOGS))
                .save(consumer);
    }
}
