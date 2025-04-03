package com.copycatsplus.copycats.neoforge;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCCreativeTabs;
import com.copycatsplus.copycats.CopycatRegistrate;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.FeatureToggle;
import com.simibubi.create.Create;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CCCreativeTabsImpl extends CCCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Copycats.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.copycats.main"))
                    .withTabsBefore(Create.asResource("palettes"))
                    .icon(CCBlocks.COPYCAT_SLAB::asStack)
                    .displayItems(new DisplayItemsGenerator(DECORATIVE))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FUNCTIONAL_TAB = TAB_REGISTER.register("functional",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.copycats.functional"))
                    .withTabsBefore(MAIN_TAB.getKey())
                    .icon(CCBlocks.COPYCAT_COGWHEEL::asStack)
                    .displayItems(new DisplayItemsGenerator(FUNCTIONAL))
                    .build());

    public static void setCreativeTab() {
        CopycatRegistrate.setTab(MAIN_TAB);
    }

    public static void register(IEventBus modEventBus) {
        TAB_REGISTER.register(modEventBus);
        modEventBus.addListener(CCCreativeTabsImpl::modifyTabEntries);
    }

    public static void modifyTabEntries(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab().getType() == CreativeModeTab.Type.SEARCH) {
            Set<Item> hiddenItems = Stream.concat(DECORATIVE.stream(), FUNCTIONAL.stream())
                    .filter(x -> !FeatureToggle.isEnabled(x.getId()))
                    .map(ItemProviderEntry::asItem)
                    .collect(Collectors.toSet());
            event.getSearchEntries().removeIf(entry -> hiddenItems.contains(entry.getItem()));
        }
    }

    public static CreativeModeTab getBaseTab() {
        return MAIN_TAB.get();
    }

    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        return MAIN_TAB.getKey();
    }

    public static CreativeModeTab getFunctionalTab() {
        return FUNCTIONAL_TAB.get();
    }

    public static ResourceKey<CreativeModeTab> getFunctionalTabKey() {
        return FUNCTIONAL_TAB.getKey();
    }


}
