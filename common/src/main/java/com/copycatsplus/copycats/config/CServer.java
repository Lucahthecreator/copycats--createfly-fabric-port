package com.copycatsplus.copycats.config;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class CServer extends ConfigBase {
    public final CStress stress;

    public CServer() {
        this.stress = this.nested(0, CStress::new, new String[]{Comments.stress});
    }

    public @NotNull String getName() {
        return "server";
    }

    private static class Comments {
        static String stress = "Fine tune the kinetic stats of individual components";
    }
}
