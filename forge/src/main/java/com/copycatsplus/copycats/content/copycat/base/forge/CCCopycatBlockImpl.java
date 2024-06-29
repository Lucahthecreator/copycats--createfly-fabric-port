package com.copycatsplus.copycats.content.copycat.base.forge;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.model.forge.CopycatModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelDataManager;

public class CCCopycatBlockImpl {

    @SuppressWarnings("UnstableApiUsage")
    public static BlockState multiPlatformGetAppearance(IFunctionalCopycatBlock block, BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                                        BlockState queryState, BlockPos queryPos) {

        ModelDataManager modelDataManager = level.getModelDataManager();
        if (modelDataManager == null)
            return IFunctionalCopycatBlock.getMaterial(level, pos);
        return CopycatModel.getMaterial(modelDataManager.getAt(pos));
    }
}
