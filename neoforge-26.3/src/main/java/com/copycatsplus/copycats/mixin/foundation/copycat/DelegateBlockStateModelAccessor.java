package com.copycatsplus.copycats.mixin.foundation.copycat;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * NeoForge: replaces the Fabric API {@code WrapperBlockStateModel} accessor. Mods that wrap block models on NeoForge
 * extend {@link DelegateBlockStateModel}, so copycat models are found through it.
 */
@Mixin(DelegateBlockStateModel.class)
public interface DelegateBlockStateModelAccessor {
    @Accessor("delegate")
    BlockStateModel copycats$getWrapped();
}
