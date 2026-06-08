/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ClientModInitializer
 */
package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.CopycatsClient;
import net.fabricmc.api.ClientModInitializer;

public class CopycatsClientImpl
implements ClientModInitializer {
    public void onInitializeClient() {
        CopycatsClient.init();
    }
}

