package com.copycatsplus.copycats.content.copycat.base.fabric;

import com.copycatsplus.copycats.content.copycat.base.CCCopycatBlockEntity;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class CCCopycatBlockEntityFabric extends CCCopycatBlockEntity implements RenderAttachmentBlockEntity {
    public CCCopycatBlockEntityFabric(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public @Nullable Object getRenderAttachmentData() {
        return material;
    }
}
