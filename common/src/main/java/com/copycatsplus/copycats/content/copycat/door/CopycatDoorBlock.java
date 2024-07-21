package com.copycatsplus.copycats.content.copycat.door;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Set;

public class CopycatDoorBlock extends DoorBlock implements IMultiStateCopycatBlock, IBE<MultiStateCopycatBlockEntity>, IStateType {

    public CopycatDoorBlock(Properties properties, BlockSetType type) {
        super(properties, type);
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<S> p_153214_) {
        return null;
    }

    @Override
    public @NotNull String defaultProperty() {
        return DoubleBlockHalf.LOWER.getSerializedName();
    }

    @Override
    public @NotNull Vec3i vectorScale(BlockState state) {
        return new Vec3i(1, 1, 1);
    }

    @Override
    public @NotNull Set<String> storageProperties() {
        return Set.of(DoubleBlockHalf.LOWER.getSerializedName(), DoubleBlockHalf.UPPER.getSerializedName());
    }

    @Override
    public int getColorIndex(String property) {
        return 0;
    }

    @Override
    public boolean partExists(BlockState state, String property) {
       return switch (state.getValue(HALF)) {
           case UPPER -> property.equals(DoubleBlockHalf.UPPER.getSerializedName());
           case LOWER -> property.equals(DoubleBlockHalf.LOWER.getSerializedName());
       };
    }

    @Override
    public @NotNull Vec3i getVectorFromProperty(BlockState state, String property) {
        return new Vec3i(1, 1, 1);
    }

    @Override
    public @NotNull String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        return state.getValue(HALF).getSerializedName();
    }

    @Override
    public InteractionResult use(BlockState state,
                                 Level level,
                                 BlockPos pos,
                                 Player player,
                                 InteractionHand hand,
                                 BlockHitResult hit) {
        return InteractionUtils.sequential(
                () -> IMultiStateCopycatBlock.super.use(state, level, pos, player, hand, hit),
                () -> super.use(state, level, pos, player, hand, hit),
                () -> migrateDataDown(state, level, pos)
        );
    }

    private InteractionResult migrateDataDown(BlockState state, Level level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            withBlockEntityDo(level, pos, be -> {
                String prop = DoubleBlockHalf.UPPER.getSerializedName();
                IMultiStateCopycatBlockEntity belowBE = (IMultiStateCopycatBlockEntity) level.getBlockEntity(pos.below());
                assert belowBE != null;
                belowBE.setConsumedItem(prop, be.getMaterialItemStorage().getMaterialItem(prop).consumedItem());
                belowBE.setMaterial(prop, be.getMaterialItemStorage().getMaterialItem(prop).material());
                belowBE.notifyUpdate();
            });
        }
        return InteractionResult.SUCCESS;
    }

    private InteractionResult migrateDataDown(BlockState state, UseOnContext context) {
        return migrateDataDown(state, context.getLevel(), context.getClickedPos());
    }

    @Override
    public @NotNull InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(
                () -> IMultiStateCopycatBlock.super.onWrenched(state, context),
                () -> migrateDataDown(state, context));
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        IMultiStateCopycatBlock.super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        IMultiStateCopycatBlock.super.setPlacedBy(pLevel, pPos.above(), pState.setValue(HALF, DoubleBlockHalf.UPPER), pPlacer, pStack);
        migrateDataDown(pState.setValue(HALF, DoubleBlockHalf.UPPER), pLevel, pPos.above());
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        IMultiStateCopycatBlock.super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        IMultiStateCopycatBlock.super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {

    }

    @Override
    public Class<MultiStateCopycatBlockEntity> getBlockEntityClass() {
        return MultiStateCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MultiStateCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.MULTI_STATE_COPYCAT.get();
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                             BlockPos fromPos, BlockPos toPos) {
        return true;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return false;
    }

    @Override
    public boolean isIgnoredConnectivitySide(String property, BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        return false;
    }

    @Override
    public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return false;
    }


    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }
}
