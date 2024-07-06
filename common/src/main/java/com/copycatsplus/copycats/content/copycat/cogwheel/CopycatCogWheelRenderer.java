package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.content.copycat.base.ICopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.model.kinetic.IKineticCopycatBlockRenderer;
import com.copycatsplus.copycats.content.copycat.base.model.kinetic.KineticCopycatRenderer;
import com.copycatsplus.copycats.content.copycat.partial.CopycatPartialModel;
import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatCogWheelRenderer extends BracketedKineticBlockEntityRenderer implements IKineticCopycatBlockRenderer {
    public CopycatCogWheelRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(BracketedKineticBlockEntity be, BlockState state) {
        return IKineticCopycatBlockRenderer.super.getRotatedModel(CopycatPartialModel.SHAFT, (ICopycatBlockEntity) be);
    }

    @Override
    protected void renderSafe(BracketedKineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (Backend.canUseInstancing(be.getLevel()))
            return;

        if (!AllBlocks.LARGE_COGWHEEL.has(be.getBlockState())) {
            super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
            BlockState state = getRenderedBlockState(be);
            RenderType type = getRenderType(be, state);
            if (type != null)
                renderRotatingBuffer(be, KineticCopycatRenderer.getBuffer(CopycatPartialModel.COGWHEEL, (ICopycatBlockEntity) be), ms, buffer.getBuffer(type), light);
            return;
        }
    }
}
