package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.List;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.DOWN;

public class CopycatSlidingDoorModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        super.registerModels(entries);
        entries.add(SUPER);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int rot = (int) state.getValue(DoorBlock.FACING).toYRot();
        boolean rightHinge = state.getValue(DoorBlock.HINGE).equals(DoorHingeSide.RIGHT);
        DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
        boolean open = state.getValue(DoorBlock.OPEN);
        AssemblyTransform transform = t -> t.rotateY(rot);
        if (!open) {
            if (half == DoubleBlockHalf.LOWER) {
                //Front
                context.assemblePiece(transform,
                        vec3(0, 0, 0),
                        aabb(16, 13, 1.5),
                        cull(SOUTH | UP));
                context.assemblePiece(transform,
                        vec3(0, 13, 0),
                        aabb(16, 3, 1.5).move(0, 3, 0),
                        cull(SOUTH | UP | DOWN));
                //Back
                context.assemblePiece(transform,
                        vec3(0, 0, 1.5),
                        aabb(16, 13, 1.5).move(0, 0, 14.5),
                        cull(NORTH | UP));
                context.assemblePiece(transform,
                        vec3(0, 13, 1.5),
                        aabb(16, 3, 1.5).move(0, 3, 14.5),
                        cull(NORTH | UP | DOWN));

            } else {
                //Front
                context.assemblePiece(transform,
                        vec3(0, 3, 0),
                        aabb(16, 13, 1.5).move(0, 3, 0),
                        cull(SOUTH | DOWN));
                context.assemblePiece(transform,
                        vec3(0, 0, 0),
                        aabb(16, 3, 1.5).move(0, 10, 0),
                        cull(SOUTH | UP | DOWN));
                //Back
                context.assemblePiece(transform,
                        vec3(0, 3, 1.5),
                        aabb(16, 13, 1.5).move(0, 3, 14.5),
                        cull(NORTH | DOWN));
                context.assemblePiece(transform,
                        vec3(0, 0, 1.5),
                        aabb(16, 3, 1.5).move(0, 10, 14.5),
                        cull(NORTH | UP | DOWN));
            }
        }
    }
}
