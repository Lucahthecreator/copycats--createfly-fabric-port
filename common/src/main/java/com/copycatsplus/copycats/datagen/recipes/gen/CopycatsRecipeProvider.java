package com.copycatsplus.copycats.datagen.recipes.gen;

import com.copycatsplus.copycats.Copycats;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static com.copycatsplus.copycats.datagen.recipes.gen.GeneratedRecipeBuilder.GeneratedRecipe;

public abstract class CopycatsRecipeProvider extends RecipeProvider {

    protected static final List<GeneratedRecipe> all = new ArrayList<>();

    public CopycatsRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public void registerRecipes(@NotNull RecipeOutput output) {
        all.forEach(c -> c.register(output));
        Copycats.LOGGER.info("{} registered {} recipe{}", getName(), all.size(), all.size() == 1 ? "" : "s");
    }

    public static GeneratedRecipe register(GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        all.forEach(recipe -> recipe.register(output));
    }

}
