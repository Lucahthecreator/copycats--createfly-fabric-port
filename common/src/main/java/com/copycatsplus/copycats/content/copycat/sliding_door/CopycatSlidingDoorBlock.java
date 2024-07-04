package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.content.copycat.base.ICopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.IStateType;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopycatSlidingDoorBlock extends SlidingDoorBlock implements ICopycatBlock, IStateType {

    public static CopycatSlidingDoorBlock metal(Properties properties, boolean folds) {
        return new CopycatSlidingDoorBlock(properties, TRAIN_SET_TYPE.get(), folds);
    }

    public static CopycatSlidingDoorBlock glass(Properties properties, boolean folds) {
        return new CopycatSlidingDoorBlock(properties, GLASS_SET_TYPE.get(), folds);
    }

    public CopycatSlidingDoorBlock(Properties properties, BlockSetType type, boolean folds) {
        super(properties, type, folds);
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<S> p_153214_) {
        return null;
    }

    @NotNull
    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(
                () -> ICopycatBlock.super.onSneakWrenched(state, context),
                () -> super.onSneakWrenched(state, context)
        );
    }

    @NotNull
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(
                () -> ICopycatBlock.super.onWrenched(state, context),
                () -> super.onWrenched(state, context)
        );
    }

    @NotNull
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        return InteractionUtils.sequential(
                () -> ICopycatBlock.super.use(state, world, pos, player, hand, ray),
                () -> super.use(state, world, pos, player, hand, ray)
        );
    }

    @Nullable
    @Override
    public BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        return ICopycatBlock.super.getAcceptedBlockState(pLevel, pPos, item, face);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        ICopycatBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        ICopycatBlock.super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        super.playerWillDestroy(level, pos, state, player);
        ICopycatBlock.super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        return true;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return false;
    }

    @Override
    public BlockEntityType<? extends CopycatSlidingDoorBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT_SLIDING_DOOR_BLOCK_ENTITY.get();
    }

}
