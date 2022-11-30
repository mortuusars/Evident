package io.github.mortuusars.evident.entity;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.evident.config.CommonConfig;
import io.github.mortuusars.evident.setup.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.naming.OperationNotSupportedException;

public class TorchArrow extends AbstractArrow {
    public boolean torchWasPlaced = false;

    private BlockItem torchItem = (BlockItem) Items.TORCH;

    public TorchArrow(EntityType<? extends TorchArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public TorchArrow(Level level, LivingEntity shooter) {
        super(ModEntities.TORCH_ARROW.get(), shooter, level);
    }

    public TorchArrow(Level pLevel, double pX, double pY, double pZ) {
        super(ModEntities.TORCH_ARROW.get(), pX, pY, pZ, pLevel);
    }

    public TorchArrow(Level level, LivingEntity shooter, StandingAndWallBlockItem torchItem) {
        super(ModEntities.TORCH_ARROW.get(), shooter, level);
        setTorchItem(torchItem);
    }

    public void setTorchItem(BlockItem item) {
        this.torchItem = item;
    }

    public BlockItem getTorchItem() {
        return torchItem;
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.WOOD_PLACE;
    }

    @Override
    public void setSoundEvent(SoundEvent pSoundEvent) {
        super.setSoundEvent(getDefaultHitGroundSoundEvent());
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(torchItem);
    }

    @Override
    public double getBaseDamage() {
        return CommonConfig.SHOOTING_TORCHES_DAMAGE.get();
    }

    @Override
    public boolean isCritArrow() {
        return false;
    }

    @Override
    public void tick() {
        if (torchWasPlaced)
            this.remove(RemovalReason.DISCARDED);

        super.tick();

        if (level.isClientSide)
            spawnOnTickParticles(level);
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity targetEntity) {
        targetEntity.setSecondsOnFire(CommonConfig.SHOOTING_TORCHES_IGNITE_SECONDS.get());
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

        Direction impactFace = blockHitResult.getDirection();
        BlockPos blockPos = blockHitResult.getBlockPos().relative(impactFace);
        BlockState blockStateAtPos = level.getBlockState(blockPos);

        boolean canPlaceTorchThere = (blockStateAtPos.isAir() || blockStateAtPos.getMaterial().isReplaceable()) && !(blockStateAtPos.getBlock() instanceof LiquidBlock);
        boolean isSameItem = torchItem == blockStateAtPos.getBlock().asItem();

        if (!isInWater() && canPlaceTorchThere && !isSameItem) {

            Direction torchFacing = null;

            if (impactFace == Direction.DOWN) { // Search the nearest block that can hold a torch
                boolean hasHorizontalBlock = false;

                for(Direction direction : Direction.orderedByNearest(this)) {
                    if (direction.getAxis().isHorizontal()) {
                        BlockPos pos = blockPos.relative(direction);
                        Direction oppositeFace = direction.getOpposite();
                        if (level.getBlockState(pos).isFaceSturdy(level, pos, oppositeFace)) {
                            torchFacing = oppositeFace;
                            hasHorizontalBlock = true;
                        }
                    }
                }

                if (!hasHorizontalBlock) { // Check block below
                    BlockPos belowPos = blockPos.relative(Direction.DOWN);
                    BlockState belowBlockState = level.getBlockState(belowPos);
                    if (belowBlockState.isFaceSturdy(level, belowPos, Direction.UP, SupportType.CENTER))
                        torchFacing = Direction.UP;
                }
            }
            else if (level.getBlockState(blockHitResult.getBlockPos())
                    .isFaceSturdy(level, blockHitResult.getBlockPos(), impactFace, impactFace == Direction.UP ? SupportType.CENTER : SupportType.FULL))
                torchFacing = impactFace;

            if (torchFacing != null) {
                    try {
                        BlockState torchBlockState = createTorchBlockStateForFacing(torchFacing);
                        if (!level.isClientSide) {
                            level.setBlockAndUpdate(blockPos, torchBlockState);
                        }
                        torchWasPlaced = true;
                    } catch (OperationNotSupportedException ignored) { }
                }
            }

        if (level.isClientSide)
            spawnOnBlockHitParticles(level, blockHitResult);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);

        if (level instanceof ServerLevel serverLevel) {
            Entity entity = entityHitResult.getEntity();
            for (int i = 0; i < 8; i++) {
                double x = random.nextDouble() * 0.2;
                double y = random.nextDouble() * 0.2;
                double z = random.nextDouble() * 0.2;

                serverLevel.sendParticles(ParticleTypes.LAVA, entity.getX(), position().y, entity.getZ(), 1, x, y, z, 0);
            }
        }
    }

    protected BlockState createTorchBlockStateForFacing(Direction facing) throws OperationNotSupportedException {
        if (facing == Direction.DOWN)
            throw new OperationNotSupportedException("Ceiling torches are not supported.");

        if (torchItem instanceof StandingAndWallBlockItem standingAndWallBlockItem) {

            // Checking this firstly to prevent torch mis-matching:
            // If the torch cannot work fully (standing and wall) - better to use default torch.
            if ( !standingAndWallBlockItem.wallBlock.defaultBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                LogUtils.getLogger().error("Failed to place torch from arrow: '{}' has no HorizontalFacing property. " +
                        "Default torch will be used instead. (Even if the direction is UP).", torchItem);
                return getDefaultTorchBlockStateForFacing(facing);
            }

            if (facing == Direction.UP)
                return torchItem.getBlock().defaultBlockState();
            else
                return standingAndWallBlockItem.wallBlock.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
        }
        else if (torchItem.getBlock().defaultBlockState().hasProperty(BlockStateProperties.FACING))
            return torchItem.getBlock().defaultBlockState().setValue(BlockStateProperties.FACING, facing);
        else
            return getDefaultTorchBlockStateForFacing(facing);
    }

    /**
     * Used as a backup in case Block from specified TorchItem cannot be placed correctly.
     */
    protected BlockState getDefaultTorchBlockStateForFacing(Direction facing) throws OperationNotSupportedException {
        if (facing == Direction.DOWN)
            throw new OperationNotSupportedException("Ceiling torches are not supported.");

        if (facing == Direction.UP)
            return Blocks.TORCH.defaultBlockState();
        else
            return Blocks.WALL_TORCH.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
    }

    /**
     * Only called client-side.
     */
    protected void spawnOnTickParticles(Level level) {
        if (isInWater()) return;

        if  (inGround && random.nextInt(3) != 0)
            return;

        if (level.random.nextFloat() >= 0.7)
            level.addAlwaysVisibleParticle(ParticleTypes.FLAME, true, this.getX(), this.getY(), this.getZ(), 0.0D, 0.03D, 0.0D);
        else
            level.addAlwaysVisibleParticle(ParticleTypes.SMALL_FLAME, true, this.getX(), this.getY(), this.getZ(), 0.0D, 0.03D, 0.0D);

        if (level.getRandom().nextInt(2) == 0)
            level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.03D, 0.0D);
    }

    /**
     * Only called client-side.
     */
    protected void spawnOnBlockHitParticles(Level level, @NotNull BlockHitResult blockHitResult) {
        Direction impactFace = blockHitResult.getDirection();
        BlockPos blockPos = blockHitResult.getBlockPos().relative(impactFace);

        boolean isInWater = isInWater() || level.getBlockState(blockPos).is(Blocks.WATER);
        if (isInWater)
            return;

        Vec3i impactFaceNormal = impactFace.getOpposite().getNormal();
        Vec3 faceOffset = new Vec3(impactFaceNormal.getX() / 3D, impactFaceNormal.getY() / 3D, impactFaceNormal.getZ() / 3D);
        Vec3 pos = new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5).add(faceOffset);


        int particles = torchWasPlaced ? 14 : 5;

        for (int i = 0; i < particles; i++) {
            double xo = impactFaceNormal.getX() * 0.15D + (level.random.nextDouble() * 0.2 - 0.1);
            double yo = impactFaceNormal.getY() * 0.15D + (level.random.nextDouble() * 0.2 - 0.1);
            double zo = impactFaceNormal.getZ() * 0.15D + (level.random.nextDouble() * 0.2 - 0.1);

            if (random.nextInt(3) == 0)
                level.addAlwaysVisibleParticle(ParticleTypes.SMALL_FLAME, true, pos.x, pos.y, pos.z, xo * -1, yo * -1, zo * -1);
            else
                level.addAlwaysVisibleParticle(ParticleTypes.LAVA, true, pos.x, pos.y, pos.z, xo * -1, yo * -1, zo * -1);
        }
    }
}
