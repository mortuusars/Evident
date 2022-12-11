package io.github.mortuusars.evident.integration.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.evident.Evident;
import io.github.mortuusars.evident.content.chopping_block.ChoppingBlockRecipe;
import io.github.mortuusars.evident.recipe.ingredient.ChanceResult;
import io.github.mortuusars.evident.setup.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class ChoppingRecipeCategory implements IRecipeCategory<ChoppingBlockRecipe> {
    public static final ResourceLocation UID = Evident.resource("chopping");
    private static final ResourceLocation TEXTURE = Evident.resource("textures/gui/jei/chopping_block.png");
    private static final int OUTPUT_X = 80;
    private static final int OUTPUT_Y = 20;
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slot;
    private final IDrawable slotChance;
    private final IDrawable choppingBlock;
    private final IDrawable arrow;
    private final IDrawable downArrow;
    private final IDrawable toolShadow;

    public ChoppingRecipeCategory(IGuiHelper guiHelper) {
        title = Evident.translate("gui.jei.category.chopping");
        background = guiHelper.createBlankDrawable(120, 65);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.CHOPPING_BLOCK.get()));
        slot = guiHelper.createDrawable(TEXTURE, 0, 0, 18, 18);
        slotChance = guiHelper.createDrawable(TEXTURE, 19, 0, 18, 18);
        choppingBlock = guiHelper.createDrawable(TEXTURE, 0, 33, 50, 50);
        arrow = guiHelper.createDrawable(TEXTURE, 0, 19, 17, 9);
        downArrow = guiHelper.createDrawable(TEXTURE, 18, 19, 22, 13);
        toolShadow = guiHelper.createDrawable(TEXTURE, 41, 19, 20, 5);
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @SuppressWarnings("removal")
    @Override
    public Class<? extends ChoppingBlockRecipe> getRecipeClass() {
        return ChoppingBlockRecipe.class;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChoppingBlockRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 17, 17)
                .addIngredients(VanillaTypes.ITEM_STACK, Arrays.asList(recipe.getInput().getItems()));

        builder.addSlot(RecipeIngredientRole.CATALYST, 46, 1)
                .addIngredients(VanillaTypes.ITEM_STACK, Arrays.asList(recipe.getTool().getItems()));

        NonNullList<ChanceResult> results = recipe.getRollableResults();

        int size = results.size();
        int centerX = size > 1 ? 0 : 9;
        int centerY = size > 2 ? 0 : 9;

        for (int i = 0; i < size; i++) {
            int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
            int yOffset = centerY + ((i / 2) * 19);
            ChanceResult chanceResult = results.get(i);
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X + xOffset, OUTPUT_Y + yOffset)
                    .addIngredient(VanillaTypes.ITEM_STACK, chanceResult.getStack())
                    .addTooltipCallback((recipeSlotView, tooltip) -> {
                        float chance = chanceResult.getChance();
                        if (chance != 1) {
                            tooltip.add(Evident.translate("gui.jei.chance", chance < 0.01 ? "<1" : (int) (chance * 100))
                                    .withStyle(ChatFormatting.GOLD));
                        }
                    });
        }
    }

    @Override
    public void draw(ChoppingBlockRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        choppingBlock.draw(stack, 4, 14);
        arrow.draw(stack, 59, 37);
        downArrow.draw(stack, 22, 3);
        toolShadow.draw(stack, 44, 14);

        NonNullList<ChanceResult> results = recipe.getRollableResults();

        int size = results.size();
        int centerX = size > 1 ? 0 : 9;
        int centerY = size > 2 ? 0 : 9;

        for (int i = 0; i < size; i++) {
            int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
            int yOffset = centerY + ((i / 2) * 19);
            if (results.get(i).getChance() != 1) {
                slotChance.draw(stack, OUTPUT_X - 1 + xOffset, OUTPUT_X - 1 + yOffset);
            } else {
                slot.draw(stack, OUTPUT_X - 1 + xOffset, OUTPUT_X - 1 + yOffset);
            }
        }
    }
}
