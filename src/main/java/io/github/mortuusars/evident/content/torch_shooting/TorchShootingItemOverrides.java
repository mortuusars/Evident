package io.github.mortuusars.evident.content.torch_shooting;

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
     * These properties could then be used to select different models for torches.
     */
    public static void registerItemProperties() {
        ItemProperties.register(Items.BOW, new ResourceLocation("torch"), TorchShootingItemOverrides::getBowTorchOverrideValue);
        ItemProperties.register(Items.BOW, new ResourceLocation("torch_pulled_animation"), TorchShootingItemOverrides::getTorchPulledValue);

        ItemProperties.register(Items.CROSSBOW, new ResourceLocation("torch"), TorchShootingItemOverrides::getCrossbowTorchOverrideValue);
        ItemProperties.register(Items.CROSSBOW, new ResourceLocation("torch_animation"), TorchShootingItemOverrides::getTorchPulledValue);
    }

    /**
     * Ammo type. Torch - 0.1, Soul Torch - 0.2, Redstone Torch - 0.3. All other ammo - 0.0.
     */
    public static float getBowTorchOverrideValue(ItemStack stack, ClientLevel level, LivingEntity livingEntity, int seed) {
        return livingEntity != null ? TorchType.getFromStack(livingEntity.getProjectile(stack)).getFloatValue() : 0F;
    }

    /**
     * Same as for bow, but from a crossbow stack 'ChargedProjectiles' tag.
     */
    public static float getCrossbowTorchOverrideValue(ItemStack stack, ClientLevel level, LivingEntity livingEntity, int seed) {
        for (ItemStack projectileStack : CrossbowItem.getChargedProjectiles(stack)) {
            return TorchType.getFromStack(projectileStack).getFloatValue();
        }
        return 0F;
    }

    /**
     * Value for a looping animation. 0.05 per tick.
     */
    public static float getTorchPulledValue(ItemStack stack, ClientLevel level, LivingEntity livingEntity, int seed) {
        return level != null ? level.getGameTime() % 20 / 20F : 0F;
    }
}
