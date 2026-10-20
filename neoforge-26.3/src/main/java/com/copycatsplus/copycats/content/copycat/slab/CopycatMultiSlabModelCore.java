package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.compat.render.CopycatRenderFlags;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public class CopycatMultiSlabModelCore extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        this.registerForMultiState(entries, CCBlocks.COPYCAT_SLAB.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        CopycatsDebug.log(
            "slab",
            () -> "emit key="
                + key
                + " state="
                + state
                + " material="
                + material
                + " slabType="
                + state.getValue(CopycatSlabBlock.SLAB_TYPE)
                + " axis="
                + state.getValue(CopycatSlabBlock.AXIS)
                + " hideDoubleSlabSeam="
                + CopycatRenderFlags.hideDoubleSlabSeam()
        );
        if (CopycatRenderFlags.hideDoubleSlabSeam() && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.DOUBLE) {
            if (!Objects.equals(key, SlabType.BOTTOM.getSerializedName())) {
                CopycatsDebug.log("slab", () -> "skip duplicate full-block double slab key=" + key + " state=" + state);
            } else {
                CopycatsDebug.log("slab", () -> "render same-material double slab as full block key=" + key + " state=" + state + " material=" + material);
                context.assembleAll();
            }
        } else if (Objects.equals(key, SlabType.TOP.getSerializedName()) && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.BOTTOM) {
            CopycatsDebug.log("slab", () -> "skip top key for bottom slab state=" + state);
        } else if (Objects.equals(key, SlabType.BOTTOM.getSerializedName()) && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.TOP) {
            CopycatsDebug.log("slab", () -> "skip bottom key for top slab state=" + state);
        } else {
            Axis axis = state.getValue(CopycatSlabBlock.AXIS);
            Direction facing = Direction.fromAxisAndDirection(
                axis, Objects.equals(key, SlabType.BOTTOM.getSerializedName()) ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE
            );
            if (facing.getAxis().isHorizontal()) {
                QuadAutoCull autoCull = CopycatRenderContext.autoCull(CopycatRenderContext.aabb(16.0, 16.0, 8.0));
                AssemblyTransform transform = t -> t.rotateY((int)facing.toYRot());
                context.assemblePiece(
                    transform,
                    CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                    CopycatRenderContext.aabb(16.0, 16.0, 4.0),
                    CopycatRenderContext.cull(MutableCullFace.SOUTH),
                    autoCull
                );
                int endCull = MutableCullFace.NORTH;
                if (CopycatRenderFlags.hideDoubleSlabSeam()) {
                    endCull |= MutableCullFace.SOUTH;
                }

                int loggedEndCull = endCull;
                CopycatsDebug.log(
                    "slab", () -> "horizontal pieces key=" + key + " facing=" + facing + " firstCull=" + MutableCullFace.SOUTH + " secondCull=" + loggedEndCull
                );
                context.assemblePiece(
                    transform,
                    CopycatRenderContext.vec3(0.0, 0.0, 4.0),
                    CopycatRenderContext.aabb(16.0, 16.0, 4.0).move(0.0, 0.0, 12.0),
                    CopycatRenderContext.cull(endCull),
                    autoCull
                );
            } else {
                QuadAutoCull autoCull = CopycatRenderContext.autoCull(CopycatRenderContext.aabb(16.0, 8.0, 16.0));
                AssemblyTransform transform = t -> t.flipY(facing.getAxisDirection() == AxisDirection.NEGATIVE);
                context.assemblePiece(
                    transform,
                    CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                    CopycatRenderContext.aabb(16.0, 4.0, 16.0),
                    CopycatRenderContext.cull(MutableCullFace.UP),
                    autoCull
                );
                int endCull = MutableCullFace.DOWN;
                if (CopycatRenderFlags.hideDoubleSlabSeam()) {
                    endCull |= MutableCullFace.UP;
                }

                int loggedEndCull = endCull;
                CopycatsDebug.log(
                    "slab", () -> "vertical pieces key=" + key + " facing=" + facing + " firstCull=" + MutableCullFace.UP + " secondCull=" + loggedEndCull
                );
                context.assemblePiece(
                    transform,
                    CopycatRenderContext.vec3(0.0, 4.0, 0.0),
                    CopycatRenderContext.aabb(16.0, 4.0, 16.0).move(0.0, 12.0, 0.0),
                    CopycatRenderContext.cull(endCull),
                    autoCull
                );
            }
        }
    }
}
