package com.copycatsplus.copycats.foundation.copycat;

import net.minecraft.world.level.block.Block;

@Deprecated(since = "1.4")
public enum StateType {
    SINGULAR,
    MULTI;

    public static StateType getTypeFromBlock(Block block) {
        return block instanceof IStateType stateType ? stateType.stateType() : SINGULAR;
    }
}
