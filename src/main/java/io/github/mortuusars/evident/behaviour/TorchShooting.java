package io.github.mortuusars.evident.behaviour;

import io.github.mortuusars.evident.config.CommonConfig;
import io.github.mortuusars.evident.entity.TorchArrow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TorchShooting {
    @SubscribeEvent
    public static void onArrowLoose(ArrowLooseEvent event) {

        Player player = event.getPlayer();
        ItemStack weapon = event.getBow();
        ItemStack projectile = player.getProjectile(weapon);

        if (projectile.is(Items.TORCH) && CommonConfig.SHOOTING_TORCHES_ENABLED.get()) {
            event.setCanceled(true);

            int chargeTicks = event.getCharge();
            float power = weapon.is(Items.CROSSBOW) ? 1F : BowItem.getPowerForTime(chargeTicks);

            if (power >= 0.075F) {
                Level level = player.getLevel();
                TorchArrow arrow = new TorchArrow(level, player);
                arrow.setBaseDamage(0.1D);
                arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, power * 3.0F, 0.5F);
                level.addFreshEntity(arrow);
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1F, 0.6F + (power * 0.6F) + (level.random.nextFloat() * 0.2F));

                if (!level.isClientSide) {
                    weapon.hurtAndBreak(CommonConfig.SHOOTING_TORCHES_DURABILITY_COST.get(), player, (pl) -> {
                        pl.broadcastBreakEvent(player.getUsedItemHand());
                    });
                }

                if (!player.isCreative()) {
                    projectile.shrink(1);
                    if (projectile.isEmpty()) {
                        player.getInventory().removeItem(projectile);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(weapon.getItem()));
            }
        }
    }

    @SubscribeEvent
    public static void onArrowNock(ArrowNockEvent event) {
        Player player = event.getPlayer();
        if (player instanceof ServerPlayer) {
            ItemStack bow = event.getBow();
            ItemStack projectile = player.getProjectile(bow);

            if (projectile.is(Items.TORCH)) {

            }
        }
    }

    @SubscribeEvent
    public static void onGetProjectile(LivingGetProjectileEvent event) {

    }
}
