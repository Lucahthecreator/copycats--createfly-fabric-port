package com.copycatsplus.copycats.utility.neoforge;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class InteractionUtilsImpl {

    public static AttributeInstance getPlayerReach(Player player) {
       return player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
    }
}
