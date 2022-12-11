package io.github.mortuusars.evident.content.chopping_block;

import io.github.mortuusars.evident.config.Configuration;
import io.github.mortuusars.evident.setup.ModBlockEntityTypes;
import io.github.mortuusars.evident.setup.ModBlocks;
import io.github.mortuusars.evident.setup.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("ALL")
public class ChoppingBlockBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 14, 16);

    public ChoppingBlockBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WATERLOGGED);
    }

    public static Damage damageFromItemStack(ItemStack stack) {
        if (stack.is(ModItems.CHIPPED_CHOPPING_BLOCK.get()))
            return Damage.CHIPPED;
        else if (stack.is(ModItems.DAMAGED_CHOPPING_BLOCK.get()))
            return Damage.DAMAGED;
        else
            return Damage.NONE;
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        Damage wear = damageFromItemStack(context.getItemInHand());
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return state;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }


    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {

        if ( !(level.getBlockEntity(pos) instanceof ChoppingBlockBlockEntity choppingBlockEntity))
            return InteractionResult.FAIL;

        if (level.isClientSide)
            return InteractionResult.CONSUME;

        ItemStack mainHandStack = player.getMainHandItem();
        ItemStack offHandStack = player.getOffhandItem();

        if (choppingBlockEntity.isEmpty())
            return placeItemOnBlock(choppingBlockEntity, player);

        if (player.getItemInHand(hand).isEmpty()) {

            if (choppingBlockEntity.getStoredItem().isStackable())
                choppingBlockEntity.dropStoredItem();
            else
                player.setItemInHand(hand, choppingBlockEntity.removeStoredItem());

            level.playSound(null, pos, SoundEvents.WOOD_FALL, SoundSource.BLOCKS, 1F, 1F + ((float) level.getRandom().nextGaussian()) * 0.2F);

            return InteractionResult.SUCCESS;
        }

        ItemStack ingredientStack = choppingBlockEntity.getStoredItem().copy();
        if (choppingBlockEntity.processStoredItemWith(player.getItemInHand(hand), player)) {

            Random random = level.random;
            for (int i = 0; i < 20; ++i) {
                Vec3 vec3d = new Vec3(random.nextGaussian() * 0.1D, random.nextGaussian() * 0.1D + 0.05D, random.nextGaussian() * 0.1D);
                if (level instanceof ServerLevel) {
                    ((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, ingredientStack), pos.getX() + 0.5F, pos.getY() + 0.85F, pos.getZ() + 0.5F, 1, vec3d.x, vec3d.y + 0.08D, vec3d.z, 0.03D);
                }
//                else {
//                    level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, ingredientStack), pos.getX() + 0.5F, pos.getY() + 0.8F, pos.getZ() + 0.5F, vec3d.x, vec3d.y + 0.05D, vec3d.z);
//                }
            }

            if (!level.isClientSide && !player.isCreative() && level.getRandom().nextDouble() <= Configuration.CHOPPING_BLOCK_DAMAGE_CHANCE.get()) {
                incrementDamage(blockState, (ServerLevel)level , pos);
                spawnDestroyParticles(level, player, pos, blockState);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.CONSUME;
    }

    private static void incrementDamage(BlockState blockState, ServerLevel level, BlockPos pos) {
        @Nullable BlockState newState;

        if (blockState.is(ModBlocks.CHOPPING_BLOCK.get()))
            newState = ModBlocks.CHIPPED_CHOPPING_BLOCK.get().defaultBlockState();
        else if (blockState.is(ModBlocks.CHIPPED_CHOPPING_BLOCK.get()))
            newState = ModBlocks.DAMAGED_CHOPPING_BLOCK.get().defaultBlockState();
        else
            newState = null;

        if (newState == null) {
            level.removeBlock(pos, false);
            level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1F, level.getRandom().nextFloat() * 0.1F + 0.9F);
        }
        else {
            level.setBlockAndUpdate(pos, newState
                    .setValue(FACING, blockState.getValue(FACING))
                    .setValue(WATERLOGGED, blockState.getValue(WATERLOGGED)));
            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1F, level.getRandom().nextFloat() * 0.1F + 0.9F);
            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1F, level.getRandom().nextFloat() * 0.1F + 0.9F);
            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1F, level.getRandom().nextFloat() * 0.1F + 0.9F);

        }
    }

    private InteractionResult placeItemOnBlock(ChoppingBlockBlockEntity choppingBlockEntity, Player player) {
        ItemStack mainHandStack = player.getMainHandItem();
        ItemStack offHandStack = player.getOffhandItem();

        if (mainHandStack.isEmpty() && offHandStack.isEmpty())
            return InteractionResult.CONSUME;

        // OffHand takes priority
        ItemStack placedStack = offHandStack.isEmpty() ? mainHandStack : offHandStack;
        choppingBlockEntity.placeItem(placedStack, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof ChoppingBlockBlockEntity choppingBlockEntity) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), choppingBlockEntity.getStoredItem());
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.CHOPPING_BLOCK.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> blockEntityType) {
        return pLevel.isClientSide ?
                createTickerHelper(blockEntityType, ModBlockEntityTypes.CHOPPING_BLOCK.get(), ChoppingBlockBlockEntity::animateTick)
                : null;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }
}
