/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.zurrtum.create.catnip.math.AngleHelper
 *  com.zurrtum.create.client.catnip.render.SuperBufferFactory
 *  com.zurrtum.create.client.catnip.render.SuperByteBufferRenderState
 *  com.zurrtum.create.client.foundation.blockEntity.renderer.SmartBlockEntityRenderer
 *  com.zurrtum.create.content.decoration.slidingDoor.SlidingDoorBlock
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
 *  net.minecraft.core.Direction
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.DoorBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.DoorHingeSide
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 */
package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatFoldingDoorModelCore;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.CreateFlyCopycatModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.zurrtum.create.catnip.math.AngleHelper;
import com.zurrtum.create.client.catnip.render.SuperBufferFactory;
import com.zurrtum.create.client.catnip.render.SuperByteBufferRenderState;
import com.zurrtum.create.client.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.zurrtum.create.content.decoration.slidingDoor.SlidingDoorBlock;
import java.util.ArrayList;
import java.util.List;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

public class CopycatSlidingDoorRenderBridge
implements BlockEntityRenderer<CopycatSlidingDoorBlockEntity, CopycatSlidingDoorRenderBridge.RenderState> {
    public CopycatSlidingDoorRenderBridge(BlockEntityRendererProvider.Context context) {
    }

    public RenderState createRenderState() {
        return new RenderState();
    }

    public void extractRenderState(CopycatSlidingDoorBlockEntity blockEntity, RenderState renderState, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        Level level = SmartBlockEntityRenderer.extractBase((BlockEntity)blockEntity, (BlockEntityRenderState)renderState, (ModelFeatureRenderer.CrumblingOverlay)breakProgress);
        BlockState lowerState = (BlockState)((BlockState)blockEntity.getBlockState().setValue((Property)DoorBlock.HALF, (Comparable)DoubleBlockHalf.LOWER)).setValue((Property)DoorBlock.OPEN, (Comparable)Boolean.valueOf(false));
        BlockState upperState = (BlockState)lowerState.setValue((Property)DoorBlock.HALF, (Comparable)DoubleBlockHalf.UPPER);
        Direction facing = (Direction)lowerState.getValue((Property)DoorBlock.FACING);
        float value = blockEntity.animation().getValue(partialTick);
        float forwardOffset = Mth.clamp((float)(value * 10.0f), (float)0.0f, (float)1.0f) / 32.0f;
        renderState.folding = ((SlidingDoorBlock)lowerState.getBlock()).isFoldingDoor();
        renderState.facing = facing;
        renderState.flip = lowerState.getValue((Property)DoorBlock.HINGE) == DoorHingeSide.RIGHT;
        renderState.progress = value * value;
        renderState.forwardOffset = forwardOffset;
        if (renderState.folding) {
            renderState.lower = null;
            renderState.upper = null;
            renderState.lowerLeft = CopycatSlidingDoorRenderBridge.createModel(level, blockEntity.getBlockPos(), lowerState, blockEntity, renderState.lightCoords, new CopycatFoldingDoorModelCore(true, true));
            renderState.upperLeft = CopycatSlidingDoorRenderBridge.createModel(level, blockEntity.getBlockPos().above(), upperState, blockEntity, renderState.lightCoords, new CopycatFoldingDoorModelCore(true, true));
            renderState.lowerRight = CopycatSlidingDoorRenderBridge.createModel(level, blockEntity.getBlockPos(), lowerState, blockEntity, renderState.lightCoords, new CopycatFoldingDoorModelCore(false, true));
            renderState.upperRight = CopycatSlidingDoorRenderBridge.createModel(level, blockEntity.getBlockPos().above(), upperState, blockEntity, renderState.lightCoords, new CopycatFoldingDoorModelCore(false, true));
            return;
        }
        renderState.lowerLeft = null;
        renderState.upperLeft = null;
        renderState.lowerRight = null;
        renderState.upperRight = null;
        renderState.lower = CopycatSlidingDoorRenderBridge.createModel(level, blockEntity.getBlockPos(), lowerState, blockEntity, renderState.lightCoords);
        renderState.upper = CopycatSlidingDoorRenderBridge.createModel(level, blockEntity.getBlockPos().above(), upperState, blockEntity, renderState.lightCoords);
        Direction movement = renderState.flip ? facing.getClockWise() : facing.getCounterClockWise();
        float sideOffset = renderState.progress * 13.0f / 16.0f;
        renderState.offsetX = (float)movement.getStepX() * sideOffset + (float)facing.getStepX() * forwardOffset;
        renderState.offsetZ = (float)movement.getStepZ() * sideOffset + (float)facing.getStepZ() * forwardOffset;
    }

    private static SuperByteBufferRenderState createModel(Level level, BlockPos pos, BlockState state, CopycatSlidingDoorBlockEntity blockEntity, int light) {
        BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
        if (!(model instanceof CreateFlyCopycatModel)) {
            return null;
        }
        CreateFlyCopycatModel copycatModel = (CreateFlyCopycatModel)model;
        ArrayList<BlockStateModelPart> parts = new ArrayList<BlockStateModelPart>();
        copycatModel.addAnimationParts((BlockAndTintGetter)level, pos, state, RandomSource.create((long)42L), parts, blockEntity);
        if (parts.isEmpty()) {
            return null;
        }
        return CopycatsClient.withAnimatedTint(blockEntity.getMaterial(), (BlockAndTintGetter)level, pos, () -> SuperBufferFactory.getInstance().createForBlock((BlockStateModel)new FixedPartsModel(parts), state)).cardinalLighting(level).light(light).extractRenderState();
    }

    private static SuperByteBufferRenderState createModel(Level level, BlockPos pos, BlockState state, CopycatSlidingDoorBlockEntity blockEntity, int light, CopycatFoldingDoorModelCore animationCore) {
        BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
        if (!(model instanceof CreateFlyCopycatModel)) {
            return null;
        }
        CreateFlyCopycatModel copycatModel = (CreateFlyCopycatModel)model;
        ArrayList<BlockStateModelPart> parts = new ArrayList<BlockStateModelPart>();
        copycatModel.addAnimationParts((BlockAndTintGetter)level, pos, state, RandomSource.create((long)42L), parts, blockEntity, animationCore);
        if (parts.isEmpty()) {
            return null;
        }
        return CopycatsClient.withAnimatedTint(blockEntity.getMaterial(), (BlockAndTintGetter)level, pos, () -> SuperBufferFactory.getInstance().createForBlock((BlockStateModel)new FixedPartsModel(parts), state)).cardinalLighting(level).light(light).extractRenderState();
    }

    public void submit(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (renderState.folding) {
            CopycatSlidingDoorRenderBridge.submitFoldingPanel(renderState, poseStack, submitNodeCollector, true);
            CopycatSlidingDoorRenderBridge.submitFoldingPanel(renderState, poseStack, submitNodeCollector, false);
            return;
        }
        poseStack.pushPose();
        poseStack.translate(renderState.offsetX, 0.0f, renderState.offsetZ);
        if (renderState.lower != null) {
            renderState.lower.submit(poseStack, (OrderedSubmitNodeCollector)submitNodeCollector);
        }
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(renderState.offsetX, 1.0f, renderState.offsetZ);
        if (renderState.upper != null) {
            renderState.upper.submit(poseStack, (OrderedSubmitNodeCollector)submitNodeCollector);
        }
        poseStack.popPose();
    }

    private static void submitFoldingPanel(RenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, boolean left) {
        SuperByteBufferRenderState upper;
        float direction = renderState.flip ? -1.0f : 1.0f;
        poseStack.pushPose();
        poseStack.translate(0.0f, -0.001953125f, 0.0f);
        poseStack.translate((float)renderState.facing.getStepX() * renderState.forwardOffset, 0.0f, (float)renderState.facing.getStepZ() * renderState.forwardOffset);
        CopycatSlidingDoorRenderBridge.rotateCenteredY(poseStack, AngleHelper.horizontalAngle((Direction)renderState.facing.getClockWise()));
        if (renderState.flip) {
            poseStack.translate(0.0f, 0.0f, 1.0f);
        }
        CopycatSlidingDoorRenderBridge.rotateY(poseStack, 91.0f * direction * renderState.progress);
        if (!left) {
            poseStack.translate(0.0f, 0.0f, direction / 2.0f);
            CopycatSlidingDoorRenderBridge.rotateY(poseStack, -181.0f * direction * renderState.progress);
        }
        if (renderState.flip) {
            poseStack.translate(0.0f, 0.0f, -0.5f);
        }
        SuperByteBufferRenderState lower = left ? renderState.lowerLeft : renderState.lowerRight;
        SuperByteBufferRenderState superByteBufferRenderState = upper = left ? renderState.upperLeft : renderState.upperRight;
        if (lower != null) {
            lower.submit(poseStack, (OrderedSubmitNodeCollector)submitNodeCollector);
        }
        poseStack.translate(0.0f, 1.0f, 0.0f);
        if (upper != null) {
            upper.submit(poseStack, (OrderedSubmitNodeCollector)submitNodeCollector);
        }
        poseStack.popPose();
    }

    private static void rotateCenteredY(PoseStack poseStack, float degrees) {
        poseStack.rotateAround((Quaternionfc)new Quaternionf().rotationY(AngleHelper.rad((double)degrees)), 0.5f, 0.5f, 0.5f);
    }

    private static void rotateY(PoseStack poseStack, float degrees) {
        poseStack.mulPose((Quaternionfc)new Quaternionf().rotationY(AngleHelper.rad((double)degrees)));
    }

    public static class RenderState
    extends BlockEntityRenderState {
        SuperByteBufferRenderState lower;
        SuperByteBufferRenderState upper;
        SuperByteBufferRenderState lowerLeft;
        SuperByteBufferRenderState upperLeft;
        SuperByteBufferRenderState lowerRight;
        SuperByteBufferRenderState upperRight;
        Direction facing = Direction.NORTH;
        boolean folding;
        boolean flip;
        float progress;
        float forwardOffset;
        float offsetX;
        float offsetZ;
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
