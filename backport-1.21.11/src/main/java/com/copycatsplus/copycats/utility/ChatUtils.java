/*
 * Decompiled with CFR 0.152.
 */
package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.utility.ClientUtils;
import com.copycatsplus.copycats.utility.Platform;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ChatUtils {
    public static Set<String> messages = new HashSet<String>();
    private static final Supplier<Boolean> disableWarnings = () -> false;

    public static void sendWarningOnce(String id, String message) {
        if (disableWarnings.get().booleanValue()) {
            return;
        }
        if (messages.contains(id)) {
            return;
        }
        messages.add(id);
        if (!Platform.Environment.CLIENT.isCurrent() || !ClientUtils.sendSystemMessage("Warning: " + message)) {
            Copycats.LOGGER.warn("Warning: {}", (Object)message);
        }
    }
}

