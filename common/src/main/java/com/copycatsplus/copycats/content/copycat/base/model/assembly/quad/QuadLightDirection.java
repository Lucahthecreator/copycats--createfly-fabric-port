package com.copycatsplus.copycats.content.copycat.base.model.assembly.quad;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

/**
 * Transforms the light direction of a quad.
 * <p>
 * Note that this is currently only implemented for Forge, since Fabric calculates quad light direction based on
 * the vertex normals.
 *
 * @param directionMapper The function that maps the original direction to the new direction.
 */
@ApiStatus.Experimental
public record QuadLightDirection(Function<Direction, Direction> directionMapper) implements QuadTransform {

    @Override
    public void transformVertices(MutableQuad quad, TextureAtlasSprite sprite) {
        quad.direction = directionMapper.apply(quad.direction);
    }
}
