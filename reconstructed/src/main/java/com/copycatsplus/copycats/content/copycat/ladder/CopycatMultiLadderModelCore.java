/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.LadderBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
package com.copycatsplus.copycats.content.copycat.ladder;

import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderBlock;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import java.util.Objects;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatMultiLadderModelCore
extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (Objects.equals(key, CopycatLadderBlock.RAILS.getName()) && !((Boolean)state.getValue((Property)CopycatLadderBlock.RAILS)).booleanValue()) {
            return;
        }
        if (Objects.equals(key, CopycatLadderBlock.STEPS.getName()) && !((Boolean)state.getValue((Property)CopycatLadderBlock.STEPS)).booleanValue()) {
            return;
        }
        int rot = (int)((Direction)state.getValue((Property)LadderBlock.FACING)).toYRot();
        AssemblyTransform transform = t -> t.rotateY(rot);
        if (((Boolean)state.getValue((Property)CopycatLadderBlock.RAILS)).booleanValue()) {
            CopycatLadderModelCore.assemblePoles(context, transform);
        }
        if (((Boolean)state.getValue((Property)CopycatLadderBlock.STEPS)).booleanValue()) {
            CopycatLadderModelCore.assembleSteps(context, transform);
        }
    }
}

