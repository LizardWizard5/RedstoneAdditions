package ca.lizardwizard.redstoneadditions.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class RedstoneClock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty DELAY = IntegerProperty.create("delay", 1, 20);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);

    public RedstoneClock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, false)
                .setValue(DELAY, 20).setValue(FACING, Direction.NORTH));


    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        builder.add(DELAY);
        builder.add(FACING);
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
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        //Set the block to face away from the player
        Direction facing = level.getBlockState(pos).getValue(FACING);
        state.setValue(FACING, facing);
        level.setBlock(pos, state, 3);
        if (!level.isClientSide) { // Server-side only
            int delay = state.getValue(DELAY);
            level.scheduleTick(pos, this, delay); // Schedule the first tick in 20 ticks (1 second)
        }


    }
    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        // Only emit full power (15) if the request comes from the block in front (North)
        Direction facing = state.getValue(FACING);
        return direction == facing && state.getValue(POWERED) ? 15 : 0;
    }


    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0; // Direct power for adjacent blocks
    }


    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if(direction == state.getValue(FACING)){
            return true;
        }
        return false;
        //return true; // Allows Redstone to visually connect
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit){
        int delay = state.getValue(DELAY);
        switch(delay){
            case 20:
                delay = 15;
                break;
            case 15:
                delay = 10;
                break;
            case 10:
                delay = 5;
                break;
            case 5:
                delay = 1;
                break;
            case 1:
                delay = 20;
                break;

        }
        level.setBlock(pos, state.setValue(DELAY, delay), 3);
        if (!level.isClientSide) { // Server-side only

            level.scheduleTick(pos, this, delay);
        }
        level.playSound(player, pos, SoundEvents.AMETHYST_CLUSTER_PLACE, SoundSource.BLOCKS, 1f, 1f*delay);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Get the updated block state from the world
        BlockState currentState = level.getBlockState(pos);
        boolean isPowered = currentState.getValue(POWERED);
        int delay = currentState.getValue(DELAY);

        // Toggle the power state while preserving the delay
        level.setBlock(pos, currentState.setValue(POWERED, !isPowered), 3);
        level.updateNeighborsAt(pos, this);


        // Schedule the next tick using the updated delay
        level.scheduleTick(pos, this, delay);
    }
}
