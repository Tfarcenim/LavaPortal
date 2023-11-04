package tfar.lavaportal;

import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class LavaPortalShape {
    private static final int MIN_WIDTH = 2;
    public static final int MAX_WIDTH = 21;
    private static final int MIN_HEIGHT = 3;
    public static final int MAX_HEIGHT = 21;

    private final LevelAccessor level;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int numPortalBlocks;
    @Nullable
    private BlockPos bottomLeft;
    private final Block portal;
    private int height;
    private final int width;

    private final BlockBehaviour.StatePredicate frame;

    public static Optional<LavaPortalShape> findEmptyPortalShape(LevelAccessor level, BlockPos $$1, Direction.Axis $$2, Block portalFrame, Block portal) {
        return findPortalShape(level, $$1, (portalShape) -> portalShape.isValid() && portalShape.numPortalBlocks == 0, $$2,portalFrame,portal);
    }

    public static Optional<LavaPortalShape> findPortalShape(LevelAccessor $$0, BlockPos $$1, Predicate<LavaPortalShape> $$2, Direction.Axis $$3, Block portalFrame, Block portal) {
        Optional<LavaPortalShape> $$4 = Optional.of(new LavaPortalShape($$0, $$1, $$3,portalFrame,portal)).filter($$2);
        if ($$4.isPresent()) {
            return $$4;
        } else {
            Direction.Axis $$5 = $$3 == Axis.X ? Axis.Z : Axis.X;
            return Optional.of(new LavaPortalShape($$0, $$1, $$5,portalFrame,portal)).filter($$2);
        }
    }

    public LavaPortalShape(LevelAccessor level, BlockPos pos, Direction.Axis axis, Block portalFrame, Block portal) {
        frame = ($$0, $$1, $$2) -> $$0.is(portalFrame);
        this.level = level;
        this.axis = axis;
        this.rightDir = axis == Axis.X ? Direction.WEST : Direction.SOUTH;
        this.bottomLeft = this.calculateBottomLeft(pos);
        this.portal = portal;
        if (this.bottomLeft == null) {
            this.bottomLeft = pos;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.calculateWidth();
            if (this.width > 0) {
                this.height = this.calculateHeight();
            }
        }
    }

    @Nullable
    private BlockPos calculateBottomLeft(BlockPos $$0) {
        for(int $$1 = Math.max(this.level.getMinBuildHeight(), $$0.getY() - 21); $$0.getY() > $$1 && isEmpty(this.level.getBlockState($$0.below())); $$0 = $$0.below()) {
        }

        Direction $$2 = this.rightDir.getOpposite();
        int $$3 = this.getDistanceUntilEdgeAboveFrame($$0, $$2) - 1;
        return $$3 < 0 ? null : $$0.relative($$2, $$3);
    }

    private int calculateWidth() {
        int $$0 = this.getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir);
        return $$0 >= 2 && $$0 <= 21 ? $$0 : 0;
    }

    private int getDistanceUntilEdgeAboveFrame(BlockPos $$0, Direction $$1) {
        BlockPos.MutableBlockPos $$2 = new BlockPos.MutableBlockPos();

        for(int $$3 = 0; $$3 <= 21; ++$$3) {
            $$2.set($$0).move($$1, $$3);
            BlockState $$4 = this.level.getBlockState($$2);
            if (!isEmpty($$4)) {
                if (frame.test($$4, this.level, $$2)) {
                    return $$3;
                }
                break;
            }

            BlockState $$5 = this.level.getBlockState($$2.move(Direction.DOWN));
            if (!frame.test($$5, this.level, $$2)) {
                break;
            }
        }

        return 0;
    }

    private int calculateHeight() {
        BlockPos.MutableBlockPos $$0 = new BlockPos.MutableBlockPos();
        int $$1 = this.getDistanceUntilTop($$0);
        return $$1 >= 3 && $$1 <= 21 && this.hasTopFrame($$0, $$1) ? $$1 : 0;
    }

    private boolean hasTopFrame(BlockPos.MutableBlockPos $$0, int $$1) {
        for(int $$2 = 0; $$2 < this.width; ++$$2) {
            BlockPos.MutableBlockPos $$3 = $$0.set(this.bottomLeft).move(Direction.UP, $$1).move(this.rightDir, $$2);
            if (!frame.test(this.level.getBlockState($$3), this.level, $$3)) {
                return false;
            }
        }

        return true;
    }

    private int getDistanceUntilTop(BlockPos.MutableBlockPos $$0) {
        for(int $$1 = 0; $$1 < 21; ++$$1) {
            $$0.set(this.bottomLeft).move(Direction.UP, $$1).move(this.rightDir, -1);
            if (!frame.test(this.level.getBlockState($$0), this.level, $$0)) {
                return $$1;
            }

            $$0.set(this.bottomLeft).move(Direction.UP, $$1).move(this.rightDir, this.width);
            if (!frame.test(this.level.getBlockState($$0), this.level, $$0)) {
                return $$1;
            }

            for(int $$2 = 0; $$2 < this.width; ++$$2) {
                $$0.set(this.bottomLeft).move(Direction.UP, $$1).move(this.rightDir, $$2);
                BlockState $$3 = this.level.getBlockState($$0);
                if (!isEmpty($$3)) {
                    return $$1;
                }

                if ($$3.is(Blocks.NETHER_PORTAL)) {
                    ++this.numPortalBlocks;
                }
            }
        }

        return 21;
    }

    private boolean isEmpty(BlockState $$0) {
        return $$0.isAir() || $$0.is(BlockTags.FIRE) || $$0.is(portal);
    }

    public boolean isValid() {
        return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }

    public void createPortalBlocks() {
        BlockState $$0 = portal.defaultBlockState().setValue(NetherPortalBlock.AXIS, this.axis);
        BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).forEach(($$1) -> {
            this.level.setBlock($$1, $$0, 18);
        });
    }

    public boolean isComplete() {
        return this.isValid() && this.numPortalBlocks == this.width * this.height;
    }

    public static Vec3 getRelativePosition(BlockUtil.FoundRectangle $$0, Direction.Axis $$1, Vec3 $$2, EntityDimensions $$3) {
        double $$4 = (double)$$0.axis1Size - (double)$$3.width;
        double $$5 = (double)$$0.axis2Size - (double)$$3.height;
        BlockPos $$6 = $$0.minCorner;
        double $$9;
        if ($$4 > 0.0) {
            float $$7 = (float)$$6.get($$1) + $$3.width / 2.0F;
            $$9 = Mth.clamp(Mth.inverseLerp($$2.get($$1) - (double)$$7, 0.0, $$4), 0.0, 1.0);
        } else {
            $$9 = 0.5;
        }

        double $$12;
        Direction.Axis $$13;
        if ($$5 > 0.0) {
            $$13 = Axis.Y;
            $$12 = Mth.clamp(Mth.inverseLerp($$2.get($$13) - (double)$$6.get($$13), 0.0, $$5), 0.0, 1.0);
        } else {
            $$12 = 0.0;
        }

        $$13 = $$1 == Axis.X ? Axis.Z : Axis.X;
        double $$14 = $$2.get($$13) - ((double)$$6.get($$13) + 0.5);
        return new Vec3($$9, $$12, $$14);
    }

    public static PortalInfo createPortalInfo(ServerLevel $$0, BlockUtil.FoundRectangle $$1, Direction.Axis $$2, Vec3 $$3, EntityDimensions $$4, Vec3 $$5, float $$6, float $$7) {
        BlockPos $$8 = $$1.minCorner;
        BlockState $$9 = $$0.getBlockState($$8);
        Direction.Axis $$10 = $$9.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Axis.X);
        double $$11 = $$1.axis1Size;
        double $$12 = $$1.axis2Size;
        int $$13 = $$2 == $$10 ? 0 : 90;
        Vec3 $$14 = $$2 == $$10 ? $$5 : new Vec3($$5.z, $$5.y, -$$5.x);
        double $$15 = (double)$$4.width / 2.0 + ($$11 - (double)$$4.width) * $$3.x();
        double $$16 = ($$12 - (double)$$4.height) * $$3.y();
        double $$17 = 0.5 + $$3.z();
        boolean $$18 = $$10 == Axis.X;
        Vec3 $$19 = new Vec3((double)$$8.getX() + ($$18 ? $$15 : $$17), (double)$$8.getY() + $$16, (double)$$8.getZ() + ($$18 ? $$17 : $$15));
        return new PortalInfo($$19, $$14, $$6 + (float)$$13, $$7);
    }
}
