/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.content.kinetics.simpleRelays.CogWheelBlock
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVertex;
import com.zurrtum.create.content.kinetics.simpleRelays.CogWheelBlock;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatLargeCogWheelModelCore
extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(new CopycatModelCore.ModelEntry("material", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatLargeCogWheelModelCore.updatePropertiesIfMatch(CogWheelBlock.class), CopycatModelCore.EntryType.KINETIC_COPYCAT));
        entries.add(new CopycatModelCore.ModelEntry("cogwheel", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatLargeCogWheelModelCore.updatePropertiesIfMatch(CogWheelBlock.class), CopycatModelCore.EntryType.KINETIC_COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        AssemblyTransform transform;
        int rotation;
        int i;
        Direction.Axis axis = (Direction.Axis)state.getValue((Property)CopycatShaftBlock.AXIS);
        if (material.getBlock() instanceof CogWheelBlock) {
            context.assemblePiece(t -> t.rotateX(axis == Direction.Axis.Z ? 90 : 0).rotateZ(axis == Direction.Axis.X ? 90 : 0), CopycatRenderContext.vec3(-8.0, -8.0, -8.0), CopycatRenderContext.aabb(32.0, 32.0, 32.0).move(-8.0, -8.0, -8.0), CopycatRenderContext.cull(0), CopycatRenderContext.noCull(), (quad, sprite) -> {
                for (MutableVertex vertex : quad.vertices) {
                    if (!(vertex.xyz.y < 0.01) && !(vertex.xyz.y > 0.99)) continue;
                    return false;
                }
                return true;
            });
            return;
        }
        for (i = 0; i < 4; ++i) {
            rotation = i * 90;
            transform = t -> t.rotateZ(rotation).rotateY(axis == Direction.Axis.X ? 90 : 0).rotateX(axis == Direction.Axis.Y ? 90 : 0);
            context.assemblePiece(transform, CopycatRenderContext.vec3(1.0, -1.0, 5.975), CopycatRenderContext.aabb(7.0, 2.0, 2.025), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.UP), CopycatRenderContext.noCull());
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, -1.0, 5.975), CopycatRenderContext.aabb(7.0, 2.0, 2.025).move(9.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.SOUTH | MutableCullFace.UP), CopycatRenderContext.noCull());
            context.assemblePiece(transform, CopycatRenderContext.vec3(1.0, -1.0, 8.0), CopycatRenderContext.aabb(7.0, 2.0, 2.025).move(0.0, 0.0, 13.975), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH | MutableCullFace.UP), CopycatRenderContext.noCull());
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, -1.0, 8.0), CopycatRenderContext.aabb(7.0, 2.0, 2.025).move(9.0, 0.0, 13.975), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.NORTH | MutableCullFace.UP), CopycatRenderContext.noCull());
            context.assemblePiece(transform, CopycatRenderContext.vec3(1.0, 1.0, 5.975), CopycatRenderContext.aabb(7.0, 7.0, 4.05).move(3.0, 3.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.DOWN), CopycatRenderContext.noCull());
            context.assemblePiece(transform, CopycatRenderContext.vec3(-2.0, -2.0, 6.4), CopycatRenderContext.aabb(10.0, 10.0, 1.6), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.UP), CopycatRenderContext.noCull());
            context.assemblePiece(transform, CopycatRenderContext.vec3(-2.0, -2.0, 8.0), CopycatRenderContext.aabb(10.0, 10.0, 1.6).move(0.0, 0.0, 14.4), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH | MutableCullFace.UP), CopycatRenderContext.noCull());
            context.assemblePiece(transform, CopycatRenderContext.vec3(-2.0, -2.0, 6.625), CopycatRenderContext.aabb(10.0, 10.0, 1.375), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.UP), CopycatRenderContext.rotate(CopycatRenderContext.pivot(8.0, 8.0, 8.0), CopycatRenderContext.angle(0.0, 0.0, 45.0)), CopycatRenderContext.noCull());
            context.assemblePiece(transform, CopycatRenderContext.vec3(-2.0, -2.0, 8.0), CopycatRenderContext.aabb(10.0, 10.0, 1.375).move(0.0, 0.0, 14.625), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH | MutableCullFace.UP), CopycatRenderContext.rotate(CopycatRenderContext.pivot(8.0, 8.0, 8.0), CopycatRenderContext.angle(0.0, 0.0, 45.0)), CopycatRenderContext.noCull());
        }
        for (i = 0; i < 4; ++i) {
            rotation = i * 90;
            transform = t -> t.rotateZ(rotation).rotateY(axis == Direction.Axis.X ? 90 : 0).rotateX(axis == Direction.Axis.Y ? 90 : 0);
            for (int j = 0; j < 4; ++j) {
                double gearRotation = (double)j * 22.5;
                double delta = -0.025 + (double)j * 0.025;
                context.assemblePiece(transform, CopycatRenderContext.vec3(6.5, -7.0, 6.6 + delta), CopycatRenderContext.aabb(1.5, 6.0, 1.5), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.UP), CopycatRenderContext.rotate(CopycatRenderContext.pivot(8.0, 8.0, 8.0), CopycatRenderContext.angle(0.0, 0.0, gearRotation)), CopycatRenderContext.noCull());
                context.assemblePiece(transform, CopycatRenderContext.vec3(6.5, -7.0, 8.0 + delta), CopycatRenderContext.aabb(1.5, 6.0, 1.5).move(0.0, 0.0, 14.5), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH | MutableCullFace.UP), CopycatRenderContext.rotate(CopycatRenderContext.pivot(8.0, 8.0, 8.0), CopycatRenderContext.angle(0.0, 0.0, gearRotation)), CopycatRenderContext.noCull());
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, -7.0, 6.6 + delta), CopycatRenderContext.aabb(1.5, 6.0, 1.5).move(14.5, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.SOUTH | MutableCullFace.UP), CopycatRenderContext.rotate(CopycatRenderContext.pivot(8.0, 8.0, 8.0), CopycatRenderContext.angle(0.0, 0.0, gearRotation)), CopycatRenderContext.noCull());
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, -7.0, 8.0 + delta), CopycatRenderContext.aabb(1.5, 6.0, 1.5).move(14.5, 0.0, 14.5), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.NORTH | MutableCullFace.UP), CopycatRenderContext.rotate(CopycatRenderContext.pivot(8.0, 8.0, 8.0), CopycatRenderContext.angle(0.0, 0.0, gearRotation)), CopycatRenderContext.noCull());
            }
        }
    }
}

