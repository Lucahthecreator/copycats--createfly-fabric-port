/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Half
 */
package com.copycatsplus.copycats.content.copycat.slope;

import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadSlope;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

public class CopycatSlopeModelCore
extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = (Direction)state.getValue(CopycatSlopeBlock.FACING);
        Half half = (Half)state.getValue(CopycatSlopeBlock.HALF);
        int rot = (int)facing.toYRot();
        boolean flipY = half == Half.TOP;
        AssemblyTransform transform = t -> t.flipY(flipY).rotateY(rot);
        CopycatSlopeModelCore.assembleSlope(context, transform, 0.0, 16.0, this.enhanced);
    }

    public static void assembleSlope(CopycatRenderContext context, AssemblyTransform transform, double minHeight, double maxHeight, boolean enhanced) {
        if (minHeight == 0.0) {
            if (enhanced) {
                CopycatSlopeModelCore.assembleTriangularSlope(context, transform, maxHeight, CopycatSlopeModelCore.getMarginForHeight(maxHeight));
            } else {
                CopycatSlopeModelCore.assembleTriangularSlope(context, transform, maxHeight);
            }
        } else if (enhanced) {
            CopycatSlopeModelCore.assembleTrapezoidSlope(context, transform, minHeight, maxHeight, Math.min(2.0, CopycatSlopeModelCore.getMarginForHeight(maxHeight)));
        } else {
            CopycatSlopeModelCore.assembleTrapezoidSlope(context, transform, minHeight, maxHeight);
        }
    }

    private static double getMarginForHeight(double maxHeight) {
        if (maxHeight <= 2.5) {
            return 0.5;
        }
        if (maxHeight <= 4.5) {
            return 1.0;
        }
        if (maxHeight <= 8.5) {
            return 2.0;
        }
        return 3.0;
    }

    public static void assembleTriangularSlope(CopycatRenderContext context, AssemblyTransform transform, double maxHeight) {
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 16.0, 16.0), CopycatRenderContext.cull(MutableCullFace.NORTH), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.UP, (a, b) -> QuadSlope.map(0.0, 16.0, 0.0, maxHeight, b))));
    }

    public static void assembleTrapezoidSlope(CopycatRenderContext context, AssemblyTransform transform, double minHeight, double maxHeight) {
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 16.0, 16.0), CopycatRenderContext.cull(0), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.UP, (a, b) -> QuadSlope.map(0.0, 16.0, minHeight, maxHeight, b))));
    }

    public static void assembleTriangularSlope(CopycatRenderContext context, AssemblyTransform transform, double maxHeight, double margin) {
        double angleBottom = Math.toDegrees(Math.atan2(maxHeight, 16.0));
        double marginAdjBottom = margin / Math.tan(Math.toRadians(angleBottom) / 2.0);
        double angleTop = Math.toDegrees(Math.atan2(16.0, maxHeight));
        double marginAdjTop = margin / Math.tan(Math.toRadians(angleTop) / 2.0);
        double halfLength = Math.sqrt(maxHeight * maxHeight + 256.0) / 2.0;
        double midLengthBottom = halfLength - marginAdjBottom;
        double marginAdjExcessBottom = marginAdjBottom - Math.floor(marginAdjBottom);
        double midLengthTop = halfLength - marginAdjTop;
        double marginAdjExcessTop = marginAdjTop - Math.floor(marginAdjTop);
        double alignedLengthBottom = Math.abs(Math.floor(midLengthBottom + marginAdjExcessBottom) - marginAdjExcessBottom);
        double alignedLengthTop = Math.floor(midLengthTop + marginAdjExcessTop) - marginAdjExcessTop;
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 16.0, marginAdjBottom), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.SOUTH), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.UP, (a, b) -> QuadSlope.map(0.0, marginAdjBottom, 0.0, margin, b))));
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, marginAdjBottom), CopycatRenderContext.aabb(16.0, 16.0, 16.0 - margin - marginAdjBottom).move(0.0, 0.0, marginAdjBottom), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.SOUTH), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.UP, (a, b) -> QuadSlope.map(marginAdjBottom, 16.0 - margin, margin, maxHeight - marginAdjTop, b))));
        if (maxHeight == 16.0) {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 16.0 - margin), CopycatRenderContext.aabb(16.0, 16.0, margin).move(0.0, 0.0, 16.0 - margin), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.UP, (a, b) -> QuadSlope.map(16.0 - margin, 16.0, maxHeight - marginAdjTop, maxHeight, b))));
        } else {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 16.0 - margin), CopycatRenderContext.aabb(16.0, maxHeight / 2.0, margin).move(0.0, 0.0, 16.0 - margin), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 16.0 - maxHeight / 2.0, 16.0 - margin), CopycatRenderContext.aabb(16.0, maxHeight / 2.0, margin).move(0.0, 16.0 - maxHeight / 2.0, 16.0 - margin), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.NORTH), CopycatRenderContext.scale(CopycatRenderContext.pivot(16.0, 16.0, 16.0), CopycatRenderContext.scale(1.0, 32.0 / maxHeight, 1.0)), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.UP, (a, b) -> QuadSlope.map(16.0 - margin, 16.0, 16.0 - marginAdjTop / maxHeight * 32.0, 16.0, b))), CopycatRenderContext.scale(CopycatRenderContext.pivot(16.0, 16.0, 16.0), CopycatRenderContext.scale(1.0, maxHeight / 32.0, 1.0)), CopycatRenderContext.translate(0.0, -16.0 + maxHeight, 0.0));
        }
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 16.0, marginAdjBottom), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.SOUTH), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.DOWN, (a, b) -> QuadSlope.map(0.0, marginAdjBottom, 0.0, margin, b))), CopycatRenderContext.translate(0.0, -16.0, 0.0), CopycatRenderContext.rotate(CopycatRenderContext.pivot(0.0, 0.0, 0.0), CopycatRenderContext.angle(-angleBottom, 0.0, 0.0)));
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, maxHeight - margin, 16.0 - alignedLengthBottom), CopycatRenderContext.aabb(16.0, margin, alignedLengthBottom).move(0.0, 16.0 - margin, marginAdjBottom), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.SOUTH), CopycatRenderContext.scale(CopycatRenderContext.pivot(16.0, maxHeight, 16.0), CopycatRenderContext.scale(1.0, 1.0, midLengthBottom / alignedLengthBottom)), CopycatRenderContext.translate(0.0, 0.0, -halfLength), CopycatRenderContext.rotate(CopycatRenderContext.pivot(16.0, maxHeight, 16.0), CopycatRenderContext.angle(-angleBottom, 0.0, 0.0)));
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, maxHeight - margin, 16.0 - alignedLengthTop), CopycatRenderContext.aabb(16.0, margin, alignedLengthTop).move(0.0, 16.0 - margin, 16.0 - marginAdjTop - alignedLengthTop), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.SOUTH), CopycatRenderContext.scale(CopycatRenderContext.pivot(16.0, maxHeight, 16.0), CopycatRenderContext.scale(1.0, 1.0, midLengthTop / alignedLengthTop)), CopycatRenderContext.translate(0.0, 0.0, -marginAdjTop), CopycatRenderContext.rotate(CopycatRenderContext.pivot(16.0, maxHeight, 16.0), CopycatRenderContext.angle(-angleBottom, 0.0, 0.0)));
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 16.0 - marginAdjTop), CopycatRenderContext.aabb(16.0, 16.0, marginAdjTop).move(0.0, 0.0, 16.0 - marginAdjTop), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.SOUTH), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.DOWN, (a, b) -> QuadSlope.map(16.0 - marginAdjTop, 16.0, margin, 0.0, b))), CopycatRenderContext.translate(0.0, -16.0 + maxHeight, 0.0), CopycatRenderContext.rotate(CopycatRenderContext.pivot(16.0, maxHeight, 16.0), CopycatRenderContext.angle(-angleBottom, 0.0, 0.0)));
    }

    public static void assembleTrapezoidSlope(CopycatRenderContext context, AssemblyTransform transform, double minHeight, double maxHeight, double margin) {
        double angleBottom = Math.toDegrees(Math.atan2(maxHeight - minHeight, 16.0)) + 90.0;
        double marginAdjBottom = margin / Math.tan(Math.toRadians(angleBottom) / 2.0);
        double angleTop = Math.toDegrees(Math.atan2(16.0, maxHeight - minHeight));
        double marginAdjTop = margin / Math.tan(Math.toRadians(angleTop) / 2.0);
        double halfLength = Math.sqrt((maxHeight - minHeight) * (maxHeight - minHeight) + 256.0) / 2.0;
        double midLengthBottom = halfLength - marginAdjBottom;
        double marginAdjExcessBottom = marginAdjBottom - Math.floor(marginAdjBottom);
        double midLengthTop = halfLength - marginAdjTop;
        double marginAdjExcessTop = marginAdjTop - Math.floor(marginAdjTop);
        double alignedLengthBottom = Math.abs(Math.floor(midLengthBottom + marginAdjExcessBottom) - marginAdjExcessBottom);
        double alignedLengthTop = Math.floor(midLengthTop + marginAdjExcessTop) - marginAdjExcessTop;
        if (minHeight == 16.0 || minHeight == 0.0) {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 16.0, margin), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.UP, (a, b) -> QuadSlope.map(0.0, margin, minHeight, minHeight - marginAdjBottom, b))));
        } else {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, minHeight / 2.0, margin).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, minHeight / 2.0, margin).move(0.0, 16.0 - minHeight / 2.0, 0.0), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.SOUTH), CopycatRenderContext.scale(CopycatRenderContext.pivot(0.0, 0.0, 0.0), CopycatRenderContext.scale(1.0, 32.0 / minHeight, 1.0)), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.UP, (a, b) -> QuadSlope.map(0.0, margin, 16.0, 16.0 - marginAdjBottom / minHeight * 32.0, b))), CopycatRenderContext.scale(CopycatRenderContext.pivot(0.0, 0.0, 0.0), CopycatRenderContext.scale(1.0, minHeight / 32.0, 1.0)), CopycatRenderContext.translate(0.0, minHeight / 2.0, 0.0));
        }
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, margin), CopycatRenderContext.aabb(16.0, 16.0, 16.0 - margin * 2.0).move(0.0, 0.0, margin), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.SOUTH), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.UP, (a, b) -> QuadSlope.map(margin, 16.0 - margin, minHeight - marginAdjBottom, maxHeight - marginAdjTop, b))));
        if (maxHeight == 16.0 || maxHeight == 0.0) {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 16.0 - margin), CopycatRenderContext.aabb(16.0, 16.0, margin).move(0.0, 0.0, 16.0 - margin), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.UP, (a, b) -> QuadSlope.map(16.0 - margin, 16.0, maxHeight - marginAdjTop, maxHeight, b))));
        } else {
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 16.0 - margin), CopycatRenderContext.aabb(16.0, maxHeight / 2.0, margin).move(0.0, 0.0, 16.0 - margin), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH));
            context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 16.0 - maxHeight / 2.0, 16.0 - margin), CopycatRenderContext.aabb(16.0, maxHeight / 2.0, margin).move(0.0, 16.0 - maxHeight / 2.0, 16.0 - margin), CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.NORTH), CopycatRenderContext.scale(CopycatRenderContext.pivot(16.0, 16.0, 16.0), CopycatRenderContext.scale(1.0, 32.0 / maxHeight, 1.0)), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.UP, (a, b) -> QuadSlope.map(16.0 - margin, 16.0, 16.0 - marginAdjTop / maxHeight * 32.0, 16.0, b))), CopycatRenderContext.scale(CopycatRenderContext.pivot(16.0, 16.0, 16.0), CopycatRenderContext.scale(1.0, maxHeight / 32.0, 1.0)), CopycatRenderContext.translate(0.0, -16.0 + maxHeight, 0.0));
        }
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 0.0), CopycatRenderContext.aabb(16.0, 16.0, marginAdjBottom), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.SOUTH), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.DOWN, (a, b) -> QuadSlope.map(0.0, marginAdjBottom, 0.0, margin, b))), CopycatRenderContext.translate(0.0, -16.0 + minHeight, 0.0), CopycatRenderContext.rotate(CopycatRenderContext.pivot(0.0, minHeight, 0.0), CopycatRenderContext.angle(90.0 - angleBottom, 0.0, 0.0)));
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, maxHeight - margin, 16.0 - alignedLengthBottom), CopycatRenderContext.aabb(16.0, margin, alignedLengthBottom).move(0.0, 16.0 - margin, marginAdjBottom), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.SOUTH), CopycatRenderContext.scale(CopycatRenderContext.pivot(16.0, maxHeight, 16.0), CopycatRenderContext.scale(1.0, 1.0, midLengthBottom / alignedLengthBottom)), CopycatRenderContext.translate(0.0, 0.0, -halfLength), CopycatRenderContext.rotate(CopycatRenderContext.pivot(16.0, maxHeight, 16.0), CopycatRenderContext.angle(90.0 - angleBottom, 0.0, 0.0)));
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, maxHeight - margin, 16.0 - alignedLengthTop), CopycatRenderContext.aabb(16.0, margin, alignedLengthTop).move(0.0, 16.0 - margin, 16.0 - marginAdjTop - alignedLengthTop), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.SOUTH), CopycatRenderContext.scale(CopycatRenderContext.pivot(16.0, maxHeight, 16.0), CopycatRenderContext.scale(1.0, 1.0, midLengthTop / alignedLengthTop)), CopycatRenderContext.translate(0.0, 0.0, -marginAdjTop), CopycatRenderContext.rotate(CopycatRenderContext.pivot(16.0, maxHeight, 16.0), CopycatRenderContext.angle(90.0 - angleBottom, 0.0, 0.0)));
        context.assemblePiece(transform, CopycatRenderContext.vec3(0.0, 0.0, 16.0 - marginAdjTop), CopycatRenderContext.aabb(16.0, 16.0, marginAdjTop).move(0.0, 0.0, 16.0 - marginAdjTop), CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.SOUTH), CopycatRenderContext.updateUV(CopycatRenderContext.slope(Direction.DOWN, (a, b) -> QuadSlope.map(16.0 - marginAdjTop, 16.0, margin, 0.0, b))), CopycatRenderContext.translate(0.0, -16.0 + maxHeight, 0.0), CopycatRenderContext.rotate(CopycatRenderContext.pivot(16.0, maxHeight, 16.0), CopycatRenderContext.angle(90.0 - angleBottom, 0.0, 0.0)));
    }
}

