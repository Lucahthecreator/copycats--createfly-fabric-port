package com.copycatsplus.copycats.foundation.copycat.model.kinetic.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.engine_room.flywheel.lib.model.SimpleModel;
import dev.engine_room.flywheel.lib.model.baked.EmptyVirtualBlockGetter;
import net.createmod.catnip.render.VirtualRenderHelper;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

//TODO: Figure out the replacements for this
public final class BakedModelWithDataBuilder implements Bufferable {
    private final BakedModel model;
    private BlockAndTintGetter renderWorld = EmptyVirtualBlockGetter.FULL_BRIGHT;
    private BlockState referenceState = Blocks.AIR.defaultBlockState();
    private PoseStack poseStack = new PoseStack();
    private BlockPos renderPos = BlockPos.ZERO;
    private ModelData data = VirtualRenderHelper.VIRTUAL_DATA;

    public BakedModelWithDataBuilder(BakedModel model) {
        this.model = model;
    }

    public BakedModelWithDataBuilder withRenderPos(BlockPos renderPos) {
        this.renderPos = renderPos;
        return this;
    }

    public BakedModelWithDataBuilder withRenderWorld(BlockAndTintGetter renderWorld) {
        this.renderWorld = renderWorld;
        return this;
    }

    public BakedModelWithDataBuilder withReferenceState(BlockState referenceState) {
        this.referenceState = referenceState;
        return this;
    }

    public BakedModelWithDataBuilder withPoseStack(PoseStack poseStack) {
        this.poseStack = poseStack;
        return this;
    }

    public BakedModelWithDataBuilder withData(ModelData data) {
        this.data = data;
        return this;
    }

    @Override
    public void bufferInto(VertexConsumer consumer, ModelBlockRenderer blockRenderer, RandomSource random) {
        blockRenderer.tesselateBlock(renderWorld, model, referenceState, renderPos, poseStack, consumer, false, random, 42, OverlayTexture.NO_OVERLAY, data, null);
    }

    public SimpleModel toModel(String name) {
        return BlockModel.of(this, name);
    }

    public BlockModel toModel() {
        return toModel(referenceState.toString());
    }
}
