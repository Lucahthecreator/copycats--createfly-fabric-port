package com.copycatsplus.copycats.foundation.copycat.model;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlockEntity;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelVisuals;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.zurrtum.create.client.catnip.render.SuperBufferFactory;
import com.zurrtum.create.client.catnip.render.SuperByteBufferRenderState;
import com.zurrtum.create.client.content.kinetics.base.KineticBlockEntityRenderer;
import com.zurrtum.create.client.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.zurrtum.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.Material.Baked;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CopycatKineticRenderBridge implements BlockEntityRenderer<BracketedKineticBlockEntity, CopycatKineticRenderBridge.RenderState> {
    public CopycatKineticRenderBridge(Context context) {
    }

    public CopycatKineticRenderBridge.RenderState createRenderState() {
        return new CopycatKineticRenderBridge.RenderState();
    }

    public void extractRenderState(
        BracketedKineticBlockEntity blockEntity,
        CopycatKineticRenderBridge.RenderState renderState,
        float partialTick,
        Vec3 cameraPos,
        CrumblingOverlay breakProgress
    ) {
        Level level = SmartBlockEntityRenderer.extractBase(blockEntity, renderState, breakProgress);
        renderState.models.clear();
        if (!(blockEntity instanceof CopycatCogWheelBlockEntity cogwheel && CopycatCogWheelVisuals.shouldUseVisualization(cogwheel))) {
            if (blockEntity instanceof ICopycatBlockEntity copycat) {
                BlockState state = blockEntity.getBlockState();
                BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
                CreateFlyCopycatModel copycatModel = CreateFlyCopycatModel.findCopycatModel(model, state);
                if (copycatModel != null) {
                    Map<BlockState, List<BlockStateModelPart>> partsByMaterial = copycatModel.getAnimationPartsByMaterial(
                        (BlockAndTintGetter)level, blockEntity.getBlockPos(), state, RandomSource.create(42L), copycat
                    );
                    if (!partsByMaterial.isEmpty()) {
                        Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
                        partsByMaterial.forEach(
                            (material, parts) -> renderState.models
                                .add(
                                    CopycatsClient.withAnimatedTint(
                                            material,
                                            (BlockAndTintGetter)level,
                                            blockEntity.getBlockPos(),
                                            () -> SuperBufferFactory.getInstance().createForBlock(new CopycatKineticRenderBridge.FixedPartsModel(parts), state)
                                        )
                                        .cardinalLighting(level)
                                        .rotateCentered(
                                            KineticBlockEntityRenderer.getAngleForBe(blockEntity, blockEntity.getBlockPos(), axis), axis.getPositive()
                                        )
                                        .light(renderState.lightCoords)
                                        .extractRenderState()
                                )
                        );
                    }
                }
            }
        }
    }

    public void submit(
        CopycatKineticRenderBridge.RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState
    ) {
        renderState.models.forEach(model -> model.submit(poseStack, submitNodeCollector));
    }

    private record FixedPartsModel(List<BlockStateModelPart> parts) implements BlockStateModel {
        @Override
        public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
            output.addAll(this.parts);
        }

        @Override
        public Baked particleMaterial() {
            return this.parts.getFirst().particleMaterial();
        }

        @Override
        public int materialFlags() {
            return this.parts.stream().mapToInt(BlockStateModelPart::materialFlags).reduce(0, (left, right) -> left | right);
        }
    }

    public static class RenderState extends BlockEntityRenderState {
        final List<SuperByteBufferRenderState> models = new ArrayList<>();
    }
}
