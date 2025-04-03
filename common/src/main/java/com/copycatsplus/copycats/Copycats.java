package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes;
import com.copycatsplus.copycats.datagen.recipes.gen.CopycatsRecipeProvider;
import com.copycatsplus.copycats.foundation.tooltip.CopycatDescription;
import com.copycatsplus.copycats.network.CCPackets;
import com.copycatsplus.copycats.utility.TooltipUtils;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Copycats {
    public static final String MODID = "copycats";
    /**
     * For user-facing non-translatable display names
     */
    public static final String NAME = "Copycats";
    //Only used for the data fixers!!!
    public static final int DATA_FIXER_VERSION = 1;
    public static final Logger LOGGER = LoggerFactory.getLogger("Copycats+");

    private static final CopycatRegistrate REGISTRATE = CopycatRegistrate.create(MODID);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> TooltipUtils.sequential(
                CopycatDescription.create(item),
                new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE),
                TooltipModifier.mapNull(KineticStats.create(item)))
        );
    }

    public static void init() {

        CCCreativeTabs.setCreativeTab();

        CCBlocks.register();
        CCBlockEntityTypes.register();
        CCCatVariants.register();
        CCItems.register();

        CCConfigs.register();

        CCPackets.register();

        finalizeRegistrate();
    }

    public static CopycatRegistrate getRegistrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @ExpectPlatform
    public static void finalizeRegistrate() {
        throw new AssertionError();
    }

}
