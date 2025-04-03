package com.copycatsplus.copycats.neoforge.mixin_interfaces;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;

public interface CreateRegistrateAccessor {

    void copycats$setCreativeTab(DeferredHolder<CreativeModeTab, CreativeModeTab> tab);
}
