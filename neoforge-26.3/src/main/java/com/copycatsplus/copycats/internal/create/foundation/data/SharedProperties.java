package com.copycatsplus.copycats.internal.create.foundation.data;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SharedProperties {
    public static Properties softMetal() {
        return Properties.ofFullCopy(Blocks.GOLD_BLOCK);
    }
}
