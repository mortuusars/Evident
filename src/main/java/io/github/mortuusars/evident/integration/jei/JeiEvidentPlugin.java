package io.github.mortuusars.evident.integration.jei;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.content.chopping_block.ChoppingBlockRecipe;
import io.github.mortuusars.evident.integration.jei.category.ChoppingRecipeCategory;
import io.github.mortuusars.evident.setup.ModItems;
import io.github.mortuusars.evident.setup.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class JeiEvidentPlugin implements IModPlugin {
    private static final ResourceLocation ID = Evident.resource("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ChoppingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<ChoppingBlockRecipe> choppingRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.CHOPPING.get());
        registration.addRecipes(RecipeTypes.CHOPPING, choppingRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.CHOPPING_BLOCK.get()), RecipeTypes.CHOPPING);
        registration.addRecipeCatalyst(new ItemStack(ModItems.CHIPPED_CHOPPING_BLOCK.get()), RecipeTypes.CHOPPING);
        registration.addRecipeCatalyst(new ItemStack(ModItems.DAMAGED_CHOPPING_BLOCK.get()), RecipeTypes.CHOPPING);
    }
}
