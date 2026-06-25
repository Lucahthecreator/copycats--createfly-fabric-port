package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.FeatureCategory;
import com.copycatsplus.copycats.config.FeatureToggle;
import com.copycatsplus.copycats.utility.LogicalSidedProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public class CopycatsImpl implements ModInitializer {
    public void onInitialize() {
        Copycats.init();
        this.registerCreativeTabs();
        ServerLifecycleEvents.SERVER_STARTING.register(this::serverStarting);
    }

    private void registerCreativeTabs() {
        ResourceKey<CreativeModeTab> mainKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Copycats.asResource("main"));
        CreativeModeTab mainTab = FabricCreativeModeTab.builder()
                .title(Component.translatable("itemGroup.copycats.main"))
                .icon(CCBlocks.COPYCAT_SLAB::asStack)
                .displayItems((parameters, output) -> Copycats.getRegistrate().getAll(BuiltInRegistries.ITEM.key()).forEach(entry -> {
                    Item item = (Item) entry.get();
                    if (!isFunctional(item)) {
                        output.accept((ItemLike) item);
                    }
                }))
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, mainKey, mainTab);

        ResourceKey<CreativeModeTab> functionalKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Copycats.asResource("functional"));
        CreativeModeTab functionalTab = FabricCreativeModeTab.builder()
                .title(Component.translatable("itemGroup.copycats.functional"))
                .icon(CCBlocks.COPYCAT_COGWHEEL::asStack)
                .displayItems((parameters, output) -> Copycats.getRegistrate().getAll(BuiltInRegistries.ITEM.key()).forEach(entry -> {
                    Item item = (Item) entry.get();
                    if (isFunctional(item)) {
                        output.accept((ItemLike) item);
                    }
                }))
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, functionalKey, functionalTab);
    }

    private static boolean isFunctional(Item item) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item);
        return FeatureToggle.FEATURE_CATEGORIES
                .getOrDefault(id, java.util.Set.of())
                .contains(FeatureCategory.FUNCTIONAL);
    }

    private void serverStarting(MinecraftServer server) {
        LogicalSidedProvider.setServer(() -> server);
    }

    public static void finalizeRegistrate() {
        Copycats.getRegistrate().register();
    }
}
