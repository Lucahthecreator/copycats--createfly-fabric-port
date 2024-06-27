package com.copycatsplus.copycats.content.copycat.fluid_pipe.forge;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.forge.CopycatModel;
import com.copycatsplus.copycats.content.copycat.base.model.forge.SimpleCopycatModel;
import com.copycatsplus.copycats.content.copycat.base.model.functional.forge.FunctionalCopycatRenderHelperImpl;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModel;
import com.jozufozu.flywheel.core.model.ModelUtil;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CopycatFluidPipeModelForge extends SimpleCopycatModel {

    private static final ModelProperty<CopycatFluidPipeModel.PipeModelData> PIPE_PROPERTY = new ModelProperty<>();

    public CopycatFluidPipeModelForge(BakedModel originalModel, SimpleCopycatPart part) {
        super(originalModel, part);
    }

    @Override
    public ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state,
                                             ModelData blockEntityData) {
        super.gatherModelData(builder, world, pos, state, blockEntityData);
        CopycatFluidPipeModel.PipeModelData data = new CopycatFluidPipeModel.PipeModelData();
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
        if (part instanceof SimpleCopycatPart.WithData<?>) {
            @SuppressWarnings("unchecked")
            SimpleCopycatPart.WithData<CopycatFluidPipeModel.PipeModelData> dataPart = (SimpleCopycatPart.WithData<CopycatFluidPipeModel.PipeModelData>) part;
            CopycatFluidPipeModel.PipeModelData pipeData = data.get(PIPE_PROPERTY);
            dataPart.acceptData(pipeData);
        }
    }
}
