package com.copycatsplus.copycats.content.copycat.fluid_pipe.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.fabric.SimpleCopycatModel;
import com.copycatsplus.copycats.content.copycat.base.model.functional.fabric.WorldWithRenderData;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModel;
import com.jozufozu.flywheel.core.virtual.VirtualEmptyBlockGetter;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import io.github.fabricators_of_create.porting_lib.models.CustomParticleIconModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CopycatFluidPipeModelFabric extends SimpleCopycatModel {

    public CopycatFluidPipeModelFabric(BakedModel originalModel, SimpleCopycatPart part) {
        super(originalModel, part);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void prepareCopycatPart(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext renderContext, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        CopycatFluidPipeModel.PipeModelData data = new CopycatFluidPipeModel.PipeModelData();
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

        if (part instanceof SimpleCopycatPart.WithData<?>) {
            @SuppressWarnings("unchecked")
            SimpleCopycatPart.WithData<CopycatFluidPipeModel.PipeModelData> dataPart = (SimpleCopycatPart.WithData<CopycatFluidPipeModel.PipeModelData>) part;
            dataPart.acceptData(data);
        }
    }
}
