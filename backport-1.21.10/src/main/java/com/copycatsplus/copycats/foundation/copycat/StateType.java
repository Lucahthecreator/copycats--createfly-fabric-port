/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.Block
 */
package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.IStateType;
import net.minecraft.world.level.block.Block;

@Deprecated(since="1.4")
public enum StateType {
    SINGULAR,
    MULTI;


    public static StateType getTypeFromBlock(Block block) {
        if (block instanceof IStateType) {
            IStateType stateType = (IStateType)block;
            return stateType.stateType();
        }
        return SINGULAR;
    }
}

