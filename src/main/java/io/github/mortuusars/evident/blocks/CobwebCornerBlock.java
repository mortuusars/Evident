package io.github.mortuusars.evident.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CobwebCornerBlock extends WebBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final DirectionProperty VERTICAL_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;

    private static final VoxelShape X_SHAPE = Block.box(6, 0, 0, 10, 16, 16);
    private static final VoxelShape Z_SHAPE = Block.box(0, 0, 6, 16, 16, 10);

    public CobwebCornerBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(VERTICAL_DIRECTION, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(VERTICAL_DIRECTION);
    }

    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case WEST, EAST -> Z_SHAPE;
            default -> X_SHAPE;
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {

        Direction vertical = context.getClickLocation().y - ((double)context.getClickedPos().getY()) > 0.5D
                ? Direction.UP : Direction.DOWN;

        Direction face = context.getClickedFace();

        // Facing depending where on the block was clicked
        if (face == Direction.UP || face == Direction.DOWN) {
            Vec3 loc = context.getClickLocation();
            double x = loc.x - context.getClickedPos().getX() - 0.5D;
            double z = loc.z - context.getClickedPos().getZ() - 0.5D;

            if (Math.abs(x) > Math.abs(z))
                face = x > 0D ? Direction.WEST : Direction.EAST;
            else
                face = z > 0D ? Direction.NORTH : Direction.SOUTH;
        }

        return defaultBlockState()
                .setValue(FACING, face)
                .setValue(VERTICAL_DIRECTION, vertical);
    }

    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        entity.makeStuckInBlock(blockState, new Vec3(0.9D, (double)0.85F, 0.9D));
    }
}
