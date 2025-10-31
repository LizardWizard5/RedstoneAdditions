package ca.lizardwizard.redstoneadditions.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NotGate extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty DELAY = IntegerProperty.create("delay", 1, 4);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);

    public NotGate(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, false)
                .setValue(DELAY, 1).setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true; // This block can be used as a signal source
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        builder.add(DELAY);
        builder.add(FACING);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return (direction == state.getValue(FACING)) || (direction == state.getValue(FACING).getOpposite());

    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction toDirection) {
        // Only output to the front (facing direction)
        if (toDirection == state.getValue(FACING)) {
            return state.getValue(POWERED) ? 15 : 0;
        }
        return 0;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide) return; // server-side only

        Direction facing = state.getValue(FACING);
        Direction inputSide = facing.getOpposite();

        boolean shouldBePowered = shouldBePowered(level, pos, state, inputSide);
        if (state.getValue(POWERED) != shouldBePowered) {
            ServerLevel serverLevel = (ServerLevel) level;
            if (!serverLevel.getBlockTicks().hasScheduledTick(pos, this)) {
                serverLevel.scheduleTick(pos, this, getDelayTicks(state));
            }
        }
    }

    private boolean shouldBePowered(Level level, BlockPos pos, BlockState state, Direction inputSide) {
        int inputPower = level.getSignal(pos.relative(inputSide), inputSide);
        return inputPower == 0;
    }

    private int getDelayTicks(BlockState state) {
        return state.getValue(DELAY);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Direction facing = state.getValue(FACING);
        Direction inputSide = facing.getOpposite();
        boolean shouldBePowered = shouldBePowered(level, pos, state, inputSide);
        if (state.getValue(POWERED) != shouldBePowered) {
            level.setBlock(pos, state.setValue(POWERED, shouldBePowered), 3);
            level.updateNeighborsAt(pos, this);
            level.updateNeighborsAt(pos.relative(facing), this);
        }
    }





}



