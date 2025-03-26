package com.copycatsplus.copycats.foundation.copycat.model.kinetic.forge;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.NonInstancedRenderWorld;
import com.copycatsplus.copycats.utility.forge.ModelDataUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.ponder.render.VirtualRenderHelper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.model.data.ModelData;


public class KineticCopycatRendererImpl {

    public static SuperByteBuffer getCopycatBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        NonInstancedRenderWorld renderWorld = new NonInstancedRenderWorld(be);
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
