package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatFoldingDoorModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatModelPart;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CopycatFoldingDoorModelCore.class)
abstract class CopycatFoldingDoorModelCoreMixin {
    @Shadow private boolean kinetic;
    @Inject(method = "registerModels", at = @At("HEAD"), cancellable = true)
    private void copycats$registerHalves(List<CopycatModelCore.ModelEntry> entries, CallbackInfo ci) {
        CopycatModelCore.EntryType type = kinetic
                ? CopycatModelCore.EntryType.KINETIC_COPYCAT
                : CopycatModelCore.EntryType.COPYCAT;
        CopycatModelPart part = (CopycatModelPart) (Object) this;
        entries.add(new CopycatModelCore.ModelEntry("lower", (state, material) -> CopycatModelCore.getModelOf(material), part, type));
        entries.add(new CopycatModelCore.ModelEntry("upper", (state, material) -> CopycatModelCore.getModelOf(material), part, type));
        ci.cancel();
    }
    @Inject(method = "emitCopycatQuads", at = @At("HEAD"), cancellable = true)
    private void copycats$onlyRenderMatchingHalf(String key, BlockState state, CopycatRenderContext context, BlockState material, CallbackInfo ci) {
        boolean upper = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER;
        if (!key.equals(upper ? "upper" : "lower")) ci.cancel();
    }
}
