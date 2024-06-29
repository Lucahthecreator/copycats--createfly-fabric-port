package com.copycatsplus.copycats.content.copycat.fluid_pipe.forge;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelPart;
import com.copycatsplus.copycats.content.copycat.base.model.forge.SimpleCopycatModel;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModelPart;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class CopycatFluidPipeModelForge extends SimpleCopycatModel {

    private static final ModelProperty<CopycatFluidPipeModelPart.PipeModelData> PIPE_PROPERTY = new ModelProperty<>();

    public CopycatFluidPipeModelForge(BakedModel originalModel, CopycatModelPart part) {
        super(originalModel, part);
    }

    @Override
    public ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state,
                                             ModelData blockEntityData) {
        super.gatherModelData(builder, world, pos, state, blockEntityData);
        CopycatFluidPipeModelPart.PipeModelData data = new CopycatFluidPipeModelPart.PipeModelData();
        FluidTransportBehaviour transport = BlockEntityBehaviour.get(world, pos, FluidTransportBehaviour.TYPE);
        BracketedBlockEntityBehaviour bracket = BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);

        if (transport != null)
            for (Direction d : Iterate.directions)
                data.putAttachment(d, transport.getRenderedRimAttachment(world, pos, state, d));
        if (bracket != null)
            data.putBracket(bracket.getBracket());

        data.setEncased(FluidPipeBlock.shouldDrawCasing(world, pos, state));
        return builder.with(PIPE_PROPERTY, data);
    }

    @Override
    protected void prepareCopycatPart(BlockState state, ModelData data, Direction side, RandomSource rand, BlockState material, ModelData wrappedData, RenderType renderType) {
        if (part instanceof CopycatModelPart.WithData<?>) {
            @SuppressWarnings("unchecked")
            CopycatModelPart.WithData<CopycatFluidPipeModelPart.PipeModelData> dataPart = (CopycatModelPart.WithData<CopycatFluidPipeModelPart.PipeModelData>) part;
            CopycatFluidPipeModelPart.PipeModelData pipeData = data.get(PIPE_PROPERTY);
            dataPart.acceptData(pipeData);
        }
    }
}
