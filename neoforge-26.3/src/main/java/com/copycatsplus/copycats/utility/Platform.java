package com.copycatsplus.copycats.utility;

import java.util.function.Supplier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.ApiStatus.Internal;

public enum Platform {
    FORGE,
    FABRIC,
    NEOFORGE;

    public static final Platform CURRENT = getCurrent();

    public boolean isCurrent() {
        return this == CURRENT;
    }

    public void runIfCurrent(Supplier<Runnable> run) {
        if (this.isCurrent()) {
            run.get().run();
        }
    }

    @Internal
    public static Platform getCurrent() {
        return NEOFORGE;
    }

    public static enum Environment {
        CLIENT,
        SERVER;

        public static final Platform.Environment CURRENT = getCurrent();

        public boolean isCurrent() {
            return this == CURRENT;
        }

        public void runIfCurrent(Supplier<Runnable> run) {
            if (this.isCurrent()) {
                run.get().run();
            }
        }

        public <T> T getIfCurrent(Supplier<T> supplier) {
            return this.isCurrent() ? supplier.get() : null;
        }

        public <T> T getIfCurrent(Supplier<T> supplier, T defaultValue) {
            return this.isCurrent() ? supplier.get() : defaultValue;
        }

        @Internal
        public static Platform.Environment getCurrent() {
            return FMLEnvironment.getDist() == Dist.CLIENT ? CLIENT : SERVER;
        }
    }
}
