package com.copycatsplus.copycats.compat;

import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.BlockGetter;

import java.lang.reflect.Field;

public class SodiumCompat {
    public static BlockGetter unwrapSodiumLevel(BlockGetter level) {
        if (level instanceof LevelSlice slice) {
            return getPrivateField(slice);
        }
        return level;
    }

    private static ClientLevel getPrivateField(LevelSlice instance) {
        try {
            Field f = LevelSlice.class.getDeclaredField("level");
            f.setAccessible(true);
            return (ClientLevel) f.get(instance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access field", e);
        }
    }
}
