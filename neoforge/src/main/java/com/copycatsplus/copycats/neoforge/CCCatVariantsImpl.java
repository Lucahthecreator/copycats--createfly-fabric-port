package com.copycatsplus.copycats.neoforge;

import com.copycatsplus.copycats.CCCatVariants;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.CatVariant;
import net.neoforged.neoforge.registries.RegisterEvent;

public class CCCatVariantsImpl extends CCCatVariants {
    public static void register() {
        CopycatsImpl.modBus.addListener(CCCatVariantsImpl::onRegister);
    }

    private static void onRegister(RegisterEvent event) {
        event.register(BuiltInRegistries.CAT_VARIANT.key(), helper -> {
            for (Pair<Holder.Reference<CatVariant>, ResourceLocation> entry : ENTRIES) {
                CatVariant instance = new CatVariant(entry.getSecond());
                helper.register(entry.getFirst().key(), instance);
                entry.getFirst().bindValue(instance);
            }
        });
    }
}
