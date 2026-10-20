/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.api.behaviour.BlockEntityBehaviour
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.copycatsplus.copycats.content.copycat.ladder;

import com.copycatsplus.copycats.foundation.copycat.multistate.MultiStateCopycatBlockEntity;
import com.zurrtum.create.api.behaviour.BlockEntityBehaviour;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MultiStateCopycatLadderBlockEntity
extends MultiStateCopycatBlockEntity {
    public MultiStateCopycatLadderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour<?>> behaviours) {
        super.addBehaviours(behaviours);
    }
}

