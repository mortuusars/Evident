package io.github.mortuusars.evident.behaviour.torch_shooting;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.mortuusars.evident.config.CommonConfig;
import io.github.mortuusars.evident.entity.RedstoneTorchArrow;
import io.github.mortuusars.evident.entity.SoulTorchArrow;
import io.github.mortuusars.evident.entity.TorchArrow;
import io.github.mortuusars.evident.setup.ModTags;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class TorchShooting {

    public static final DispenseItemBehavior DISPENSER_BEHAVIOR = new AbstractProjectileDispenseBehavior() {
        @Override
        protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
            TorchArrow arrow;

            if (stack.is(ModTags.SOUL_TORCH))
                arrow = new SoulTorchArrow(level, position.x(), position.y(), position.z());
            else if (stack.is(ModTags.REDSTONE_TORCH))
                arrow = new RedstoneTorchArrow(level, position.x(), position.y(), position.z());
            else
                arrow = new TorchArrow(level, position.x(), position.y(), position.z());

            arrow.pickup = AbstractArrow.Pickup.ALLOWED;
            if (stack.getItem() instanceof BlockItem item)
                arrow.setTorchItem(item);
            return arrow;
        }
    };

    public static boolean isTorchProjectile(ItemStack stack) {
        return TorchType.getFromStack(stack) != TorchType.NONE;
    }

    public static ItemStack getFirstSupportedProjectileFromInventory(Player player) {
        // Not checking for offhand/mainhand there, because it is checked in 'onGetProjectile'

        Inventory playerInventory = player.getInventory();
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack stack = playerInventory.getItem(i);
            if (isTorchProjectile(stack))
                return stack;
        }

        return ItemStack.EMPTY;
    }

    public static AbstractArrow createArrowFromItem(ItemStack itemStack, Level level, LivingEntity shooter) {
        if (itemStack.is(ModTags.TORCH)) {
            TorchArrow torchArrow = new TorchArrow(level, shooter);
            if (itemStack.getItem() instanceof BlockItem blockItem) {
                torchArrow.setTorchItem(blockItem);
            }
            return torchArrow;
        }
        else if (itemStack.is(ModTags.SOUL_TORCH)) {
            TorchArrow torchArrow = new SoulTorchArrow(level, shooter);
            if (itemStack.getItem() instanceof BlockItem blockItem) {
                torchArrow.setTorchItem(blockItem);
            }
            return torchArrow;
        }
        else if (itemStack.is(ModTags.REDSTONE_TORCH)) {
            TorchArrow torchArrow = new RedstoneTorchArrow(level, shooter);
            if (itemStack.getItem() instanceof BlockItem blockItem) {
                torchArrow.setTorchItem(blockItem);
            }
            return torchArrow;
        }
        else return new Arrow(level, shooter);
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
            else if (isTorchProjectile(offhandItem)) {
                event.setProjectileItemStack(offhandItem);
                return;
            }

            ItemStack mainHandItem = player.getMainHandItem();
            // and MainHand too
            if (mainHandItem.sameItem(projectileItemStack))
                return;
            else if (isTorchProjectile(player.getMainHandItem())) {
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
                    if (isTorchProjectile(itemStack)) {
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

        // Crossbow is damaged by 1 for each shoot by default - this handles increased durability cost:
        if (!level.isClientSide && weapon.getItem() instanceof CrossbowItem && CommonConfig.SHOOTING_TORCHES_DURABILITY_COST.get() > 1) {
            List<ItemStack> chargedProjectiles = CrossbowItem.getChargedProjectiles(weapon);

            for (ItemStack chargedProjectileStack : chargedProjectiles) {
                if (isTorchProjectile(chargedProjectileStack) && !weapon.isEmpty()) {
                    weapon.hurtAndBreak(CommonConfig.SHOOTING_TORCHES_DURABILITY_COST.get() - 1, player,
                            pl -> pl.broadcastBreakEvent(player.getUsedItemHand()));
                }
            }
            return;
        }

        // Crossbow shooting is handled by mixin

        if (weapon.getItem() instanceof BowItem) {
            ItemStack projectile = player.getProjectile(weapon);

            if (TorchType.getFromStack(projectile) == TorchType.NONE)
                return;

            float power = BowItem.getPowerForTime(event.getCharge());

            if (power >= 0.1F) {
                AbstractArrow arrow = createArrowFromItem(projectile, level, player);
                arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, power * 3.0F, 1F);

                if (power == 1.0F) {
                    arrow.setCritArrow(true);
                }

                int powerEnchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, weapon);
                if (powerEnchantmentLevel > 0) {
                    arrow.setBaseDamage(arrow.getBaseDamage() + (double)powerEnchantmentLevel * 0.5D + 0.5D);
                }

                int punchEnchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, weapon);
                if (punchEnchantmentLevel > 0) {
                    arrow.setKnockback(punchEnchantmentLevel);
                }

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
                event.setCanceled(true);
            }
        }
    }
}
