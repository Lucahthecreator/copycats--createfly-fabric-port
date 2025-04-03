package com.copycatsplus.copycats.datagen.recipes.neoforge;


import com.copycatsplus.copycats.Copycats;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

public class CCCraftingConditions {

    public static void register() {
        NeoForge.EVENT_BUS.addListener(RegisterEvent.class, registerEvent -> {
            registerEvent.register(NeoForgeRegistries.CONDITION_SERIALIZERS.key(), helper -> {
               helper.register(Copycats.asResource("feature_enabled"), FeatureEnabledCondition.CODEC);
            });
        });
    }
}
