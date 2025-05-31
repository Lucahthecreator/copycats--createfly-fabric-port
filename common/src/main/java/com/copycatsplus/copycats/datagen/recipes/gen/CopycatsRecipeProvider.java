package com.copycatsplus.copycats.datagen.recipes.gen;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
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

    public static GeneratedRecipe register(GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }

    @Override
    public void buildRecipes(@NotNull RecipeOutput output) {
        all.forEach(recipe -> recipe.register(output));
        Copycats.LOGGER.info("{} registered {} recipe{}", getName(), all.size(), all.size() == 1 ? "" : "s");
    }

}
