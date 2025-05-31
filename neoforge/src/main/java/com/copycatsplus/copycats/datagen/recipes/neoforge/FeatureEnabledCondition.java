package com.copycatsplus.copycats.datagen.recipes.neoforge;

import com.copycatsplus.copycats.config.FeatureToggle;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

public record FeatureEnabledCondition(ResourceLocation feature, boolean invert) implements ICondition {
    public static MapCodec<FeatureEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ResourceLocation.CODEC.fieldOf("feature").forGetter(FeatureEnabledCondition::feature),
            Codec.BOOL.fieldOf("invert").forGetter(FeatureEnabledCondition::invert))
            .apply(builder, FeatureEnabledCondition::new));

    public FeatureEnabledCondition(ResourceLocation feature) {
        this(feature, false);
    }

    @Override
    public boolean test(IContext context) {
        return FeatureToggle.isEnabled(feature) != invert;
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public String toString() {
        return "FeatureEnabledCondition{" +
                "feature=" + feature +
                ", invert=" + invert +
                '}';
    }
}
