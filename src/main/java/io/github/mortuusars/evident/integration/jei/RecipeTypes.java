package io.github.mortuusars.evident.integration.jei;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.content.chopping_block.ChoppingBlockRecipe;
import mezz.jei.api.recipe.RecipeType;

public class RecipeTypes {
    public static final RecipeType<ChoppingBlockRecipe> CHOPPING = RecipeType.create(Evident.ID, "chopping", ChoppingBlockRecipe.class);
}
