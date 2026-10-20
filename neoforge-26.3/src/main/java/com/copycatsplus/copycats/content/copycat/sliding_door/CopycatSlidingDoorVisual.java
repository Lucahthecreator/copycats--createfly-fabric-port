package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.foundation.copycat.model.CreateFlyCopycatModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.zurrtum.create.catnip.math.AngleHelper;
import com.zurrtum.create.client.flywheel.api.instance.Instance;
import com.zurrtum.create.client.flywheel.api.model.Model;
import com.zurrtum.create.client.flywheel.api.visual.ShaderLightVisual;
import com.zurrtum.create.client.flywheel.api.visual.DynamicVisual.Context;
import com.zurrtum.create.client.flywheel.api.visualization.VisualizationContext;
import com.zurrtum.create.client.flywheel.lib.instance.AbstractInstance;
import com.zurrtum.create.client.flywheel.lib.instance.InstanceTypes;
import com.zurrtum.create.client.flywheel.lib.instance.TransformedInstance;
import com.zurrtum.create.client.flywheel.lib.model.baked.BakedModelBuilder;
import com.zurrtum.create.client.flywheel.lib.visual.AbstractBlockEntityVisual;
import com.zurrtum.create.client.flywheel.lib.visual.SimpleDynamicVisual;
import com.zurrtum.create.content.decoration.slidingDoor.SlidingDoorBlock;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material.Baked;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class CopycatSlidingDoorVisual extends AbstractBlockEntityVisual<CopycatSlidingDoorBlockEntity> implements SimpleDynamicVisual, ShaderLightVisual {
    private final List<TransformedInstance> instances = new ArrayList<>();
    private BlockState renderedState;
    private BlockState renderedMaterial;

    public CopycatSlidingDoorVisual(VisualizationContext context, CopycatSlidingDoorBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        this.rebuild();
        this.transformModels(partialTick);
    }

    public static boolean canVisualize(CopycatSlidingDoorBlockEntity blockEntity) {
        if (blockEntity.getMaterial() != null && blockEntity.getLevel() != null) {
            BlockState state = blockEntity.getBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER).setValue(DoorBlock.OPEN, false);
            BlockPos lowerPos = blockEntity.getBlockPos();
            return ((SlidingDoorBlock)state.getBlock()).isFoldingDoor()
                ? hasParts(blockEntity, lowerPos, state, new CopycatFoldingDoorModelCore(true, true))
                    && hasParts(blockEntity, lowerPos, state, new CopycatFoldingDoorModelCore(false, true))
                    && hasParts(
                        blockEntity, lowerPos.above(), state.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), new CopycatFoldingDoorModelCore(true, true)
                    )
                    && hasParts(
                        blockEntity, lowerPos.above(), state.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), new CopycatFoldingDoorModelCore(false, true)
                    )
                : hasParts(blockEntity, lowerPos, state, null)
                    && hasParts(blockEntity, lowerPos.above(), state.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), null);
        } else {
            return false;
        }
    }

    private static boolean hasParts(CopycatSlidingDoorBlockEntity blockEntity, BlockPos pos, BlockState state, CopycatFoldingDoorModelCore animationCore) {
        BlockStateModel registeredModel = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
        CreateFlyCopycatModel copycatModel = CreateFlyCopycatModel.findCopycatModel(registeredModel, state);
        if (copycatModel == null) {
            return false;
        } else {
            List<BlockStateModelPart> parts = new ArrayList<>();
            if (animationCore == null) {
                copycatModel.addAnimationParts((BlockAndTintGetter)blockEntity.getLevel(), pos, state, RandomSource.create(42L), parts, blockEntity);
            } else {
                copycatModel.addAnimationParts(
                    (BlockAndTintGetter)blockEntity.getLevel(), pos, state, RandomSource.create(42L), parts, blockEntity, animationCore
                );
            }

            return !parts.isEmpty();
        }
    }

    @Override
    public void update(float partialTick) {
        if (this.needsRebuild()) {
            this.rebuild();
        }

        this.transformModels(partialTick);
    }

    @Override
    public void beginFrame(Context context) {
        if (this.needsRebuild()) {
            this.rebuild();
        }

        this.transformModels(context.partialTick());
    }

    private boolean needsRebuild() {
        return this.renderedState != this.blockEntity.getBlockState() || this.renderedMaterial != this.blockEntity.getMaterial();
    }

    private void rebuild() {
        this.instances.forEach(AbstractInstance::delete);
        this.instances.clear();
        this.renderedState = this.blockEntity.getBlockState();
        this.renderedMaterial = this.blockEntity.getMaterial();
        BlockState lowerState = this.renderedState.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER).setValue(DoorBlock.OPEN, false);
        BlockState upperState = lowerState.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
        BlockPos lowerPos = this.blockEntity.getBlockPos();
        BlockPos upperPos = lowerPos.above();
        boolean folding = ((SlidingDoorBlock)lowerState.getBlock()).isFoldingDoor();
        if (folding) {
            this.addModel(lowerPos, lowerState, new CopycatFoldingDoorModelCore(true, true));
            this.addModel(upperPos, upperState, new CopycatFoldingDoorModelCore(true, true));
            this.addModel(lowerPos, lowerState, new CopycatFoldingDoorModelCore(false, true));
            this.addModel(upperPos, upperState, new CopycatFoldingDoorModelCore(false, true));
        } else {
            this.addModel(lowerPos, lowerState, null);
            this.addModel(upperPos, upperState, null);
        }
    }

    private void addModel(BlockPos pos, BlockState state, CopycatFoldingDoorModelCore animationCore) {
        Model model = this.createModel(pos, state, animationCore);
        if (model != null) {
            this.instances.add(this.instancerProvider().instancer(InstanceTypes.TRANSFORMED, model).createInstance());
        }
    }

    private Model createModel(BlockPos pos, BlockState state, CopycatFoldingDoorModelCore animationCore) {
        BlockStateModel registeredModel = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
        CreateFlyCopycatModel copycatModel = CreateFlyCopycatModel.findCopycatModel(registeredModel, state);
        if (copycatModel != null && this.renderedMaterial != null) {
            List<BlockStateModelPart> parts = new ArrayList<>();
            if (animationCore == null) {
                copycatModel.addAnimationParts((BlockAndTintGetter)this.level, pos, state, RandomSource.create(42L), parts, this.blockEntity);
            } else {
                copycatModel.addAnimationParts((BlockAndTintGetter)this.level, pos, state, RandomSource.create(42L), parts, this.blockEntity, animationCore);
            }

            return parts.isEmpty()
                ? null
                : CopycatsClient.withAnimatedTint(
                    this.renderedMaterial,
                    (BlockAndTintGetter)this.level,
                    pos,
                    () -> new BakedModelBuilder(new CopycatSlidingDoorVisual.FixedPartsModel(parts)).level((BlockAndTintGetter)this.level).pos(pos).build()
                );
        } else {
            return null;
        }
    }

    private void transformModels(float partialTick) {
        if (!this.instances.isEmpty()) {
            BlockState state = this.blockEntity.getBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
            Direction facing = state.getValue(DoorBlock.FACING);
            boolean flip = state.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
            float value = this.blockEntity.animation().getValue(partialTick);
            float progress = value * value;
            float forwardOffset = Mth.clamp(value * 10.0F, 0.0F, 1.0F) / 32.0F;
            BlockPos lowerPos = this.blockEntity.getBlockPos();
            boolean folding = ((SlidingDoorBlock)state.getBlock()).isFoldingDoor();
            if (folding) {
                this.transformFolding(lowerPos, facing, flip, progress, forwardOffset);
            } else {
                Direction movement = flip ? facing.getClockWise() : facing.getCounterClockWise();
                float sideOffset = progress * 13.0F / 16.0F;
                float offsetX = movement.getStepX() * sideOffset + facing.getStepX() * forwardOffset;
                float offsetZ = movement.getStepZ() * sideOffset + facing.getStepZ() * forwardOffset;
                this.setTransform(0, new Matrix4f().translation(lowerPos.getX() + offsetX, lowerPos.getY(), lowerPos.getZ() + offsetZ));
                this.setTransform(1, new Matrix4f().translation(lowerPos.getX() + offsetX, lowerPos.getY() + 1, lowerPos.getZ() + offsetZ));
            }
        }
    }

    private void transformFolding(BlockPos lowerPos, Direction facing, boolean flip, float progress, float forwardOffset) {
        this.setTransform(0, foldingTransform(lowerPos, facing, flip, true, progress, forwardOffset));
        this.setTransform(1, foldingTransform(lowerPos.above(), facing, flip, true, progress, forwardOffset));
        this.setTransform(2, foldingTransform(lowerPos, facing, flip, false, progress, forwardOffset));
        this.setTransform(3, foldingTransform(lowerPos.above(), facing, flip, false, progress, forwardOffset));
    }

    private void setTransform(int index, Matrix4f transform) {
        if (index < this.instances.size()) {
            this.instances.get(index).setTransform(transform).setChanged();
        }
    }

    private static Matrix4f foldingTransform(BlockPos origin, Direction facing, boolean flip, boolean left, float progress, float forwardOffset) {
        float direction = flip ? -1.0F : 1.0F;
        PoseStack poseStack = new PoseStack();
        poseStack.translate((float)origin.getX(), (float)origin.getY(), (float)origin.getZ());
        poseStack.translate(0.0F, -0.001953125F, 0.0F);
        poseStack.translate(facing.getStepX() * forwardOffset, 0.0F, facing.getStepZ() * forwardOffset);
        poseStack.rotateAround(new Quaternionf().rotationY(AngleHelper.rad(AngleHelper.horizontalAngle(facing.getClockWise()))), 0.5F, 0.5F, 0.5F);
        if (flip) {
            poseStack.translate(0.0F, 0.0F, 1.0F);
        }

        poseStack.rotate(new Quaternionf().rotationY(AngleHelper.rad(91.0F * direction * progress)));
        if (!left) {
            poseStack.translate(0.0F, 0.0F, direction / 2.0F);
            poseStack.rotate(new Quaternionf().rotationY(AngleHelper.rad(-181.0F * direction * progress)));
        }

        if (flip) {
            poseStack.translate(0.0F, 0.0F, -0.5F);
        }

        return new Matrix4f(poseStack.last().pose());
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        this.instances.forEach(consumer::accept);
    }

    @Override
    public void updateLight(float partialTick) {
        this.relight(this.instances);
    }

    @Override
    protected void _delete() {
        this.instances.forEach(AbstractInstance::delete);
        this.instances.clear();
    }

    private record FixedPartsModel(List<BlockStateModelPart> parts) implements BlockStateModel {
        @Override
        public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
            output.addAll(this.parts);
        }

        public boolean useAmbientOcclusion() {
            return !this.parts.isEmpty() && this.parts.getFirst().useAmbientOcclusion();
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
}
