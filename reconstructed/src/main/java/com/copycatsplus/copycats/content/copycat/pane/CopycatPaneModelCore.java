/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.data.Iterate
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.IronBarsBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.pane;

import com.copycatsplus.copycats.content.copycat.pane.CopycatPaneBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.zurrtum.create.catnip.data.Iterate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatPaneModelCore
extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(new CopycatModelCore.ModelEntry("material", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatPaneModelCore.updatePropertiesIfMatch(IronBarsBlock.class), CopycatModelCore.EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof IronBarsBlock) {
            context.assembleAll();
            return;
        }
        Set present = Arrays.stream(Iterate.horizontalDirections).filter(dir -> (Boolean)state.getValue((Property)CopycatPaneBlock.propertyForDirection(dir))).collect(Collectors.toSet());
        if (present.size() == 2 && present.contains(Direction.NORTH) && present.contains(Direction.SOUTH)) {
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(7.0, 0.0, 0.0), CopycatRenderContext.aabb(1.0, 16.0, 16.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(8.0, 0.0, 0.0), CopycatRenderContext.aabb(1.0, 16.0, 16.0).move(15.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST));
            return;
        }
        if (present.size() == 2 && present.contains(Direction.EAST) && present.contains(Direction.WEST)) {
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(0.0, 0.0, 7.0), CopycatRenderContext.aabb(16.0, 16.0, 1.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(0.0, 0.0, 8.0), CopycatRenderContext.aabb(16.0, 16.0, 1.0).move(0.0, 0.0, 15.0), CopycatRenderContext.cull(MutableCullFace.NORTH));
            return;
        }
        if (present.size() == 1) {
            Direction dir2 = (Direction)present.iterator().next();
            AssemblyTransform directionTransform = t -> t.rotateY((int)dir2.toYRot());
            context.assemblePiece(directionTransform, CopycatRenderContext.vec3(7.0, 0.0, 7.0), CopycatRenderContext.aabb(1.0, 16.0, 9.0).move(0.0, 0.0, 7.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH));
            context.assemblePiece(directionTransform, CopycatRenderContext.vec3(8.0, 0.0, 7.0), CopycatRenderContext.aabb(1.0, 16.0, 9.0).move(15.0, 0.0, 7.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.NORTH));
            context.assemblePiece(directionTransform, CopycatRenderContext.vec3(7.0, 0.0, 7.0), CopycatRenderContext.aabb(1.0, 16.0, 9.0).move(7.0, 0.0, 7.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.SOUTH));
            context.assemblePiece(directionTransform, CopycatRenderContext.vec3(8.0, 0.0, 7.0), CopycatRenderContext.aabb(1.0, 16.0, 9.0).move(8.0, 0.0, 7.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.SOUTH));
            return;
        }
        for (Direction direction : Iterate.horizontalDirections) {
            AssemblyTransform directionTransform = t -> t.rotateY((int)direction.toYRot());
            context.assemblePiece(directionTransform, CopycatRenderContext.vec3(7.0, 0.0, 8.0), CopycatRenderContext.aabb(1.0, 16.0, 1.0).move(0.0, 0.0, 8.0), CopycatRenderContext.cull((present.contains(direction.getClockWise()) ? MutableCullFace.WEST : 0) | MutableCullFace.SOUTH | MutableCullFace.NORTH | MutableCullFace.EAST));
            if (present.contains(direction)) continue;
            context.assemblePiece(directionTransform, CopycatRenderContext.vec3(7.0, 0.0, 8.0), CopycatRenderContext.aabb(1.0, 16.0, 1.0).move(7.0, 0.0, 15.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.DOWN));
        }
        for (Direction direction : present) {
            AssemblyTransform directionTransform = t -> t.rotateY((int)direction.toYRot());
            context.assemblePiece(directionTransform, CopycatRenderContext.vec3(7.0, 0.0, 9.0), CopycatRenderContext.aabb(1.0, 16.0, 7.0).move(0.0, 0.0, 9.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.NORTH));
            context.assemblePiece(directionTransform, CopycatRenderContext.vec3(8.0, 0.0, 9.0), CopycatRenderContext.aabb(1.0, 16.0, 7.0).move(15.0, 0.0, 9.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.NORTH));
        }
    }
}

