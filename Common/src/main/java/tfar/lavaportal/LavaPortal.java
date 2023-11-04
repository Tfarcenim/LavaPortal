package tfar.lavaportal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.lavaportal.init.Init;

import java.util.List;
import java.util.Optional;

public class LavaPortal {

    public static final String MOD_ID = "lavaportal";
    public static final String MOD_NAME = "Multi Loader Template";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // This method serves as an initialization hook for the mod. The vanilla
    // game has no mechanism to load tooltip listeners so this must be
    // invoked from a mod loader specific project like Forge or Fabric.
    public static void init() {

    }

    // This method serves as a hook to modify item tooltips. The vanilla game
    // has no mechanism to load tooltip listeners so this must be registered
    // by a mod loader like Forge or Fabric.
    public static void onItemTooltip(ItemStack stack, TooltipFlag context, List<Component> tooltip) {

        if (!stack.isEmpty()) {

            final FoodProperties food = stack.getItem().getFoodProperties();

            if (food != null) {

                tooltip.add(Component.literal("Nutrition: " + food.getNutrition()));
                tooltip.add(Component.literal("Saturation: " + food.getSaturationModifier()));
            }
        }
    }

    public static void tickEntity(LivingEntity livingEntity) {

    }

    public static boolean onCustomPortal(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {

        if (!oldState.is(state.getBlock())) {
            Optional<LavaPortalShape> optional = LavaPortalShape.findEmptyPortalShape(level, pos, Direction.Axis.X, Init.LAVA_PORTAL_FRAME,Init.LAVA_PORTAL);
            //optional = net.minecraftforge.event.ForgeEventFactory.onTrySpawnPortal(pLevel, pPos, optional);
            if (optional.isPresent()) {
                optional.get().createPortalBlocks();
                return true;
            }
        }
        return false;
    }

}