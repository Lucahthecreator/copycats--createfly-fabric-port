package com.copycatsplus.copycats.foundation.copycat.model.kinetic.neoforge;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.WrappedRenderWorld;
import com.copycatsplus.copycats.utility.neoforge.ModelDataUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.engine_room.flywheel.api.material.CardinalLightingMode;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.lib.material.LightShaders;
import dev.engine_room.flywheel.lib.material.SimpleMaterial;
import dev.engine_room.flywheel.lib.model.ModelUtil;
import dev.engine_room.flywheel.lib.model.baked.BakedModelBuilder;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.ponder.render.VirtualRenderHelper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.model.data.ModelData;


public class KineticCopycatRendererImpl {

    private static ModelData prepareModelData(BlockAndTintGetter renderWorld, BakedModel model, ICopycatBlockEntity be) {
        ModelData blockEntityData = ModelDataUtils.mergeData(
                ((BlockEntity) be).getModelData(),
                VirtualRenderHelper.VIRTUAL_DATA
        ).build();
        ModelData renderData = model.getModelData(renderWorld, be.getBlockPos(), be.getBlockState(), blockEntityData);
        ModelData.Builder builder = ModelData.builder();
        ModelDataUtils.copyModelData(renderData, builder);
        builder.with(VirtualRenderHelper.VIRTUAL_PROPERTY, true);
        return builder.build();
    }

    public static SuperByteBuffer renderBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        WrappedRenderWorld renderWorld = new WrappedRenderWorld(be);
        ModelData data = prepareModelData(renderWorld, model, be);

        return new BakedModelWithDataBuilder(model)
                .withRenderWorld(renderWorld)
                .withRenderPos(be.getBlockPos())
                .withReferenceState(be.getBlockState())
                .withPoseStack(ms)
                .withData(data)
                .build();
    }

    public static Model instancedModel(BakedModel model, ICopycatBlockEntity be) {
        WrappedRenderWorld renderWorld = new WrappedRenderWorld(be);
        ModelData data = prepareModelData(renderWorld, model, be);

        return new BakedModelBuilder(model)
                .level(renderWorld.withModelData(data))
                .pos(be.getBlockPos())
                .build();
    }
}
