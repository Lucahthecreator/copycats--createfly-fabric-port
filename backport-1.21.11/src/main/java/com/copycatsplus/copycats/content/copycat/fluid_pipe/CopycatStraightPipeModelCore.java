/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.fluid_pipe;

import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModelCore;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatGlassFluidPipeBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatStraightPipeModelCore
extends CopycatFluidPipeModelCore {
    private static final double EPSILON = 0.02;

    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        super.registerModels(entries);
        entries.add(SUPER);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Direction.Axis axis = (Direction.Axis)state.getValue((Property)CopycatGlassFluidPipeBlock.AXIS);
        int yRot = axis == Direction.Axis.X ? 90 : 0;
        int xRot = axis == Direction.Axis.Y ? 90 : 0;
        this.renderWindowCore(context, t -> t.rotateY(yRot).rotateX(xRot));
        this.renderWindowCore(context, t -> t.rotateZ(90).rotateY(yRot).rotateX(xRot));
        this.renderWindowCore(context, t -> t.rotateZ(180).rotateY(yRot).rotateX(xRot));
        this.renderWindowCore(context, t -> t.rotateZ(270).rotateY(yRot).rotateX(xRot));
        this.assembleAccessories(context);
    }

    protected void renderWindowCore(CopycatRenderContext context, AssemblyTransform transform) {
        context.assemblePiece(transform, CopycatRenderContext.vec3(4.02, 4.02, 0.0), CopycatRenderContext.aabb(2.0, 2.0, 16.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.SOUTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 4.0, 0.0), CopycatRenderContext.aabb(1.0, 1.0, 16.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.SOUTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 5.0, 0.0), CopycatRenderContext.aabb(1.0, 1.0, 16.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.SOUTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(4.02, 6.02, 0.0), CopycatRenderContext.aabb(1.0, 3.96, 3.0).move(0.0, 6.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.NORTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(4.02, 6.02, 13.0), CopycatRenderContext.aabb(1.0, 3.96, 3.0).move(0.0, 6.0, 13.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.SOUTH));
    }
}

