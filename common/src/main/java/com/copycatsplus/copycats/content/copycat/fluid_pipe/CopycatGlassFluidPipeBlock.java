package com.copycatsplus.copycats.content.copycat.fluid_pipe;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CopycatGlassFluidPipeBlock extends GlassFluidPipeBlock implements IFunctionalCopycatBlock {
    public CopycatGlassFluidPipeBlock(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        InteractionResult result = IFunctionalCopycatBlock.super.onSneakWrenched(state, context);
        if (result.consumesAction()) {
            return result;
        }
        return super.onSneakWrenched(state, context);
    }

    @Override
    public @NotNull InteractionResult onWrenched(@NotNull BlockState state, @NotNull UseOnContext context) {
        InteractionResult result = IFunctionalCopycatBlock.super.onWrenched(state, context);
        if (result.consumesAction()) {
            return result;
        }
        return super.onWrenched(state, context);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        InteractionResult result = IFunctionalCopycatBlock.super.use(state, world, pos, player, hand, ray);
        if (result.consumesAction()) {
            return result;
        }

        return super.use(state, world, pos, player, hand, ray);
    }

    @Nullable
    @Override
    public BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        if (item.getItem() instanceof BlockItem bi) {
            if (bi.getBlock() instanceof BracketBlock) return null;
        }

        return IFunctionalCopycatBlock.super.getAcceptedBlockState(pLevel, pPos, item, face);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        IFunctionalCopycatBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        IFunctionalCopycatBlock.super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        super.playerWillDestroy(level, pos, state, player);
        IFunctionalCopycatBlock.super.playerWillDestroy(level, pos, state, player);
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
    public BlockState toRegularPipe(LevelAccessor world, BlockPos pos, BlockState state) {
        Direction side = Direction.get(Direction.AxisDirection.POSITIVE, state.getValue(AXIS));
        Map<Direction, BooleanProperty> facingToPropertyMap = FluidPipeBlock.PROPERTY_BY_DIRECTION;
        return CCBlocks.COPYCAT_FLUID_PIPE.get()
                .updateBlockState(CCBlocks.COPYCAT_FLUID_PIPE.getDefaultState()
                        .setValue(facingToPropertyMap.get(side), true)
                        .setValue(facingToPropertyMap.get(side.getOpposite()), true), side, null, world, pos);
    }

    @Override
    public @NotNull BlockEntityType<? extends StraightPipeBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT_GLASS_FLUID_PIPE.get();
    }
}
