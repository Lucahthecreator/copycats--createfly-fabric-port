package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.CCBlockStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CopycatBaseBlock extends Block {
    public static final int BASE_TYPE_COUNT = 3;
    public static final IntegerProperty BASE_TYPE = CCBlockStateProperties.BASE_TYPE;

    public CopycatBaseBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(BASE_TYPE, 0));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(BASE_TYPE));
    }
}
