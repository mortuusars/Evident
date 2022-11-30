package io.github.mortuusars.evident.behaviour;

import com.google.common.collect.Lists;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.mortuusars.evident.config.CommonConfig;
import io.github.mortuusars.evident.entity.TorchArrow;
import io.github.mortuusars.evident.setup.ModItems;
import io.github.mortuusars.evident.setup.ModTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class TorchShooting {

    public static final DispenseItemBehavior DISPENSER_BEHAVIOR = new AbstractProjectileDispenseBehavior() {
        @Override
        protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
            TorchArrow arrow = new TorchArrow(level, position.x(), position.y(), position.z());
            arrow.pickup = AbstractArrow.Pickup.ALLOWED;
            if (stack.getItem() instanceof BlockItem item)
                arrow.setTorchItem(item);
            return arrow;
        }
    };

    public static boolean isSupportedProjectile(ItemStack stack) {
        return stack.is(ModTags.TORCH);
    }

    public static ItemStack getFirstSupportedProjectileFromInventory(Player player) {
        // Not checking for offhand/mainhand there, because it is checked in 'onGetProjectile'

        Inventory playerInventory = player.getInventory();
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack stack = playerInventory.getItem(i);
            if (isSupportedProjectile(stack))
                return stack;
        }

        return ItemStack.EMPTY;
    }

    @SubscribeEvent
    public static void onGetProjectile(LivingGetProjectileEvent event) {

        if (!CommonConfig.SHOOTING_TORCHES_ENABLED.get())
            return;

        if (event.getEntity() instanceof Player player) {
            ItemStack projectileItemStack = event.getProjectileItemStack();

            ItemStack offhandItem = player.getOffhandItem();
            // OffHand projectile always takes priority
            if (offhandItem.sameItem(projectileItemStack))
                return;
            else if (isSupportedProjectile(offhandItem)) {
                event.setProjectileItemStack(offhandItem);
                return;
            }

            ItemStack mainHandItem = player.getMainHandItem();
            // and MainHand too
            if (mainHandItem.sameItem(projectileItemStack))
                return;
            else if (isSupportedProjectile(player.getMainHandItem())) {
                event.setProjectileItemStack(player.getMainHandItem());
                return;
            }


            if (projectileItemStack.isEmpty()) { // No vanilla projectiles found, so we get any our projectile (if present)
                ItemStack projectile = getFirstSupportedProjectileFromInventory(player);
                event.setProjectileItemStack(projectile);
            }
            else {
                Inventory playerInventory = player.getInventory();
                int projectileSlotIndex = playerInventory.findSlotMatchingItem(projectileItemStack);

                /* It shouldn't be -1 there by normal means because we checked above for empty stack.
                   Doing this as a precaution. Maybe other mod gets the projectile from somewhere else. IDK. */
                if (projectileSlotIndex == -1) // I shouldn't be -1 there by normal means.
                    return;

                int firstSearchableIndex = CommonConfig.SHOOTING_TORCHES_IGNORE_HOTBAR.get() ? 8 : 0;

                // Checking only up to found projectileSlotIndex because further it is taking priority anyway
                for(int i = firstSearchableIndex; i < projectileSlotIndex; ++i) {
                    ItemStack itemStack = playerInventory.getItem(i);
                    if (isSupportedProjectile(itemStack)) {
                        event.setProjectileItemStack(itemStack);
                        return;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onArrowLoose(ArrowLooseEvent event) {

        if (!CommonConfig.SHOOTING_TORCHES_ENABLED.get())
            return;

        Player player = event.getPlayer();
        Level level = event.getWorld();
        ItemStack weapon = event.getBow();

        boolean shotsFired = false;

        if (weapon.getItem() instanceof CrossbowItem) {
            shotsFired = shootFromCrossbow(player, level, weapon);
        }
        else if (weapon.getItem() instanceof BowItem) {
            ItemStack projectile = player.getProjectile(weapon);
            int chargeTicks = event.getCharge();
            shotsFired = shootFromBow(player, level, weapon, projectile, chargeTicks);
        }

        if (shotsFired)
            event.setCanceled(true);
    }

    private static boolean shootFromBow(Player player, Level level, ItemStack weapon, ItemStack projectile, int chargeTicks) {
        if (!projectile.is(ModTags.TORCH))
            return false;

        float power = BowItem.getPowerForTime(chargeTicks);

        if (power >= 0.1F) {
            TorchArrow arrow = new TorchArrow(level, player);
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, power * 3.0F, 1F);
            if (projectile.getItem() instanceof BlockItem projBlockItem)
                arrow.setTorchItem(projBlockItem);

            level.addFreshEntity(arrow);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1F, 0.6F + (power * 0.6F) + (level.random.nextFloat() * 0.2F));

            if (!level.isClientSide) {
                weapon.hurtAndBreak(CommonConfig.SHOOTING_TORCHES_DURABILITY_COST.get(), player,
                        pl -> pl.broadcastBreakEvent(player.getUsedItemHand()));
            }

            if (!player.isCreative()) {
                projectile.shrink(1);
                if (projectile.isEmpty()) {
                    player.getInventory().removeItem(projectile);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(weapon.getItem()));
        }

        return true;
    }

    private static boolean shootFromCrossbow(Player player, Level level, ItemStack crossbowStack) {
//        List<ItemStack> chargedCrossbowProjectiles = getChargedCrossbowProjectiles(crossbowStack);

//        if (chargedCrossbowProjectiles.isEmpty())
//            return false;

//        for (ItemStack projectile : chargedCrossbowProjectiles) {
//            if (projectile.is(ModTags.TORCH)) {
//                TorchArrow arrow = new TorchArrow(level, player);
//                arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, 3.0F, 1F);
//                if (projectile.getItem() instanceof BlockItem projBlockItem)
//                    arrow.setTorchItem(projBlockItem);
//
//                level.addFreshEntity(arrow);
//            }
//        }\

        InteractionHand weaponHand = player.getOffhandItem().sameItem(crossbowStack) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;

//        for(int i = 0; i < chargedCrossbowProjectiles.size(); ++i) {
//            ItemStack itemstack = chargedCrossbowProjectiles.get(i);
//            if (!itemstack.isEmpty()) {
//                if (i == 0) {
//                    shootCrossbowProjectile(level, player, weaponHand, crossbowStack, itemstack, afloat[i], flag, pVelocity, pInaccuracy, 0.0F);
//                } else if (i == 1) {
//                    shootProjectile(pLevel, pShooter, pUsedHand, pCrossbowStack, itemstack, afloat[i], flag, pVelocity, pInaccuracy, -10.0F);
//                } else if (i == 2) {
//                    shootProjectile(pLevel, pShooter, pUsedHand, pCrossbowStack, itemstack, afloat[i], flag, pVelocity, pInaccuracy, 10.0F);
//                }
//            }
//        }
//
//        onCrossbowShot(level, player, crossbowStack);

//        level.playSound(null, player.getX(), player.getY(), player.getZ(),
//                SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1F, 0.6F + 0.6F + (level.random.nextFloat() * 0.2F));

//        if (!level.isClientSide) {
//            crossbowStack.hurtAndBreak(CommonConfig.SHOOTING_TORCHES_DURABILITY_COST.get(), player,
//                    pl -> pl.broadcastBreakEvent(player.getUsedItemHand()));
//        }
//
//        player.awardStat(Stats.ITEM_USED.get(crossbowStack.getItem()));

        return true;
    }

    private static void shootCrossbowProjectile(Level level, Player player, InteractionHand hand, ItemStack crossbowStack, ItemStack projectile, float soundPitch, float velocity, float inaccuracy, float projectileAngle) {
        if (!level.isClientSide) {
            AbstractArrow arrow;
            if (projectile.is(ModTags.TORCH)) {
                TorchArrow torchArrow = new TorchArrow(level, player);
                torchArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, 3.0F, 1F);
                if (projectile.getItem() instanceof BlockItem projBlockItem) {
                    torchArrow.setTorchItem(projBlockItem);
                }
                arrow = torchArrow;
            }
            else {

                CrossbowItem.onCrossbowShot()

//                CrossbowItem.getArrow(level, player, crossbowStack, projectile);
                ArrowItem arrowitem = (ArrowItem)(projectile.getItem() instanceof ArrowItem ? projectile.getItem() : Items.ARROW);
                AbstractArrow abstractarrow = arrowitem.createArrow(level, projectile, player);
                abstractarrow.setCritArrow(true);
                abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
                abstractarrow.setShotFromCrossbow(true);
                int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, crossbowStack);
                if (i > 0) {
                    abstractarrow.setPierceLevel((byte)i);
                }
                arrow = abstractarrow;
            }

            Vec3 vec31 = player.getUpVector(1.0F);
            Quaternion quaternion = new Quaternion(new Vector3f(vec31), projectileAngle, true);
            Vec3 vec3 = player.getViewVector(1.0F);
            Vector3f vector3f = new Vector3f(vec3);
            vector3f.transform(quaternion);
            arrow.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocity, inaccuracy);

            level.addFreshEntity(arrow);

            int durabilityCost = projectile.is(ModTags.TORCH) ? CommonConfig.SHOOTING_TORCHES_DURABILITY_COST.get() : 1;

            crossbowStack.hurtAndBreak(durabilityCost, player, (pl) -> {
                pl.broadcastBreakEvent(hand);
            });
            level.addFreshEntity(arrow);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, soundPitch);
        }
    }
}
