package com.copycatsplus.copycats.utility;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** Inventory return API for Minecraft 26.1 and 26.2. */
public final class CopycatInventoryCompat {
    private CopycatInventoryCompat() {
    }

    public static void returnItem(Player player, ItemStack stack) {
        player.getInventory().placeItemBackInInventory(stack);
    }
}
