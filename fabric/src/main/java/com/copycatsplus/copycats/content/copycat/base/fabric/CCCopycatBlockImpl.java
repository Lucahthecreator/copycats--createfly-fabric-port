package com.copycatsplus.copycats.content.copycat.base.fabric;

import com.copycatsplus.copycats.content.copycat.base.ICopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.model.fabric.CopycatModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class CCCopycatBlockImpl {

    public static BlockState multiPlatformGetAppearance(ICopycatBlock block, BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                                        BlockState queryState, BlockPos queryPos) {

        return CopycatModel.getMaterial(ICopycatBlock.getMaterial(level, pos));
    }
}
