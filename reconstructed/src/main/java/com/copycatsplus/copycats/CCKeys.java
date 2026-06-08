/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.KeyMapping
 */
package com.copycatsplus.copycats;

import net.minecraft.client.KeyMapping;

public enum CCKeys {
    FILL_COPYCAT("fill_copycat", 342);

    public KeyMapping keybind;
    public final String description;
    public final int key;
    public final boolean modifiable;

    private CCKeys(String description, int defaultKey) {
        this.description = "keyinfo.copycats." + description;
        this.key = defaultKey;
        this.modifiable = !description.isEmpty();
    }

    public KeyMapping getKeybind() {
        return this.keybind;
    }

    public boolean isPressed() {
        return false;
    }

    public String getBoundKey() {
        return "LEFT ALT";
    }

    public static boolean isKeyDown(int key) {
        return false;
    }

    public static boolean isMouseButtonDown(int button) {
        return false;
    }

    public static void register() {
    }

    public static boolean ctrlDown() {
        return false;
    }

    public static boolean shiftDown() {
        return false;
    }

    public static boolean altDown() {
        return false;
    }
}

