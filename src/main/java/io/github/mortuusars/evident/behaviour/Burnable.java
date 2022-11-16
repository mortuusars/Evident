package io.github.mortuusars.evident.behaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class Burnable {
    public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemStack();

        if (player.getCooldowns().isOnCooldown(itemStack.getItem()))
            return;

        Level level = event.getPlayer().level;
        BlockPos blockPos = event.getPos();
        BlockState clickedBlockState = level.getBlockState(blockPos);

        if (clickedBlockState.is(Blocks.COBWEB) && itemStack.is(Items.TORCH)){
            Random random = level.getRandom();

            level.playSound(event.getPlayer(), blockPos, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS,
                    1f + random.nextFloat(-0.2f, 0.2f), 1f + random.nextFloat(-0.3f, 0.3f));

            if (level.isClientSide){

                double x = blockPos.getX() + 0.5;
                double y = blockPos.getY() + 0.2;
                double z = blockPos.getZ() + 0.5;

                for (int i = 0; i < 8; i++) {
                    double r = random.nextDouble(-0.5, 0.5);
                    double r1 = random.nextDouble(-0.5, 0.5);
                    double r2 = random.nextDouble(-0.5, 0.5);
                    level.addParticle(ParticleTypes.FLAME, x + r, y + r1 + 0.3, z + r2, 0d, 0.05d, 0d);
                    level.addParticle(ParticleTypes.ASH  , x + r, y + r1 + 0.3, z + r2, 0d, 0.02d, 0d);
                }

                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y + 0.3, z, 0d, 0.05d, 0d);

            }
            else{
                level.removeBlock(blockPos, false);
            }

            player.getCooldowns().addCooldown(itemStack.getItem(), 10);

            event.setCanceled(true);
        }
    }
}
