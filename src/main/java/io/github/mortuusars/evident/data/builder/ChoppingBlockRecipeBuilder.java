package io.github.mortuusars.evident.data.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.recipe.ingredient.ChanceResult;
import io.github.mortuusars.evident.setup.ModRecipeSerializers;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Used to create Chopping recipes with data generation.
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ChoppingBlockRecipeBuilder {
    private final List<ChanceResult> results = new ArrayList<>(4);
    private final Ingredient ingredient;
    private final Ingredient tool;

    private ChoppingBlockRecipeBuilder(Ingredient ingredient, Ingredient tool, ItemLike mainResult, int count, float chance) {
        this.results.add(new ChanceResult(new ItemStack(mainResult.asItem(), count), chance));
        this.ingredient = ingredient;
        this.tool = tool;
    }

    public static ChoppingBlockRecipeBuilder chopping(Ingredient ingredient, Ingredient tool, ItemLike mainResult, int count) {
        return new ChoppingBlockRecipeBuilder(ingredient, tool, mainResult, count, 1);
    }

    public static ChoppingBlockRecipeBuilder chopping(Ingredient ingredient, Ingredient tool, ItemLike mainResult, int count, float chance) {
        return new ChoppingBlockRecipeBuilder(ingredient, tool, mainResult, count, chance);
    }

    public static ChoppingBlockRecipeBuilder chopping(Ingredient ingredient, Ingredient tool, ItemLike mainResult) {
        return new ChoppingBlockRecipeBuilder(ingredient, tool, mainResult, 1, 1);
    }

    public ChoppingBlockRecipeBuilder addResult(ItemLike result) {
        return this.addResult(result, 1);
    }

    public ChoppingBlockRecipeBuilder addResult(ItemLike result, int count) {
        this.results.add(new ChanceResult(new ItemStack(result.asItem(), count), 1));
        return this;
    }

    public ChoppingBlockRecipeBuilder addResultWithChance(ItemLike result, float chance) {
        return this.addResultWithChance(result, chance, 1);
    }

    public ChoppingBlockRecipeBuilder addResultWithChance(ItemLike result, float chance, int count) {
        this.results.add(new ChanceResult(new ItemStack(result.asItem(), count), chance));
        return this;
    }

    public void build(Consumer<FinishedRecipe> consumerIn) {
        ResourceLocation location = ForgeRegistries.ITEMS.getKey(this.ingredient.getItems()[0].getItem());
        if (location.toString().equals("minecraft:barrier"))
            throw new IllegalStateException("Cannot get path from the ingredient: '" + ingredient.toJson() + "'. Provide custom save path.");
        this.build(consumerIn, location.getPath());
    }

    public void build(Consumer<FinishedRecipe> consumerIn, String save) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.ingredient.getItems()[0].getItem());
        ResourceLocation saveLocation = new ResourceLocation(Evident.ID, "chopping/" + save);
        if (saveLocation.equals(resourcelocation)) {
            throw new IllegalStateException("Chopping Recipe '" + save + "' should remove its 'save' argument as it's the same as the default.");
        } else {
            this.build(consumerIn, saveLocation);
        }
    }

    public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
        // Sort outputs with larger chance first:
        results.sort((result1, result2) -> result1.getChance() > result2.getChance() ? -1 : 1);
        consumerIn.accept(new ChoppingBlockRecipeBuilder.Result(id, this.ingredient, this.tool, this.results));
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final Ingredient tool;
        private final List<ChanceResult> results;

        public Result(ResourceLocation id, Ingredient ingredient, Ingredient tool, List<ChanceResult> results) {
            this.id = id;
            this.ingredient = ingredient;
            this.tool = tool;
            this.results = results;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", this.ingredient.toJson());
            json.add("tool", this.tool.toJson());

            JsonArray arrayResults = new JsonArray();
            for (ChanceResult result : this.results) {
                arrayResults.add(result.toJson());
            }
            json.add("results", arrayResults);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipeSerializers.CHOPPING.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
