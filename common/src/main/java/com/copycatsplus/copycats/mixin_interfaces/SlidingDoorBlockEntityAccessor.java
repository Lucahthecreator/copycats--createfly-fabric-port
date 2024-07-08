package com.copycatsplus.copycats.mixin_interfaces;

import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.world.level.block.state.BlockState;

public interface SlidingDoorBlockEntityAccessor {

    boolean copycats$shouldRenderSpecial(BlockState state);

    LerpedFloat copycats$animation();
}
