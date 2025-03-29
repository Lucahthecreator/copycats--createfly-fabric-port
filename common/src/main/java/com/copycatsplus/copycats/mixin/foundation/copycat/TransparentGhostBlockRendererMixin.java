package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.createmod.ponder.render.VirtualRenderHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(targets = "net.createmod.catnip.ghostblock.GhostBlockRenderer$TransparentGhostBlockRenderer")
public class TransparentGhostBlockRendererMixin {
    @WrapOperation(
            method = "renderModel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/util/RandomSource;)Ljava/util/List;")
    )
    private List<BakedQuad> renderModel(BakedModel instance, BlockState blockState, Direction direction, RandomSource randomSource, Operation<List<BakedQuad>> original) {
        return instance.getQuads(blockState, direction, randomSource, VirtualRenderHelper.VIRTUAL_DATA, null);
    }
}
