package io.github.mortuusars.evident.setup;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.recipe.ChoppingBlockRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Evident.ID);

    public static final RegistryObject<RecipeSerializer<?>> CHOPPING = RECIPE_SERIALIZERS.register("chopping", ChoppingBlockRecipe.Serializer::new);
}
