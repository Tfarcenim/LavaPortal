package tfar.lavaportal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tfar.lavaportal.init.Init;

public class LavaPortalBlock extends NetherPortalBlock implements EntityBlock {
    public LavaPortalBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public void randomTick(BlockState $$0, ServerLevel $$1, BlockPos $$2, RandomSource $$3) {
        //super.randomTick($$0, $$1, $$2, $$3);
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        Direction.Axis direction$axis = pFacing.getAxis();
        Direction.Axis direction$axis1 = pState.getValue(AXIS);
        boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
        return !flag && !pFacingState.is(this) &&
                !new LavaPortalShape(pLevel, pCurrentPos, direction$axis1, Init.LAVA_PORTAL_FRAME,this).isComplete() ? Blocks.AIR.defaultBlockState() :
                pState;
    }


    public void entityInside(BlockState $$0, Level level, BlockPos pos, Entity entity) {
        if (!entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof LavaPortalBlockEntity lavaPortalBlockEntity) {
                lavaPortalBlockEntity.handle(entity);
            }

        //    entity.handleInsidePortal($$2);
        }

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LavaPortalBlockEntity(blockPos,blockState);
    }
}
