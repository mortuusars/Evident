package io.github.mortuusars.evident.content.torch_shooting.arrow;

import io.github.mortuusars.evident.content.torch_shooting.TorchType;
import io.github.mortuusars.evident.config.Configuration;
import io.github.mortuusars.evident.setup.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.naming.OperationNotSupportedException;

public class RedstoneTorchArrow extends TorchArrow {
    public RedstoneTorchArrow(EntityType<? extends TorchArrow> entityType, Level level) {
        super(entityType, level);
    }

    public RedstoneTorchArrow(Level level, LivingEntity shooter) {
        super(ModEntities.REDSTONE_TORCH_ARROW.get(), level, shooter);
        setTorchType(TorchType.REDSTONE);
        setTorchItem(((BlockItem)Items.REDSTONE_TORCH));
    }

    public RedstoneTorchArrow(Level pLevel, double pX, double pY, double pZ) {
        super(ModEntities.REDSTONE_TORCH_ARROW.get(), pX, pY, pZ, pLevel);
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity targetEntity) {
        int seconds = Configuration.SHOOTING_TORCHES_SLOWNESS_SECONDS.get();
        if (seconds > 0) {
            MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, seconds * 20, 0);
            targetEntity.addEffect(mobeffectinstance, this.getEffectSource());
        }
    }

    protected BlockState getDefaultTorchBlockStateForFacing(Direction facing) throws OperationNotSupportedException {
        if (facing == Direction.DOWN)
            throw new OperationNotSupportedException("Ceiling torches are not supported.");

        if (facing == Direction.UP)
            return Blocks.REDSTONE_TORCH.defaultBlockState();
        else
            return Blocks.REDSTONE_WALL_TORCH.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
    }

    protected void spawnOnTickParticles(Level level) {
        if (isInWater()) return;

        if  (inGround && random.nextInt(3) != 0)
            return;

        if (random.nextInt(2) == 0)
            level.addAlwaysVisibleParticle(DustParticleOptions.REDSTONE, true, this.getX(), this.getY(), this.getZ(), 0.0D, 0.005D, 0.0D);
    }

    protected void spawnOnBlockHitParticles(Level level, @NotNull BlockHitResult blockHitResult) {
        Direction impactFace = blockHitResult.getDirection();
        BlockPos blockPos = blockHitResult.getBlockPos().relative(impactFace);

        boolean isInWater = isInWater() || level.getBlockState(blockPos).is(Blocks.WATER);
        if (isInWater)
            return;

        Vec3i impactFaceNormal = impactFace.getOpposite().getNormal();
        Vec3 faceOffset = new Vec3(impactFaceNormal.getX() / 3D, impactFaceNormal.getY() / 3D, impactFaceNormal.getZ() / 3D);
        Vec3 pos = new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5).add(faceOffset);


        int particles = torchWasPlaced ? 12 : 5;

        for (int i = 0; i < particles; i++) {
            double xo = impactFaceNormal.getX() * 0.2D + (level.random.nextDouble() * 0.5);
            double yo = impactFaceNormal.getY() * 0.2D + (level.random.nextDouble() * 0.5);
            double zo = impactFaceNormal.getZ() * 0.2D + (level.random.nextDouble() * 0.5);

            level.addAlwaysVisibleParticle(DustParticleOptions.REDSTONE, true, pos.x + level.random.nextDouble() * 0.9 - 0.45, pos.y + level.random.nextDouble() * 0.9 - 0.45, pos.z + level.random.nextDouble() * 0.9 - 0.45, xo * -1, yo * -1, zo * -1);
        }
    }

    protected void spawnServerOnEntityHitParticles(ServerLevel level, EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        for (int i = 0; i < 8; i++) {
            double x = random.nextDouble() * 0.2;
            double y = random.nextDouble() * 0.2;
            double z = random.nextDouble() * 0.2;

            level.sendParticles(DustParticleOptions.REDSTONE, entity.getX(), position().y, entity.getZ(), 1, x, y, z, 0);
        }
    }
}