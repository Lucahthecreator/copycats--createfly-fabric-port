package com.copycatsplus.copycats.content.copycat.fluid_pipe.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelPart;
import com.copycatsplus.copycats.content.copycat.base.model.fabric.SimpleCopycatModel;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModelPart;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CopycatFluidPipeModelFabric extends SimpleCopycatModel {

    public CopycatFluidPipeModelFabric(BakedModel originalModel, CopycatModelPart part) {
        super(originalModel, part);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void prepareCopycatPart(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext renderContext, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        CopycatFluidPipeModelPart.PipeModelData data = new CopycatFluidPipeModelPart.PipeModelData();
        BracketedBlockEntityBehaviour bracket = BlockEntityBehaviour.get(blockView, pos, BracketedBlockEntityBehaviour.TYPE);

        RenderAttachedBlockView attachmentView = (RenderAttachedBlockView) blockView;
        Object attachment = attachmentView.getBlockEntityRenderAttachment(pos);
        if (attachment instanceof FluidTransportBehaviour.AttachmentTypes[] attachments) {
            for (int i = 0; i < attachments.length; i++) {
                data.putAttachment(Iterate.directions[i], attachments[i]);
            }
        }

        if (bracket != null)
            data.putBracket(bracket.getBracket());

        data.setEncased(FluidPipeBlock.shouldDrawCasing(blockView, pos, state));

        if (part instanceof CopycatModelPart.WithData<?>) {
            @SuppressWarnings("unchecked")
            CopycatModelPart.WithData<CopycatFluidPipeModelPart.PipeModelData> dataPart = (CopycatModelPart.WithData<CopycatFluidPipeModelPart.PipeModelData>) part;
            dataPart.acceptData(data);
        }
    }
}
