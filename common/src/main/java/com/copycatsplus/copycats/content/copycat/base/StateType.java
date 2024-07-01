package com.copycatsplus.copycats.content.copycat.base;

import net.minecraft.world.level.block.Block;

/**
 * Represents the type of a copycat.
 *
 * @deprecated To identify copycats, check that the block class is an instanceof {@link ICopycatBlock}. To identify multi-state copycats, check that the block class is an instanceof {@link com.copycatsplus.copycats.content.copycat.base.multistate.IMultiStateCopycatBlock}.
 */
@Deprecated(since = "1.4")
public enum StateType {
    SINGULAR,
    MULTI;

    StateType getTypeFromBlock(Block block) {
        if (block instanceof IStateType stateType) {
            return stateType.stateType();
        } else {
            return SINGULAR;
        }
    }
}
