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

public class CopycatCogWheelModelCore
extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(new CopycatModelCore.ModelEntry("material", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatCogWheelModelCore.updatePropertiesIfMatch(CogWheelBlock.class), CopycatModelCore.EntryType.KINETIC_COPYCAT));
        entries.add(new CopycatModelCore.ModelEntry("cogwheel", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatCogWheelModelCore.updatePropertiesIfMatch(CogWheelBlock.class), CopycatModelCore.EntryType.KINETIC_COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
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
        for (int i = 0; i < 4; ++i) {
            int rotation = i * 90;
            AssemblyTransform transform = t -> t.rotateZ(rotation).rotateY(axis == Direction.Axis.X ? 90 : 0).rotateX(axis == Direction.Axis.Y ? 90 : 0);
            context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 6.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.UP), CopycatRenderContext.noCull());
            context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 8.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(0.0, 0.0, 14.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH | MutableCullFace.UP), CopycatRenderContext.noCull());
            context.assemblePiece(transform, CopycatRenderContext.vec3(2.0, 2.0, 6.55), CopycatRenderContext.aabb(6.0, 6.0, 1.45), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH | MutableCullFace.UP), CopycatRenderContext.noCull());
            context.assemblePiece(transform, CopycatRenderContext.vec3(2.0, 2.0, 8.0), CopycatRenderContext.aabb(6.0, 6.0, 1.45).move(0.0, 0.0, 14.55), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH | MutableCullFace.UP), CopycatRenderContext.noCull());
            for (int j = 0; j < 2; ++j) {
                int gearRotation = j * 45;
                context.assemblePiece(transform, CopycatRenderContext.vec3(6.5, 0.0, 6.5), CopycatRenderContext.aabb(1.5, 16.0, 1.5), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.SOUTH), CopycatRenderContext.scale(CopycatRenderContext.pivot(8.0, 8.0, 8.0), CopycatRenderContext.scale(1.0, 1.125, 1.0 + (double)j * 0.02)), CopycatRenderContext.rotate(CopycatRenderContext.pivot(8.0, 8.0, 8.0), CopycatRenderContext.angle(0.0, 0.0, gearRotation)), CopycatRenderContext.noCull());
                context.assemblePiece(transform, CopycatRenderContext.vec3(6.5, 0.0, 8.0), CopycatRenderContext.aabb(1.5, 16.0, 1.5).move(0.0, 0.0, 14.5), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH), CopycatRenderContext.scale(CopycatRenderContext.pivot(8.0, 8.0, 8.0), CopycatRenderContext.scale(1.0, 1.125, 1.0 + (double)j * 0.02)), CopycatRenderContext.rotate(CopycatRenderContext.pivot(8.0, 8.0, 8.0), CopycatRenderContext.angle(0.0, 0.0, gearRotation)), CopycatRenderContext.noCull());
            }
        }
    }
}

