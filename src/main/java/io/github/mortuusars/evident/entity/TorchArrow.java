package io.github.mortuusars.evident.entity;

import io.github.mortuusars.evident.behaviour.Burnable;
import io.github.mortuusars.evident.config.CommonConfig;
import io.github.mortuusars.evident.setup.ModEntities;
import io.github.mortuusars.evident.setup.ModTags;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.CallbackI;

public class TorchArrow extends AbstractArrow {

    public boolean torchWasPlaced = false;

    public TorchArrow(EntityType<? extends TorchArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setBaseDamage(0D);
        this.setSoundEvent(SoundEvents.WOOD_PLACE);
    }

    public TorchArrow(Level pLevel, LivingEntity pShooter) {
        super(ModEntities.TORCH_ARROW.get(), pShooter, pLevel);
    }

    public TorchArrow(Level pLevel, double pX, double pY, double pZ) {
        super(ModEntities.TORCH_ARROW.get(), pX, pY, pZ, pLevel);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(Items.TORCH);
    }

    @Override
    public void move(MoverType pType, Vec3 pPos) {
        super.move(pType, pPos);


    }

    @Override
    public void tick() {
        if (torchWasPlaced)
            this.remove(RemovalReason.DISCARDED);

        super.tick();

        // Particles
        if (level instanceof ServerLevel serverLevel) {
            if  (inGround && random.nextInt(3) != 0)
                return;

            if (level.random.nextFloat() >= 0.7)
                serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 1, 0.0D, 0.03D, 0.0D, 0F);
            else
                serverLevel.sendParticles(ParticleTypes.SMALL_FLAME, this.getX(), this.getY(), this.getZ(), 1, 0.0D, 0.03D, 0.0D, 0F);

            if (level.getRandom().nextInt(2) == 0)
                serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1, 0.0D, 0.03D, 0.0D, 0F);
        }

//        Vec3 pos = position();
//        Vec3 nextPos = pos.add(getDeltaMovement());
//
//        if (firstTick) {
//            firstTick = false;
//            Entity owner = getOwner();
//            if (owner != null)
//                pos = owner.position().add(0D, 1.5D, 0D);
//        }
//
////        Arrow ar1 = new Arrow(level, pos.x, pos.y, pos.z);
////        ar1.setNoPhysics(true);
////        level.addFreshEntity(ar1);
////
////        Arrow ar2 = new Arrow(level, nextPos.x, nextPos.y, nextPos.z);
////        ar2.setNoPhysics(true);
////        level.addFreshEntity(ar2);
//
//        HitResult hitResult = level.clip(new ClipContext(pos, nextPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));
//
//        BlockPos hitBlockPos = new BlockPos(hitResult.getLocation());
//        BlockState blockState = level.getBlockState(hitBlockPos);
//        if (blockState.is(ModTags.BURNABLE)) {
//
//            level.playSound(null, blockPosition(), SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE, SoundSource.MASTER, 1F, 1F);
//
//            Burnable.burnBlock(level, hitBlockPos, getOwner() instanceof Player player ? player : null);
//            this.remove(RemovalReason.DISCARDED);
//            return;
//        }



//        if (level.isClientSide)
//            return;
//
//        Vec3 pos = position();
//        Vec3 nextPos = pos.add(getDeltaMovement());
//
//        if (firstTick) {
//            firstTick = false;
//            Entity owner = getOwner();
//            if (owner != null)
//                pos = owner.position().add(0D, 1.5D, 0D);
//        }
//
//        HitResult hitResult = level.clip(new ClipContext(pos, nextPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));
//
////        BlockGetter.traverseBlocks(pos, nextPos, ClipContext.Block.OUTLINE, (block, blockPos) -> {
////            ((ServerLevel) level).sendParticles(ParticleTypes.CLOUD, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 1, 0.0D, 0.03D, 0.0D, 0F);
////
////            if (level.getBlockState(blockPos).is(ModTags.BURNABLE))
////                level.playSound(null, blockPosition(), SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE, SoundSource.MASTER, 1F, 1F);
////
////            return 0;
////        }, block -> { return 0; });
//
//        BlockPos hitBlockPos = new BlockPos(hitResult.getLocation());
//        BlockState blockState = level.getBlockState(hitBlockPos);
//        if (blockState.is(ModTags.BURNABLE)) {
//
//            level.playSound(null, blockPosition(), SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE, SoundSource.MASTER, 1F, 1F);
//
//            Burnable.burnBlock(level, hitBlockPos, getOwner() instanceof Player player ? player : null);
//            this.remove(RemovalReason.DISCARDED);
//        }

    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity targetEntity) {
        targetEntity.setSecondsOnFire(CommonConfig.SHOOTING_TORCHES_INGITE_SECONDS.get());
    }

    @Override
    protected void onInsideBlock(BlockState pState) {
//        if (pState.is(ModTags.BURNABLE)) {
//            Burnable.burnBlock(level, blockPosition(), null);
//            this.remove(RemovalReason.DISCARDED);
//        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);

        Direction impactFace = pResult.getDirection();
        BlockPos blockPos = pResult.getBlockPos().relative(impactFace);
        BlockState blockStateAtPos = level.getBlockState(blockPos);

        if (blockStateAtPos.isAir()) {
            BlockState torchBlockState = null;

            if (impactFace == Direction.DOWN) {
                for(Direction direction : Direction.orderedByNearest(this)) {
                    if (direction.getAxis().isHorizontal()) {
                        BlockPos relative = blockPos.relative(direction);
                        Direction oppositeFace = direction.getOpposite();
                        if (level.getBlockState(relative).isFaceSturdy(level, relative, oppositeFace)) {
                            torchBlockState = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, oppositeFace);
                            impactFace = oppositeFace;
                        }
                    }
                }
            }
            else if (impactFace == Direction.UP)
                torchBlockState = Blocks.TORCH.defaultBlockState();
            else
                torchBlockState = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, impactFace);

            if (torchBlockState == null)
                return;

            if (!level.isClientSide)
                level.setBlockAndUpdate(blockPos, torchBlockState);

            torchWasPlaced = true;
        }

        Vec3i impactFaceNormal = impactFace.getOpposite().getNormal();
        Vec3 faceOffset = new Vec3(impactFaceNormal.getX() / 3D, impactFaceNormal.getY() / 3D, impactFaceNormal.getZ() / 3D);
        Vec3 pos = new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5).add(faceOffset);

        int particles = torchWasPlaced ? 20 : 6;

        for (int i = 0; i < particles; i++) {
            double xo = impactFaceNormal.getX() * 0.15D + (level.random.nextDouble() * 0.2 - 0.1);
            double yo = impactFaceNormal.getY() * 0.15D + (level.random.nextDouble() * 0.2 - 0.1);
            double zo = impactFaceNormal.getZ() * 0.15D + (level.random.nextDouble() * 0.2 - 0.1);

            if (random.nextInt(3) == 0)
                level.addAlwaysVisibleParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, xo * -1, yo * -1, zo * -1);
            else
                level.addAlwaysVisibleParticle(ParticleTypes.SMALL_FLAME, pos.x, pos.y, pos.z, xo * -1, yo * -1, zo * -1);
        }
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.WOOD_PLACE;
    }
}
