package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.compat.render.CopycatRenderShape;
import com.copycatsplus.copycats.compat.render.connectivity.CopycatConnectivity;
import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(ScaledBlockAndTintGetter.class)
public abstract class ScaledBlockAndTintGetterMixin {
    @Shadow
    protected BlockAndTintGetter wrapped;

    @Shadow
    protected BlockPos origin;

    @Shadow
    protected Predicate<BlockPos> filter;

    @Shadow
    public abstract BlockPos getTruePos(BlockPos pos);

    @Inject(method = "getBlockState", at = @At("RETURN"), cancellable = true)
    private void copycats$allowMatchingShapeCT(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        BlockPos targetPos = getTruePos(pos);
        BlockState source = wrapped.getBlockState(origin);
        BlockState target = wrapped.getBlockState(targetPos);
        if (filter.test(pos)) {
            CopycatsDebug.log("filter", () -> "scaled pass sourcePos=" + origin + " source=" + source
                    + " scaledTargetPos=" + pos + " targetPos=" + targetPos
                    + " target=" + target + " returned=" + cir.getReturnValue());
            return;
        }

        Direction face = copycats$directionBetween(origin, targetPos);
        boolean allow = copycats$hasSlab(source, target)
                ? CopycatConnectivity.canConnectForCT(wrapped, origin, targetPos, source, target, source, face)
                : CopycatRenderShape.usesMatchingShapeTextures(source, target);
        CopycatsDebug.log("filter", () -> "scaled sourcePos=" + origin + " source=" + source
                + " scaledTargetPos=" + pos + " targetPos=" + targetPos
                + " target=" + target + " face=" + face + " allow=" + allow);
        if (allow) {
            cir.setReturnValue(target);
        }
    }

    private static boolean copycats$hasSlab(BlockState source, BlockState target) {
        return source.getBlock() instanceof CopycatSlabBlock
                || target.getBlock() instanceof CopycatSlabBlock;
    }

    private static Direction copycats$directionBetween(BlockPos from, BlockPos to) {
        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();
        int dz = to.getZ() - from.getZ();
        int changedAxes = (dx == 0 ? 0 : 1) + (dy == 0 ? 0 : 1) + (dz == 0 ? 0 : 1);
        if (changedAxes == 1 && Math.abs(dx + dy + dz) == 1) {
            return Direction.getApproximateNearest(dx, dy, dz);
        }
        return null;
    }
}
