package ca.lizardwizard.redstoneadditions.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class OrGate  extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);
    public static final BooleanProperty  XOR = BooleanProperty.create("xor");
    public static final BooleanProperty BURNED = BooleanProperty.create("burned");//Handles if gate is flipping too fast
    int flips = 0;
    int ticks=0;

    public OrGate(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, false)
                .setValue(FACING, Direction.NORTH)
                .setValue(XOR, false)
                .setValue(BURNED, false));
    }

    // Boilerplate code
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        builder.add(FACING);
        builder.add(XOR);
        builder.add(BURNED);
    }

    //Facing same direction as player
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    public boolean isSignalSource(BlockState state) {
        return true; // This block can be used as a signal source
    }

    //Upon right click, toggle between OR and XOR mode
    @Override
    public InteractionResult use(BlockState state,Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit){
        if(level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        //Toggle XOR property

        level.setBlock(pos, state.setValue(XOR, !state.getValue(XOR)), 3);
        level.updateNeighborsAt(pos.relative(state.getValue(FACING).getOpposite()), this);
        return InteractionResult.SUCCESS;
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
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        // Often same as getSignal for repeaters/comparators reading strong power
        return getSignal(state, level, pos, side);        // strong power
    }


    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        //Check for solid block below
        BlockPos below = pos.below();
        boolean hasSupport = level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);

        // Check for water at current position or below
        boolean touchingWater = level.getFluidState(pos).isSourceOfType(Fluids.WATER);

        return hasSupport && !touchingWater;
    }
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {

        super.onPlace(state, level, pos, oldState, moving);
        if(level.isClientSide)// Server-side only
            return;

        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }

        //Out
        Direction facing = state.getValue(FACING);

        //In
        Direction leftSide = facing.getCounterClockWise();
        Direction rightSide = facing.getClockWise();

        // Read power *from the back neighbor* toward us
        int leftPower = level.getSignal(pos.relative(leftSide), leftSide);
        int rightPower = level.getSignal(pos.relative(rightSide), rightSide);


        boolean shouldBePowered = (leftPower >0 || rightPower>0); // OR gate
        System.out.println("Left power: " + leftPower + ", Right power: " + rightPower + ", Should be powered: " + shouldBePowered);
        level.setBlock(pos, state.setValue(POWERED, shouldBePowered), 3);

        return;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide || state.getValue(BURNED)) return; // server-side only

        //Check for if nearby changes cause block destroy
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true); // Break the block and drop items
            return;
        }

        //Out
        Direction facing = state.getValue(FACING);

        //In
        Direction leftSide = facing.getCounterClockWise();
        Direction rightSide = facing.getClockWise();

        // Read power *from the back neighbor* toward us
        int leftPower = level.getSignal(pos.relative(leftSide), leftSide);
        int rightPower = level.getSignal(pos.relative(rightSide), rightSide);


        boolean shouldBePowered = (leftPower >0 || rightPower>0); // OR gate
        if(state.getValue(XOR)){
            shouldBePowered = (leftPower >0) ^ (rightPower>0); // XOR gate
        }
        if (state.getValue(POWERED) != shouldBePowered) {

            flips++;

            if(flips >= 80) {//For some reason OrGate requires a higher limit to accomodate clock at delay of 1

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
