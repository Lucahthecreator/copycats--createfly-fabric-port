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
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.BlockHitResult;

@ParametersAreNonnullByDefault
public class CopycatWallBlock extends WallBlock implements ICopycatBlock, IBE<CCCopycatBlockEntity>, IStateType {
    public CopycatWallBlock(Properties properties) {
        super(properties);
    }

    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        return true;
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level, BlockState state, BlockEntityType<S> type) {
        return null;
    }

    @Override
    protected InteractionResult useItemOn(
        ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit
    ) {
        return InteractionUtils.sequential(
            () -> ICopycatBlock.super.use(state, level, pos, player, hand, hit), () -> super.useItemOn(heldStack, state, level, pos, player, hand, hit)
        );
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

    @Override
    public Class<CCCopycatBlockEntity> getBlockEntityClass() {
        return CCCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CCCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT.get();
    }

    @Override
    public boolean isIgnoredConnectivitySide(
        BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState
    ) {
        if (toPos == null) {
            return true;
        } else if (isPole(state)) {
            return ICopycatBlock.super.isIgnoredConnectivitySide(reader, state, face, fromPos, toPos, toState);
        } else {
            if (toState == null) {
                toState = reader.getBlockState(toPos);
            }

            if (toState.is(this) && state.is(this)) {
                boolean isCross = true;

                for (Direction direction : Iterate.horizontalDirections) {
                    if (toState.getValue(byDirection(direction)) == WallSide.NONE) {
                        isCross = false;
                        break;
                    }
                }

                return isCross;
            } else {
                return true;
            }
        }
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        if (isPole(state)) {
            return ICopycatBlock.super.canConnectTexturesToward(reader, fromPos, toPos, state);
        } else {
            BlockState toState = reader.getBlockState(toPos);
            if (!toState.is(this)) {
                return false;
            } else {
                long sideCount = Arrays.stream(Iterate.horizontalDirections).filter(s -> state.getValue(byDirection(s)) != WallSide.NONE).count();
                if (sideCount > 2L) {
                    return false;
                } else if (sideCount != 2L || state.getValue(NORTH) == state.getValue(SOUTH) && state.getValue(EAST) == state.getValue(WEST)) {
                    BlockPos diff = toPos.subtract(fromPos);
                    if (diff.equals(Vec3i.ZERO)) {
                        return true;
                    } else {
                        Direction face = Direction.getApproximateNearest((float)diff.getX(), (float)diff.getY(), (float)diff.getZ());
                        if (face == null) {
                            if (diff.distManhattan(Vec3i.ZERO) > 2) {
                                return false;
                            } else if (diff.getY() == 0) {
                                return false;
                            } else {
                                Direction horizontalDiff = Direction.fromAxisAndDirection(
                                    diff.getX() == 0 ? Axis.Z : Axis.X, diff.getX() + diff.getZ() > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE
                                );
                                if (diff.getY() > 0) {
                                    if (state.getValue(byDirection(horizontalDiff)) != WallSide.TALL) {
                                        return false;
                                    }

                                    if (toState.getValue(byDirection(horizontalDiff.getOpposite())) == WallSide.NONE) {
                                        return false;
                                    }
                                } else {
                                    if (state.getValue(byDirection(horizontalDiff)) == WallSide.NONE) {
                                        return false;
                                    }

                                    if (toState.getValue(byDirection(horizontalDiff.getOpposite())) != WallSide.TALL) {
                                        return false;
                                    }
                                }

                                return true;
                            }
                        } else if (face == Direction.DOWN || face == Direction.UP) {
                            return this.canConnectVertically(state) && this.canConnectVertically(toState);
                        } else {
                            return state.getValue(WallBlock.UP) ? false : state.getValue(byDirection(face)) != WallSide.NONE;
                        }
                    }
                } else {
                    return false;
                }
            }
        }
    }

    private static boolean isPole(BlockState state) {
        return Arrays.stream(Iterate.horizontalDirections).allMatch(s -> state.getValue(byDirection(s)) == WallSide.NONE);
    }

    private boolean canConnectVertically(BlockState state) {
        if (!state.getValue(WallBlock.UP)) {
            return false;
        } else {
            for (Direction direction : Iterate.horizontalDirections) {
                WallSide side = state.getValue(byDirection(direction));
                if (side != WallSide.NONE) {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    public static EnumProperty<WallSide> byDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> throw new IllegalArgumentException("Vertical directions not supported");
        };
    }
}
