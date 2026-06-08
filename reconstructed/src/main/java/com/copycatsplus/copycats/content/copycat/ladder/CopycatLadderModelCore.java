/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.LadderBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.ladder;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatLadderModelCore
extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(new CopycatModelCore.ModelEntry("material", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatLadderModelCore.updatePropertiesIfMatch(LadderBlock.class), CopycatModelCore.EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof LadderBlock) {
            context.assembleAll();
            return;
        }
        int rot = (int)((Direction)state.getValue((Property)LadderBlock.FACING)).toYRot();
        AssemblyTransform transform = t -> t.rotateY(rot);
        CopycatLadderModelCore.assemblePoles(context, transform);
        CopycatLadderModelCore.assembleSteps(context, transform);
    }

    public static void assemblePoles(CopycatRenderContext context, AssemblyTransform transform) {
        context.assemblePiece(transform, CopycatRenderContext.vec3(2.0, 0.0, 0.0), CopycatRenderContext.aabb(2.0, 16.0, 1.0), CopycatRenderContext.cull(0));
        context.assemblePiece(transform, CopycatRenderContext.vec3(12.0, 0.0, 0.0), CopycatRenderContext.aabb(2.0, 16.0, 1.0).move(14.0, 0.0, 0.0), CopycatRenderContext.cull(0));
    }

    public static void assembleSteps(CopycatRenderContext context, AssemblyTransform transform) {
        context.assemblePiece(transform, CopycatRenderContext.vec3(1.0, 1.0, 0.1), CopycatRenderContext.aabb(14.0, 2.0, 0.8), CopycatRenderContext.cull(0));
        context.assemblePiece(transform, CopycatRenderContext.vec3(1.0, 5.0, 0.1), CopycatRenderContext.aabb(14.0, 2.0, 0.8), CopycatRenderContext.cull(0));
        context.assemblePiece(transform, CopycatRenderContext.vec3(1.0, 9.0, 0.1), CopycatRenderContext.aabb(14.0, 2.0, 0.8), CopycatRenderContext.cull(0));
        context.assemblePiece(transform, CopycatRenderContext.vec3(1.0, 13.0, 0.1), CopycatRenderContext.aabb(14.0, 2.0, 0.8), CopycatRenderContext.cull(0));
    }
}

