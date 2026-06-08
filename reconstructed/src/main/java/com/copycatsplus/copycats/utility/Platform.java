/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.loader.api.FabricLoader
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package com.copycatsplus.copycats.utility;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;

public enum Platform {
    FORGE,
    FABRIC;

    public static final Platform CURRENT;

    public boolean isCurrent() {
        return this == CURRENT;
    }

    public void runIfCurrent(Supplier<Runnable> run) {
        if (this.isCurrent()) {
            run.get().run();
        }
    }

    @ApiStatus.Internal
    public static Platform getCurrent() {
        return FABRIC;
    }

    static {
        CURRENT = Platform.getCurrent();
    }

    public static enum Environment {
        CLIENT,
        SERVER;

        public static final Environment CURRENT;

        public boolean isCurrent() {
            return this == CURRENT;
        }

        public void runIfCurrent(Supplier<Runnable> run) {
            if (this.isCurrent()) {
                run.get().run();
            }
        }

        public <T> T getIfCurrent(Supplier<T> supplier) {
            return this.isCurrent() ? (T)supplier.get() : null;
        }

        public <T> T getIfCurrent(Supplier<T> supplier, T defaultValue) {
            return this.isCurrent() ? supplier.get() : defaultValue;
        }

        @ApiStatus.Internal
        public static Environment getCurrent() {
            return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? CLIENT : SERVER;
        }

        static {
            CURRENT = Environment.getCurrent();
        }
    }
}

