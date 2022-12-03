package io.github.mortuusars.evident.recipe;

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
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.input);
        return nonnulllist;
    }

    public Ingredient getTool() {
        return this.tool;
    }

    public NonNullList<Ingredient> getIngredientsAndTool() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.input);
        nonnulllist.add(this.tool);
        return nonnulllist;
    }

    @Override
    public ItemStack assemble(RecipeWrapper inv) {
        return this.results.get(0).getStack().copy();
    }

    @Override
    public ItemStack getResultItem() {
        return this.results.get(0).getStack();
    }

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
    public boolean matches(RecipeWrapper container, Level pLevel) {
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
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CHOPPING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.CHOPPING.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ChoppingBlockRecipe> {

        @Override
        public ChoppingBlockRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            final String group = GsonHelper.getAsString(json, "group", "");

            final NonNullList<Ingredient> inputItems = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            final JsonObject toolObject = GsonHelper.getAsJsonObject(json, "tool");
            final Ingredient tool = Ingredient.fromJson(toolObject);
            if (inputItems.isEmpty()) {
                throw new JsonParseException("No ingredients for chopping recipe");
            } else if (inputItems.size() > 1) {
                throw new JsonParseException("Too many ingredients for chopping recipe! Please define only one ingredient");
            } else if (tool.isEmpty()) {
                throw new JsonParseException("No tool for chopping recipe");
            } else {
                final NonNullList<ChanceResult> results = readResults(GsonHelper.getAsJsonArray(json, "result"));
                if (results.size() > 4) {
                    throw new JsonParseException("Too many results for chopping recipe! The maximum quantity of unique results is 4");
                } else {
                    return new ChoppingBlockRecipe(recipeId, group, inputItems.get(0), results, tool);
                }
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();
            for (int i = 0; i < ingredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }
            return nonnulllist;
        }

        private static NonNullList<ChanceResult> readResults(JsonArray resultArray) {
            NonNullList<ChanceResult> results = NonNullList.create();
            for (JsonElement result : resultArray) {
                results.add(ChanceResult.deserialize(result));
            }
            return results;
        }

        @Nullable
        @Override
        public ChoppingBlockRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient input = Ingredient.fromNetwork(buffer);
            Ingredient tool = Ingredient.fromNetwork(buffer);

            int resultsCount = buffer.readVarInt();
            NonNullList<ChanceResult> results = NonNullList.withSize(resultsCount, ChanceResult.EMPTY);
            for (int i = 0; i < results.size(); ++i) {
                results.set(i, ChanceResult.read(buffer));
            }

            return new ChoppingBlockRecipe(recipeId, group, input, results, tool);
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
    }
}
