package com.copycatsplus.copycats.datagen.neoforge;

import com.copycatsplus.copycats.datagen.CCDatagen;
import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes;
import com.copycatsplus.copycats.datagen.recipes.gen.CopycatsRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CCDatagenImpl extends CCDatagen {

    protected static final List<CopycatsRecipeProvider> GENERATORS = new ArrayList();

    public static void gatherData(GatherDataEvent event) {
        addExtraRegistrateData();

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            GENERATORS.add(new CCStandardRecipes(output, lookupProvider));

            generator.addProvider(true, new DataProvider() {

                @Override
                public String getName() {
                    return "Copycat+'s Processing Recipes";
                }

                @Override
                public CompletableFuture<?> run(CachedOutput dc) {
                    return CompletableFuture.allOf(GENERATORS.stream()
                            .map(gen -> gen.run(dc))
                            .toArray(CompletableFuture[]::new));
                }
            });
        }
    }
}
