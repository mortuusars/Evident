package io.github.mortuusars.evident.entity;

import io.github.mortuusars.evident.behaviour.torch_shooting.TorchType;
import io.github.mortuusars.evident.config.CommonConfig;
import io.github.mortuusars.evident.setup.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
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

public class SoulTorchArrow extends TorchArrow {

    public SoulTorchArrow(EntityType<? extends TorchArrow> entityType, Level level) {
        super(entityType, level);
    }

    public SoulTorchArrow(Level level, LivingEntity shooter) {
        super(ModEntities.SOUL_TORCH_ARROW.get(), level, shooter);
        setTorchType(TorchType.SOUL);
        setTorchItem(((BlockItem)Items.SOUL_TORCH));
    }

    public SoulTorchArrow(Level pLevel, double pX, double pY, double pZ) {
        super(ModEntities.SOUL_TORCH_ARROW.get(), pX, pY, pZ, pLevel);
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity targetEntity) {
        int seconds = CommonConfig.SHOOTING_TORCHES_WITHER_SECONDS.get();
        if (seconds > 0) {
            MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.WITHER, seconds * 20, 0);
            targetEntity.addEffect(mobeffectinstance, this.getEffectSource());
        }
    }

    protected BlockState getDefaultTorchBlockStateForFacing(Direction facing) throws OperationNotSupportedException {
        if (facing == Direction.DOWN)
            throw new OperationNotSupportedException("Ceiling torches are not supported.");

        if (facing == Direction.UP)
            return Blocks.SOUL_TORCH.defaultBlockState();
        else
            return Blocks.SOUL_WALL_TORCH.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
    }

    protected void spawnOnTickParticles(Level level) {
        if (isInWater()) return;

        if  (inGround && random.nextInt(3) != 0)
            return;

        if (level.getRandom().nextInt(2) == 0)
            level.addAlwaysVisibleParticle(ParticleTypes.SOUL_FIRE_FLAME, true, this.getX(), this.getY(), this.getZ(), 0.0D, 0.01D, 0.0D);

        if (level.getRandom().nextInt(3) == 0)
            level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.03D, 0.0D);
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


        int particles = torchWasPlaced ? 14 : 5;

        for (int i = 0; i < particles; i++) {
            double xo = impactFaceNormal.getX() * 0.15D + (level.random.nextDouble() * 0.2 - 0.1);
            double yo = impactFaceNormal.getY() * 0.15D + (level.random.nextDouble() * 0.2 - 0.1);
            double zo = impactFaceNormal.getZ() * 0.15D + (level.random.nextDouble() * 0.2 - 0.1);

            if (random.nextInt(3) == 0)
                level.addAlwaysVisibleParticle(ParticleTypes.SOUL, true, pos.x, pos.y, pos.z, xo * -1, yo * -1, zo * -1);
            else
                level.addAlwaysVisibleParticle(ParticleTypes.SOUL_FIRE_FLAME, true, pos.x, pos.y, pos.z, xo * -1, yo * -1, zo * -1);
        }
    }

    protected void spawnServerOnEntityHitParticles(ServerLevel level, EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        for (int i = 0; i < 8; i++) {
            double x = random.nextDouble() * 0.2;
            double y = random.nextDouble() * 0.2;
            double z = random.nextDouble() * 0.2;

            level.sendParticles(ParticleTypes.SOUL, entity.getX(), position().y, entity.getZ(), 1, x, y, z, 0);
        }
    }
}