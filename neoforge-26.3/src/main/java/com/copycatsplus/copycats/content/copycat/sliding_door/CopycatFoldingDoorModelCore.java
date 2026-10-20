package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.zurrtum.create.catnip.data.Iterate;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class CopycatFoldingDoorModelCore extends CopycatModelCore {
    private final boolean left;
    private final boolean kinetic;

    public CopycatFoldingDoorModelCore(boolean left, boolean kinetic) {
        this.left = left;
        this.kinetic = kinetic;
    }

    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(
            new CopycatModelCore.ModelEntry(
                "material",
                CopycatModelCore.ModelGetter.MATERIAL,
                this,
                this.kinetic ? CopycatModelCore.EntryType.KINETIC_COPYCAT : CopycatModelCore.EntryType.COPYCAT
            )
        );
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (state.getValue(CopycatSlidingDoorBlock.CT)) {
            this.assembleWithCT(state, context);
        } else {
            this.assembleWithoutCT(state, context);
        }
    }

    private void assembleWithCT(BlockState state, CopycatRenderContext context) {
        for (boolean left : Iterate.falseAndTrue) {
            if (!this.kinetic || left == this.left) {
                Direction facing = state.getValue(DoorBlock.FACING);
                int rot = this.kinetic ? 270 : (int)facing.toYRot();
                int offset = !left && !this.kinetic ? 0 : 8;
                DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
                AssemblyTransform transform = t -> t.rotateY(rot);
                if (half == DoubleBlockHalf.LOWER) {
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset, 0.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 12.0, 2.0).move(0.0, 0.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset + 4, 0.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 12.0, 2.0).move(12.0, 0.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset, 12.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(0.0, 4.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN | (this.kinetic ? MutableCullFace.UP : 0) | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset + 4, 12.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(12.0, 4.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN | (this.kinetic ? MutableCullFace.UP : 0) | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset, 0.0, 2.0),
                        CopycatRenderContext.aabb(4.0, 12.0, 1.0).move(0.0, 0.0, 15.0),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.UP | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset + 4, 0.0, 2.0),
                        CopycatRenderContext.aabb(4.0, 12.0, 1.0).move(12.0, 0.0, 15.0),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.UP | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset, 12.0, 2.0),
                        CopycatRenderContext.aabb(4.0, 4.0, 1.0).move(0.0, 4.0, 15.0),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.DOWN | (this.kinetic ? MutableCullFace.UP : 0) | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset + 4, 12.0, 2.0),
                        CopycatRenderContext.aabb(4.0, 4.0, 1.0).move(12.0, 4.0, 15.0),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.DOWN | (this.kinetic ? MutableCullFace.UP : 0) | MutableCullFace.WEST)
                    );
                } else {
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset, 4.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 12.0, 2.0).move(0.0, 4.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset + 4, 4.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 12.0, 2.0).move(12.0, 4.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset, 0.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(0.0, 8.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP | (this.kinetic ? MutableCullFace.DOWN : 0) | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset + 4, 0.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(12.0, 8.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP | (this.kinetic ? MutableCullFace.DOWN : 0) | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset, 4.0, 2.0),
                        CopycatRenderContext.aabb(4.0, 12.0, 1.0).move(0.0, 4.0, 15.0),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.DOWN | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset + 4, 4.0, 2.0),
                        CopycatRenderContext.aabb(4.0, 12.0, 1.0).move(12.0, 4.0, 15.0),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.DOWN | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset, 0.0, 2.0),
                        CopycatRenderContext.aabb(4.0, 4.0, 1.0).move(0.0, 8.0, 15.0),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.UP | (this.kinetic ? MutableCullFace.DOWN : 0) | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset + 4, 0.0, 2.0),
                        CopycatRenderContext.aabb(4.0, 4.0, 1.0).move(12.0, 8.0, 15.0),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.UP | (this.kinetic ? MutableCullFace.DOWN : 0) | MutableCullFace.WEST)
                    );
                }
            }
        }
    }

    private void assembleWithoutCT(BlockState state, CopycatRenderContext context) {
        for (boolean left : Iterate.falseAndTrue) {
            if (!this.kinetic || left == this.left) {
                Direction facing = state.getValue(DoorBlock.FACING);
                int rot = this.kinetic ? 270 : (int)facing.toYRot();
                int offset = !left && !this.kinetic ? 0 : 8;
                DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
                AssemblyTransform transform = t -> t.rotateY(rot);
                if (half == DoubleBlockHalf.LOWER) {
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset, 0.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 16.0, 2.0).move(0.0, 0.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | (this.kinetic ? MutableCullFace.UP : 0) | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset + 4, 0.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 16.0, 2.0).move(12.0, 0.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | (this.kinetic ? MutableCullFace.UP : 0) | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset, 0.0, 2.0),
                        CopycatRenderContext.aabb(4.0, 16.0, 1.0).move(0.0, 0.0, 15.0),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | (this.kinetic ? MutableCullFace.UP : 0) | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset + 4, 0.0, 2.0),
                        CopycatRenderContext.aabb(4.0, 16.0, 1.0).move(12.0, 0.0, 15.0),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | (this.kinetic ? MutableCullFace.UP : 0) | MutableCullFace.WEST)
                    );
                } else {
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset, 0.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 16.0, 2.0).move(0.0, 0.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | (this.kinetic ? MutableCullFace.DOWN : 0) | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset + 4, 0.0, 0.0),
                        CopycatRenderContext.aabb(4.0, 16.0, 2.0).move(12.0, 0.0, 0.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | (this.kinetic ? MutableCullFace.DOWN : 0) | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset, 0.0, 2.0),
                        CopycatRenderContext.aabb(4.0, 16.0, 1.0).move(0.0, 0.0, 15.0),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | (this.kinetic ? MutableCullFace.DOWN : 0) | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(offset + 4, 0.0, 2.0),
                        CopycatRenderContext.aabb(4.0, 16.0, 1.0).move(12.0, 0.0, 15.0),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | (this.kinetic ? MutableCullFace.DOWN : 0) | MutableCullFace.WEST)
                    );
                }
            }
        }
    }
}
