package com.copycatsplus.copycats.config;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class CClient extends ConfigBase {

    @Override
    public @NotNull String getName() {
        return "client";
    }

    public final ConfigBool useEnhancedModels = b(true, "useEnhancedModels", Comments.useEnhancedModels);
    public final ConfigBool disableGraphicsWarnings = b(false, "disableGraphicsWarnings", Comments.disableGraphicsWarnings);
    public final ConfigBool colorizeMultiStates = b(false, "colorizeMultiStates", Comments.colorizeMultiStates);

    private static class Comments {
        static String useEnhancedModels = "Use more complex copycat models to improve appearance with certain materials.";
        static String disableGraphicsWarnings = "Disable warnings about graphics settings that may cause issues with the mod.";
        static String colorizeMultiStates = "Colorize different parts of multi-state copycats to distinguish them visually.";
    }
}
