package com.copycatsplus.copycats.foundation.copycat;

/** Short-lived break context shared between an animated door and its block entity. */
public final class AnimatedDoorBreakContext {
    private static final ThreadLocal<Boolean> PRESERVE_MATERIALS = ThreadLocal.withInitial(() -> false);

    private AnimatedDoorBreakContext() {
    }

    public static boolean preserveMaterials() {
        return PRESERVE_MATERIALS.get();
    }

    public static void preserveMaterials(boolean preserve) {
        PRESERVE_MATERIALS.set(preserve);
    }
}
