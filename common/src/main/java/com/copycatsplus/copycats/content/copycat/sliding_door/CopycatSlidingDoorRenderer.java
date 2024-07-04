package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.content.copycat.base.model.kinetic.IKineticCopycatBlockRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CopycatSlidingDoorRenderer extends SlidingDoorRenderer implements IKineticCopycatBlockRenderer {

    public CopycatSlidingDoorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(SlidingDoorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
    }
}
