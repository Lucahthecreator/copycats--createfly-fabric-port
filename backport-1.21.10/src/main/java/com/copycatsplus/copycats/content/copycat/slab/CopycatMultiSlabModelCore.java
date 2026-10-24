/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.SlabType
 */
package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.compat.render.CopycatRenderFlags;
import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public class CopycatMultiSlabModelCore
extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        this.registerForMultiState(entries, (IMultiStateCopycatBlock)CCBlocks.COPYCAT_SLAB.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        CopycatsDebug.log("slab", () -> "emit key=" + key
                + " state=" + state + " material=" + material
                + " slabType=" + state.getValue(CopycatSlabBlock.SLAB_TYPE)
                + " axis=" + state.getValue(CopycatSlabBlock.AXIS)
                + " hideDoubleSlabSeam=" + CopycatRenderFlags.hideDoubleSlabSeam());
        if (CopycatRenderFlags.hideDoubleSlabSeam()
                && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.DOUBLE) {
            if (!Objects.equals(key, SlabType.BOTTOM.getSerializedName())) {
                CopycatsDebug.log("slab", () -> "skip duplicate full-block double slab key=" + key
                        + " state=" + state);
                return;
            }
            CopycatsDebug.log("slab", () -> "render same-material double slab as full block key=" + key
                    + " state=" + state + " material=" + material);
            context.assembleAll();
            return;
        }
        if (Objects.equals(key, SlabType.TOP.getSerializedName()) && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.BOTTOM) {
            CopycatsDebug.log("slab", () -> "skip top key for bottom slab state=" + state);
            return;
        }
        if (Objects.equals(key, SlabType.BOTTOM.getSerializedName()) && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.TOP) {
            CopycatsDebug.log("slab", () -> "skip bottom key for top slab state=" + state);
            return;
        }
        Direction.Axis axis = (Direction.Axis)state.getValue(CopycatSlabBlock.AXIS);
        Direction facing = Direction.fromAxisAndDirection((Direction.Axis)axis, (Direction.AxisDirection)(Objects.equals(key, SlabType.BOTTOM.getSerializedName()) ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE));
        if (facing.getAxis().isHorizontal()) {
            QuadAutoCull autoCull = CopycatRenderContext.autoCull(CopycatRenderContext.aabb(16.0, 16.0, 8.0));
            AssemblyTransform transform = t -> t.rotateY((int)facing.toYRot());
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 16.0, 4.0), CopycatRenderContext.cull(MutableCullFace.SOUTH), autoCull);
            int endCull = MutableCullFace.NORTH;
            if (CopycatRenderFlags.hideDoubleSlabSeam()) {
                endCull |= MutableCullFace.SOUTH;
            }
            int loggedEndCull = endCull;
            CopycatsDebug.log("slab", () -> "horizontal pieces key=" + key
                    + " facing=" + facing + " firstCull=" + MutableCullFace.SOUTH
                    + " secondCull=" + loggedEndCull);
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 4.0), CopycatRenderContext.aabb(16.0, 16.0, 4.0).move(0.0, 0.0, 12.0), CopycatRenderContext.cull(endCull), autoCull);
        } else {
            QuadAutoCull autoCull = CopycatRenderContext.autoCull(CopycatRenderContext.aabb(16.0, 8.0, 16.0));
            AssemblyTransform transform = t -> t.flipY(facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE);
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 4.0, 16.0), CopycatRenderContext.cull(MutableCullFace.UP), autoCull);
            int endCull = MutableCullFace.DOWN;
            if (CopycatRenderFlags.hideDoubleSlabSeam()) {
                endCull |= MutableCullFace.UP;
            }
            int loggedEndCull = endCull;
            CopycatsDebug.log("slab", () -> "vertical pieces key=" + key
                    + " facing=" + facing + " firstCull=" + MutableCullFace.UP
                    + " secondCull=" + loggedEndCull);
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 4.0, 0.0), CopycatRenderContext.aabb(16.0, 4.0, 16.0).move(0.0, 12.0, 0.0), CopycatRenderContext.cull(endCull), autoCull);
        }
    }
}

