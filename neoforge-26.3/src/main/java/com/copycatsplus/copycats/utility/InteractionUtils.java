package com.copycatsplus.copycats.utility;

import com.zurrtum.create.catnip.placement.IPlacementHelper;
import com.zurrtum.create.catnip.placement.PlacementHelpers;
import java.util.function.Supplier;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@ParametersAreNonnullByDefault
public class InteractionUtils {
    @SafeVarargs
    public static InteractionResult sequential(Supplier<InteractionResult>... handlers) {
        InteractionResult fallback = InteractionResult.PASS;

        for (Supplier<InteractionResult> handler : handlers) {
            InteractionResult result = handler.get();
            if (result.consumesAction()) {
                return result;
            }

            if (result == InteractionResult.TRY_WITH_EMPTY_HAND) {
                fallback = result;
            }
        }

        return fallback;
    }

    public static InteractionResult usePlacementHelper(
        int placementHelperId, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray
    ) {
        if (!player.isShiftKeyDown() && player.mayBuild()) {
            ItemStack heldItem = player.getItemInHand(hand);
            IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
            if (placementHelper.matchesItem(heldItem)) {
                placementHelper.getOffset(player, world, state, pos, ray).placeInWorld(world, (BlockItem)heldItem.getItem(), player, hand);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public static AttributeInstance getPlayerReach(Player player) {
        return null;
    }
}
