package io.github.mortuusars.evident.content.chopping_block;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.mortuusars.evident.recipe.ingredient.ChanceResult;
import io.github.mortuusars.evident.setup.ModRecipeSerializers;
import io.github.mortuusars.evident.setup.ModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ChoppingBlockRecipe implements Recipe<RecipeWrapper> {

    private final ResourceLocation id;
    private final String group;
    private final Ingredient input;
    private final NonNullList<ChanceResult> results;
    private final Ingredient tool;

    public ChoppingBlockRecipe(ResourceLocation id, String group, Ingredient input, NonNullList<ChanceResult> results, Ingredient tool) {
        this.id = id;
        this.group = group;
        this.input = input;
        this.results = results;
        this.tool = tool;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull String getGroup() {
        return this.group;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.input);
        return nonnulllist;
    }

    public Ingredient getInput() {
        return this.input;
    }

    public Ingredient getTool() {
        return this.tool;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper inv) {
        return this.results.get(0).getStack().copy();
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return this.results.get(0).getStack();
    }

    /**
     * Gets all results as ItemStacks ignoring the chances.
     */
    public List<ItemStack> getResults() {
        return getRollableResults().stream()
                .map(ChanceResult::getStack)
                .collect(Collectors.toList());
    }

    public NonNullList<ChanceResult> getRollableResults() {
        return this.results;
    }

    public List<ItemStack> rollResults(Random rand) {
        List<ItemStack> results = new ArrayList<>();
        NonNullList<ChanceResult> rollableResults = getRollableResults();
        for (ChanceResult output : rollableResults) {
            ItemStack stack = output.rollOutput(rand);
            if (!stack.isEmpty())
                results.add(stack);
        }
        return results;
    }

    @Override
    public boolean matches(RecipeWrapper container, @NotNull Level pLevel) {
        if (container.isEmpty())
            return false;
        return input.test(container.getItem(0));
    }

    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.getMaxInputCount();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CHOPPING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.CHOPPING.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ChoppingBlockRecipe> {

        @Override
        public @NotNull ChoppingBlockRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {

            final String group = GsonHelper.getAsString(json, "group", "");

            final Ingredient ingredient = Ingredient.fromJson(json.getAsJsonObject("ingredient"));
            final JsonObject toolObject = GsonHelper.getAsJsonObject(json, "tool");
            final Ingredient tool = Ingredient.fromJson(toolObject);
            if (ingredient.isEmpty()) {
                throw new JsonParseException("No ingredients for chopping recipe");
            } else if (tool.isEmpty()) {
                throw new JsonParseException("No tool for chopping recipe");
            } else {
                final NonNullList<ChanceResult> results = NonNullList.create();
                JsonArray resultsJsonArray = GsonHelper.getAsJsonArray(json, "results");
                for (JsonElement result : resultsJsonArray) {
                    results.add(ChanceResult.fromJson(result));
                }
                if (results.size() > 4) {
                    throw new JsonParseException("Too many results for chopping recipe! The maximum quantity of unique results is 4");
                } else {
                    return new ChoppingBlockRecipe(recipeId, group, ingredient, results, tool);
                }
            }
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ChoppingBlockRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.input.toNetwork(buffer);
            recipe.tool.toNetwork(buffer);
            buffer.writeVarInt(recipe.results.size());
            for (ChanceResult result : recipe.results) {
                result.write(buffer);
            }
        }

        @SuppressWarnings("Java8ListReplaceAll")
        @Nullable
        @Override
        public ChoppingBlockRecipe fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient input = Ingredient.fromNetwork(buffer);
            Ingredient tool = Ingredient.fromNetwork(buffer);

            int resultsCount = buffer.readVarInt();
            NonNullList<ChanceResult> results = NonNullList.withSize(resultsCount, ChanceResult.EMPTY);
            for (int i = 0; i < results.size(); ++i) {
                results.set(i, ChanceResult.fromBuffer(buffer));
            }

            return new ChoppingBlockRecipe(recipeId, group, input, results, tool);
        }
    }
}