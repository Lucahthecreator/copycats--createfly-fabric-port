package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.foundation.copycat.CopycatMaterialStore;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@ParametersAreNonnullByDefault
public class BlockEntityUtils {
    public static void redraw(BlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if (level != null) {
            if (blockEntity instanceof IMultiStateCopycatBlockEntity multiStateBE) {
                CopycatMaterialStore.setMaterial(level, blockEntity.getBlockPos(), multiStateBE.getMaterialItemStorage().getMaterialMap());
            } else if (blockEntity instanceof ICopycatBlockEntity copycatBE) {
                CopycatMaterialStore.setMaterial(level, blockEntity.getBlockPos(), copycatBE.getMaterial());
            }

            if (level.isClientSide()) {
                requestModelDataUpdate(blockEntity);
            } else {
                blockEntity.setChanged();
            }

            BlockState state = blockEntity.getBlockState();
            level.sendBlockUpdated(blockEntity.getBlockPos(), state, state, 16);
            updateLight(blockEntity);
        }
    }

    public static void redrawAt(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null) {
            redraw(blockEntity);
        }
    }

    public static void saveMetadata(BlockEntity blockEntity, CompoundTag tag) {
    }

    public static void requestModelDataUpdate(BlockEntity blockEntity) {
    }

    private static void updateLight(BlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if (level != null) {
            BlockPos pos = blockEntity.getBlockPos();
            level.getChunkSource().getLightEngine().checkBlock(pos);
        }
    }

    public static boolean isWorldRenderWorld(Level level) {
        return Platform.Environment.CLIENT.isCurrent() ? Platform.Environment.CLIENT.getIfCurrent(() -> ClientUtils.isVirtualRenderWorld(level), false) : false;
    }
}
