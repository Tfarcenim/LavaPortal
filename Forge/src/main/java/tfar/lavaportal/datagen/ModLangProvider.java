package tfar.lavaportal.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import tfar.lavaportal.LavaPortal;
import tfar.lavaportal.init.Init;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(DataGenerator output) {
        super(output, LavaPortal.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(Init.LAVA_PORTAL,"Lava Portal");
        add(Init.LAVA_PORTAL_FRAME,"Lava Portal Frame");
    }
}
