/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package com.copycatsplus.copycats.config;

import com.copycatsplus.copycats.config.FeatureCategory;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;

public class FeatureToggle {
    public static final Set<ResourceLocation> TOGGLEABLE_FEATURES = new HashSet<ResourceLocation>();
    public static final Map<ResourceLocation, ResourceLocation> DEPENDENT_FEATURES = new HashMap<ResourceLocation, ResourceLocation>();
    public static final Map<ResourceLocation, Set<FeatureCategory>> FEATURE_CATEGORIES = new HashMap<ResourceLocation, Set<FeatureCategory>>();

    public static void register(ResourceLocation key) {
        TOGGLEABLE_FEATURES.add(key);
    }

    public static void register(ResourceLocation key, FeatureCategory ... categories) {
        FeatureToggle.register(key);
        FEATURE_CATEGORIES.put(key, Set.of(categories));
    }

    public static void registerDependent(ResourceLocation key, ResourceLocation dependency) {
        DEPENDENT_FEATURES.put(key, dependency);
    }

    public static void registerDependent(ResourceLocation key, ResourceLocation dependency, FeatureCategory ... categories) {
        FeatureToggle.registerDependent(key, dependency);
        FEATURE_CATEGORIES.put(key, Set.of(categories));
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> register() {
        return b -> {
            FeatureToggle.register(ResourceLocation.fromNamespaceAndPath((String)b.getOwner().getModid(), (String)b.getName()));
            return b;
        };
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> register(FeatureCategory ... categories) {
        return b -> {
            FeatureToggle.register(ResourceLocation.fromNamespaceAndPath((String)b.getOwner().getModid(), (String)b.getName()), categories);
            return b;
        };
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(ResourceLocation dependency) {
        return b -> {
            FeatureToggle.registerDependent(ResourceLocation.fromNamespaceAndPath((String)b.getOwner().getModid(), (String)b.getName()), dependency);
            return b;
        };
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(ResourceLocation dependency, FeatureCategory ... categories) {
        return b -> {
            FeatureToggle.registerDependent(ResourceLocation.fromNamespaceAndPath((String)b.getOwner().getModid(), (String)b.getName()), dependency, categories);
            return b;
        };
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(BlockEntry<?> dependency) {
        return b -> {
            FeatureToggle.registerDependent(ResourceLocation.fromNamespaceAndPath((String)b.getOwner().getModid(), (String)b.getName()), dependency.getId());
            return b;
        };
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(BlockEntry<?> dependency, FeatureCategory ... categories) {
        return b -> {
            FeatureToggle.registerDependent(ResourceLocation.fromNamespaceAndPath((String)b.getOwner().getModid(), (String)b.getName()), dependency.getId(), categories);
            return b;
        };
    }

    public static boolean isEnabled(ResourceLocation key) {
        return true;
    }

    static void refreshItemVisibility() {
    }
}

