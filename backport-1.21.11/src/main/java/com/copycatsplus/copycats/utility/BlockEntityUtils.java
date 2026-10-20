/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.foundation.copycat.CopycatMaterialStore;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.utility.ClientUtils;
import com.copycatsplus.copycats.utility.Platform;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@ParametersAreNonnullByDefault
public class BlockEntityUtils {
    public static void redraw(BlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if (level != null) {
            if (blockEntity instanceof IMultiStateCopycatBlockEntity) {
                IMultiStateCopycatBlockEntity multiStateBE = (IMultiStateCopycatBlockEntity)blockEntity;
                CopycatMaterialStore.setMaterial((BlockGetter)level, blockEntity.getBlockPos(), multiStateBE.getMaterialItemStorage().getMaterialMap());
            } else if (blockEntity instanceof ICopycatBlockEntity) {
                ICopycatBlockEntity copycatBE = (ICopycatBlockEntity)blockEntity;
                CopycatMaterialStore.setMaterial((BlockGetter)level, blockEntity.getBlockPos(), copycatBE.getMaterial());
            }
            if (level.isClientSide()) {
                BlockEntityUtils.requestModelDataUpdate(blockEntity);
            } else {
                blockEntity.setChanged();
            }
            BlockState state = blockEntity.getBlockState();
            level.sendBlockUpdated(blockEntity.getBlockPos(), state, state, 16);
            BlockEntityUtils.updateLight(blockEntity);
        }
    }

    public static void redrawAt(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null) {
            BlockEntityUtils.redraw(blockEntity);
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
        if (Platform.Environment.CLIENT.isCurrent()) {
            return Platform.Environment.CLIENT.getIfCurrent(() -> ClientUtils.isVirtualRenderWorld(level), false);
        }
        return false;
    }
}

