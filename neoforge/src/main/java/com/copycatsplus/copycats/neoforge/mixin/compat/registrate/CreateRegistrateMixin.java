package com.copycatsplus.copycats.neoforge.mixin.compat.registrate;

import com.copycatsplus.copycats.neoforge.mixin_interfaces.CreateRegistrateAccessor;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Allow setting the creative tab during registration.
 */
@Mixin(CreateRegistrate.class)
public class CreateRegistrateMixin implements CreateRegistrateAccessor {
    @Shadow protected DeferredHolder<CreativeModeTab, CreativeModeTab> currentTab;

    @Override
    public void copycats$setCreativeTab(DeferredHolder<CreativeModeTab, CreativeModeTab> tab) {
        currentTab = tab;
    }
}
