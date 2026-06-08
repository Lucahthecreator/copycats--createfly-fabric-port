/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.content.kinetics.simpleRelays.ShaftBlock
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.shaft;

import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.zurrtum.create.content.kinetics.simpleRelays.ShaftBlock;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatShaftModelCore
extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(new CopycatModelCore.ModelEntry("material", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatShaftModelCore.updatePropertiesIfMatch(ShaftBlock.class), CopycatModelCore.EntryType.KINETIC_COPYCAT));
        entries.add(new CopycatModelCore.ModelEntry("shaft", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatShaftModelCore.updatePropertiesIfMatch(ShaftBlock.class), CopycatModelCore.EntryType.KINETIC_COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof ShaftBlock) {
            context.assembleAll();
            return;
        }
        Direction.Axis axis = (Direction.Axis)state.getValue((Property)CopycatShaftBlock.AXIS);
        AssemblyTransform transform = t -> t.rotateY(axis == Direction.Axis.X ? 90 : 0).rotateX(axis == Direction.Axis.Y ? 90 : 0);
        context.assemblePiece(transform, CopycatRenderContext.vec3(6.0, 6.0, 0.0), CopycatRenderContext.aabb(2.0, 2.0, 16.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST), CopycatRenderContext.noCull());
        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 6.0, 0.0), CopycatRenderContext.aabb(2.0, 2.0, 16.0).move(14.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST), CopycatRenderContext.noCull());
        context.assemblePiece(transform, CopycatRenderContext.vec3(6.0, 8.0, 0.0), CopycatRenderContext.aabb(2.0, 2.0, 16.0).move(0.0, 14.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST), CopycatRenderContext.noCull());
        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 0.0), CopycatRenderContext.aabb(2.0, 2.0, 16.0).move(14.0, 14.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST), CopycatRenderContext.noCull());
    }
}

