package io.github.mortuusars.evident.behaviour.torch_shooting;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class TorchShootingItemOverrides {
    public static float getTorchOverrideValue(ItemStack stack, ClientLevel level, LivingEntity livingEntity, int seed) {
        return livingEntity != null ? TorchType.getFromStack(livingEntity.getProjectile(stack)).getFloatValue() : 0F;
    }

    public static float getTorchPulledValue(ItemStack stack, ClientLevel level, LivingEntity livingEntity, int seed) {
        return level != null ? level.getGameTime() % 20 / 20F : 0F;
    }
}
