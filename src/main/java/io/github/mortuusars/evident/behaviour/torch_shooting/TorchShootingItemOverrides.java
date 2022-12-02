package io.github.mortuusars.evident.behaviour.torch_shooting;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TorchShootingItemOverrides {

    /**
     * Called on ClientSetupEvent with enqueueWork.
     */
    public static void registerItemProperties() {
        ItemProperties.register(Items.BOW, new ResourceLocation("torch"), TorchShootingItemOverrides::getTorchOverrideValue);
        ItemProperties.register(Items.BOW, new ResourceLocation("torch_pulled_animation"), TorchShootingItemOverrides::getTorchPulledValue);

        ItemProperties.register(Items.CROSSBOW, new ResourceLocation("torch"), TorchShootingItemOverrides::getCrossbowTorchOverrideValue);
        ItemProperties.register(Items.CROSSBOW, new ResourceLocation("torch_animation"), TorchShootingItemOverrides::getTorchPulledValue);
    }

    public static float getTorchOverrideValue(ItemStack stack, ClientLevel level, LivingEntity livingEntity, int seed) {
        return livingEntity != null ? TorchType.getFromStack(livingEntity.getProjectile(stack)).getFloatValue() : 0F;
    }

    public static float getCrossbowTorchOverrideValue(ItemStack stack, ClientLevel level, LivingEntity livingEntity, int seed) {
        for (ItemStack projectileStack : CrossbowItem.getChargedProjectiles(stack)) {
            return TorchType.getFromStack(projectileStack).getFloatValue();
        }
        return 0F;
    }

    public static float getTorchPulledValue(ItemStack stack, ClientLevel level, LivingEntity livingEntity, int seed) {
        return level != null ? level.getGameTime() % 20 / 20F : 0F;
    }
}
