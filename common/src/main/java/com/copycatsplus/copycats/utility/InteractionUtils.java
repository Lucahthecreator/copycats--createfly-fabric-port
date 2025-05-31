package com.copycatsplus.copycats.utility;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class InteractionUtils {
    /**
     * Sequentially runs a list of handlers until one consumes the action.
     */
    @SafeVarargs
    public static InteractionResult sequential(Supplier<InteractionResult>... handlers) {
        for (Supplier<InteractionResult> handler : handlers) {
            InteractionResult result = handler.get();
            if (result.consumesAction()) {
                return result;
            }
        }
        return InteractionResult.PASS;
    }

    /**
     * Sequentially runs a list of handlers until one consumes the action.
     */
    @SafeVarargs
    public static ItemInteractionResult sequentialItem(Supplier<ItemInteractionResult>... handlers) {
        for (Supplier<ItemInteractionResult> handler : handlers) {
            ItemInteractionResult result = handler.get();
            if (result.consumesAction()) {
                return result;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /**
     * Handles the use action with a registered placement helper.
     */
    public static ItemInteractionResult usePlacementHelper(int placementHelperId, ItemStack heldItem, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (!player.isShiftKeyDown() && player.mayBuild()) {
            IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
            if (placementHelper.matchesItem(heldItem)) {
                placementHelper.getOffset(player, world, state, pos, ray)
                        .placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @ExpectPlatform
    public static AttributeInstance getPlayerReach(Player player) {
        throw new AssertionError("This shouldn't ever be seen");
    }
}
