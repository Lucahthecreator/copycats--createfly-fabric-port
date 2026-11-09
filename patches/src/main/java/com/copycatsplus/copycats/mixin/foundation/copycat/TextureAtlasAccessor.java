package com.copycatsplus.copycats.mixin.foundation.copycat;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(TextureAtlas.class)
public interface TextureAtlasAccessor {
    @Accessor("sprites")
    List<TextureAtlasSprite> copycats$getSprites();
}
