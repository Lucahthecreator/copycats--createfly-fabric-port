/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.data.Iterate
 *  com.zurrtum.create.content.fluids.FluidTransportBehaviour$AttachmentTypes
 *  com.zurrtum.create.content.fluids.FluidTransportBehaviour$AttachmentTypes$ComponentPartials
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.block.dispatch.BlockStateModel
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.world.level.block.PipeBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.fluid_pipe;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.content.fluids.FluidTransportBehaviour;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatFluidPipeModelCore
extends CopycatModelCore.WithData<CopycatFluidPipeModelCore.PipeModelData> {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        super.registerModels(entries);
        entries.add(new CopycatModelCore.ModelEntry("bracket", (state, material) -> ((PipeModelData)this.getData()).getBracket(), null, CopycatModelCore.EntryType.STATIC));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        ArrayList<Direction> directions = new ArrayList<Direction>(6);
        for (Direction direction : Iterate.directions) {
            if (!((Boolean)state.getValue((Property)PipeBlock.PROPERTY_BY_DIRECTION.get(direction))).booleanValue()) continue;
            directions.add(direction);
        }
        if (directions.size() == 2) {
            if (((Direction)directions.get(0)).getAxis() == ((Direction)directions.get(1)).getAxis()) {
                int yRot = ((Direction)directions.get(0)).getAxis() == Direction.Axis.X ? 90 : 0;
                int xRot = ((Direction)directions.get(0)).getAxis() == Direction.Axis.Y ? 90 : 0;
                this.renderStraightCore(context, t -> t.rotateY(yRot).rotateX(xRot));
            } else {
                Direction base = null;
                if (((Direction)directions.get(0)).getAxis() == Direction.Axis.X) {
                    base = (Direction)directions.remove(0);
                } else if (((Direction)directions.get(1)).getAxis() == Direction.Axis.X) {
                    base = (Direction)directions.remove(1);
                }
                if (base != null) {
                    boolean flipX = base.getAxisDirection() == Direction.AxisDirection.POSITIVE;
                    int xRot = CopycatFluidPipeModelCore.getXRot((Direction)directions.get(0));
                    this.renderBend(context, t -> t.flipX(flipX).rotateX(xRot));
                } else {
                    if (((Direction)directions.get(0)).getAxis() == Direction.Axis.Z) {
                        base = (Direction)directions.remove(0);
                    } else if (((Direction)directions.get(1)).getAxis() == Direction.Axis.Z) {
                        base = (Direction)directions.remove(1);
                    }
                    if (base != null) {
                        boolean flipZ = base.getAxisDirection() == Direction.AxisDirection.POSITIVE;
                        int zRot = CopycatFluidPipeModelCore.getZRot((Direction)directions.get(0));
                        this.renderBend(context, t -> t.flipZ(flipZ).rotateZ(zRot));
                    }
                }
            }
        } else if (directions.size() == 3) {
            boolean flipX = false;
            boolean flipY = false;
            boolean flipZ = false;
            for (Direction direction : directions) {
                if (direction.getAxis() == Direction.Axis.X) {
                    flipX = direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
                    continue;
                }
                if (direction.getAxis() == Direction.Axis.Y) {
                    flipY = direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
                    continue;
                }
                flipZ = direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
            }
            boolean finalFlipX = flipX;
            boolean finalFlipY = flipY;
            boolean finalFlipZ = flipZ;
            this.renderCorner(context, t -> t.flipX(finalFlipX).flipY(finalFlipY).flipZ(finalFlipZ));
        }
        this.assembleAccessories(context);
    }

    protected void assembleAccessories(CopycatRenderContext context) {
        for (Direction direction : Iterate.directions) {
            for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials partial : ((PipeModelData)this.getData()).getAttachment((Direction)direction).partials) {
                this.renderComponent(context, direction, partial);
            }
        }
        if (((PipeModelData)this.getData()).isEncased()) {
            this.renderEncasing(context);
        }
    }

    protected static int getXRot(Direction direction) {
        return switch (direction) {
            case Direction.NORTH -> 0;
            case Direction.DOWN -> 90;
            case Direction.SOUTH -> 180;
            case Direction.UP -> 270;
            default -> 0;
        };
    }

    protected static int getZRot(Direction direction) {
        return switch (direction) {
            case Direction.WEST -> 0;
            case Direction.UP -> 90;
            case Direction.EAST -> 180;
            case Direction.DOWN -> 270;
            default -> 0;
        };
    }

    protected void renderStraightCore(CopycatRenderContext context, AssemblyTransform transform) {
        context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 4.0), CopycatRenderContext.aabb(4.0, 4.0, 8.0).move(0.0, 0.0, 4.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.SOUTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 4.0, 4.0), CopycatRenderContext.aabb(4.0, 4.0, 8.0).move(12.0, 0.0, 4.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.SOUTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 8.0, 4.0), CopycatRenderContext.aabb(4.0, 4.0, 8.0).move(0.0, 12.0, 4.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.SOUTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 4.0), CopycatRenderContext.aabb(4.0, 4.0, 8.0).move(12.0, 12.0, 4.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.SOUTH));
    }

    protected void renderBend(CopycatRenderContext context, AssemblyTransform transform) {
        if (this.enhanced) {
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 4.0, 4.0), CopycatRenderContext.aabb(4.0, 4.0, 8.0).move(12.0, 0.0, 8.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 4.0), CopycatRenderContext.aabb(4.0, 4.0, 8.0).move(12.0, 12.0, 8.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 8.0), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(8.0, 0.0, 12.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 8.0, 8.0), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(8.0, 12.0, 12.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.DOWN | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(7.0, 4.0, 4.0), CopycatRenderContext.aabb(1.0, 8.0, 4.0).move(3.0, 0.0, 8.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.NORTH | MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 4.0, 4.0), CopycatRenderContext.aabb(2.0, 8.0, 2.0).move(1.0, 0.0, 8.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.NORTH | MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 6.0), CopycatRenderContext.aabb(3.0, 8.0, 2.0).move(8.0, 0.0, 2.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.NORTH | MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 4.0), CopycatRenderContext.aabb(1.0, 8.0, 2.0).move(8.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.NORTH | MutableCullFace.SOUTH));
        } else {
            context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 4.0), CopycatRenderContext.aabb(8.0, 4.0, 8.0).move(8.0, 0.0, 8.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 8.0, 4.0), CopycatRenderContext.aabb(8.0, 4.0, 8.0).move(8.0, 12.0, 8.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN | MutableCullFace.NORTH));
        }
    }

    protected void renderCorner(CopycatRenderContext context, AssemblyTransform transform) {
        if (this.enhanced) {
            context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 4.0), CopycatRenderContext.aabb(4.0, 8.0, 4.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP | MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 8.0), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(0.0, 0.0, 4.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 4.0, 4.0), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(4.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.SOUTH));
            this.renderCornerPart(context, transform);
            this.renderCornerPart(context, t -> transform.apply((AssemblyTransform.Transformable<?>)t.rotateY(-90).flipZ(true)));
            this.renderCornerPart(context, t -> transform.apply((AssemblyTransform.Transformable<?>)t.rotateX(90).flipZ(true)));
        } else {
            context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 4.0), CopycatRenderContext.aabb(8.0, 8.0, 8.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP | MutableCullFace.SOUTH));
        }
    }

    protected void renderCornerPart(CopycatRenderContext context, AssemblyTransform transform) {
        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 4.0), CopycatRenderContext.aabb(4.0, 1.0, 4.0).move(4.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.SOUTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(10.0, 9.0, 4.0), CopycatRenderContext.aabb(2.0, 2.0, 4.0).move(6.0, 13.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.SOUTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 9.0, 4.0), CopycatRenderContext.aabb(2.0, 3.0, 4.0).move(12.0, 5.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.SOUTH));
        context.assemblePiece(transform, CopycatRenderContext.vec3(10.0, 11.0, 4.0), CopycatRenderContext.aabb(2.0, 1.0, 4.0).move(14.0, 7.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.SOUTH));
    }

    protected void renderEncasing(CopycatRenderContext context) {
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(3.0, 3.0, 3.0), CopycatRenderContext.aabb(5.0, 5.0, 5.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP | MutableCullFace.SOUTH));
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(8.0, 3.0, 3.0), CopycatRenderContext.aabb(5.0, 5.0, 5.0).move(11.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.SOUTH));
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(3.0, 8.0, 3.0), CopycatRenderContext.aabb(5.0, 5.0, 5.0).move(0.0, 11.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN | MutableCullFace.SOUTH));
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(8.0, 8.0, 3.0), CopycatRenderContext.aabb(5.0, 5.0, 5.0).move(11.0, 11.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN | MutableCullFace.SOUTH));
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(3.0, 3.0, 8.0), CopycatRenderContext.aabb(5.0, 5.0, 5.0).move(0.0, 0.0, 11.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP | MutableCullFace.NORTH));
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(8.0, 3.0, 8.0), CopycatRenderContext.aabb(5.0, 5.0, 5.0).move(11.0, 0.0, 11.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.NORTH));
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(3.0, 8.0, 8.0), CopycatRenderContext.aabb(5.0, 5.0, 5.0).move(0.0, 11.0, 11.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN | MutableCullFace.NORTH));
        context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(8.0, 8.0, 8.0), CopycatRenderContext.aabb(5.0, 5.0, 5.0).move(11.0, 11.0, 11.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN | MutableCullFace.NORTH));
    }

    protected void renderComponent(CopycatRenderContext context, Direction direction, FluidTransportBehaviour.AttachmentTypes.ComponentPartials component) {
        AssemblyTransform transform = direction.getAxis().isVertical() ? t -> t.rotateX(direction == Direction.DOWN ? 90 : -90) : t -> t.rotateY((int)direction.toYRot() + 180);
        switch (component) {
            case RIM: {
                context.assemblePiece(transform, CopycatRenderContext.vec3(3.0, 3.0, 0.0), CopycatRenderContext.aabb(5.0, 5.0, 2.0).move(0.0, 0.0, 14.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 3.0, 0.0), CopycatRenderContext.aabb(5.0, 5.0, 2.0).move(11.0, 0.0, 14.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP));
                context.assemblePiece(transform, CopycatRenderContext.vec3(3.0, 8.0, 0.0), CopycatRenderContext.aabb(5.0, 5.0, 2.0).move(0.0, 11.0, 14.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 0.0), CopycatRenderContext.aabb(5.0, 5.0, 2.0).move(11.0, 11.0, 14.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN));
                break;
            }
            case CONNECTION: {
                context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 0.0), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(0.0, 0.0, 4.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.NORTH));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 4.0, 0.0), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 0.0, 4.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.NORTH));
                context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 8.0, 0.0), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(0.0, 12.0, 4.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.NORTH));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 0.0), CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 12.0, 4.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.NORTH));
                break;
            }
            case RIM_CONNECTOR: {
                context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 2.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.NORTH));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 4.0, 2.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(12.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.NORTH));
                context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 8.0, 2.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(0.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.NORTH));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 2.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(12.0, 12.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.NORTH));
                context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 4.0, 0.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(0.0, 0.0, 4.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.NORTH));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 4.0, 0.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(12.0, 0.0, 4.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.NORTH));
                context.assemblePiece(transform, CopycatRenderContext.vec3(4.0, 8.0, 0.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(0.0, 12.0, 4.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.NORTH));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, 0.0), CopycatRenderContext.aabb(4.0, 4.0, 2.0).move(12.0, 12.0, 4.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.NORTH));
                break;
            }
            case DRAIN: {
                context.assemblePiece(transform, CopycatRenderContext.vec3(3.0, 3.0, -1.0), CopycatRenderContext.aabb(5.0, 5.0, 3.0).move(0.0, 0.0, 13.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 3.0, -1.0), CopycatRenderContext.aabb(5.0, 5.0, 3.0).move(11.0, 0.0, 13.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP));
                context.assemblePiece(transform, CopycatRenderContext.vec3(3.0, 8.0, -1.0), CopycatRenderContext.aabb(5.0, 5.0, 3.0).move(0.0, 11.0, 13.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, -1.0), CopycatRenderContext.aabb(5.0, 5.0, 3.0).move(11.0, 11.0, 13.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN));
                context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 5.0, -4.0), CopycatRenderContext.aabb(3.0, 3.0, 3.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.UP));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 5.0, -4.0), CopycatRenderContext.aabb(3.0, 3.0, 3.0).move(13.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.UP));
                context.assemblePiece(transform, CopycatRenderContext.vec3(5.0, 8.0, -4.0), CopycatRenderContext.aabb(3.0, 3.0, 3.0).move(0.0, 13.0, 0.0), CopycatRenderContext.cull(MutableCullFace.EAST | MutableCullFace.DOWN));
                context.assemblePiece(transform, CopycatRenderContext.vec3(8.0, 8.0, -4.0), CopycatRenderContext.aabb(3.0, 3.0, 3.0).move(13.0, 13.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.DOWN));
            }
        }
    }

    public static class PipeModelData {
        private final FluidTransportBehaviour.AttachmentTypes[] attachments = new FluidTransportBehaviour.AttachmentTypes[6];
        private boolean encased;
        private BlockStateModel bracket;

        public PipeModelData() {
            Arrays.fill(this.attachments, FluidTransportBehaviour.AttachmentTypes.NONE);
        }

        public void putBracket(BlockState state) {
            if (state != null) {
                this.bracket = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
            }
        }

        public BlockStateModel getBracket() {
            return this.bracket;
        }

        public void putAttachment(Direction face, FluidTransportBehaviour.AttachmentTypes rim) {
            this.attachments[face.get3DDataValue()] = rim;
        }

        public FluidTransportBehaviour.AttachmentTypes getAttachment(Direction face) {
            return this.attachments[face.get3DDataValue()];
        }

        public void setEncased(boolean encased) {
            this.encased = encased;
        }

        public boolean isEncased() {
            return this.encased;
        }
    }
}
