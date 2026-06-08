/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.item.CreativeModeTab
 */
package com.copycatsplus.copycats.registrate.fabric;

import com.copycatsplus.copycats.fabric.mixin_interfaces.CreateRegistrateAccessor;
import com.copycatsplus.copycats.registrate.CopycatRegistrate;
import com.zurrtum.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class CopycatRegistrateImpl
extends CopycatRegistrate {
    protected CopycatRegistrateImpl(String modid) {
        super(modid);
    }

    public static <Tab> CreateRegistrate setTab(Tab tab) {
        ((CreateRegistrateAccessor)((Object)CopycatRegistrateImpl.getInstance())).copycats$setCreativeTab((ResourceKey<CreativeModeTab>)((ResourceKey)tab));
        return CopycatRegistrateImpl.getInstance();
    }
}

