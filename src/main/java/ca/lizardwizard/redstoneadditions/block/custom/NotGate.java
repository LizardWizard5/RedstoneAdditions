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

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class NotGate extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty DELAY = IntegerProperty.create("delay", 1, 4);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);
    public static final BooleanProperty BURNED = BooleanProperty.create("burned");//Handles if gate is flipping too fast
    int flips = 0;
    int ticks=0;

    public NotGate(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, false)
                .setValue(DELAY, 1).setValue(FACING, Direction.NORTH).setValue(BURNED, false));
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
        builder.add(BURNED);
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
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {

        if (!level.isClientSide()) { // Server-side only
            int delay = state.getValue(DELAY);
            level.scheduleTick(pos, this, 20); // Schedule the first tick in 20 ticks (1 second)
        }


    }


    //public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation p_369340_, boolean p_55046_){
        if (level.isClientSide() ) return; // server-side only or burned out
        if(state.getValue(BURNED)) {
            return;
        };
        Direction facing = state.getValue(FACING);
        Direction inputSide = facing; // convention: input is the back

        // Read power *from the back neighbor* toward us
        int inputPower = level.getSignal(pos.relative(inputSide), inputSide);

        boolean shouldBePowered = (inputPower == 0); // NOT gate
        if (state.getValue(POWERED) != shouldBePowered) {

            flips++;
            if(flips >= 50) {//Only permits 50 states changes per 2 ticks
                // Burn out the gate
                level.setBlock(pos, state.setValue(BURNED, true).setValue(POWERED,false), 3);
                level.scheduleTick(pos, this, 100);

                flips = 0;
            }
            else {
                level.setBlock(pos, state.setValue(POWERED, shouldBePowered), 3);
            }


            // Tell neighbors our output changed
            level.updateNeighborsAt(pos, this);
            level.updateNeighborsAt(pos.relative(facing), this);
        }


        return;
    }


    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        ticks++;
        if(state.getValue(BURNED)){
            level.setBlock(pos, state.setValue(BURNED, false).setValue(POWERED,true), 3);

        }
        if(ticks >=2){
            flips = 0;
            ticks=0;
        }

        level.scheduleTick(pos, this, 20);//Continue normal ticking
    }





}



