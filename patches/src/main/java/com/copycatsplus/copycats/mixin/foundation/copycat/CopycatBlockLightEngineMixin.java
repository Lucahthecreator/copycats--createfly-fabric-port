package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LightChunk;
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
        BlockEntity blockEntity = copycats$getLoadedBlockEntity(chunkSource, BlockPos.of(packedPos));

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

    // The light engine runs off the main thread. Level#getBlockEntity can wait for the main thread to load the
    // chunk while the main thread waits for this chunk's light, which deadlocks the server, so only read chunks
    // that are already loaded.
    private static BlockEntity copycats$getLoadedBlockEntity(LightChunkGetter chunkSource, BlockPos pos) {
        LightChunk chunk = chunkSource.getChunkForLighting(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
        return chunk instanceof LevelChunk levelChunk ? levelChunk.getBlockEntities().get(pos) : null;
    }
}
