/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.zurrtum.create.client.catnip.render.SuperBufferFactory
 *  com.zurrtum.create.client.catnip.render.SuperByteBuffer
 *  com.zurrtum.create.client.catnip.render.SuperByteBufferRenderState
 *  com.zurrtum.create.client.content.kinetics.base.KineticBlockEntityRenderer
 *  com.zurrtum.create.client.foundation.blockEntity.renderer.SmartBlockEntityRenderer
 *  com.zurrtum.create.content.kinetics.base.KineticBlockEntity
 *  com.zurrtum.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.OrderedSubmitNodeCollector
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.client.renderer.block.dispatch.BlockStateModel
 *  net.minecraft.client.renderer.block.dispatch.BlockStateModelPart
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
 *  net.minecraft.client.renderer.feature.ModelFeatureRenderer$CrumblingOverlay
 *  net.minecraft.client.renderer.state.level.CameraRenderState
 *  net.minecraft.client.resources.model.sprite.Material$Baked
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 */
package com.copycatsplus.copycats.foundation.copycat.model;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlockEntity;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelVisuals;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.CreateFlyCopycatModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.zurrtum.create.client.catnip.render.SuperBufferFactory;
import com.zurrtum.create.client.catnip.render.SuperByteBuffer;
import com.zurrtum.create.client.catnip.render.SuperByteBufferRenderState;
import com.zurrtum.create.client.content.kinetics.base.KineticBlockEntityRenderer;
import com.zurrtum.create.client.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.zurrtum.create.content.kinetics.base.KineticBlockEntity;
import com.zurrtum.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CopycatKineticRenderBridge
implements BlockEntityRenderer<BracketedKineticBlockEntity, CopycatKineticRenderBridge.RenderState> {
    public CopycatKineticRenderBridge(BlockEntityRendererProvider.Context context) {
    }

    public RenderState createRenderState() {
        return new RenderState();
    }

    public void extractRenderState(BracketedKineticBlockEntity blockEntity, RenderState renderState, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        Level level = SmartBlockEntityRenderer.extractBase((BlockEntity)blockEntity, (BlockEntityRenderState)renderState, (ModelFeatureRenderer.CrumblingOverlay)breakProgress);
        renderState.models.clear();
        if (blockEntity instanceof CopycatCogWheelBlockEntity cogwheel
                && CopycatCogWheelVisuals.shouldUseVisualization(cogwheel)) {
            return;
        }
        if (!(blockEntity instanceof ICopycatBlockEntity)) {
            return;
        }
        ICopycatBlockEntity copycat = (ICopycatBlockEntity)blockEntity;
        BlockState state = blockEntity.getBlockState();
        BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
        CreateFlyCopycatModel copycatModel = CreateFlyCopycatModel.findCopycatModel(model, state);
        if (copycatModel == null) {
            return;
        }
        Map<BlockState, List<BlockStateModelPart>> partsByMaterial = copycatModel.getAnimationPartsByMaterial((BlockAndTintGetter)level, blockEntity.getBlockPos(), state, RandomSource.create((long)42L), copycat);
        if (partsByMaterial.isEmpty()) {
            return;
        }
        Direction.Axis axis = KineticBlockEntityRenderer.getRotationAxisOf((KineticBlockEntity)blockEntity);
        partsByMaterial.forEach((material, parts) -> renderState.models.add(((SuperByteBuffer)CopycatsClient.withAnimatedTint(material, (BlockAndTintGetter)level, blockEntity.getBlockPos(), () -> SuperBufferFactory.getInstance().createForBlock((BlockStateModel)new FixedPartsModel((List<BlockStateModelPart>)parts), state)).cardinalLighting(level).rotateCentered(KineticBlockEntityRenderer.getAngleForBe((KineticBlockEntity)blockEntity, (BlockPos)blockEntity.getBlockPos(), (Direction.Axis)axis), axis.getPositive())).light(renderState.lightCoords).extractRenderState()));
    }

    public void submit(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        renderState.models.forEach(model -> model.submit(poseStack, (OrderedSubmitNodeCollector)submitNodeCollector));
    }

    public static class RenderState
    extends BlockEntityRenderState {
        final List<SuperByteBufferRenderState> models = new ArrayList<SuperByteBufferRenderState>();
    }

    private record FixedPartsModel(List<BlockStateModelPart> parts) implements BlockStateModel
    {
        public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
            output.addAll(this.parts);
        }

        public Material.Baked particleMaterial() {
            return this.parts.getFirst().particleMaterial();
        }

        public int materialFlags() {
            return this.parts.stream().mapToInt(BlockStateModelPart::materialFlags).reduce(0, (left, right) -> left | right);
        }
    }
}
