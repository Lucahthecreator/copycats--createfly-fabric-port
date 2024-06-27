package com.copycatsplus.copycats.content.copycat.fluid_pipe.fabric;

import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatStraightPipeBlockEntity;
import com.simibubi.create.foundation.utility.Pair;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class CopycatStraightPipeBlockEntityFabric extends CopycatStraightPipeBlockEntity implements RenderAttachmentBlockEntity {
    public CopycatStraightPipeBlockEntityFabric(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public Object getRenderAttachmentData() {
        return Pair.of(super.getRenderAttachmentData(), getMaterial());
    }
}
