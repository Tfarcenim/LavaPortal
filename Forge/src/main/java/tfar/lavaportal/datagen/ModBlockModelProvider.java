package tfar.lavaportal.datagen;

import com.google.gson.JsonElement;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import tfar.lavaportal.init.Init;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModBlockModelProvider extends BlockModelGenerators {

    public ModBlockModelProvider(Consumer<BlockStateGenerator> pBlockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> pModelOutput, Consumer<Item> pSkippedAutoModelsOutput) {
        super(pBlockStateOutput, pModelOutput, pSkippedAutoModelsOutput);
    }

    @Override
    public void run() {
        this.createTrivialCube(Init.LAVA_PORTAL_FRAME);
        createLavaPortalBlock();
    }

    public void createLavaPortalBlock() {
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Init.LAVA_PORTAL)
                .with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_AXIS).select(Direction.Axis.X, Variant.variant()
                        .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Init.LAVA_PORTAL, "_ns")))
                        .select(Direction.Axis.Z, Variant.variant()
                                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Init.LAVA_PORTAL, "_ew")))));
    }

}
