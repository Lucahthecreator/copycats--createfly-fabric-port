package com.copycatsplus.copycats.content.copycat.ladder;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import java.util.Objects;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatMultiLadderModelCore extends CopycatModelCore {
    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (!Objects.equals(key, CopycatLadderBlock.RAILS.getName()) || state.getValue(CopycatLadderBlock.RAILS)) {
            if (!Objects.equals(key, CopycatLadderBlock.STEPS.getName()) || state.getValue(CopycatLadderBlock.STEPS)) {
                int rot = (int)state.getValue(LadderBlock.FACING).toYRot();
                AssemblyTransform transform = t -> t.rotateY(rot);
                if (state.getValue(CopycatLadderBlock.RAILS)) {
                    CopycatLadderModelCore.assemblePoles(context, transform);
                }

                if (state.getValue(CopycatLadderBlock.STEPS)) {
                    CopycatLadderModelCore.assembleSteps(context, transform);
                }
            }
        }
    }
}
