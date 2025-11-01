package ca.lizardwizard.redstoneadditions.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class AndGate extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);


    public AndGate(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, false)
                .setValue(FACING, Direction.NORTH));
    }

    // Boilerplate code
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        builder.add(FACING);
    }

    //Facing same direction as player
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



    //Redstone can only connect left right and forward
    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        // Allow connections only on the front, left, and right sides
        if (direction == null || !direction.getAxis().isHorizontal()) return false;

        Direction facing = state.getValue(FACING); // assume horizontal
        Direction fromWire = direction.getOpposite();   // how the wire approaches *your* block

        return fromWire == facing
                || fromWire == facing.getClockWise()
                || fromWire == facing.getCounterClockWise();


    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction toDirection) {
        //Output only to the front (facing direction)
        if (toDirection == state.getValue(FACING).getOpposite()) {//For some reason getOpposite is needed here
            return state.getValue(POWERED) ? 15 : 0;
        }
        return 0;
    }


    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation p_369340_, boolean p_55046_) {
        if (level.isClientSide()) return; // server-side only
        //Out
        Direction facing = state.getValue(FACING);

        //In
        Direction leftSide = facing.getCounterClockWise();
        Direction rightSide = facing.getClockWise();

        // Read power from the left and right neighbors
        int leftPower = level.getSignal(pos.relative(leftSide), leftSide);
        int rightPower = level.getSignal(pos.relative(rightSide), rightSide);

        //AND gate logic
        boolean shouldBePowered = (leftPower > 0 && rightPower > 0); // AND gate
        if (state.getValue(POWERED) != shouldBePowered) {

            level.setBlock(pos, state.setValue(POWERED, shouldBePowered), 3);
            // Tell neighbors our output changed
            level.updateNeighborsAt(pos, this);
            level.updateNeighborsAt(pos.relative(facing), this);
        }


        return;
    }

}
