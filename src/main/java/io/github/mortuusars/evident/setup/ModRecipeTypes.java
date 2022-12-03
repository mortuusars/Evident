package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.recipe.ChoppingBlockRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE.key(), Evident.ID);

    public static final RegistryObject<RecipeType<ChoppingBlockRecipe>> CHOPPING = RECIPE_TYPES.register("chopping",
            () -> new RecipeType<>() {
                public String toString() {
                    return Evident.ID + ":chopping";
            }
    });
}
