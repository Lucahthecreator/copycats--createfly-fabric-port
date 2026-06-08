/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.byte_panel;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.byte_panel.CopycatBytePanelBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatMultiBytePanelModelCore
extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        this.registerForMultiState(entries, (IMultiStateCopycatBlock)CCBlocks.COPYCAT_BYTE_PANEL.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (!((Boolean)state.getValue((Property)CopycatBytePanelBlock.fromProperty(key))).booleanValue()) {
            return;
        }
        int i = key.equals(CopycatBytePanelBlock.BOTTOM_LEFT.getName()) || key.equals(CopycatBytePanelBlock.TOP_LEFT.getName()) ? 1 : 0;
        int j = key.equals(CopycatBytePanelBlock.TOP_LEFT.getName()) || key.equals(CopycatBytePanelBlock.TOP_RIGHT.getName()) ? 1 : 0;
        Direction facing = (Direction)state.getValue(CopycatBytePanelBlock.FACING);
        if (facing.getAxis().isHorizontal()) {
            int rot = (int)facing.toYRot();
            AssemblyTransform transform = t -> t.rotateY(rot);
            QuadAutoCull autoCull = CopycatRenderContext.autoCull(CopycatRenderContext.aabb(8.0, 8.0, 16.0).move(i * 8, j * 8, 0.0));
            context.assemblePiece(transform, CopycatRenderContext.vec3(i * 8, j * 8, 13.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.SOUTH), autoCull);
            context.assemblePiece(transform, CopycatRenderContext.vec3(i * 8 + 4, j * 8, 13.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(12.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.SOUTH), autoCull);
            context.assemblePiece(transform, CopycatRenderContext.vec3(i * 8, j * 8 + 4, 13.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(0.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.SOUTH), autoCull);
            context.assemblePiece(transform, CopycatRenderContext.vec3(i * 8 + 4, j * 8 + 4, 13.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(12.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.SOUTH), autoCull);
            context.assemblePiece(transform, CopycatRenderContext.vec3(i * 8, j * 8, 15.0), CopycatRenderContext.aabb(4.0, 4.0, 1.0).move(0.0, 0.0, 15.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.NORTH), autoCull);
            context.assemblePiece(transform, CopycatRenderContext.vec3(i * 8 + 4, j * 8, 15.0), CopycatRenderContext.aabb(4.0, 4.0, 1.0).move(12.0, 0.0, 15.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.NORTH), autoCull);
            context.assemblePiece(transform, CopycatRenderContext.vec3(i * 8, j * 8 + 4, 15.0), CopycatRenderContext.aabb(4.0, 4.0, 1.0).move(0.0, 12.0, 15.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.NORTH), autoCull);
            context.assemblePiece(transform, CopycatRenderContext.vec3(i * 8 + 4, j * 8 + 4, 15.0), CopycatRenderContext.aabb(4.0, 4.0, 1.0).move(12.0, 12.0, 15.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.NORTH), autoCull);
        } else if (facing == Direction.DOWN) {
            QuadAutoCull autoCull = CopycatRenderContext.autoCull(CopycatRenderContext.aabb(8.0, 16.0, 8.0).move(i * 8, 0.0, j * 8));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8, 0.0, j * 8), CopycatRenderContext.aabb(4.0, 2.0, 4.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.SOUTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8 + 4, 0.0, j * 8), CopycatRenderContext.aabb(4.0, 2.0, 4.0).move(12.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.SOUTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8, 0.0, j * 8 + 4), CopycatRenderContext.aabb(4.0, 2.0, 4.0).move(0.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.NORTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8 + 4, 0.0, j * 8 + 4), CopycatRenderContext.aabb(4.0, 2.0, 4.0).move(12.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.NORTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8, 2.0, j * 8), CopycatRenderContext.aabb(4.0, 1.0, 4.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.SOUTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8 + 4, 2.0, j * 8), CopycatRenderContext.aabb(4.0, 1.0, 4.0).move(12.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.SOUTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8, 2.0, j * 8 + 4), CopycatRenderContext.aabb(4.0, 1.0, 4.0).move(0.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.NORTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8 + 4, 2.0, j * 8 + 4), CopycatRenderContext.aabb(4.0, 1.0, 4.0).move(12.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.NORTH), autoCull);
        } else if (facing == Direction.UP) {
            QuadAutoCull autoCull = CopycatRenderContext.autoCull(CopycatRenderContext.aabb(8.0, 16.0, 8.0).move(i * 8, 0.0, 8 - j * 8));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8, 13.0, 8 - j * 8), CopycatRenderContext.aabb(4.0, 2.0, 4.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.SOUTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8 + 4, 13.0, 8 - j * 8), CopycatRenderContext.aabb(4.0, 2.0, 4.0).move(12.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.SOUTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8, 13.0, 8 - j * 8 + 4), CopycatRenderContext.aabb(4.0, 2.0, 4.0).move(0.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.NORTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8 + 4, 13.0, 8 - j * 8 + 4), CopycatRenderContext.aabb(4.0, 2.0, 4.0).move(12.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.NORTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8, 15.0, 8 - j * 8), CopycatRenderContext.aabb(4.0, 1.0, 4.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.SOUTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8 + 4, 15.0, 8 - j * 8), CopycatRenderContext.aabb(4.0, 1.0, 4.0).move(12.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.SOUTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8, 15.0, 8 - j * 8 + 4), CopycatRenderContext.aabb(4.0, 1.0, 4.0).move(0.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.NORTH), autoCull);
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(i * 8 + 4, 15.0, 8 - j * 8 + 4), CopycatRenderContext.aabb(4.0, 1.0, 4.0).move(12.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.NORTH), autoCull);
        }
    }
}

