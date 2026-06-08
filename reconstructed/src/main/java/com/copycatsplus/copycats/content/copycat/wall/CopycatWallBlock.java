/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.data.Iterate
 *  com.zurrtum.create.foundation.block.IBE
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.core.Vec3i
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.WallBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.block.state.properties.WallSide
 *  net.minecraft.world.phys.BlockHitResult
 */
package com.copycatsplus.copycats.content.copycat.wall;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.foundation.block.IBE;
import java.util.Arrays;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.BlockHitResult;

@ParametersAreNonnullByDefault
public class CopycatWallBlock
extends WallBlock
implements ICopycatBlock,
IBE<CCCopycatBlockEntity>,
IStateType {
    public CopycatWallBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        return true;
    }

    @Nullable
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level, BlockState state, BlockEntityType<S> type) {
        return null;
    }

    protected InteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionUtils.sequential(() -> ICopycatBlock.super.use(state, level, pos, player, hand, hit), () -> super.useItemOn(heldStack, state, level, pos, player, hand, hit));
    }

    @Override
    public boolean isAcceptedRegardless(BlockState material) {
        return material.getBlock() instanceof WallBlock;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        ICopycatBlock.super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    @Override
    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        ICopycatBlock.super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    public Class<CCCopycatBlockEntity> getBlockEntityClass() {
        return CCCopycatBlockEntity.class;
    }

    public BlockEntityType<? extends CCCopycatBlockEntity> getBlockEntityType() {
        return (BlockEntityType)CCBlockEntityTypes.COPYCAT.get();
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState) {
        if (toPos == null) {
            return true;
        }
        if (CopycatWallBlock.isPole(state)) {
            return ICopycatBlock.super.isIgnoredConnectivitySide(reader, state, face, fromPos, toPos, toState);
        }
        if (toState == null) {
            toState = reader.getBlockState(toPos);
        }
        if (!toState.is((Object)this) || !state.is((Object)this)) {
            return true;
        }
        boolean isCross = true;
        for (Direction direction : Iterate.horizontalDirections) {
            if (toState.getValue(CopycatWallBlock.byDirection(direction)) != WallSide.NONE) continue;
            isCross = false;
            break;
        }
        return isCross;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        if (CopycatWallBlock.isPole(state)) {
            return ICopycatBlock.super.canConnectTexturesToward(reader, fromPos, toPos, state);
        }
        BlockState toState = reader.getBlockState(toPos);
        if (!toState.is((Object)this)) {
            return false;
        }
        long sideCount = Arrays.stream(Iterate.horizontalDirections).filter(s -> state.getValue(CopycatWallBlock.byDirection(s)) != WallSide.NONE).count();
        if (sideCount > 2L) {
            return false;
        }
        if (sideCount == 2L && (state.getValue((Property)NORTH) != state.getValue((Property)SOUTH) || state.getValue((Property)EAST) != state.getValue((Property)WEST))) {
            return false;
        }
        BlockPos diff = toPos.subtract((Vec3i)fromPos);
        if (diff.equals((Object)Vec3i.ZERO)) {
            return true;
        }
        Direction face = Direction.getApproximateNearest((float)diff.getX(), (float)diff.getY(), (float)diff.getZ());
        if (face == null) {
            if (diff.distManhattan(Vec3i.ZERO) > 2) {
                return false;
            }
            if (diff.getY() == 0) {
                return false;
            }
            Direction horizontalDiff = Direction.fromAxisAndDirection((Direction.Axis)(diff.getX() == 0 ? Direction.Axis.Z : Direction.Axis.X), (Direction.AxisDirection)(diff.getX() + diff.getZ() > 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE));
            if (diff.getY() > 0) {
                if (state.getValue(CopycatWallBlock.byDirection(horizontalDiff)) != WallSide.TALL) {
                    return false;
                }
                if (toState.getValue(CopycatWallBlock.byDirection(horizontalDiff.getOpposite())) == WallSide.NONE) {
                    return false;
                }
            } else {
                if (state.getValue(CopycatWallBlock.byDirection(horizontalDiff)) == WallSide.NONE) {
                    return false;
                }
                if (toState.getValue(CopycatWallBlock.byDirection(horizontalDiff.getOpposite())) != WallSide.TALL) {
                    return false;
                }
            }
            return true;
        }
        if (face == Direction.DOWN || face == Direction.UP) {
            return this.canConnectVertically(state) && this.canConnectVertically(toState);
        }
        if (((Boolean)state.getValue((Property)WallBlock.UP)).booleanValue()) {
            return false;
        }
        return state.getValue(CopycatWallBlock.byDirection(face)) != WallSide.NONE;
    }

    private static boolean isPole(BlockState state) {
        return Arrays.stream(Iterate.horizontalDirections).allMatch(s -> state.getValue(CopycatWallBlock.byDirection(s)) == WallSide.NONE);
    }

    private boolean canConnectVertically(BlockState state) {
        if (!((Boolean)state.getValue((Property)WallBlock.UP)).booleanValue()) {
            return false;
        }
        for (Direction direction : Iterate.horizontalDirections) {
            WallSide side = (WallSide)state.getValue(CopycatWallBlock.byDirection(direction));
            if (side == WallSide.NONE) continue;
            return false;
        }
        return true;
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    public static EnumProperty<WallSide> byDirection(Direction direction) {
        return switch (direction) {
            case Direction.NORTH -> NORTH;
            case Direction.SOUTH -> SOUTH;
            case Direction.WEST -> WEST;
            case Direction.EAST -> EAST;
            default -> throw new IllegalArgumentException("Vertical directions not supported");
        };
    }
}

