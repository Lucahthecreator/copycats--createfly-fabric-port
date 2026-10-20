package com.copycatsplus.copycats;

import com.copycatsplus.copycats.registrate.CopycatRegistrate;
import net.minecraft.data.DataGenerator.PackGenerator;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Copycats {
    public static final String MODID = "copycats";
    public static final String NAME = "Copycats";
    public static final int DATA_FIXER_VERSION = 1;
    public static final Logger LOGGER = LoggerFactory.getLogger("Copycats+");
    private static final CopycatRegistrate REGISTRATE = CopycatRegistrate.create("copycats");

    public static void init() {
        CCBlocks.register();
        CCBlockEntityTypes.register();
        CCItems.register();
        finalizeRegistrate();
    }

    public static void gatherData(PackGenerator gen) {
    }

    public static CopycatRegistrate getRegistrate() {
        return REGISTRATE;
    }

    public static Identifier asResource(String path) {
        return Identifier.fromNamespaceAndPath("copycats", path);
    }

    public static void finalizeRegistrate() {
        REGISTRATE.register();
    }
}
