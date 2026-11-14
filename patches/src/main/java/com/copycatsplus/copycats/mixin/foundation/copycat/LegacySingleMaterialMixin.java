package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.compat.LegacyMaterialCodec;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.mojang.serialization.Codec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = ICopycatBlockEntity.class, remap = false)
public interface LegacySingleMaterialMixin {
    @ModifyArg(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/ValueInput;read(Ljava/lang/String;Lcom/mojang/serialization/Codec;)Ljava/util/Optional;", ordinal = 1), index = 1)
    private static Codec<?> copycats$readLegacyMaterial(Codec<?> codec) {
        return LegacyMaterialCodec.CODEC;
    }
}
