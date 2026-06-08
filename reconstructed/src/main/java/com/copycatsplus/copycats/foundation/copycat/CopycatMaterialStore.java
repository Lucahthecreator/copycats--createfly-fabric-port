/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Either
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ChunkPos
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.chunk.LevelChunk
 */
package com.copycatsplus.copycats.foundation.copycat;

import com.mojang.datafixers.util.Either;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public class CopycatMaterialStore {
    private final Map<ChunkPos, Map<BlockPos, Either<BlockState, Map<String, BlockState>>>> materialMap = new ConcurrentHashMap<ChunkPos, Map<BlockPos, Either<BlockState, Map<String, BlockState>>>>();
    private static final Map<BlockGetter, CopycatMaterialStore> STORES = Collections.synchronizedMap(new WeakHashMap());

    private void setMaterial(BlockPos pos, BlockState state) {
        ChunkPos chunkPos = ChunkPos.containing((BlockPos)pos);
        this.materialMap.computeIfAbsent(chunkPos, p -> new ConcurrentHashMap()).put(pos, Either.left((Object)state));
    }

    private void setMaterial(BlockPos pos, Map<String, BlockState> states) {
        ChunkPos chunkPos = ChunkPos.containing((BlockPos)pos);
        this.materialMap.computeIfAbsent(chunkPos, p -> new ConcurrentHashMap()).put(pos, Either.right(states));
    }

    private Either<BlockState, Map<String, BlockState>> getMaterial(BlockPos pos) {
        return this.materialMap.getOrDefault(ChunkPos.containing((BlockPos)pos), Map.of()).getOrDefault(pos, Either.left((Object)Blocks.AIR.defaultBlockState()));
    }

    public void unloadChunk(ChunkPos chunk) {
        this.materialMap.remove(chunk);
    }

    private static CopycatMaterialStore get(BlockGetter level) {
        if (level instanceof LevelChunk) {
            LevelChunk chunk = (LevelChunk)level;
            level = chunk.getLevel();
        }
        return STORES.computeIfAbsent(level, l -> new CopycatMaterialStore());
    }

    public static void setMaterial(BlockGetter level, BlockPos pos, BlockState state) {
        CopycatMaterialStore.get(level).setMaterial(pos, state);
    }

    public static void setMaterial(BlockGetter level, BlockPos pos, Map<String, BlockState> states) {
        CopycatMaterialStore.get(level).setMaterial(pos, states);
    }

    public static Either<BlockState, Map<String, BlockState>> getMaterial(BlockGetter level, BlockPos pos) {
        return CopycatMaterialStore.get(level).getMaterial(pos);
    }

    public static void unloadLevel(BlockGetter level) {
        STORES.remove(level);
    }

    public static void unloadChunk(BlockGetter level, ChunkPos chunk) {
        CopycatMaterialStore.get(level).unloadChunk(chunk);
    }
}

