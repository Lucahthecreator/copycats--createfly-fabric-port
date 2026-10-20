package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.Copycats;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ChatUtils {
    public static Set<String> messages = new HashSet<>();
    private static final Supplier<Boolean> disableWarnings = () -> false;

    public static void sendWarningOnce(String id, String message) {
        if (!disableWarnings.get()) {
            if (!messages.contains(id)) {
                messages.add(id);
                if (!Platform.Environment.CLIENT.isCurrent() || !ClientUtils.sendSystemMessage("Warning: " + message)) {
                    Copycats.LOGGER.warn("Warning: {}", message);
                }
            }
        }
    }
}
