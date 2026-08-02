package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Allows either of the two cogwheel slots to be replaced without creating or aliasing a third slot. */
@Mixin(CopycatCogWheelBlock.class)
public abstract class CopycatCogWheelMaterialMixin {
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void copycats$replaceSelectedMaterial(ItemStack heldStack, BlockState state, Level level,
                                                  BlockPos pos, Player player, InteractionHand hand,
                                                  BlockHitResult hit,
                                                  CallbackInfoReturnable<InteractionResult> cir) {
        if (!player.mayBuild()) {
            return;
        }

        IMultiStateCopycatBlock block = (IMultiStateCopycatBlock) this;
        String property = block.getPropertyFromInteraction(state, (BlockGetter) level, pos, hit, true);
        IMultiStateCopycatBlockEntity blockEntity = block.getCopycatBlockEntity((BlockGetter) level, pos);
        if (blockEntity == null || !block.partExists(state, property)) {
            return;
        }

        MaterialItemStorage.MaterialItem oldItem = blockEntity.getMaterialItemStorage().getMaterialItem(property);
        if (oldItem == null || !oldItem.hasCustomMaterial()) {
            return;
        }

        BlockState material = block.getAcceptedBlockState(property, level, pos, heldStack, hit.getDirection());
        if (material != null) {
            material = block.prepareMaterial(level, pos, state, player, hand, hit, material);
        }
        if (material == null || oldItem.material().is(material.getBlock())) {
            return;
        }

        if (level.isClientSide()) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        boolean freeToApply = blockEntity.getMaterialItemStorage().getAllConsumedItems().stream()
                .anyMatch(stack -> stack.getItem() == heldStack.getItem());
        ItemStack refund = oldItem.consumedItem();

        // Preserve shared-material ownership: another slot using the old material keeps its one paid item.
        if (!refund.isEmpty()) {
            for (String otherProperty : blockEntity.getMaterialItemStorage().getAllProperties()) {
                if (otherProperty.equals(property)) {
                    continue;
                }
                MaterialItemStorage.MaterialItem other = blockEntity.getMaterialItemStorage()
                        .getMaterialItem(otherProperty);
                if (other != null && other.consumedItem().isEmpty()
                        && other.material().getBlock() == oldItem.material().getBlock()) {
                    blockEntity.setConsumedItem(otherProperty, refund);
                    refund = ItemStack.EMPTY;
                    break;
                }
            }
        }

        blockEntity.setConsumedItem(property, ItemStack.EMPTY);
        if (!player.isCreative() && !refund.isEmpty()) {
            player.getInventory().placeItemBackInInventory(refund);
        }

        blockEntity.setMaterial(property, material);
        if (!freeToApply) {
            blockEntity.setConsumedItem(property, heldStack);
        }
        blockEntity.getLevel().playSound(null, blockEntity.getBlockPos(),
                material.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0f, 0.75f);

        if (!player.isCreative() && !freeToApply) {
            heldStack.shrink(1);
            if (heldStack.isEmpty()) {
                player.setItemInHand(hand, ItemStack.EMPTY);
            }
        }
        cir.setReturnValue(InteractionResult.SUCCESS);
    }
}
