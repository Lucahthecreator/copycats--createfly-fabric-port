package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.compat.render.CopycatRenderFlags;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatMultiByteModelCore extends CopycatModelCore {
    private static final Map<String, CopycatByteBlock.Byte> byteMap = CopycatByteBlock.allBytes
        .stream()
        .collect(Collectors.toMap(s -> CopycatByteBlock.byByte(s).getName(), s -> (CopycatByteBlock.Byte)s));

    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        this.registerForMultiState(entries, CCBlocks.COPYCAT_BYTE.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        CopycatByteBlock.Byte bite = byteMap.get(key);
        if (state.getValue(CopycatByteBlock.byByte(bite))) {
            int offsetX = bite.x() ? 8 : 0;
            int offsetY = bite.y() ? 8 : 0;
            int offsetZ = bite.z() ? 8 : 0;
            int joinedCull = joinedCull(state, bite);
            CopycatsDebug.log("model", () -> "byte emit key=" + key + " bite=" + bite + " material=" + material + " joinedCull=" + joinedCull);
            QuadAutoCull autoCull = CopycatRenderContext.autoCull(CopycatRenderContext.aabb(8.0, 8.0, 8.0).move(offsetX, offsetY, offsetZ));
            context.assemblePiece(
                AssemblyTransform.IDENTITY,
                CopycatRenderContext.vec3(offsetX, offsetY, offsetZ),
                CopycatRenderContext.aabb(4.0, 4.0, 4.0),
                CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.SOUTH | joinedCull),
                autoCull
            );
            context.assemblePiece(
                AssemblyTransform.IDENTITY,
                CopycatRenderContext.vec3(offsetX + 4, offsetY, offsetZ),
                CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 0.0, 0.0),
                CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.SOUTH | joinedCull),
                autoCull
            );
            context.assemblePiece(
                AssemblyTransform.IDENTITY,
                CopycatRenderContext.vec3(offsetX, offsetY, offsetZ + 4),
                CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(0.0, 0.0, 12.0),
                CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.NORTH | joinedCull),
                autoCull
            );
            context.assemblePiece(
                AssemblyTransform.IDENTITY,
                CopycatRenderContext.vec3(offsetX + 4, offsetY, offsetZ + 4),
                CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 0.0, 12.0),
                CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.NORTH | joinedCull),
                autoCull
            );
            context.assemblePiece(
                AssemblyTransform.IDENTITY,
                CopycatRenderContext.vec3(offsetX, offsetY + 4, offsetZ),
                CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(0.0, 12.0, 0.0),
                CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.SOUTH | joinedCull),
                autoCull
            );
            context.assemblePiece(
                AssemblyTransform.IDENTITY,
                CopycatRenderContext.vec3(offsetX + 4, offsetY + 4, offsetZ),
                CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 12.0, 0.0),
                CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.SOUTH | joinedCull),
                autoCull
            );
            context.assemblePiece(
                AssemblyTransform.IDENTITY,
                CopycatRenderContext.vec3(offsetX, offsetY + 4, offsetZ + 4),
                CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(0.0, 12.0, 12.0),
                CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.NORTH | joinedCull),
                autoCull
            );
            context.assemblePiece(
                AssemblyTransform.IDENTITY,
                CopycatRenderContext.vec3(offsetX + 4, offsetY + 4, offsetZ + 4),
                CopycatRenderContext.aabb(4.0, 4.0, 4.0).move(12.0, 12.0, 12.0),
                CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.NORTH | joinedCull),
                autoCull
            );
        }
    }

    private static int joinedCull(BlockState state, CopycatByteBlock.Byte bite) {
        int cull = 0;
        cull |= joinedCull(state, bite, Direction.EAST, !bite.x());
        cull |= joinedCull(state, bite, Direction.WEST, bite.x());
        cull |= joinedCull(state, bite, Direction.UP, !bite.y());
        cull |= joinedCull(state, bite, Direction.DOWN, bite.y());
        cull |= joinedCull(state, bite, Direction.SOUTH, !bite.z());
        return cull | joinedCull(state, bite, Direction.NORTH, bite.z());
    }

    private static int joinedCull(BlockState state, CopycatByteBlock.Byte bite, Direction direction, boolean hasInternalNeighbor) {
        if (!hasInternalNeighbor) {
            return 0;
        } else {
            CopycatByteBlock.Byte neighbor = bite.relative(direction);
            String neighborKey = CopycatByteBlock.byByte(neighbor).getName();
            return state.getValue(CopycatByteBlock.byByte(neighbor)) && CopycatRenderFlags.sameMaterialAsCurrent(neighborKey) ? mask(direction) : 0;
        }
    }

    private static int mask(Direction direction) {
        return switch (direction) {
            case DOWN -> MutableCullFace.DOWN;
            case UP -> MutableCullFace.UP;
            case NORTH -> MutableCullFace.NORTH;
            case SOUTH -> MutableCullFace.SOUTH;
            case WEST -> MutableCullFace.WEST;
            case EAST -> MutableCullFace.EAST;
        };
    }
}
