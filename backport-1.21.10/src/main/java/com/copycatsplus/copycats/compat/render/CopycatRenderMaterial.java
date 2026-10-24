package com.copycatsplus.copycats.compat.render;

import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.zurrtum.create.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;

public final class CopycatRenderMaterial {
    private CopycatRenderMaterial() {
    }

    public static BlockState resolve(BlockGetter level, BlockPos pos, BlockState state) {
        BlockState material;
        String property = null;
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycat) {
            property = getProperty(level, pos, state, copycat);
            material = IMultiStateCopycatBlock.getMaterial(level, pos, property);
        } else if (state.getBlock() instanceof ICopycatBlock) {
            material = ICopycatBlock.getMaterial(level, pos);
            if ((material.is(Blocks.AIR) || material.getBlock() == AllBlocks.COPYCAT_BASE)
                    && state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
                DoubleBlockHalf half = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
                BlockPos partnerPos = half == DoubleBlockHalf.UPPER ? pos.below() : pos.above();
                BlockState partnerState = level.getBlockState(partnerPos);
                if (partnerState.getBlock() == state.getBlock()) {
                    BlockState partnerMaterial = ICopycatBlock.getMaterial(level, partnerPos);
                    if (!partnerMaterial.is(Blocks.AIR) && partnerMaterial.getBlock() != AllBlocks.COPYCAT_BASE) {
                        material = partnerMaterial;
                    }
                }
            }
        } else {
            return state;
        }
        BlockState resolved = material.is(Blocks.AIR) ? AllBlocks.COPYCAT_BASE.defaultBlockState() : material;
        String selectedProperty = property;
        BlockState selectedMaterial = material;
        CopycatsDebug.log("material", () -> "resolve pos=" + pos + " state=" + state
                + " property=" + selectedProperty + " material=" + selectedMaterial + " resolved=" + resolved
                + " reader=" + level.getClass().getSimpleName());
        return resolved;
    }

    public static BlockState resolveForConnectedTexture(BlockGetter level, BlockPos fromPos, BlockPos toPos,
                                                        BlockState fromState, BlockState state,
                                                        BlockState reference, Direction renderedFace) {
        Direction sourceFace = getSourceFace(fromPos, toPos, renderedFace);
        Direction targetFace = getTargetFace(fromPos, toPos, renderedFace);
        Map<Object, BlockState> sourceMaterials = getContactMaterials(level, fromPos, fromState, sourceFace);
        Map<Object, BlockState> targetMaterials = getContactMaterials(level, toPos, state, targetFace);
        CopycatsDebug.log("material", () -> "CT lookup from=" + fromPos + " " + fromState
                + " to=" + toPos + " " + state + " reference=" + reference
                + " renderedFace=" + renderedFace + " sourceFace=" + sourceFace
                + " targetFace=" + targetFace + " sourceMaterials=" + sourceMaterials.values()
                + " targetMaterials=" + targetMaterials.values());

        BlockState sharedMaterial = selectSharedMaterial(sourceMaterials, targetMaterials, reference);
        if (sharedMaterial != null) {
            CopycatsDebug.log("material", () -> "CT selected shared material=" + sharedMaterial);
            return sharedMaterial;
        }

        CopycatsDebug.log("material", () -> "CT contact materials differ; using default resolution for " + state);
        return resolve(level, toPos, state);
    }

    public static boolean hasSharedContactMaterial(BlockGetter level, BlockPos fromPos, BlockPos toPos,
                                                   BlockState fromState, BlockState state,
                                                   BlockState reference, Direction renderedFace) {
        Direction sourceFace = getSourceFace(fromPos, toPos, renderedFace);
        Direction targetFace = getTargetFace(fromPos, toPos, renderedFace);
        Map<Object, BlockState> sourceMaterials = getContactMaterials(level, fromPos, fromState, sourceFace);
        Map<Object, BlockState> targetMaterials = getContactMaterials(level, toPos, state, targetFace);
        boolean result = selectSharedMaterial(sourceMaterials, targetMaterials, reference) != null;
        CopycatsDebug.log("material", () -> "CT shared material from=" + fromPos + " " + fromState
                + " to=" + toPos + " " + state + " reference=" + reference
                + " sourceFace=" + sourceFace + " targetFace=" + targetFace
                + " sourceMaterials=" + sourceMaterials.values()
                + " targetMaterials=" + targetMaterials.values()
                + " result=" + result);
        return result;
    }

    private static Map<Object, BlockState> getContactMaterials(BlockGetter level, BlockPos pos,
                                                                BlockState state, Direction face) {
        Map<Object, BlockState> materials = new LinkedHashMap<>();
        CopycatsDebug.log("material", () -> "contact scan pos=" + pos + " state=" + state
                + " face=" + face + " scaledReader=" + (level instanceof ScaledBlockAndTintGetter)
                + " reader=" + level.getClass().getName());
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycat
                && !(level instanceof ScaledBlockAndTintGetter)) {
            for (String property : copycat.storageProperties()) {
                boolean exists = copycat.partExists(state, property);
                boolean faceEmpty = !exists || copycat.getPartialFaceShape(level, state, property, face).isEmpty();
                CopycatsDebug.log("material", () -> "contact property pos=" + pos
                        + " property=" + property + " exists=" + exists
                        + " faceEmpty=" + faceEmpty + " face=" + face);
                if (!exists || faceEmpty) {
                    continue;
                }
                BlockState material = IMultiStateCopycatBlock.getMaterial(level, pos, property);
                CopycatsDebug.log("material", () -> "contact material pos=" + pos
                        + " property=" + property + " material=" + material);
                materials.put(material.getBlock(), material);
            }
        } else {
            BlockState material = resolve(level, pos, state);
            materials.put(material.getBlock(), material);
        }
        CopycatsDebug.log("material", () -> "contact result pos=" + pos + " face=" + face
                + " materials=" + materials.values());
        return materials;
    }

    public static BlockState resolveForFace(BlockGetter level, BlockPos pos, BlockState state, Direction face) {
        Map<Object, BlockState> materials = getContactMaterials(level, pos, state, face);
        if (materials.size() == 1) {
            BlockState selected = materials.values().iterator().next();
            CopycatsDebug.log("material", () -> "face material selected pos=" + pos
                    + " face=" + face + " material=" + selected);
            return selected;
        }
        BlockState fallback = resolve(level, pos, state);
        CopycatsDebug.log("material", () -> "face material fallback pos=" + pos
                + " face=" + face + " count=" + materials.size() + " fallback=" + fallback);
        return fallback;
    }

    private static Direction getSourceFace(BlockPos fromPos, BlockPos toPos, Direction renderedFace) {
        int dx = toPos.getX() - fromPos.getX();
        int dy = toPos.getY() - fromPos.getY();
        int dz = toPos.getZ() - fromPos.getZ();
        int changedAxes = (dx == 0 ? 0 : 1) + (dy == 0 ? 0 : 1) + (dz == 0 ? 0 : 1);
        if (changedAxes == 1 && Math.abs(dx + dy + dz) == 1) {
            return Direction.getApproximateNearest(dx, dy, dz);
        }
        return renderedFace;
    }

    private static Direction getTargetFace(BlockPos fromPos, BlockPos toPos, Direction renderedFace) {
        int dx = toPos.getX() - fromPos.getX();
        int dy = toPos.getY() - fromPos.getY();
        int dz = toPos.getZ() - fromPos.getZ();
        int changedAxes = (dx == 0 ? 0 : 1) + (dy == 0 ? 0 : 1) + (dz == 0 ? 0 : 1);
        if (changedAxes == 1 && Math.abs(dx + dy + dz) == 1) {
            return Direction.getApproximateNearest(dx, dy, dz).getOpposite();
        }
        return renderedFace;
    }

    private static BlockState selectSharedMaterial(Map<Object, BlockState> sourceMaterials,
                                                   Map<Object, BlockState> targetMaterials,
                                                   BlockState reference) {
        if (reference != null && usableMaterial(reference)) {
            BlockState targetMaterial = targetMaterials.get(reference.getBlock());
            if (targetMaterial != null && usableMaterial(targetMaterial)) {
                return targetMaterial;
            }
        }
        for (BlockState sourceMaterial : sourceMaterials.values()) {
            if (!usableMaterial(sourceMaterial)) {
                continue;
            }
            BlockState targetMaterial = targetMaterials.get(sourceMaterial.getBlock());
            if (targetMaterial != null && usableMaterial(targetMaterial)) {
                return targetMaterial;
            }
        }
        return null;
    }

    public static boolean isCTEnabled(BlockGetter level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycat) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof IMultiStateCopycatBlockEntity copycatEntity) {
                var item = copycatEntity.getMaterialItemStorage().getMaterialItem(getProperty(level, pos, state, copycat));
                boolean enabled = item == null || item.enableCT();
                CopycatsDebug.log("material", () -> "CT enabled pos=" + pos + " state=" + state
                        + " multistate=true enabled=" + enabled);
                return enabled;
            }
        }
        if (state.getBlock() instanceof ICopycatBlock copycat) {
            boolean enabled = copycat.isCTEnabled(state, level, pos);
            CopycatsDebug.log("material", () -> "CT enabled pos=" + pos + " state=" + state
                    + " multistate=false enabled=" + enabled);
            return enabled;
        }
        return true;
    }

    private static String getProperty(BlockGetter level, BlockPos pos, BlockState state, IMultiStateCopycatBlock copycat) {
        if (level instanceof ScaledBlockAndTintGetter scaledLevel) {
            return scaledLevel.getPropertyForRender(state, pos);
        }
        return copycat.defaultProperty();
    }

    private static boolean usableMaterial(BlockState state) {
        return state != null
                && !state.is(Blocks.AIR)
                && state.getBlock() != AllBlocks.COPYCAT_BASE;
    }
}
