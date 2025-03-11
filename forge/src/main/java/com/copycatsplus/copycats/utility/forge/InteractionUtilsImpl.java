package com.copycatsplus.copycats.utility.forge;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

public class InteractionUtilsImpl {

    public static AttributeInstance getPlayerReach(Player player) {
       return player.getAttribute(ForgeMod.BLOCK_REACH.get());
    }
}
