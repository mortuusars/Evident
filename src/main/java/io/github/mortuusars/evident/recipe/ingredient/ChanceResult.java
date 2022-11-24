package io.github.mortuusars.evident.recipe.ingredient;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mortuusars.evident.Evident;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

/**
 * Credits to the Create team (and Farmer's Delight) for the implementation of results with chances!
 */
public class ChanceResult {
    public static final ChanceResult EMPTY = new ChanceResult(ItemStack.EMPTY, 1);

    private final ItemStack stack;
    private final float chance;

    public ChanceResult(ItemStack stack, float chance) {
        this.stack = stack;
        this.chance = chance;
    }

    public ItemStack getStack() {
        return stack;
    }

    public float getChance() {
        return chance;
    }

    public ItemStack rollOutput(Random rand) {
        int outputAmount = stack.getCount();
        for (int roll = 0; roll < stack.getCount(); roll++)
            if (rand.nextFloat() > chance)
                outputAmount--;
        if (outputAmount == 0)
            return ItemStack.EMPTY;
        ItemStack out = stack.copy();
        out.setCount(outputAmount);
        return out;
    }

    public JsonElement serialize() {
        JsonObject json = new JsonObject();

        ResourceLocation resourceLocation = stack.getItem().getRegistryName();
        json.addProperty("item", resourceLocation.toString());

        int count = stack.getCount();
        if (count != 1)
            json.addProperty("count", count);
        if (stack.hasTag())
            json.add("nbt", new JsonParser().parse(stack.getTag().toString()));
        if (chance != 1)
            json.addProperty("chance", chance);
        return json;
    }

    public static ChanceResult deserialize(JsonElement je) {
        if (!je.isJsonObject())
            throw new JsonSyntaxException("Must be a json object");

        JsonObject json = je.getAsJsonObject();
        String itemId = GsonHelper.getAsString(json, "item");
        int count = GsonHelper.getAsInt(json, "count", 1);
        float chance = GsonHelper.getAsFloat(json, "chance", 1);
        ItemStack itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)), count);

        if (GsonHelper.isValidPrimitive(json, "nbt")) {
            try {
                JsonElement element = json.get("nbt");
                itemstack.setTag(TagParser.parseTag(
                        element.isJsonObject() ? Evident.GSON.toJson(element) : GsonHelper.convertToString(element, "nbt")));
            }
            catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }

        return new ChanceResult(itemstack, chance);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(getStack());
        buf.writeFloat(getChance());
    }

    public static ChanceResult read(FriendlyByteBuf buf) {
        return new ChanceResult(buf.readItem(), buf.readFloat());
    }
}
