/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.client.foundation.virtualWorld.VirtualRenderWorld
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.level.Level
 */
package com.copycatsplus.copycats.utility;

import com.zurrtum.create.client.foundation.virtualWorld.VirtualRenderWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class ClientUtils {
    public static boolean sendSystemMessage(String message) {
        if (Minecraft.getInstance().player == null) {
            return false;
        }
        Minecraft.getInstance().player.sendSystemMessage((Component)Component.literal((String)message));
        return true;
    }

    public static boolean isVirtualRenderWorld(Level level) {
        return level instanceof VirtualRenderWorld;
    }
}

