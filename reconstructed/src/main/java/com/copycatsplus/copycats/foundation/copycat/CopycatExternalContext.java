/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package com.copycatsplus.copycats.foundation.copycat;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class CopycatExternalContext {
    private static final ThreadLocal<String> propertyForBlockColor = new ThreadLocal();
    private static final ThreadLocal<String> propertyForAppearance = new ThreadLocal();
    private static final ThreadLocal<Boolean> forBlockingLogic = ThreadLocal.withInitial(() -> false);

    public static void setPropertyForBlockColor(String property) {
        propertyForBlockColor.set(property);
    }

    public static String getPropertyForBlockColor() {
        return propertyForBlockColor.get();
    }

    public static void setPropertyForAppearance(String property) {
        propertyForAppearance.set(property);
    }

    public static String getPropertyForAppearance() {
        return propertyForAppearance.get();
    }

    public static void setForBlockingLogic(boolean value) {
        forBlockingLogic.set(value);
    }

    public static boolean isForBlockingLogic() {
        return forBlockingLogic.get();
    }
}

