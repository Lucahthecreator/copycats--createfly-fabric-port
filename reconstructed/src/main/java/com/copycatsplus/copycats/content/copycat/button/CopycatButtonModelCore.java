/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.ButtonBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.AttachFace
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.button;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatButtonModelCore
extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(new CopycatModelCore.ModelEntry("material", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatButtonModelCore.updatePropertiesIfMatch(ButtonBlock.class), CopycatModelCore.EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof ButtonBlock) {
            context.assembleAll();
            return;
        }
        AttachFace face = (AttachFace)state.getValue((Property)ButtonBlock.FACE);
        int rot = (int)((Direction)state.getValue((Property)ButtonBlock.FACING)).toYRot();
        boolean pressed = (Boolean)state.getValue((Property)ButtonBlock.POWERED);
        switch (face) {
            case WALL: {
                AssemblyTransform transform = t -> t.rotateY(rot);
                context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 6.0, !pressed ? 1 : 0), CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(1.0, 1.0, 1.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 8.0, !pressed ? 1 : 0), CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(1.0, 13.0, 1.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, !pressed ? 1 : 0), CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(12.0, 13.0, 1.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 6.0, !pressed ? 1 : 0), CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(12.0, 1.0, 1.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST));
                if (pressed) break;
                context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 6.0, 0.0), CopycatRenderContext.aabb(3.0, 2.0, 1.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP | MutableCullFace.EAST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 8.0, 0.0), CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(0.0, 14.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN | MutableCullFace.EAST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 0.0), CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(13.0, 14.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN | MutableCullFace.WEST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 6.0, 0.0), CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(13.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP | MutableCullFace.WEST));
                break;
            }
            case CEILING: 
            case FLOOR: {
                AssemblyTransform transform = t -> t.rotateY(rot).flipY(face != AttachFace.FLOOR);
                context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, !pressed ? 1 : 0, 6.0), CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(1.0, 0.0, 1.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, !pressed ? 1 : 0, 8.0), CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(1.0, 0.0, 13.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, !pressed ? 1 : 0, 6.0), CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(12.0, 0.0, 1.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.WEST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, !pressed ? 1 : 0, 8.0), CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(12.0, 0.0, 13.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.WEST));
                if (pressed) break;
                context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 0.0, 6.0), CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.EAST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 0.0, 8.0), CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(0.0, 0.0, 14.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, 6.0), CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(13.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, 8.0), CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(13.0, 0.0, 14.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST));
            }
        }
    }
}

