package com.copycatsplus.copycats.datagen.recipes.neoforge;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.datagen.recipes.gen.CopycatsRecipeProvider;
import com.copycatsplus.copycats.datagen.recipes.gen.GeneratedRecipeBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.platform.CatnipServices;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.common.crafting.ConditionalRecipeOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.inventoryTrigger;

public class GeneratedRecipeBuilderNeoForge implements GeneratedRecipeBuilder {

    private final List<ICondition> recipeConditions = new ArrayList<>();
    private String path = "";
    private String suffix;
    private Supplier<? extends ItemLike> result;
    private ResourceLocation compatDatagenOutput;

    private Supplier<ItemPredicate> unlockedBy;
    private int amount;

    private GeneratedRecipeBuilderNeoForge(String path) {
        this.path = path;
        suffix = "";
        this.amount = 1;
    }

    public GeneratedRecipeBuilderNeoForge(String path, Supplier<? extends ItemLike> result) {
        this(path);
        this.result = result;
    }

    public GeneratedRecipeBuilderNeoForge(String path, ResourceLocation result) {
        this(path);
        compatDatagenOutput = result;
    }

    @Override
    public GeneratedRecipeBuilder returns(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
        this.unlockedBy = () -> ItemPredicate.Builder.item()
                .of(item.get())
                .build();
        return this;
    }

    @Override
    public GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
        this.unlockedBy = () -> ItemPredicate.Builder.item()
                .of(tag.get())
                .build();
        return this;
    }

    @Override
    public GeneratedRecipeBuilder withSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    @Override
    public GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
        return handleConditions(consumer -> {
            ShapedRecipeBuilder b = builder.apply(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get(), amount));
            if (unlockedBy != null)
                b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
            b.save(consumer, createLocation("crafting"));
        });
    }

    @Override
    public GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
        return handleConditions(consumer -> {
            ShapelessRecipeBuilder b = builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), amount));
            if (unlockedBy != null)
                b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
            b.save(consumer, createLocation("crafting"));
        });
    }

    private ResourceLocation clean(ResourceLocation loc) {
        String path = loc.getPath();
        while (path.contains("//"))
            path = path.replaceAll("//", "/");
        return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), path);
    }

    private ResourceLocation createSimpleLocation(String recipeType) {
        ResourceLocation loc = clean(Copycats.asResource(recipeType + "/" + getRegistryName().getPath() + suffix));
        return loc;
    }

    protected ResourceLocation createLocation(String recipeType) {
        ResourceLocation loc = clean(Copycats.asResource(recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix));
        return loc;
    }

    private ResourceLocation getRegistryName() {
        return compatDatagenOutput == null ? RegisteredObjectsHelper.getKeyOrThrow(result.get()
                .asItem()) : compatDatagenOutput;
    }


    @Override
    public GeneratedRecipeBuilder requiresResultFeature() {
        return requiresFeature(RegisteredObjectsHelper.getKeyOrThrow(result.get().asItem()));
    }

    @Override
    public GeneratedRecipeBuilder requiresFeature(ResourceLocation location) {
        return requiresFeature(location, false);
    }

    @Override
    public GeneratedRecipeBuilder requiresFeature(BlockEntry<?> block) {
        return requiresFeature(block, false);
    }

    @Override
    public GeneratedRecipeBuilder requiresFeature(BlockEntry<?> block, boolean invert) {
        return requiresFeature(block.getId(), invert);
    }

    @Override
    public GeneratedRecipeBuilder requiresFeature(ResourceLocation location, boolean invert) {
        recipeConditions.add(new FeatureEnabledCondition(location, invert));
        return this;
    }

    @Override
    public GeneratedRecipe handleConditions(Consumer<RecipeOutput> recipe) {
        return CopycatsRecipeProvider.register(output -> {
            if (!recipeConditions.isEmpty()) {
                recipe.accept(output.withConditions(recipeConditions.toArray(new ICondition[0])));
            } else {
                recipe.accept(output);
            }
        });
    }

    @Override
    public GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item) {
        return unlockedBy(item).viaCookingIngredient(() -> Ingredient.of(item.get()));
    }

    @Override
    public GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag) {
        return unlockedByTag(tag).viaCookingIngredient(() -> Ingredient.of(tag.get()));
    }

    @Override
    public GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient) {
        return new GeneratedCookingRecipeBuilderNeoForge(ingredient);
    }

    @Override
    public GeneratedStoneCuttingRecipeBuilder viaStonecutting(Supplier<? extends ItemLike> item) {
        return unlockedBy(item).viaStonecuttingIngredient(() -> Ingredient.of(item.get()));
    }

    @Override
    public GeneratedStoneCuttingRecipeBuilder viaStonecuttingTag(Supplier<TagKey<Item>> tag) {
        return unlockedByTag(tag).viaStonecuttingIngredient(() -> Ingredient.of(tag.get()));
    }

    @Override
    public GeneratedStoneCuttingRecipeBuilder viaStonecuttingIngredient(Supplier<Ingredient> ingredient) {
        return new GeneratedStoneCuttingRecipeBuilderNeoForge(ingredient);
    }

    public class GeneratedCookingRecipeBuilderNeoForge implements GeneratedCookingRecipeBuilder {

        private final Supplier<Ingredient> ingredient;
        private float exp;
        private int cookingTime;

        GeneratedCookingRecipeBuilderNeoForge(Supplier<Ingredient> ingredient) {
            this.ingredient = ingredient;
            cookingTime = 200;
            exp = 0;
        }

        @Override
        public GeneratedCookingRecipeBuilder forDuration(int duration) {
            cookingTime = duration;
            return this;
        }

        @Override
        public GeneratedCookingRecipeBuilder rewardXP(float xp) {
            exp = xp;
            return this;
        }

        @Override
        public GeneratedRecipe inFurnace() {
            return inFurnace(b -> b);
        }

        @Override
        public GeneratedRecipe inFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
            return create(RecipeSerializer.SMELTING_RECIPE, builder, SmeltingRecipe::new, 1);
        }

        @Override
        public GeneratedRecipe inSmoker() {
            return inSmoker(b -> b);
        }

        @Override
        public GeneratedRecipe inSmoker(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
            create(RecipeSerializer.SMELTING_RECIPE, builder, SmeltingRecipe::new, 1);
            create(RecipeSerializer.CAMPFIRE_COOKING_RECIPE, builder, CampfireCookingRecipe::new, 3);
            return create(RecipeSerializer.SMOKING_RECIPE, builder, SmokingRecipe::new, .5f);
        }

        @Override
        public GeneratedRecipe inBlastFurnace() {
            return inBlastFurnace(b -> b);
        }

        @Override
        public GeneratedRecipe inBlastFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
            create(RecipeSerializer.SMELTING_RECIPE, builder, SmeltingRecipe::new, 1);
            return create(RecipeSerializer.BLASTING_RECIPE, builder, BlastingRecipe::new, .5f);
        }

        private <T extends AbstractCookingRecipe> GeneratedRecipe create(RecipeSerializer<T> serializer,
                                       UnaryOperator<SimpleCookingRecipeBuilder> builder, AbstractCookingRecipe.Factory<T> factory, float cookingTimeModifier) {
            return CopycatsRecipeProvider.register(consumer -> {
                boolean isOtherMod = compatDatagenOutput != null;

                SimpleCookingRecipeBuilder b = builder.apply(SimpleCookingRecipeBuilder.generic(ingredient.get(),
                        RecipeCategory.MISC, isOtherMod ? Items.DIRT : result.get(), exp,
                        (int) (cookingTime * cookingTimeModifier), serializer, factory));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createSimpleLocation(RegisteredObjectsHelper.getKeyOrThrow(serializer)
                        .getPath()));
            });
        }
    }

    public class GeneratedStoneCuttingRecipeBuilderNeoForge implements GeneratedStoneCuttingRecipeBuilder {

        private final Supplier<Ingredient> ingredient;

        GeneratedStoneCuttingRecipeBuilderNeoForge(Supplier<Ingredient> ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        public GeneratedRecipe create() {
            return create(b -> b);
        }

        private GeneratedRecipe create(UnaryOperator<SingleItemRecipeBuilder> builder) {
            return handleConditions(consumer -> {
                SingleItemRecipeBuilder b = builder.apply(SingleItemRecipeBuilder.stonecutting(ingredient.get(), RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("stonecutting"));
            });
        }
    }
}

