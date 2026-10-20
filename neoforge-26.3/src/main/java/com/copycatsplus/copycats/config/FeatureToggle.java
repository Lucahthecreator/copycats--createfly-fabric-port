package com.copycatsplus.copycats.config;

import com.copycatsplus.copycats.internal.registrate.builders.Builder;
import com.copycatsplus.copycats.internal.registrate.util.entry.BlockEntry;
import com.copycatsplus.copycats.internal.registrate.util.nullness.NonNullUnaryOperator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.Identifier;

public class FeatureToggle {
    public static final Set<Identifier> TOGGLEABLE_FEATURES = new HashSet<>();
    public static final Map<Identifier, Identifier> DEPENDENT_FEATURES = new HashMap<>();
    public static final Map<Identifier, Set<FeatureCategory>> FEATURE_CATEGORIES = new HashMap<>();

    public static void register(Identifier key) {
        TOGGLEABLE_FEATURES.add(key);
    }

    public static void register(Identifier key, FeatureCategory... categories) {
        register(key);
        FEATURE_CATEGORIES.put(key, Set.of(categories));
    }

    public static void registerDependent(Identifier key, Identifier dependency) {
        DEPENDENT_FEATURES.put(key, dependency);
    }

    public static void registerDependent(Identifier key, Identifier dependency, FeatureCategory... categories) {
        registerDependent(key, dependency);
        FEATURE_CATEGORIES.put(key, Set.of(categories));
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> register() {
        return b -> {
            register(Identifier.fromNamespaceAndPath(b.getOwner().getModid(), b.getName()));
            return b;
        };
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> register(FeatureCategory... categories) {
        return b -> {
            register(Identifier.fromNamespaceAndPath(b.getOwner().getModid(), b.getName()), categories);
            return b;
        };
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(Identifier dependency) {
        return b -> {
            registerDependent(Identifier.fromNamespaceAndPath(b.getOwner().getModid(), b.getName()), dependency);
            return b;
        };
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(
        Identifier dependency, FeatureCategory... categories
    ) {
        return b -> {
            registerDependent(Identifier.fromNamespaceAndPath(b.getOwner().getModid(), b.getName()), dependency, categories);
            return b;
        };
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(BlockEntry<?> dependency) {
        return b -> {
            registerDependent(Identifier.fromNamespaceAndPath(b.getOwner().getModid(), b.getName()), dependency.getId());
            return b;
        };
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(
        BlockEntry<?> dependency, FeatureCategory... categories
    ) {
        return b -> {
            registerDependent(Identifier.fromNamespaceAndPath(b.getOwner().getModid(), b.getName()), dependency.getId(), categories);
            return b;
        };
    }

    public static boolean isEnabled(Identifier key) {
        return true;
    }

    static void refreshItemVisibility() {
    }
}
