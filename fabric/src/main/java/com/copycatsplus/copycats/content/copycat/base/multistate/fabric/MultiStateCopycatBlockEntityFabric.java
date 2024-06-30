package com.copycatsplus.copycats.content.copycat.base.multistate.fabric;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class MultiStateCopycatBlockEntityFabric extends MultiStateCopycatBlockEntity implements RenderAttachmentBlockEntity {
    public MultiStateCopycatBlockEntityFabric(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public @Nullable Map<String, BlockState> getRenderAttachmentData() {
        return Collections.synchronizedMap(getMaterialItemStorage().getMaterialMap());
    }
}
