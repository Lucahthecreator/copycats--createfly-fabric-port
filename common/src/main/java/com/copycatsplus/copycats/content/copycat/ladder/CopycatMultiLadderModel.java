package com.copycatsplus.copycats.content.copycat.ladder;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderBlock.RAILS;
import static com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderBlock.STEPS;

public class CopycatMultiLadderModel implements SimpleMultiStateCopycatPart {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        if (Objects.equals(key, RAILS.getName()) && !state.getValue(RAILS))
            return;
        if (Objects.equals(key, STEPS.getName()) && !state.getValue(STEPS))
            return;

        int rot = (int) state.getValue(LadderBlock.FACING).toYRot();
        GlobalTransform transform = t -> t.rotateY(rot);
        if (state.getValue(RAILS)) {
            CopycatLadderModel.assemblePoles(context, transform);
        }

        if (state.getValue(STEPS)) {
            CopycatLadderModel.assembleSteps(context, transform);
        }
    }
}
