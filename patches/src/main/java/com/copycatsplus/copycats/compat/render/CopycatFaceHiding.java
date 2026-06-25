package com.copycatsplus.copycats.compat.render;

import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.compat.render.connectivity.CopycatConnectivity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public final class CopycatFaceHiding {
    private CopycatFaceHiding() {
    }

    public static Optional<Boolean> getRenderOverride(BlockAndTintGetter level, BlockPos pos, BlockState state, Direction face) {
        BlockPos neighborPos = pos.relative(face);
        BlockState neighborState = level.getBlockState(neighborPos);

        if (state.getBlock() instanceof ICopycatBlock || neighborState.getBlock() instanceof ICopycatBlock) {
            BlockState material = CopycatRenderMaterial.resolveForFace(level, pos, state, face);
            BlockState neighborMaterial = CopycatRenderMaterial.resolveForFace(
                    level, neighborPos, neighborState, face.getOpposite());
            boolean familyCanHide = CopycatConnectivity.canHideFace(level, pos, state, neighborPos, neighborState, face);
            boolean materialSkips = material.skipRendering(neighborMaterial, face);
            CopycatsDebug.log("face_hiding", () -> "check pos=" + pos + " state=" + state
                    + " neighbor=" + neighborPos + " neighborState=" + neighborState
                    + " face=" + face + " material=" + material
                    + " neighborMaterial=" + neighborMaterial
                    + " familyCanHide=" + familyCanHide
                    + " materialSkips=" + materialSkips);
            if (familyCanHide && materialSkips) {
                CopycatsDebug.log("face_hiding", () -> "hide pos=" + pos + " state=" + state
                        + " neighbor=" + neighborPos + " neighborState=" + neighborState
                        + " face=" + face + " material=" + material
                        + " neighborMaterial=" + neighborMaterial);
                return Optional.of(false);
            }
            CopycatsDebug.log("face_hiding", () -> "keep pos=" + pos + " state=" + state
                    + " neighbor=" + neighborPos + " neighborState=" + neighborState
                    + " face=" + face + " material=" + material
                    + " neighborMaterial=" + neighborMaterial);
            return Optional.empty();
        }
        return Optional.empty();
    }
}
