/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ModInitializer
 *  net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.Identifier
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.level.ItemLike
 */
package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
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
import net.minecraft.world.level.ItemLike;

public class CopycatsImpl
implements ModInitializer {
    public void onInitialize() {
        Copycats.init();
        this.registerCreativeTab();
        ServerLifecycleEvents.SERVER_STARTING.register(this::serverStarting);
    }

    private void registerCreativeTab() {
        ResourceKey key = ResourceKey.create((ResourceKey)Registries.CREATIVE_MODE_TAB, (Identifier)Copycats.asResource("main"));
        CreativeModeTab tab = FabricCreativeModeTab.builder().title((Component)Component.translatable((String)"itemGroup.copycats.main")).icon(CCBlocks.COPYCAT_SLAB::asStack).displayItems((parameters, output) -> Copycats.getRegistrate().getAll(BuiltInRegistries.ITEM.key()).forEach(entry -> output.accept((ItemLike)entry.get()))).build();
        Registry.register((Registry)BuiltInRegistries.CREATIVE_MODE_TAB, (ResourceKey)key, (Object)tab);
    }

    private void serverStarting(MinecraftServer server) {
        LogicalSidedProvider.setServer(() -> server);
    }

    public static void finalizeRegistrate() {
        Copycats.getRegistrate().register();
    }
}

