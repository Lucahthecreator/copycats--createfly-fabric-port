package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.foundation.block.IBE;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public abstract class CCCopycatBlock extends Block implements IBE<CCCopycatBlockEntity>, ICopycatBlock {
    public CCCopycatBlock(Properties pProperties) {
        super(pProperties);
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
        return ICopycatBlock.super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        ICopycatBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        ICopycatBlock.super.playerWillDestroy(level, pos, state, player);
        return super.playerWillDestroy(level, pos, state, player);
    }

    @NotNull
    @Override
    public BlockState rotate(@NotNull BlockState state, Rotation rot) {
        return ICopycatBlock.super.rotateCopycat(state, rot);
    }

    @NotNull
    @Override
    public BlockState mirror(@NotNull BlockState state, Mirror mirror) {
        return ICopycatBlock.super.mirrorCopycat(state, mirror);
    }

    @Override
    public abstract BlockState transform(BlockState var1, StructureTransform var2);

    @Override
    public Class<CCCopycatBlockEntity> getBlockEntityClass() {
        return CCCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CCCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT.get();
    }

    public static BlockState getMaterial(BlockGetter level, BlockPos pos) {
        return ICopycatBlock.getMaterial(level, pos);
    }
}
