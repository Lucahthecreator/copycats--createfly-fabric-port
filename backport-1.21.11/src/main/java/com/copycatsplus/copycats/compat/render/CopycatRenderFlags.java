package com.copycatsplus.copycats.compat.render;

import com.zurrtum.create.AllBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public final class CopycatRenderFlags {
    private static final ThreadLocal<Boolean> HIDE_DOUBLE_SLAB_SEAM =
            ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Map<String, BlockState>> MATERIALS =
            ThreadLocal.withInitial(Map::of);
    private static final ThreadLocal<String> CURRENT_KEY = new ThreadLocal<>();

    private CopycatRenderFlags() {
    }

    public static boolean hideDoubleSlabSeam() {
        return HIDE_DOUBLE_SLAB_SEAM.get();
    }

    public static void setHideDoubleSlabSeam(boolean hide) {
        HIDE_DOUBLE_SLAB_SEAM.set(hide);
    }

    public static void setRenderContext(boolean hideDoubleSlabSeam, Map<String, BlockState> materials, String currentKey) {
        HIDE_DOUBLE_SLAB_SEAM.set(hideDoubleSlabSeam);
        MATERIALS.set(materials);
        CURRENT_KEY.set(currentKey);
    }

    public static boolean sameMaterial(String firstKey, String secondKey) {
        Map<String, BlockState> materials = MATERIALS.get();
        return sameMaterial(materials.get(firstKey), materials.get(secondKey));
    }

    public static boolean sameMaterialAsCurrent(String otherKey) {
        String currentKey = CURRENT_KEY.get();
        return currentKey != null && sameMaterial(currentKey, otherKey);
    }

    public static boolean sameMaterial(BlockState first, BlockState second) {
        return usableMaterial(first)
                && usableMaterial(second)
                && first.equals(second);
    }

    public static void clear() {
        HIDE_DOUBLE_SLAB_SEAM.remove();
        MATERIALS.remove();
        CURRENT_KEY.remove();
    }

    private static boolean usableMaterial(BlockState state) {
        return state != null
                && !state.is(Blocks.AIR)
                && state.getBlock() != AllBlocks.COPYCAT_BASE;
    }
}
