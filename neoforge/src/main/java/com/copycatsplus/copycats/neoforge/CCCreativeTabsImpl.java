package com.copycatsplus.copycats.neoforge;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCCreativeTabs;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.FeatureToggle;
import com.simibubi.create.Create;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CCCreativeTabsImpl extends CCCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Copycats.MODID);

    public static final Supplier<CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.copycats.main"))
                    .withTabsBefore(Create.asResource("palettes"))
                    .icon(CCBlocks.COPYCAT_SLAB::asStack)
                    .displayItems(new AdvancedDisplayGenerator(DECORATIVE, CCCreativeTabsImpl.MAIN_TAB))
                    .build());

    public static final Supplier<CreativeModeTab> FUNCTIONAL_TAB = TAB_REGISTER.register("functional", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.copycats.functional"))
            .withTabsBefore(Copycats.asResource("main"))
            .icon(CCBlocks.COPYCAT_COGWHEEL::asStack)
            .displayItems(new AdvancedDisplayGenerator(FUNCTIONAL, CCCreativeTabsImpl.FUNCTIONAL_TAB))
            .build());

    public static void setCreativeTab() {

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
            event.getSearchEntries().forEach(itemStack -> {
                if (hiddenItems.contains(itemStack.getItem())) {
                    if (event.getTab().contains(itemStack))
                        event.remove(itemStack, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
                }
            });
        }
    }

    public static CreativeModeTab getBaseTab() {
        return MAIN_TAB.get();
    }

    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        return TAB_REGISTER.getRegistry().get().getResourceKey(MAIN_TAB.get()).get();
    }

    public static CreativeModeTab getFunctionalTab() {
        return FUNCTIONAL_TAB.get();
    }

    public static ResourceKey<CreativeModeTab> getFunctionalTabKey() {
        return TAB_REGISTER.getRegistry().get().getResourceKey(FUNCTIONAL_TAB.get()).get();
    }


}
