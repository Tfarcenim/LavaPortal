package tfar.lavaportal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import tfar.lavaportal.datagen.ModDatagen;
import tfar.lavaportal.init.Init;

@Mod(LavaPortal.MOD_ID)
public class LavaPortalForge {
    
    public LavaPortalForge() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        LavaPortal.init();
    
        // Some code like events require special initialization from the
        // loader specific code.
        //MinecraftForge.EVENT_BUS.addListener(this::onItemTooltip);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::register);
        MinecraftForge.EVENT_BUS.addListener(this::entityTick);
        bus.addListener(ModDatagen::start);
        //MinecraftForge.EVENT_BUS.addListener(this::commands);
        MinecraftForge.EVENT_BUS.addListener(this::rightClick);
    }

    public void commands(RegisterCommandsEvent event) {
        ModCommand.register(event.getDispatcher());
    }

    public void rightClick(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();
        Player player = event.getEntity();
        BlockPos pos = event.getPos();

        if (!level.isClientSide) {

            if (stack.hasTag() && stack.getTag().getBoolean(LavaPortal.MOD_ID)) {

                BlockEntity blockEntity = level.getBlockEntity(pos);

                if (blockEntity instanceof LavaPortalBlockEntity lavaPortalBlockEntity) {
                    BlockPos target = from(stack.getTag().getIntArray("destination"));
                    if (target != null) {
                        lavaPortalBlockEntity.setDestination(target);
                        player.displayClientMessage(Component.translatable("Portal Destination set to: "+pos),false);
                    }
                } else {
                    stack.getOrCreateTag().putIntArray("destination",new int[]{pos.getX(),pos.getY(),pos.getZ()});
                    player.sendSystemMessage(Component.translatable("Destination set to: "+pos,true));
                }
            }
        }
    }

    public static BlockPos from(int[] ints) {
        return ints.length > 2 ? new BlockPos(ints[0],ints[1],ints[2]) : null;
    }

    public void entityTick(LivingEvent.LivingTickEvent event) {
        LavaPortal.tickEntity(event.getEntity());
    }

    private void register(RegisterEvent event) {
        event.register(Registry.BLOCK_REGISTRY,new ResourceLocation(LavaPortal.MOD_ID,"lava_portal"),() -> Init.LAVA_PORTAL);
        event.register(Registry.BLOCK_REGISTRY,new ResourceLocation(LavaPortal.MOD_ID,"lava_portal_frame"),() -> Init.LAVA_PORTAL_FRAME);
        event.register(Registry.ITEM_REGISTRY,new ResourceLocation(LavaPortal.MOD_ID,"lava_portal_frame"),() -> Init.LAVA_PORTAL_FRAME_I);
        event.register(Registry.BLOCK_ENTITY_TYPE_REGISTRY,new ResourceLocation(LavaPortal.MOD_ID,"lava_portal"),() -> Init.LAVA_PORTAL_BLOCK_E);
    }

    
    // This method exists as a wrapper for the code in the Common project.
    // It takes Forge's event object and passes the parameters along to
    // the Common listener.
    private void onItemTooltip(ItemTooltipEvent event) {
        
        LavaPortal.onItemTooltip(event.getItemStack(), event.getFlags(), event.getToolTip());
    }
}