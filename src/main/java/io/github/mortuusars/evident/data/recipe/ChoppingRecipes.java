package io.github.mortuusars.evident.data.recipe;

import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.data.builder.ChoppingBlockRecipeBuilder;
import io.github.mortuusars.evident.setup.ForgeTags;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChoppingRecipes {
    public static void register(Consumer<FinishedRecipe> consumer) {
        woodChopping(consumer);
    }

    private static void woodChopping(Consumer<FinishedRecipe> consumer) {

        BiConsumer<TagKey<Item>, ItemLike> logs = (inputTag, output) ->
                ChoppingBlockRecipeBuilder.chopping(Ingredient.of(inputTag), Ingredient.of(ForgeTags.TOOLS_AXES), output, 4)
                        .addResultWithChance(output, 0.4F, 2)
                        .build(consumer, "planks_from_" + inputTag.location().getPath());

        logs.accept(ItemTags.OAK_LOGS, Items.OAK_PLANKS);
        logs.accept(ItemTags.SPRUCE_LOGS, Items.SPRUCE_PLANKS);
        logs.accept(ItemTags.BIRCH_LOGS, Items.BIRCH_PLANKS);
        logs.accept(ItemTags.JUNGLE_LOGS, Items.JUNGLE_PLANKS);
        logs.accept(ItemTags.ACACIA_LOGS, Items.ACACIA_PLANKS);
        logs.accept(ItemTags.DARK_OAK_LOGS, Items.DARK_OAK_PLANKS);
        logs.accept(ItemTags.CRIMSON_STEMS, Items.CRIMSON_PLANKS);
        logs.accept(ItemTags.WARPED_STEMS, Items.WARPED_PLANKS);

        ChoppingBlockRecipeBuilder.chopping(Ingredient.of(ItemTags.PLANKS), Ingredient.of(ForgeTags.TOOLS_AXES), Items.STICK, 4)
                .addResultWithChance(Items.STICK, 0.5F, 1)
                .build(consumer, "sticks_from_planks");

        saplingAndSticksFromLeaves(Items.OAK_LEAVES, Items.OAK_SAPLING, 0.4F, 2, 0.5F, consumer);
        saplingAndSticksFromLeaves(Items.SPRUCE_LEAVES, Items.SPRUCE_SAPLING, 0.4F, 2, 0.5F, consumer);
        saplingAndSticksFromLeaves(Items.BIRCH_LEAVES, Items.BIRCH_SAPLING, 0.4F, 2, 0.5F, consumer);
        saplingAndSticksFromLeaves(Items.JUNGLE_LEAVES, Items.JUNGLE_SAPLING, 0.4F, 2, 0.5F, consumer);
        saplingAndSticksFromLeaves(Items.ACACIA_LEAVES, Items.ACACIA_SAPLING, 0.4F, 2, 0.5F, consumer);
        saplingAndSticksFromLeaves(Items.DARK_OAK_LEAVES, Items.DARK_OAK_SAPLING, 0.4F, 2, 0.5F, consumer);
        saplingAndSticksFromLeaves(Items.AZALEA_LEAVES, Items.AZALEA, 0.2F, 2, 0.5F, consumer);
        saplingAndSticksFromLeaves(Items.FLOWERING_AZALEA_LEAVES, Items.FLOWERING_AZALEA, 0.1F, 2, 0.5F, consumer);

        ChoppingBlockRecipeBuilder.chopping(Ingredient.of(Items.NETHER_WART_BLOCK), Ingredient.of(ForgeTags.TOOLS_AXES), Items.CRIMSON_FUNGUS, 1, 0.1F)
                .addResultWithChance(Items.CRIMSON_ROOTS, 0.2F, 1)
                .build(consumer);

        ChoppingBlockRecipeBuilder.chopping(Ingredient.of(Items.WARPED_WART_BLOCK), Ingredient.of(ForgeTags.TOOLS_AXES), Items.WARPED_FUNGUS, 1, 0.1F)
                .addResultWithChance(Items.WARPED_ROOTS, 0.2F, 1)
                .build(consumer);
    }

    private static void saplingAndSticksFromLeaves(ItemLike inputLeaves, ItemLike outputSapling, float saplingChance, int sticksCount, float stickChance, Consumer<FinishedRecipe> consumer) {
        ChoppingBlockRecipeBuilder.chopping(Ingredient.of(inputLeaves), Ingredient.of(ForgeTags.TOOLS_AXES), outputSapling, 1, saplingChance)
                .addResultWithChance(Items.STICK, stickChance, sticksCount)
                .build(consumer, "sapling_and_sticks_from_" + inputLeaves.asItem().getRegistryName().getPath());
    }
}
