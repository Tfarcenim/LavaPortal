package tfar.lavaportal.datagen;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import tfar.lavaportal.LavaPortal;

import java.util.stream.Stream;

public class ModDatagen {

    public static void start(GatherDataEvent event) {
        boolean client = event.includeClient();
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        dataGenerator.addProvider(client,new ModModelProvider(dataGenerator));
        dataGenerator.addProvider(client,new ModLangProvider(dataGenerator));
        dataGenerator.addProvider(client,new ModLangProvider(dataGenerator));
    }

    public static Stream<Block> getKnownBlocks() {
        return getKnown(Registry.BLOCK);
    }
    public static Stream<Item> getKnownItems() {
        return getKnown(Registry.ITEM);
    }

    public static <V> Stream<V> getKnown(Registry<V> registry) {
        return registry.stream().filter(o -> registry.getKey(o).getNamespace().equals(LavaPortal.MOD_ID));
    }
}
