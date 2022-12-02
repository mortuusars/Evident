package io.github.mortuusars.evident.behaviour.torch_shooting;

import io.github.mortuusars.evident.setup.ModTags;
import net.minecraft.world.item.ItemStack;

public enum TorchType {
    NONE(0F),
    TORCH(0.1F),
    SOUL(0.2F),
    REDSTONE(0.3F);

    private float value;

    TorchType(float value) {
        this.value = value;
    }

    /*
    * Value is used in ItemOverrides to choose model for bow/crossbow.
    */
    public float getFloatValue() {
        return value;
    }

    public static boolean isTorch(ItemStack itemStack) {
        return getFromStack(itemStack) != NONE;
    }

    public static TorchType getFromStack(ItemStack stack) {
        if (stack.is(ModTags.TORCH))
            return TORCH;
        else if (stack.is(ModTags.SOUL_TORCH))
            return SOUL;
        else if (stack.is(ModTags.REDSTONE_TORCH))
            return REDSTONE;

        return NONE;
    }
}
