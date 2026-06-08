/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.data.Iterate
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.FenceGateBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.fence_gate;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.zurrtum.create.catnip.data.Iterate;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatFenceGateModelCore
extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(new CopycatModelCore.ModelEntry("material", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatFenceGateModelCore.updatePropertiesIfMatch(FenceGateBlock.class), CopycatModelCore.EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof FenceGateBlock) {
            context.assembleAll();
            return;
        }
        int offsetWall = (Boolean)state.getValue((Property)FenceGateBlock.IN_WALL) != false ? -3 : 0;
        int rot = (int)((Direction)state.getValue((Property)FenceGateBlock.FACING)).toYRot();
        AssemblyTransform transform = t -> t.rotateY(rot);
        for (boolean eastSide : Iterate.falseAndTrue) {
            int offsetX = eastSide ? 14 : 0;
            context.assemblePiece(transform, CopycatRenderContext.vec3(offsetX, 5 + offsetWall, 7.0), CopycatRenderContext.aabb(1.0, 6.0, 1.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(offsetX + 1, 5 + offsetWall, 7.0), CopycatRenderContext.aabb(1.0, 6.0, 1.0).move(15.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(offsetX, 5 + offsetWall, 8.0), CopycatRenderContext.aabb(1.0, 6.0, 1.0).move(0.0, 0.0, 15.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(offsetX + 1, 5 + offsetWall, 8.0), CopycatRenderContext.aabb(1.0, 6.0, 1.0).move(15.0, 0.0, 15.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(offsetX, 11 + offsetWall, 7.0), CopycatRenderContext.aabb(1.0, 5.0, 1.0).move(0.0, 11.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(offsetX + 1, 11 + offsetWall, 7.0), CopycatRenderContext.aabb(1.0, 5.0, 1.0).move(15.0, 11.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(offsetX, 11 + offsetWall, 8.0), CopycatRenderContext.aabb(1.0, 5.0, 1.0).move(0.0, 11.0, 15.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST));
            context.assemblePiece(transform, CopycatRenderContext.vec3(offsetX + 1, 11 + offsetWall, 8.0), CopycatRenderContext.aabb(1.0, 5.0, 1.0).move(15.0, 11.0, 15.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST));
        }
        if (((Boolean)state.getValue((Property)FenceGateBlock.OPEN)).booleanValue()) {
            for (boolean eastDoor : Iterate.falseAndTrue) {
                for (boolean eastSide : Iterate.falseAndTrue) {
                    int offsetX = (eastDoor ? 14 : 0) + (eastSide ? 1 : 0);
                    context.assemblePiece(transform, CopycatRenderContext.vec3(offsetX, 12 + offsetWall, 9.0), CopycatRenderContext.aabb(1.0, 3.0, 6.0).move(eastSide ? 15.0 : 0.0, 13.0, 10.0), CopycatRenderContext.cull(MutableCullFace.NORTH | (eastSide ? MutableCullFace.WEST : MutableCullFace.EAST)));
                    context.assemblePiece(transform, CopycatRenderContext.vec3(offsetX, 9 + offsetWall, 13.0), CopycatRenderContext.aabb(1.0, 3.0, 2.0).move(eastSide ? 15.0 : 0.0, 7.0, 14.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.DOWN | (eastSide ? MutableCullFace.WEST : MutableCullFace.EAST)));
                    context.assemblePiece(transform, CopycatRenderContext.vec3(offsetX, 6 + offsetWall, 9.0), CopycatRenderContext.aabb(1.0, 3.0, 6.0).move(eastSide ? 15.0 : 0.0, 0.0, 10.0), CopycatRenderContext.cull(MutableCullFace.NORTH | (eastSide ? MutableCullFace.WEST : MutableCullFace.EAST)));
                }
            }
        } else {
            for (boolean southSide : Iterate.falseAndTrue) {
                int rot2 = rot + (southSide ? 180 : 0);
                AssemblyTransform transform2 = t -> t.rotateY(rot2);
                context.assemblePiece(transform2, CopycatRenderContext.vec3(8.0, 12 + offsetWall, 7.0), CopycatRenderContext.aabb(6.0, 3.0, 1.0).move(0.0, 13.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST | MutableCullFace.WEST));
                context.assemblePiece(transform2, CopycatRenderContext.vec3(8.0, 9 + offsetWall, 7.0), CopycatRenderContext.aabb(2.0, 3.0, 1.0).move(0.0, 7.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST));
                context.assemblePiece(transform2, CopycatRenderContext.vec3(8.0, 6 + offsetWall, 7.0), CopycatRenderContext.aabb(6.0, 3.0, 1.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST | MutableCullFace.WEST));
                context.assemblePiece(transform2, CopycatRenderContext.vec3(2.0, 12 + offsetWall, 7.0), CopycatRenderContext.aabb(6.0, 3.0, 1.0).move(10.0, 13.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST | MutableCullFace.WEST));
                context.assemblePiece(transform2, CopycatRenderContext.vec3(6.0, 9 + offsetWall, 7.0), CopycatRenderContext.aabb(2.0, 3.0, 1.0).move(14.0, 7.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST));
                context.assemblePiece(transform2, CopycatRenderContext.vec3(2.0, 6 + offsetWall, 7.0), CopycatRenderContext.aabb(6.0, 3.0, 1.0).move(10.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST | MutableCullFace.WEST));
            }
        }
    }
}

