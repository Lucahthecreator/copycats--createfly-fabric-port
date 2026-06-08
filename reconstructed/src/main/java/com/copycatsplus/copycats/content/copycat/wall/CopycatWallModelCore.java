/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.data.Iterate
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.WallBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.block.state.properties.WallSide
 */
package com.copycatsplus.copycats.content.copycat.wall;

import com.copycatsplus.copycats.content.copycat.wall.CopycatWallBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.zurrtum.create.catnip.data.Iterate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.WallSide;

public class CopycatWallModelCore
extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(new CopycatModelCore.ModelEntry("material", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatWallModelCore.updatePropertiesIfMatch(WallBlock.class), CopycatModelCore.EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof WallBlock) {
            context.assembleAll();
            return;
        }
        boolean pole = (Boolean)state.getValue((Property)WallBlock.UP);
        if (pole) {
            for (Direction direction : Iterate.horizontalDirections) {
                context.assemblePiece(t -> t.rotateY((int)direction.toYRot()), CopycatRenderContext.vec3(4.0, 0.0, 4.0), CopycatRenderContext.aabb(4.0, 16.0, 4.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST));
            }
            block11: for (Direction direction : Iterate.horizontalDirections) {
                int rot = (int)direction.toYRot();
                AssemblyTransform transform = t -> t.rotateY(rot);
                switch ((WallSide)state.getValue(CopycatWallBlock.byDirection(direction))) {
                    case NONE: {
                        continue block11;
                    }
                    case LOW: {
                        context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 0.0, 12.0), CopycatRenderContext.aabb(3.0, 7.0, 4.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST));
                        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, 12.0), CopycatRenderContext.aabb(3.0, 7.0, 4.0).move(13.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST));
                        context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 7.0, 12.0), CopycatRenderContext.aabb(3.0, 7.0, 4.0).move(0.0, 9.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST));
                        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 7.0, 12.0), CopycatRenderContext.aabb(3.0, 7.0, 4.0).move(13.0, 9.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST));
                        continue block11;
                    }
                    case TALL: {
                        context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 0.0, 12.0), CopycatRenderContext.aabb(3.0, 16.0, 4.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST));
                        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, 12.0), CopycatRenderContext.aabb(3.0, 16.0, 4.0).move(13.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.WEST));
                    }
                }
            }
        } else {
            int rot;
            boolean tall = false;
            HashMap<Direction, WallSide> sides = new HashMap<Direction, WallSide>();
            for (Direction direction : Iterate.horizontalDirections) {
                WallSide wall = (WallSide)state.getValue(CopycatWallBlock.byDirection(direction));
                sides.put(direction, wall);
                if (wall != WallSide.TALL) continue;
                tall = true;
            }
            if (!(sides.get(Direction.SOUTH) != sides.get(Direction.NORTH) || sides.get(Direction.EAST) != sides.get(Direction.WEST) || sides.get(Direction.NORTH) != WallSide.NONE && sides.get(Direction.EAST) != WallSide.NONE || sides.get(Direction.NORTH) == WallSide.NONE && sides.get(Direction.EAST) == WallSide.NONE)) {
                int rot2 = sides.get(Direction.SOUTH) == WallSide.NONE ? 90 : 0;
                AssemblyTransform transform = t -> t.rotateY(rot2);
                if (!tall) {
                    context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 0.0, 0.0), CopycatRenderContext.aabb(3.0, 7.0, 16.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST));
                    context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, 0.0), CopycatRenderContext.aabb(3.0, 7.0, 16.0).move(13.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST));
                    context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 7.0, 0.0), CopycatRenderContext.aabb(3.0, 7.0, 16.0).move(0.0, 9.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST));
                    context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 7.0, 0.0), CopycatRenderContext.aabb(3.0, 7.0, 16.0).move(13.0, 9.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST));
                } else {
                    context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 0.0, 0.0), CopycatRenderContext.aabb(3.0, 16.0, 16.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST));
                    context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, 0.0), CopycatRenderContext.aabb(3.0, 16.0, 16.0).move(13.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST));
                }
                return;
            }
            Direction extendSide = null;
            long sideCount = sides.values().stream().filter(s -> s != WallSide.NONE).count();
            if (sideCount == 1L) {
                extendSide = sides.entrySet().stream().filter(s -> s.getValue() != WallSide.NONE).findFirst().map(Map.Entry::getKey).orElse(null);
            } else {
                for (Direction direction : Iterate.horizontalDirections) {
                    boolean cullAdjacent;
                    boolean cullCurrent;
                    rot = (int)direction.toYRot();
                    AssemblyTransform transform = t -> t.rotateY(rot);
                    if (tall) {
                        cullCurrent = sides.get(direction.getOpposite()) == WallSide.TALL;
                        cullAdjacent = sides.get(direction.getClockWise()) == WallSide.TALL;
                        context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 0.0, 5.0), CopycatRenderContext.aabb(3.0, 16.0, 3.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST | (cullCurrent ? MutableCullFace.NORTH : 0) | (cullAdjacent ? MutableCullFace.WEST : 0)));
                        continue;
                    }
                    cullCurrent = sides.get(direction.getOpposite()) != WallSide.NONE;
                    cullAdjacent = sides.get(direction.getClockWise()) != WallSide.NONE;
                    context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 0.0, 5.0), CopycatRenderContext.aabb(3.0, 7.0, 3.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.EAST | (cullCurrent ? MutableCullFace.NORTH : 0) | (cullAdjacent ? MutableCullFace.WEST : 0)));
                    context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 7.0, 5.0), CopycatRenderContext.aabb(3.0, 7.0, 3.0).move(0.0, 9.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST | (cullCurrent ? MutableCullFace.NORTH : 0) | (cullAdjacent ? MutableCullFace.WEST : 0)));
                }
            }
            block14: for (Direction direction : Iterate.horizontalDirections) {
                rot = (int)direction.toYRot();
                boolean extend = extendSide == direction;
                boolean cullEnd = !extend;
                AssemblyTransform transform = t -> t.rotateY(rot);
                switch ((WallSide)sides.get(direction)) {
                    case NONE: {
                        continue block14;
                    }
                    case LOW: {
                        context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 0.0, extend ? 5.0 : 11.0), CopycatRenderContext.aabb(3.0, 7.0, extend ? 11.0 : 5.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | (cullEnd ? MutableCullFace.NORTH : 0) | MutableCullFace.EAST));
                        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, extend ? 5.0 : 11.0), CopycatRenderContext.aabb(3.0, 7.0, extend ? 11.0 : 5.0).move(13.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | (cullEnd ? MutableCullFace.NORTH : 0) | MutableCullFace.WEST));
                        context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 7.0, extend ? 5.0 : 11.0), CopycatRenderContext.aabb(3.0, 7.0, extend ? 11.0 : 5.0).move(0.0, 9.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | (cullEnd ? MutableCullFace.NORTH : 0) | MutableCullFace.EAST));
                        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 7.0, extend ? 5.0 : 11.0), CopycatRenderContext.aabb(3.0, 7.0, extend ? 11.0 : 5.0).move(13.0, 9.0, 0.0), CopycatRenderContext.cull(MutableCullFace.DOWN | (cullEnd ? MutableCullFace.NORTH : 0) | MutableCullFace.WEST));
                        continue block14;
                    }
                    case TALL: {
                        context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 0.0, extend ? 5.0 : 11.0), CopycatRenderContext.aabb(3.0, 16.0, extend ? 11.0 : 5.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull((cullEnd ? MutableCullFace.NORTH : 0) | MutableCullFace.EAST));
                        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 0.0, extend ? 5.0 : 11.0), CopycatRenderContext.aabb(3.0, 16.0, extend ? 11.0 : 5.0).move(13.0, 0.0, 0.0), CopycatRenderContext.cull((cullEnd ? MutableCullFace.NORTH : 0) | MutableCullFace.WEST));
                    }
                }
            }
        }
    }
}

