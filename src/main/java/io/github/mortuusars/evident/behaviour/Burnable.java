package io.github.mortuusars.evident.behaviour;

import io.github.mortuusars.evident.config.CommonConfig;
import io.github.mortuusars.evident.setup.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class Burnable {

    public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {

        if (!CommonConfig.BURNABLE_ENABLED.get()) // Disabled
            return;

        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemStack();

        if (player == null || player.getCooldowns().isOnCooldown(itemStack.getItem())
            || !ForgeRegistries.ITEMS.tags().getTag(ModTags.IGNITER).contains(itemStack.getItem()))
            return;

        Level level = event.getPlayer().level;
        BlockPos blockPos = event.getPos();
        BlockState clickedBlockState = level.getBlockState(blockPos);


        if (clickedBlockState.is(ModTags.BURNABLE)){
            Random random = level.getRandom();

            // Burn
            if (level.isClientSide) {
                spawnParticles(level, blockPos, clickedBlockState);
            }
            else {
                level.removeBlock(blockPos, false);
            }

            // Sound
            level.playSound(event.getPlayer(), blockPos, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1F, random.nextFloat() * 0.6F + 1.2F);
            if (itemStack.is(Items.FLINT_AND_STEEL)) {
                level.playSound(player, blockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
            }

            // Damage/Consume item
            if (!player.isCreative()) {
                if (itemStack.isDamageableItem() && CommonConfig.BURNABLE_DAMAGE_ITEM.get()) {
                    itemStack.hurtAndBreak(1, player, (p) -> {
                        p.broadcastBreakEvent(event.getHand());
                    });
                }

                if (!itemStack.isDamageableItem() && CommonConfig.BURNABLE_CONSUME_ITEM.get()) {
                    itemStack.shrink(1);
                }
            }

            // Add cooldown
            player.getCooldowns().addCooldown(itemStack.getItem(), 10);

            player.swing(event.getHand());

            // Cancel further actions (eg: torch would be placed at pos if we didn't cancel)
            event.setCanceled(true);
        }
    }

    private static void spawnParticles(Level level, BlockPos blockPos, BlockState clickedBlockState) {
        Random random = level.getRandom();
        AABB shapeBounds = clickedBlockState.getShape(level, blockPos).bounds();

        double sizeX = shapeBounds.maxX - shapeBounds.minX;
        double sizeY = shapeBounds.maxY - shapeBounds.minY;
        double sizeZ = shapeBounds.maxZ - shapeBounds.minZ;

        int particles = (int)(sizeX / 0.3 + sizeY / 0.3 + sizeZ / 0.3);

        for (int i = 0; i < particles; i++) {
            double x = blockPos.getX() + shapeBounds.minX + random.nextDouble() * sizeX;
            double y = blockPos.getY() + shapeBounds.minY + random.nextDouble() * sizeY;
            double z = blockPos.getZ() + shapeBounds.minZ + random.nextDouble() * sizeZ;
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0d, 0.05d, 0d);
            level.addParticle(ParticleTypes.ASH, x, y, z, 0d, 0.02d, 0d);
        }

        for (int i = 0; i < particles + 1; i++) {
            double x = blockPos.getX() + shapeBounds.minX + random.nextDouble() * sizeX;
            double y = blockPos.getY() + shapeBounds.minY + random.nextDouble() * sizeY;
            double z = blockPos.getZ() + shapeBounds.minZ + random.nextDouble() * sizeZ;
            level.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, 0d, 0.05d, 0d);
            level.addParticle(ParticleTypes.ASH, x, y, z, 0d, 0.02d, 0d);
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0d, 0.05d, 0d);
        }
    }
}
