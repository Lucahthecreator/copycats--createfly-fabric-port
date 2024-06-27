package com.copycatsplus.copycats.content.copycat.fluid_pipe;


import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.LeavesBlock;

public class CopycatFluidPipeRenderer extends SafeBlockEntityRenderer<CopycatFluidPipeBlockEntity> {

    public CopycatFluidPipeRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(CopycatFluidPipeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        if (!(be.getMaterial().is(AllBlocks.COPYCAT_BASE.get()) || be.getMaterial().getBlock() instanceof HalfTransparentBlock || be.getMaterial().getBlock() instanceof LeavesBlock))
            // todo: find a better way to detect material transparency
            return;
        renderSafe(this, be, partialTicks, ms, buffer, light, overlay);
    }

    @ExpectPlatform
    public static void renderSafe(CopycatFluidPipeRenderer renderer, CopycatFluidPipeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                                  int light, int overlay) {

    }
}

