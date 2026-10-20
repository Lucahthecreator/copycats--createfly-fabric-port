package com.copycatsplus.copycats.content.copycat.wall;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.zurrtum.create.catnip.data.Iterate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WallSide;

public class CopycatWallModelCore extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(
            new CopycatModelCore.ModelEntry(
                "material", CopycatModelCore.ModelGetter.MATERIAL, this, updatePropertiesIfMatch(WallBlock.class), CopycatModelCore.EntryType.COPYCAT
            )
        );
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof WallBlock) {
            context.assembleAll();
        } else {
            boolean pole = state.getValue(WallBlock.UP);
            if (pole) {
                for (Direction direction : Iterate.horizontalDirections) {
                    context.assemblePiece(
                        t -> t.rotateY((int)direction.toYRot()),
                        CopycatRenderContext.vec3(4.0, 0.0, 4.0),
                        CopycatRenderContext.aabb(4.0, 16.0, 4.0),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST)
                    );
                }

                for (Direction direction : Iterate.horizontalDirections) {
                    int rot = (int)direction.toYRot();
                    AssemblyTransform transform = t -> t.rotateY(rot);
                    switch ((WallSide)state.getValue(CopycatWallBlock.byDirection(direction))) {
                        case NONE:
                        default:
                            break;
                        case LOW:
                            context.assemblePiece(
                                transform,
                                CopycatRenderContext.vec3(5.0, 0.0, 12.0),
                                CopycatRenderContext.aabb(3.0, 7.0, 4.0),
                                CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
                            );
                            context.assemblePiece(
                                transform,
                                CopycatRenderContext.vec3(8.0, 0.0, 12.0),
                                CopycatRenderContext.aabb(3.0, 7.0, 4.0).move(13.0, 0.0, 0.0),
                                CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
                            );
                            context.assemblePiece(
                                transform,
                                CopycatRenderContext.vec3(5.0, 7.0, 12.0),
                                CopycatRenderContext.aabb(3.0, 7.0, 4.0).move(0.0, 9.0, 0.0),
                                CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                            );
                            context.assemblePiece(
                                transform,
                                CopycatRenderContext.vec3(8.0, 7.0, 12.0),
                                CopycatRenderContext.aabb(3.0, 7.0, 4.0).move(13.0, 9.0, 0.0),
                                CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                            );
                            break;
                        case TALL:
                            context.assemblePiece(
                                transform,
                                CopycatRenderContext.vec3(5.0, 0.0, 12.0),
                                CopycatRenderContext.aabb(3.0, 16.0, 4.0),
                                CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST)
                            );
                            context.assemblePiece(
                                transform,
                                CopycatRenderContext.vec3(8.0, 0.0, 12.0),
                                CopycatRenderContext.aabb(3.0, 16.0, 4.0).move(13.0, 0.0, 0.0),
                                CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.WEST)
                            );
                    }
                }
            } else {
                boolean tall = false;
                Map<Direction, WallSide> sides = new HashMap<>();

                for (Direction direction : Iterate.horizontalDirections) {
                    WallSide wall = state.getValue(CopycatWallBlock.byDirection(direction));
                    sides.put(direction, wall);
                    if (wall == WallSide.TALL) {
                        tall = true;
                    }
                }

                if (sides.get(Direction.SOUTH) != sides.get(Direction.NORTH)
                    || sides.get(Direction.EAST) != sides.get(Direction.WEST)
                    || sides.get(Direction.NORTH) != WallSide.NONE && sides.get(Direction.EAST) != WallSide.NONE
                    || sides.get(Direction.NORTH) == WallSide.NONE && sides.get(Direction.EAST) == WallSide.NONE) {
                    Direction extendSide = null;
                    long sideCount = sides.values().stream().filter(s -> s != WallSide.NONE).count();
                    if (sideCount == 1L) {
                        extendSide = sides.entrySet().stream().filter(s -> s.getValue() != WallSide.NONE).findFirst().map(Entry::getKey).orElse(null);
                    } else {
                        for (Direction directionx : Iterate.horizontalDirections) {
                            int rot = (int)directionx.toYRot();
                            AssemblyTransform transform = t -> t.rotateY(rot);
                            if (tall) {
                                boolean cullCurrent = sides.get(directionx.getOpposite()) == WallSide.TALL;
                                boolean cullAdjacent = sides.get(directionx.getClockWise()) == WallSide.TALL;
                                context.assemblePiece(
                                    transform,
                                    CopycatRenderContext.vec3(5.0, 0.0, 5.0),
                                    CopycatRenderContext.aabb(3.0, 16.0, 3.0).move(0.0, 0.0, 0.0),
                                    CopycatRenderContext.cull(
                                        MutableCullFace.SOUTH
                                            | MutableCullFace.EAST
                                            | (cullCurrent ? MutableCullFace.NORTH : 0)
                                            | (cullAdjacent ? MutableCullFace.WEST : 0)
                                    )
                                );
                            } else {
                                boolean cullCurrent = sides.get(directionx.getOpposite()) != WallSide.NONE;
                                boolean cullAdjacent = sides.get(directionx.getClockWise()) != WallSide.NONE;
                                context.assemblePiece(
                                    transform,
                                    CopycatRenderContext.vec3(5.0, 0.0, 5.0),
                                    CopycatRenderContext.aabb(3.0, 7.0, 3.0).move(0.0, 0.0, 0.0),
                                    CopycatRenderContext.cull(
                                        MutableCullFace.UP
                                            | MutableCullFace.SOUTH
                                            | MutableCullFace.EAST
                                            | (cullCurrent ? MutableCullFace.NORTH : 0)
                                            | (cullAdjacent ? MutableCullFace.WEST : 0)
                                    )
                                );
                                context.assemblePiece(
                                    transform,
                                    CopycatRenderContext.vec3(5.0, 7.0, 5.0),
                                    CopycatRenderContext.aabb(3.0, 7.0, 3.0).move(0.0, 9.0, 0.0),
                                    CopycatRenderContext.cull(
                                        MutableCullFace.DOWN
                                            | MutableCullFace.SOUTH
                                            | MutableCullFace.EAST
                                            | (cullCurrent ? MutableCullFace.NORTH : 0)
                                            | (cullAdjacent ? MutableCullFace.WEST : 0)
                                    )
                                );
                            }
                        }
                    }

                    for (Direction directionxx : Iterate.horizontalDirections) {
                        int rot = (int)directionxx.toYRot();
                        boolean extend = extendSide == directionxx;
                        boolean cullEnd = !extend;
                        AssemblyTransform transform = t -> t.rotateY(rot);
                        switch ((WallSide)sides.get(directionxx)) {
                            case NONE:
                            default:
                                break;
                            case LOW:
                                context.assemblePiece(
                                    transform,
                                    CopycatRenderContext.vec3(5.0, 0.0, extend ? 5.0 : 11.0),
                                    CopycatRenderContext.aabb(3.0, 7.0, extend ? 11.0 : 5.0).move(0.0, 0.0, 0.0),
                                    CopycatRenderContext.cull(MutableCullFace.UP | (cullEnd ? MutableCullFace.NORTH : 0) | MutableCullFace.EAST)
                                );
                                context.assemblePiece(
                                    transform,
                                    CopycatRenderContext.vec3(8.0, 0.0, extend ? 5.0 : 11.0),
                                    CopycatRenderContext.aabb(3.0, 7.0, extend ? 11.0 : 5.0).move(13.0, 0.0, 0.0),
                                    CopycatRenderContext.cull(MutableCullFace.UP | (cullEnd ? MutableCullFace.NORTH : 0) | MutableCullFace.WEST)
                                );
                                context.assemblePiece(
                                    transform,
                                    CopycatRenderContext.vec3(5.0, 7.0, extend ? 5.0 : 11.0),
                                    CopycatRenderContext.aabb(3.0, 7.0, extend ? 11.0 : 5.0).move(0.0, 9.0, 0.0),
                                    CopycatRenderContext.cull(MutableCullFace.DOWN | (cullEnd ? MutableCullFace.NORTH : 0) | MutableCullFace.EAST)
                                );
                                context.assemblePiece(
                                    transform,
                                    CopycatRenderContext.vec3(8.0, 7.0, extend ? 5.0 : 11.0),
                                    CopycatRenderContext.aabb(3.0, 7.0, extend ? 11.0 : 5.0).move(13.0, 9.0, 0.0),
                                    CopycatRenderContext.cull(MutableCullFace.DOWN | (cullEnd ? MutableCullFace.NORTH : 0) | MutableCullFace.WEST)
                                );
                                break;
                            case TALL:
                                context.assemblePiece(
                                    transform,
                                    CopycatRenderContext.vec3(5.0, 0.0, extend ? 5.0 : 11.0),
                                    CopycatRenderContext.aabb(3.0, 16.0, extend ? 11.0 : 5.0).move(0.0, 0.0, 0.0),
                                    CopycatRenderContext.cull((cullEnd ? MutableCullFace.NORTH : 0) | MutableCullFace.EAST)
                                );
                                context.assemblePiece(
                                    transform,
                                    CopycatRenderContext.vec3(8.0, 0.0, extend ? 5.0 : 11.0),
                                    CopycatRenderContext.aabb(3.0, 16.0, extend ? 11.0 : 5.0).move(13.0, 0.0, 0.0),
                                    CopycatRenderContext.cull((cullEnd ? MutableCullFace.NORTH : 0) | MutableCullFace.WEST)
                                );
                        }
                    }
                } else {
                    int rot = sides.get(Direction.SOUTH) == WallSide.NONE ? 90 : 0;
                    AssemblyTransform transform = t -> t.rotateY(rot);
                    if (!tall) {
                        context.assemblePiece(
                            transform,
                            CopycatRenderContext.vec3(5.0, 0.0, 0.0),
                            CopycatRenderContext.aabb(3.0, 7.0, 16.0),
                            CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST)
                        );
                        context.assemblePiece(
                            transform,
                            CopycatRenderContext.vec3(8.0, 0.0, 0.0),
                            CopycatRenderContext.aabb(3.0, 7.0, 16.0).move(13.0, 0.0, 0.0),
                            CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST)
                        );
                        context.assemblePiece(
                            transform,
                            CopycatRenderContext.vec3(5.0, 7.0, 0.0),
                            CopycatRenderContext.aabb(3.0, 7.0, 16.0).move(0.0, 9.0, 0.0),
                            CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST)
                        );
                        context.assemblePiece(
                            transform,
                            CopycatRenderContext.vec3(8.0, 7.0, 0.0),
                            CopycatRenderContext.aabb(3.0, 7.0, 16.0).move(13.0, 9.0, 0.0),
                            CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST)
                        );
                    } else {
                        context.assemblePiece(
                            transform,
                            CopycatRenderContext.vec3(5.0, 0.0, 0.0),
                            CopycatRenderContext.aabb(3.0, 16.0, 16.0).move(0.0, 0.0, 0.0),
                            CopycatRenderContext.cull(MutableCullFace.EAST)
                        );
                        context.assemblePiece(
                            transform,
                            CopycatRenderContext.vec3(8.0, 0.0, 0.0),
                            CopycatRenderContext.aabb(3.0, 16.0, 16.0).move(13.0, 0.0, 0.0),
                            CopycatRenderContext.cull(MutableCullFace.WEST)
                        );
                    }
                }
            }
        }
    }
}
