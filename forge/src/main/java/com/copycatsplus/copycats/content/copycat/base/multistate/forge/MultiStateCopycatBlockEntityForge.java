package com.copycatsplus.copycats.content.copycat.base.multistate.forge;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static com.copycatsplus.copycats.content.copycat.base.model.forge.CopycatModelForge.MATERIALS_PROPERTY;

public class MultiStateCopycatBlockEntityForge extends MultiStateCopycatBlockEntity {
    public MultiStateCopycatBlockEntityForge(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder()
                .with(MATERIALS_PROPERTY, Collections.synchronizedMap(getMaterialItemStorage().getMaterialMap()))
                .build();
    }
}
