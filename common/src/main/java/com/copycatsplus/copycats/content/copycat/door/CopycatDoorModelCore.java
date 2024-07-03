package com.copycatsplus.copycats.content.copycat.door;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatDoorModelCore extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int rot = (int) state.getValue(DoorBlock.FACING).toYRot();
        boolean rightHinge = state.getValue(DoorBlock.HINGE).equals(DoorHingeSide.RIGHT);
        DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
        boolean open = state.getValue(DoorBlock.OPEN);
        AssemblyTransform transform = t -> t.rotateY(rot);
        if (half == DoubleBlockHalf.LOWER) {
            if (!open) {
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
                if (!rightHinge) {
                    transform = t -> t.flipX(true).rotateY(rot);
                }
                //Front
                context.assemblePiece(transform,
                        vec3(0, 0, 0),
                        aabb(1.5, 13, 16),
                        cull(EAST | UP));
                context.assemblePiece(transform,
                        vec3(0, 13, 0),
                        aabb(1.5, 3, 16).move(0, 3, 0),
                        cull(EAST | UP | DOWN));
                //Back
                context.assemblePiece(transform,
                        vec3(1.5, 0, 0),
                        aabb(1.5, 13, 16).move(14.5, 0, 0),
                        cull(WEST | UP));
                context.assemblePiece(transform,
                        vec3(1.5, 13, 0),
                        aabb(1.5, 3, 16).move(14.5, 3, 0),
                        cull(WEST | UP | DOWN));
            }
        } else {
            if (!open) {
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
            } else {
                if (!rightHinge) {
                    transform = t -> t.flipX(true).rotateY(rot);
                }
                //Front
                context.assemblePiece(transform,
                        vec3(0, 3, 0),
                        aabb(1.5, 13, 16).move(0, 3, 0),
                        cull(EAST | DOWN));
                context.assemblePiece(transform,
                        vec3(0, 0, 0),
                        aabb(1.5, 3, 16).move(0, 10, 0),
                        cull(EAST | UP | DOWN));
                //Back
                context.assemblePiece(transform,
                        vec3(1.5, 3, 0),
                        aabb(1.5, 13, 16).move(14.5, 3, 0),
                        cull(WEST | DOWN));
                context.assemblePiece(transform,
                        vec3(1.5, 0, 0),
                        aabb(1.5, 3, 16).move(14.5, 10, 0),
                        cull(WEST | UP | DOWN));
            }
        }
    }
}
