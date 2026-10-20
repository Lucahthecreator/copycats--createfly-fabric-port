package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.CCBlocks;
import com.zurrtum.create.catnip.data.Iterate;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CopycatBoxItem extends BlockItem {
    public CopycatBoxItem(Properties builder) {
        super(CCBlocks.COPYCAT_BOARD.get(), builder);
    }

    @Override
    public void registerBlocks(@NotNull Map<Block, Item> map, @NotNull Item self) {
    }

    // 26.3: the instance hook updateCustomBlockEntityTag(pos, level, player, stack, state) is gone, so the board
    // state is set before placement instead of re-set after it.
    @Override
    protected boolean placeBlock(@NotNull BlockPlaceContext context, @NotNull BlockState state) {
        for (Direction direction : Iterate.directions) {
            state = state.setValue(CopycatBoardBlock.byDirection(direction), true);
        }

        return super.placeBlock(context, state);
    }
}
