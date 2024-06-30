package com.copycatsplus.copycats.content.copycat.base.forge;

import com.copycatsplus.copycats.content.copycat.base.CCCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.model.forge.CopycatModelForge;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class CCCopycatBlockEntityForge extends CCCopycatBlockEntity {
    public CCCopycatBlockEntityForge(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void redraw() {
        if (!isVirtual())
            requestModelDataUpdate();
        super.redraw();
    }

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder()
                .with(CopycatModelForge.MATERIAL_PROPERTY, material)
                .build();
    }
}
