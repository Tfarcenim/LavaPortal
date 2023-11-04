package tfar.lavaportal;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tfar.lavaportal.init.Init;

public class LavaPortalBlockEntity extends BlockEntity {
    public LavaPortalBlockEntity(BlockPos $$1, BlockState $$2) {
        super(Init.LAVA_PORTAL_BLOCK_E, $$1, $$2);
    }

    private BlockPos destination;

    private ResourceKey<Level> dimension = Level.OVERWORLD;

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (destination != null) {
            tag.putIntArray("destination", new int[]{destination.getX(), destination.getY(), destination.getZ()});
        }
    }

    public void setDestination(BlockPos destination) {
        this.destination = destination;
        setChanged();
    }

    public void handle(Entity entity) {
        if (destination == null) {

        } else {
            entity.teleportTo(destination.getX(),destination.getY()+1,destination.getZ());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        int[] ints = tag.getIntArray("destination");
        if (ints.length > 0) {
            destination = new BlockPos(ints[0], ints[1], ints[2]);
        }
    }
}
