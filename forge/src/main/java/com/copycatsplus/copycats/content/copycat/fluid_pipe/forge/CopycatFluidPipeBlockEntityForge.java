package com.copycatsplus.copycats.content.copycat.fluid_pipe.forge;

import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;


public class CopycatFluidPipeBlockEntityForge extends CopycatFluidPipeBlockEntity {
    public CopycatFluidPipeBlockEntityForge(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public @NotNull ModelData getModelData() {
        return getCopycatBlockEntity().getModelData();
    }
}
