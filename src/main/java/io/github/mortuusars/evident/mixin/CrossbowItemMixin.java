package io.github.mortuusars.evident.mixin;

import io.github.mortuusars.evident.content.torch_shooting.TorchShooting;
import io.github.mortuusars.evident.content.torch_shooting.TorchType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends ProjectileWeaponItem {
    public CrossbowItemMixin(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "getArrow", at = @At("HEAD"), cancellable = true)
    private static void getArrow(Level level, LivingEntity livingEntity, ItemStack crossbowStack, ItemStack ammoStack, CallbackInfoReturnable<AbstractArrow> cir) {
        if (TorchType.isTorch(ammoStack)) {
            AbstractArrow arrow = TorchShooting.createArrowFromItem(ammoStack, level, livingEntity);
            if (livingEntity instanceof Player) {
                arrow.setCritArrow(true);
            }
            arrow.setShotFromCrossbow(true);
            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, crossbowStack);
            if (i > 0) {
                arrow.setPierceLevel((byte)i);
            }
            cir.setReturnValue(arrow);
            cir.cancel();
        }
    }

    @Inject(method = "tryLoadProjectiles", at = @At("HEAD"), cancellable = true)
    private static void tryLoadProjectiles(LivingEntity shooter, ItemStack crossbowStack, CallbackInfoReturnable<Boolean> cir) {
        boolean isInCreative = shooter instanceof Player && ((Player)shooter).getAbilities().instabuild;
        boolean enchantedWithMultishot = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, crossbowStack) > 0;

        if (!isInCreative && enchantedWithMultishot) {
            ItemStack ammoStack = shooter.getProjectile(crossbowStack);
            if (TorchType.isTorch(ammoStack)) {
                for (int i = 0; i < 3; i++) {
                    if (loadProjectile(shooter, crossbowStack, ammoStack, false, false)) {
                        cir.setReturnValue(false);
                        cir.cancel();
                    }
                }

                cir.setReturnValue(true);
                cir.cancel();
            }
        }
    }

    @Shadow
    private static boolean loadProjectile(LivingEntity pShooter, ItemStack pCrossbowStack, ItemStack pAmmoStack, boolean pHasAmmo, boolean pIsCreative) {
        return false;
    }
}
