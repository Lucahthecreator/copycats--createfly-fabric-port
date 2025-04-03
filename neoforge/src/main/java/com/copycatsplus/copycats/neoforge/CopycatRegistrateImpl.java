package com.copycatsplus.copycats.neoforge;

import com.copycatsplus.copycats.CopycatRegistrate;
import com.copycatsplus.copycats.neoforge.mixin_interfaces.CreateRegistrateAccessor;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CopycatRegistrateImpl extends CopycatRegistrate {

    protected CopycatRegistrateImpl(String modid) {
        super(modid);
    }

    public static <Tab> CreateRegistrate setTab(Tab tab) {
        ((CreateRegistrateAccessor) getInstance()).copycats$setCreativeTab((DeferredHolder<CreativeModeTab, CreativeModeTab>) tab);
        return getInstance();
    }
}
