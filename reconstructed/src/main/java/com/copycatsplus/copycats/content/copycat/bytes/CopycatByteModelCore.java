/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatByteModelCore
extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        for (CopycatByteBlock.Byte bite : CopycatByteBlock.allBytes) {
            if (!((Boolean)state.getValue((Property)CopycatByteBlock.byByte(bite))).booleanValue()) continue;
            int offsetX = bite.x() ? 8 : 0;
            int offsetY = bite.y() ? 8 : 0;
            int offsetZ = bite.z() ? 8 : 0;
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX, offsetY, offsetZ), CopycatRenderContext.aabb(4.0, 4.0, 4.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.SOUTH));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX + 4, offsetY, offsetZ), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.SOUTH));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX, offsetY, offsetZ + 4), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(0.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.NORTH));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX + 4, offsetY, offsetZ + 4), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.NORTH));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX, offsetY + 4, offsetZ), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(0.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.SOUTH));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX + 4, offsetY + 4, offsetZ), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.SOUTH));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX, offsetY + 4, offsetZ + 4), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(0.0, 12.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.NORTH));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(offsetX + 4, offsetY + 4, offsetZ + 4), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 12.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.NORTH));
        }
    }
}

