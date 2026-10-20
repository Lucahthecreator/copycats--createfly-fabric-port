package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import java.util.List;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class CopycatSlidingDoorModelCore extends CopycatModelCore {
    private final boolean kinetic;

    public CopycatSlidingDoorModelCore(boolean kinetic) {
        this.kinetic = kinetic;
    }

    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(
            new CopycatModelCore.ModelEntry(
                "material",
                CopycatModelCore.ModelGetter.MATERIAL,
                this,
                updatePropertiesIfMatch(DoorBlock.class),
                this.kinetic ? CopycatModelCore.EntryType.KINETIC_COPYCAT : CopycatModelCore.EntryType.COPYCAT
            )
        );
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof DoorBlock) {
            context.assembleAll();
        } else {
            if (state.getValue(CopycatSlidingDoorBlock.CT)) {
                this.assembleWithCT(state, context);
            } else {
                this.assembleWithoutCT(state, context);
            }
        }
    }

    private void assembleWithCT(BlockState state, CopycatRenderContext context) {
        int rot = (int)state.getValue(DoorBlock.FACING).toYRot();
        DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
        AssemblyTransform transform = t -> t.rotateY(rot);
        if (half == DoubleBlockHalf.LOWER) {
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                CopycatRenderContext.aabb(16.0, 12.0, 2.0),
                CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP)
            );
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 12.0, 0.0),
                CopycatRenderContext.aabb(16.0, 4.0, 2.0).move(0.0, 4.0, 0.0),
                CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN | (this.kinetic ? MutableCullFace.UP : 0))
            );
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 0.0, 2.0),
                CopycatRenderContext.aabb(16.0, 12.0, 1.0).move(0.0, 0.0, 15.0),
                CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.UP)
            );
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 12.0, 2.0),
                CopycatRenderContext.aabb(16.0, 4.0, 1.0).move(0.0, 4.0, 15.0),
                CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.DOWN | (this.kinetic ? MutableCullFace.UP : 0))
            );
        } else {
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 4.0, 0.0),
                CopycatRenderContext.aabb(16.0, 12.0, 2.0).move(0.0, 4.0, 0.0),
                CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN)
            );
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                CopycatRenderContext.aabb(16.0, 4.0, 2.0).move(0.0, 8.0, 0.0),
                CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP | (this.kinetic ? MutableCullFace.DOWN : 0))
            );
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 4.0, 2.0),
                CopycatRenderContext.aabb(16.0, 12.0, 1.0).move(0.0, 4.0, 15.0),
                CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.DOWN)
            );
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 0.0, 2.0),
                CopycatRenderContext.aabb(16.0, 4.0, 1.0).move(0.0, 8.0, 15.0),
                CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.UP | (this.kinetic ? MutableCullFace.DOWN : 0))
            );
        }
    }

    private void assembleWithoutCT(BlockState state, CopycatRenderContext context) {
        int rot = (int)state.getValue(DoorBlock.FACING).toYRot();
        DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
        AssemblyTransform transform = t -> t.rotateY(rot);
        if (half == DoubleBlockHalf.LOWER) {
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                CopycatRenderContext.aabb(16.0, 16.0, 2.0),
                CopycatRenderContext.cull(MutableCullFace.SOUTH | (this.kinetic ? MutableCullFace.UP : 0))
            );
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 0.0, 2.0),
                CopycatRenderContext.aabb(16.0, 16.0, 1.0).move(0.0, 0.0, 15.0),
                CopycatRenderContext.cull(MutableCullFace.NORTH | (this.kinetic ? MutableCullFace.UP : 0))
            );
        } else {
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 0.0, 0.0),
                CopycatRenderContext.aabb(16.0, 16.0, 2.0).move(0.0, 0.0, 0.0),
                CopycatRenderContext.cull(MutableCullFace.SOUTH | (this.kinetic ? MutableCullFace.DOWN : 0))
            );
            context.assemblePiece(
                transform,
                CopycatRenderContext.vec3(0.0, 0.0, 2.0),
                CopycatRenderContext.aabb(16.0, 16.0, 1.0).move(0.0, 0.0, 15.0),
                CopycatRenderContext.cull(MutableCullFace.NORTH | (this.kinetic ? MutableCullFace.DOWN : 0))
            );
        }
    }
}
