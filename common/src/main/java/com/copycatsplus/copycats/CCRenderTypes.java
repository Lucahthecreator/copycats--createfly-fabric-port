package com.copycatsplus.copycats;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class CCRenderTypes extends RenderStateShard {

    private static final RenderType FLUID_RENDER = RenderType.create(createLayerName("fluid"),
            DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .createCompositeState(true));

    public static RenderType FLUID = CCRenderTypes.FLUID_RENDER;

    private static String createLayerName(String name) {
        return Copycats.MODID + ":" + name;
    }

    private CCRenderTypes() {
        super(null, null, null);
    }
}
