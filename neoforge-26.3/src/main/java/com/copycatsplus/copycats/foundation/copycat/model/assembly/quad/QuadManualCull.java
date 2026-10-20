package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public record QuadManualCull(QuadManualCull.CullFaceMapper mapper) implements QuadTransform {
    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        quad.cullFace = this.mapper.map(quad.computeLightFace(), quad.cullFace);
        quad.disableFinalAutoCull = true;
        return true;
    }

    @FunctionalInterface
    public interface CullFaceMapper {
        @Nullable
        Direction map(@NotNull Direction var1, @Nullable Direction var2);
    }
}
