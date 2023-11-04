package tfar.lavaportal.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.lavaportal.LavaPortal;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {

    @Inject(method = "onPlace",at = @At("HEAD"),cancellable = true)
    private void makeCustomPortal(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving, CallbackInfo ci) {
        if (LavaPortal.onCustomPortal(state, level, pos, oldState, isMoving)) {
            ci.cancel();
        }
    }
}
