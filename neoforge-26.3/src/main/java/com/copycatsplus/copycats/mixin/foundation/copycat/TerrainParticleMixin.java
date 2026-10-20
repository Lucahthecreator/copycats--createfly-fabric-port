package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.zurrtum.create.client.infrastructure.model.WrapperBlockStateModel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TerrainParticle.class)
public abstract class TerrainParticleMixin {
    @WrapOperation(
        method = "updateSprite(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/client/particle/TerrainParticle;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/block/BlockStateModelSet;getParticleMaterial(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/renderer/block/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/client/resources/model/sprite/Material$Baked;"
        )
    )
    private static Material.Baked copycats$useMaterialParticleWithPosition(
        BlockStateModelSet models,
        BlockState state,
        BlockAndTintGetter world,
        BlockPos pos,
        Operation<Material.Baked> original
    ) {
        BlockStateModel model = models.get(state);
        if (model instanceof WrapperBlockStateModel wrapper && wrapper.needUpdateTerrainParticle()) {
            return wrapper.particleMaterialWithInfo(world, WrapperBlockStateModel.findPos(world, pos, state), state);
        }
        return original.call(models, state, world, pos);
    }
}
