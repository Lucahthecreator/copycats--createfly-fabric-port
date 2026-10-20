package com.copycatsplus.copycats.content.copycat.fluid_pipe;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.zurrtum.create.content.decoration.bracket.BracketBlock;
import com.zurrtum.create.content.fluids.pipes.FluidPipeBlock;
import com.zurrtum.create.content.fluids.pipes.FluidPipeBlockEntity;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
public class CopycatFluidPipeBlock extends FluidPipeBlock implements ICopycatBlock {
    public CopycatFluidPipeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(() -> ICopycatBlock.super.onSneakWrenched(state, context), () -> super.onSneakWrenched(state, context));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(() -> ICopycatBlock.super.onWrenched(state, context), () -> super.onWrenched(state, context));
    }

    @Override
    protected InteractionResult useItemOn(
        ItemStack heldStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray
    ) {
        return InteractionUtils.sequential(
            () -> ICopycatBlock.super.use(state, world, pos, player, hand, ray), () -> super.useItemOn(heldStack, state, world, pos, player, hand, ray)
        );
    }

    @Nullable
    @Override
    public BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        Item item2 = item.getItem();
        BlockItem bi;
        return item2 instanceof BlockItem && (bi = (BlockItem)item2).getBlock() instanceof BracketBlock
            ? null
            : ICopycatBlock.super.getAcceptedBlockState(pLevel, pPos, item, face);
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

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return super.rotate(pState, pRotation);
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return super.mirror(pState, pMirror);
    }

    @Override
    public boolean isIgnoredConnectivitySide(
        BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState
    ) {
        return true;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return false;
    }

    @Override
    public BlockEntityType<? extends FluidPipeBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT_FLUID_PIPE.get();
    }
}
