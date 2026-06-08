/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.data.Iterate
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock;
import com.zurrtum.create.catnip.data.Iterate;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

public class CopycatCatwalkItem
extends BlockItem {
    public CopycatCatwalkItem(Item.Properties builder) {
        super((Block)CCBlocks.COPYCAT_BOARD.get(), builder);
    }

    public void registerBlocks(@NotNull Map<Block, Item> map, @NotNull Item self) {
    }

    protected boolean updateCustomBlockEntityTag(@NotNull BlockPos pos, @NotNull Level world, Player player, @NotNull ItemStack stack, @NotNull BlockState state) {
        Direction facing = player == null ? Direction.SOUTH : player.getDirection();
        for (Direction direction : Iterate.horizontalDirections) {
            state = (BlockState)state.setValue((Property)CopycatBoardBlock.byDirection(direction), (Comparable)Boolean.valueOf(direction.getAxis() != facing.getAxis()));
        }
        state = (BlockState)state.setValue((Property)CopycatBoardBlock.DOWN, (Comparable)Boolean.valueOf(true));
        state = (BlockState)state.setValue((Property)CopycatBoardBlock.UP, (Comparable)Boolean.valueOf(false));
        world.setBlockAndUpdate(pos, state);
        return super.updateCustomBlockEntityTag(pos, world, player, stack, state);
    }
}

