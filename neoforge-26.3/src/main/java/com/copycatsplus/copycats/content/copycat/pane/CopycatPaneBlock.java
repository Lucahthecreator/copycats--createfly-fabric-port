package com.copycatsplus.copycats.content.copycat.pane;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.content.decoration.palettes.ConnectedGlassPaneBlock;
import com.zurrtum.create.foundation.block.IBE;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class CopycatPaneBlock extends ConnectedGlassPaneBlock implements ICopycatBlock, IBE<CCCopycatBlockEntity>, IStateType, ICustomCTBlocking {
    public CopycatPaneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
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
    public boolean isAcceptedRegardless(BlockState material) {
        return material.getBlock() instanceof IronBarsBlock;
    }

    @Override
    public boolean isIgnoredConnectivitySide(
        BlockAndTintGetter reader,
        BlockState state,
        Direction face,
        BlockPos fromPos,
        @org.jetbrains.annotations.Nullable BlockPos toPos,
        @org.jetbrains.annotations.Nullable BlockState toState
    ) {
        return toPos == null
            ? true
            : face.getAxis().isVertical() || !reader.getBlockState(toPos).is(this) && !reader.getBlockState(toPos).is(CCBlocks.COPYCAT_FLAT_PANE.get());
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return reader.getBlockState(toPos).is(this) || reader.getBlockState(toPos).is(CCBlocks.COPYCAT_FLAT_PANE.get());
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        return Optional.of(false);
    }

    @Override
    public Optional<Boolean> isCTBlocked(
        BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos connectingPos, BlockPos blockingPos, Direction face
    ) {
        return Optional.of(false);
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    public static BooleanProperty propertyForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            default -> throw new IllegalStateException("Direction must be horizontal");
        };
    }

    @Override
    public Class<CCCopycatBlockEntity> getBlockEntityClass() {
        return CCCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CCCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT.get();
    }
}
