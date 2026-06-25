package com.copycatsplus.copycats.compat.debug;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public final class CopycatsDebug {
    private static final long RELOAD_INTERVAL_MS = 2000;
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir()
            .resolve("copycats-debug.properties");
    private static final ConcurrentHashMap<String, AtomicInteger> COUNTS = new ConcurrentHashMap<>();

    private static volatile Properties config = defaults();
    private static volatile long lastCheck;
    private static volatile long lastModified = -1;
    private static volatile boolean reportedConfig;

    private CopycatsDebug() {
    }

    public static boolean enabled(String category) {
        reloadIfNeeded();
        String key = normalize(category);
        return Boolean.getBoolean("copycats.debug")
                || Boolean.getBoolean("copycats.debug." + key)
                || environmentEnabled("COPYCATS_DEBUG")
                || environmentEnabled("COPYCATS_DEBUG_" + key.toUpperCase(Locale.ROOT))
                || (Boolean.parseBoolean(config.getProperty("enabled", "false"))
                && Boolean.parseBoolean(config.getProperty(key, "false")));
    }

    public static boolean anyEnabled(String... categories) {
        for (String category : categories) {
            if (enabled(category)) {
                return true;
            }
        }
        return false;
    }

    public static void log(String category, Supplier<String> message) {
        if (!enabled(category)) {
            return;
        }

        String key = normalize(category);
        int limit = parseInt(config.getProperty("limit", "2000"), 2000);
        int line = COUNTS.computeIfAbsent(key, ignored -> new AtomicInteger()).incrementAndGet();
        if (line <= limit) {
            System.out.println("[Copycats Debug/" + key + "] " + message.get());
        } else if (line == limit + 1) {
            System.out.println("[Copycats Debug/" + key + "] Line limit reached; increase 'limit' in " + CONFIG_PATH);
        }
    }

    public static Path configPath() {
        return CONFIG_PATH;
    }

    private static void reloadIfNeeded() {
        long now = System.currentTimeMillis();
        if (now - lastCheck < RELOAD_INTERVAL_MS) {
            return;
        }

        synchronized (CopycatsDebug.class) {
            if (now - lastCheck < RELOAD_INTERVAL_MS) {
                return;
            }
            lastCheck = now;
            try {
                if (Files.notExists(CONFIG_PATH)) {
                    Files.createDirectories(CONFIG_PATH.getParent());
                    try (OutputStream output = Files.newOutputStream(CONFIG_PATH)) {
                        defaults().store(output, "Copycats CreateFly diagnostics (reloads while the game is running)");
                    }
                }

                long modified = Files.getLastModifiedTime(CONFIG_PATH).toMillis();
                if (modified == lastModified) {
                    return;
                }

                Properties loaded = defaults();
                try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
                    loaded.load(input);
                }
                config = loaded;
                lastModified = modified;
                COUNTS.clear();
                reportConfig(loaded);
            } catch (IOException exception) {
                System.err.println("[Copycats Debug] Could not load " + CONFIG_PATH + ": " + exception.getMessage());
            }
        }
    }

    private static void reportConfig(Properties loaded) {
        if (!reportedConfig && !Boolean.parseBoolean(loaded.getProperty("enabled", "false"))
                && !Boolean.parseBoolean(loaded.getProperty("config", "false"))) {
            return;
        }
        reportedConfig = true;
        System.out.println("[Copycats Debug/config] loaded " + CONFIG_PATH
                + " enabled=" + loaded.getProperty("enabled", "false")
                + " ct=" + loaded.getProperty("ct", "false")
                + " door=" + loaded.getProperty("door", "false")
                + " material=" + loaded.getProperty("material", "false")
                + " shape=" + loaded.getProperty("shape", "false")
                + " filter=" + loaded.getProperty("filter", "false")
                + " face_hiding=" + loaded.getProperty("face_hiding", "false")
                + " render=" + loaded.getProperty("render", "false")
                + " model=" + loaded.getProperty("model", "false")
                + " blocking=" + loaded.getProperty("blocking", "false")
                + " light=" + loaded.getProperty("light", "false")
                + " slab=" + loaded.getProperty("slab", "false")
                + " byte=" + loaded.getProperty("byte", "false")
                + " byte_panel=" + loaded.getProperty("byte_panel", "false")
                + " full_block=" + loaded.getProperty("full_block", "false")
                + " generic=" + loaded.getProperty("generic", "false")
                + " limit=" + loaded.getProperty("limit", "2000"));
    }

    private static Properties defaults() {
        Properties defaults = new Properties();
        defaults.setProperty("enabled", "false");
        defaults.setProperty("ct", "false");
        defaults.setProperty("door", "false");
        defaults.setProperty("material", "false");
        defaults.setProperty("shape", "false");
        defaults.setProperty("filter", "false");
        defaults.setProperty("face_hiding", "false");
        defaults.setProperty("render", "false");
        defaults.setProperty("model", "false");
        defaults.setProperty("blocking", "false");
        defaults.setProperty("light", "false");
        defaults.setProperty("slab", "false");
        defaults.setProperty("byte", "false");
        defaults.setProperty("byte_panel", "false");
        defaults.setProperty("full_block", "false");
        defaults.setProperty("generic", "false");
        defaults.setProperty("config", "false");
        defaults.setProperty("limit", "2000");
        return defaults;
    }

    private static boolean environmentEnabled(String name) {
        return Boolean.parseBoolean(System.getenv(name));
    }

    private static String normalize(String category) {
        return category.toLowerCase(Locale.ROOT).replace('-', '_');
    }

    private static int parseInt(String value, int fallback) {
        try {
            return Math.max(1, Integer.parseInt(value));
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }
}
