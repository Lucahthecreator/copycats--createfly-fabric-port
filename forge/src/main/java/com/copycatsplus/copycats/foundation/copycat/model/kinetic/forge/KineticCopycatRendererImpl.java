package com.copycatsplus.copycats.foundation.copycat.model.kinetic.forge;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.WrappedRenderWorld;
import com.copycatsplus.copycats.utility.forge.ModelDataUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.render.ShadeSeparatingSuperByteBuffer;
import net.createmod.catnip.render.VirtualRenderHelper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.model.data.ModelData;


//TODO: Figure out the replacements for this
public class KineticCopycatRendererImpl {

    public static ShadeSeparatingSuperByteBuffer getCopycatBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        WrappedRenderWorld renderWorld = new WrappedRenderWorld(be);
        ModelData blockEntityData = ModelDataUtils.mergeData(
                ((BlockEntity) be).getModelData(),
                VirtualRenderHelper.VIRTUAL_DATA
        ).build();
        ModelData renderData = model.getModelData(renderWorld, be.getBlockPos(), be.getBlockState(), blockEntityData);
        ModelData.Builder builder = ModelData.builder();
        ModelDataUtils.copyModelData(renderData, builder);
        builder.with(VirtualRenderHelper.VIRTUAL_PROPERTY, true);

        return new BakedModelWithDataBuilder(model)
                .withRenderWorld(renderWorld)
                .withRenderPos(be.getBlockPos())
                .withReferenceState(be.getBlockState())
                .withPoseStack(ms)
                .withData(builder.build())
                .build();
    }
}
