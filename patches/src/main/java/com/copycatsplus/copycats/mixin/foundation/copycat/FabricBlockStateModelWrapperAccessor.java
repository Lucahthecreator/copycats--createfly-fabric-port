package com.copycatsplus.copycats.mixin.foundation.copycat;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Accesses Fabric API's generic wrapper without depending on any optional model-wrapping mod. */
@Mixin(value = WrapperBlockStateModel.class, remap = false)
public interface FabricBlockStateModelWrapperAccessor {
    @Accessor("wrapped")
    BlockStateModel copycats$getWrapped();
}
