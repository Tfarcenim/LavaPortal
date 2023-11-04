package tfar.lavaportal.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import tfar.lavaportal.LavaPortalBlock;
import tfar.lavaportal.LavaPortalBlockEntity;

public class Init {

    public static final Block LAVA_PORTAL = new LavaPortalBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_PORTAL));
    public static final BlockEntityType<LavaPortalBlockEntity> LAVA_PORTAL_BLOCK_E = BlockEntityType.Builder.of(LavaPortalBlockEntity::new,LAVA_PORTAL).build(null);
    public static final Block LAVA_PORTAL_FRAME = new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN));
    public static final Item LAVA_PORTAL_FRAME_I = new BlockItem(LAVA_PORTAL_FRAME,new Item.Properties());

}
