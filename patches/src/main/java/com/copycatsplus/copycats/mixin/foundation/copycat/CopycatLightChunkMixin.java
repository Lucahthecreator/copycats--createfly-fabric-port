package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelChunk.class)
public abstract class CopycatLightChunkMixin {
    @Shadow
    @Final
    private Level level;

    @Inject(method = "setBlockEntity", at = @At("TAIL"))
    private void copycats$checkCopiedMaterialLight(BlockEntity blockEntity, CallbackInfo ci) {
        copycats$checkCopiedMaterialLight(blockEntity);
    }

    @Inject(method = "registerAllBlockEntitiesAfterLevelLoad", at = @At("TAIL"))
    private void copycats$checkLoadedCopiedMaterialLights(CallbackInfo ci) {
        for (BlockEntity blockEntity : ((LevelChunk) (Object) this).getBlockEntities().values()) {
            copycats$checkCopiedMaterialLight(blockEntity);
        }
    }

    @Inject(method = "removeBlockEntity", at = @At("TAIL"))
    private void copycats$clearRemovedCopiedMaterialLight(net.minecraft.core.BlockPos pos, CallbackInfo ci) {
        CopycatsDebug.log("light", () -> "check removed copycat light pos=" + pos);
        level.getLightEngine().checkBlock(pos);
    }

    private void copycats$checkCopiedMaterialLight(BlockEntity blockEntity) {
        if (blockEntity instanceof ICopycatBlockEntity) {
            CopycatsDebug.log("light", () -> "check copied material light pos=" + blockEntity.getBlockPos()
                    + " blockEntity=" + blockEntity.getClass().getName()
                    + " state=" + blockEntity.getBlockState());
            level.getLightEngine().checkBlock(blockEntity.getBlockPos());
        }
    }
}
