package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlock;
import com.copycatsplus.copycats.foundation.copycat.AnimatedDoorBreakContext;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.AllTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(CopycatSlidingDoorBlock.class)
public abstract class CopycatSlidingDoorBlockMixin implements IMultiStateCopycatBlock {
    @Override public String defaultProperty() { return "lower"; }
    @Override public Set<String> storageProperties() { return Set.of("lower", "upper"); }
    @Override public Vec3i vectorScale(BlockState state) { return new Vec3i(1, 1, 1); }
    @Override public int getColorIndex(String property) { return 0; }
    @Override public boolean partExists(BlockState state, String property) { return storageProperties().contains(property); }
    @Override public Vec3i getVectorFromProperty(BlockState state, String property) { return Vec3i.ZERO; }
    @Override public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hit, BlockPos pos, Direction face, Vec3 unscaledHit) {
        return state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? "upper" : "lower";
    }
    @Override public String getPropertyFromRender(String property, BlockState state, BlockGetter level, Vec3i vector, BlockPos pos) {
        return state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? "upper" : "lower";
    }
    @Override public VoxelShape getPartialFaceShape(BlockGetter level, BlockState state, String property, Direction face) { return state.getFaceOcclusionShape(face); }
    @Override public boolean canConnectTexturesToward(String property, net.minecraft.client.renderer.block.BlockAndTintGetter reader, BlockPos from, BlockPos to, BlockState state) { return false; }
    @Override public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, com.zurrtum.create.content.contraptions.StructureTransform transform) { }
    @Override public IMultiStateCopycatBlockEntity getCopycatBlockEntity(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockPos lower = state.getBlock() instanceof CopycatSlidingDoorBlock && state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
        BlockEntity be = level.getBlockEntity(lower);
        return be instanceof IMultiStateCopycatBlockEntity multi ? multi : null;
    }

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void copycats$skinHalf(ItemStack held, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (!player.mayBuild() || held.is(AllTags.AllItemTags.WRENCH.tag)) return;
        String property = getPropertyFromInteraction(state, level, pos, hit, true);
        IMultiStateCopycatBlockEntity be = getCopycatBlockEntity(level, pos);
        if (be == null) return;
        BlockState material = getAcceptedBlockState(property, level, pos, held, hit.getDirection());
        if (material == null) return;
        material = prepareMaterial(level, pos, state, player, hand, hit, material);
        MaterialItemStorage.MaterialItem slot = be.getMaterialItemStorage().getMaterialItem(property);
        if (slot == null) return;
        if (slot.material().is(material.getBlock())) {
            if (be.cycleMaterial(property)) cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }
        if (slot.hasCustomMaterial()) return;
        if (level.isClientSide()) { cir.setReturnValue(InteractionResult.SUCCESS); return; }
        be.setMaterial(property, material);
        if (!player.isCreative()) be.setConsumedItem(property, held);
        level.playSound(null, be.getBlockPos(), material.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1, .75f);
        if (!player.isCreative()) { held.shrink(1); if (held.isEmpty()) player.setItemInHand(hand, ItemStack.EMPTY); }
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    @Inject(method = "onWrenched", at = @At("HEAD"), cancellable = true)
    private void copycats$removeHalf(BlockState state, UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        String property = getPropertyFromInteraction(state, context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), true);
        IMultiStateCopycatBlockEntity be = getCopycatBlockEntity(context.getLevel(), context.getClickedPos());
        if (be == null) return;
        MaterialItemStorage.MaterialItem slot = be.getMaterialItemStorage().getMaterialItem(property);
        if (slot == null || !slot.hasCustomMaterial()) return;
        Player player = context.getPlayer();
        if (!player.isCreative()) player.getInventory().placeItemBackInInventory(slot.consumedItem());
        context.getLevel().levelEvent(2001, context.getClickedPos(), Block.getId(slot.material()));
        be.setMaterial(property, AllBlocks.COPYCAT_BASE.defaultBlockState());
        be.setConsumedItem(property, ItemStack.EMPTY);
        BlockEntityUtils.redraw((BlockEntity) be);
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

    @Inject(method = "playerWillDestroy", at = @At("HEAD"))
    private void copycats$beginCreativeMaterialReturn(Level level, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> cir) {
        AnimatedDoorBreakContext.preserveMaterials(player.isCreative());
    }

    @Inject(method = "playerWillDestroy", at = @At("TAIL"))
    private void copycats$finishCreativeMaterialReturn(Level level, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> cir) {
        AnimatedDoorBreakContext.preserveMaterials(false);
    }

}
