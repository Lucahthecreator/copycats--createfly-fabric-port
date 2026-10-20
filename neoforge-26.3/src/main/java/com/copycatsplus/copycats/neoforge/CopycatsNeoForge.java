package com.copycatsplus.copycats.neoforge;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.FeatureCategory;
import com.copycatsplus.copycats.config.FeatureToggle;
import com.copycatsplus.copycats.utility.LogicalSidedProvider;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * NeoForge entrypoint, replacing the Fabric {@code main} entrypoint ({@code fabric.CopycatsImpl}).
 * <p>
 * Copycats' small bundled Registrate registers straight into the built-in registries, as upstream did from
 * {@code onInitialize}. NeoForge only accepts that while registries are unfrozen, so it runs from the first
 * {@link RegisterEvent} (blocks). Items and block entity types are open during the whole event phase.
 */
@Mod(Copycats.MODID)
public class CopycatsNeoForge {
    private static boolean registered;

    public CopycatsNeoForge(IEventBus modBus, ModContainer container) {
        modBus.addListener(EventPriority.HIGH, CopycatsNeoForge::register);
        CopycatsGameTests.register(modBus);
        NeoForge.EVENT_BUS.addListener((ServerAboutToStartEvent event) -> LogicalSidedProvider.setServer(event::getServer));
    }

    private static void register(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.BLOCK) && !registered) {
            registered = true;
            Copycats.init();
        } else if (event.getRegistryKey().equals(Registries.CREATIVE_MODE_TAB)) {
            registerCreativeTabs();
        }
    }

    private static void registerCreativeTabs() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Copycats.asResource("main"), CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.copycats.main"))
            .icon(CCBlocks.COPYCAT_SLAB::asStack)
            .displayItems((parameters, output) -> Copycats.getRegistrate().getAll(Registries.ITEM).forEach(entry -> {
                Item item = entry.get();
                if (!isFunctional(item)) {
                    output.accept(item);
                }
            }))
            .build());
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Copycats.asResource("functional"), CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.copycats.functional"))
            .icon(CCBlocks.COPYCAT_COGWHEEL::asStack)
            .displayItems((parameters, output) -> Copycats.getRegistrate().getAll(Registries.ITEM).forEach(entry -> {
                Item item = entry.get();
                if (isFunctional(item)) {
                    output.accept(item);
                }
            }))
            .build());
    }

    private static boolean isFunctional(Item item) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item);
        return FeatureToggle.FEATURE_CATEGORIES.getOrDefault(id, Set.of()).contains(FeatureCategory.FUNCTIONAL);
    }
}
