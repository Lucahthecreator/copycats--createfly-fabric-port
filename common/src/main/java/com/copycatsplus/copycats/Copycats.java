package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes;
import com.copycatsplus.copycats.foundation.tooltip.CopycatDescription;
import com.copycatsplus.copycats.network.CCPackets;
import com.copycatsplus.copycats.utility.TooltipUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
            .setTooltipModifierFactory(item -> TooltipUtils.sequential(
                    CopycatDescription.create(item),
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE),
                    TooltipModifier.mapNull(KineticStats.create(item)))
            );

    public static void init() {
        CCBlocks.register();
        CCBlockEntityTypes.register();
        CCCatVariants.register();
        CCItems.register();

        CCConfigs.register();

        CCPackets.register();
    }

    public static void gatherData(DataGenerator.PackGenerator gen, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        gen.addProvider(output -> new CCStandardRecipes(output, lookupProvider));
    }

    public static CreateRegistrate getRegistrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
