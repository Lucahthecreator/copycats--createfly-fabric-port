package com.copycatsplus.copycats.utility.neoforge;

import com.copycatsplus.copycats.utility.BlockEntityUtils;
import net.createmod.catnip.levelWrappers.SchematicLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.world.AuxiliaryLightManager;

public class BlockEntityUtilsImpl {
    public static void requestModelDataUpdate(BlockEntity blockEntity) {
        blockEntity.requestModelDataUpdate();
    }

    public static void updateLight(BlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if (level instanceof SchematicLevel)
            return;
        if (level != null && level.getChunkSource() != null) { // should not be necessary to check chunk source for null but seems like it happens inconsistently with other mods installed
            BlockPos pos = blockEntity.getBlockPos();
            AuxiliaryLightManager lightManager = level.getAuxLightManager(pos);
            if (lightManager != null)
                lightManager.setLightAt(pos, BlockEntityUtils.getLightEmission(blockEntity));
        }
    }
}
