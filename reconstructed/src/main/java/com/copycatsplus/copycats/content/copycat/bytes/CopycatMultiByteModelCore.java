/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatMultiByteModelCore
extends CopycatModelCore {
    private static final Map<String, CopycatByteBlock.Byte> byteMap = CopycatByteBlock.allBytes.stream().collect(Collectors.toMap(s -> CopycatByteBlock.byByte(s).getName(), s -> s));

    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        this.registerForMultiState(entries, (IMultiStateCopycatBlock)CCBlocks.COPYCAT_BYTE.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        CopycatByteBlock.Byte bite = byteMap.get(key);
        if (!((Boolean)state.getValue((Property)CopycatByteBlock.byByte(bite))).booleanValue()) {
            return;
        }
        int offsetX = bite.x() ? 8 : 0;
        int offsetY = bite.y() ? 8 : 0;
        int offsetZ = bite.z() ? 8 : 0;
        QuadAutoCull autoCull = CopycatRenderContext.autoCull(CopycatRenderContext.aabb(8.0, 8.0, 8.0).move(offsetX, offsetY, offsetZ));
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX, offsetY, offsetZ), CopycatRenderContext.aabb(4.0, 4.0, 4.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.SOUTH), autoCull);
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX + 4, offsetY, offsetZ), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.SOUTH), autoCull);
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX, offsetY, offsetZ + 4), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(0.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.NORTH), autoCull);
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX + 4, offsetY, offsetZ + 4), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.NORTH), autoCull);
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX, offsetY + 4, offsetZ), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(0.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.SOUTH), autoCull);
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX + 4, offsetY + 4, offsetZ), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.SOUTH), autoCull);
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX, offsetY + 4, offsetZ + 4), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(0.0, 12.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.NORTH), autoCull);
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX + 4, offsetY + 4, offsetZ + 4), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 12.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.NORTH), autoCull);
    }
}

