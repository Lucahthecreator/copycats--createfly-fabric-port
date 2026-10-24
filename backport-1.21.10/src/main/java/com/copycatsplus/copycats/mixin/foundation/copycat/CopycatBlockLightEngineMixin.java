package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.lighting.BlockLightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockLightEngine.class)
public abstract class CopycatBlockLightEngineMixin {
    @Redirect(
            method = "getEmission",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getLightEmission()I"
            )
    )
    private int copycats$getCopiedMaterialEmission(BlockState state, long packedPos, BlockState ignoredState) {
        int emission = state.getLightEmission();
        LightChunkGetter chunkSource = ((CopycatLightEngineAccessor) this).copycats$getChunkSource();
        BlockEntity blockEntity = chunkSource.getLevel().getBlockEntity(BlockPos.of(packedPos));

        if (blockEntity instanceof IMultiStateCopycatBlockEntity multiState) {
            for (BlockState material : multiState.getMaterialItemStorage().getAllMaterials()) {
                emission = Math.max(emission, material.getLightEmission());
            }
        } else if (blockEntity instanceof ICopycatBlockEntity copycat) {
            BlockState material = copycat.getMaterial();
            if (material != null) {
                emission = Math.max(emission, material.getLightEmission());
            }
        }

        int finalEmission = emission;
        CopycatsDebug.log("light", () -> "emission pos=" + BlockPos.of(packedPos)
                + " state=" + state + " blockEntity=" + (blockEntity == null ? "null" : blockEntity.getClass().getName())
                + " result=" + finalEmission);
        return emission;
    }
}
