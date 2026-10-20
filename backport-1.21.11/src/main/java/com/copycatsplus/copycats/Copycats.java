/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.data.DataGenerator$PackGenerator
 *  net.minecraft.resources.Identifier
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.copycatsplus.copycats;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCItems;
import com.copycatsplus.copycats.registrate.CopycatRegistrate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Copycats {
    public static final String MODID = "copycats";
    public static final String NAME = "Copycats";
    public static final int DATA_FIXER_VERSION = 1;
    public static final Logger LOGGER = LoggerFactory.getLogger((String)"Copycats+");
    private static final CopycatRegistrate REGISTRATE = CopycatRegistrate.create("copycats");

    public static void init() {
        CCBlocks.register();
        CCBlockEntityTypes.register();
        CCItems.register();
        Copycats.finalizeRegistrate();
    }

    public static void gatherData(DataGenerator.PackGenerator gen) {
    }

    public static CopycatRegistrate getRegistrate() {
        return REGISTRATE;
    }

    public static Identifier asResource(String path) {
        return Identifier.fromNamespaceAndPath((String)MODID, (String)path);
    }

    public static void finalizeRegistrate() {
        REGISTRATE.register();
    }
}

