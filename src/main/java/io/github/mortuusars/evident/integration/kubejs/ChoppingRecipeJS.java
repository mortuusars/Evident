package io.github.mortuusars.evident.integration.kubejs;

import com.google.gson.JsonArray;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.ListJS;
import io.github.mortuusars.evident.recipe.ingredient.ChanceResult;
import io.github.mortuusars.evident.setup.ForgeTags;

public class ChoppingRecipeJS extends RecipeJS {

    private IngredientJS tool = IngredientJS.of("#" + ForgeTags.TOOLS_AXES.location()); // Set default tool to axes.

    @Override
    public void create(ListJS listJS) {
        // recipes.evident.chopping('input', 'output')
        IngredientJS input = parseIngredientItem(listJS.get(0));
        ItemStackJS output = parseResultItem(listJS.get(1));

        inputItems.add(input);
        outputItems.add(output);
    }

    public ChoppingRecipeJS addResult(ItemStackJS output){
        outputItems.add(output);
        save();
        return this;
    }

    public ChoppingRecipeJS tool(IngredientJS tool) {
        this.tool = tool;
        save();
        return this;
    }

    @Override
    public void deserialize() {
        inputItems.add(IngredientJS.ingredientFromRecipeJson(json.get("ingredient")));
        tool = IngredientJS.ingredientFromRecipeJson(json.get("tool"));
        outputItems.addAll(parseResultItemList(json.get("results")));
    }

    @Override
    public void serialize() {
        if (serializeInputs) {
            json.add("ingredient", inputItems.get(0).toJson());
            json.add("tool", tool.toJson());
        }

        if (serializeOutputs) {
            int outputs = 0;
            JsonArray results = new JsonArray();
            for (ItemStackJS outputItem : outputItems) {
                if (outputs > 4)
                    throw new IllegalArgumentException("Chopping recipe cannot have more than 4 results.");

                float chance = (float) outputItem.getChance();
                ChanceResult chanceResult = new ChanceResult(outputItem.getItemStack(), outputItem.hasChance() ? chance : 1F);
                results.add(chanceResult.toJson());

                outputs++;
            }
            json.add("results", results);
        }
    }
}