package com.copycatsplus.copycats.foundation.copycat.model.kinetic.neoforge;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.engine_room.flywheel.lib.model.baked.EmptyVirtualBlockGetter;
import net.createmod.catnip.render.ShadedBlockSbbBuilder;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.ponder.render.VirtualRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

public final class BakedModelWithDataBuilder {
    private final BakedModel model;
    private BlockAndTintGetter renderWorld = EmptyVirtualBlockGetter.FULL_DARK;
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

    public SuperByteBuffer build() {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        RandomSource random = RandomSource.createNewThreadLocalInstance();

        ShadedBlockSbbBuilder sbbBuilder = ShadedBlockSbbBuilder.createForPonder();
        sbbBuilder.begin();

        poseStack.pushPose();
        dispatcher.getModelRenderer().tesselateBlock(renderWorld, model, referenceState, renderPos, poseStack, sbbBuilder, false, random, 42, OverlayTexture.NO_OVERLAY, data, null);
        poseStack.popPose();

        return sbbBuilder.end();
    }
}
