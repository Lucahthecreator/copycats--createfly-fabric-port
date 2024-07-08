package com.copycatsplus.copycats.mixin.copycat.sliding_door;

import com.copycatsplus.copycats.mixin_interfaces.SlidingDoorBlockEntityAccessor;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SlidingDoorBlockEntity.class)
public abstract class SlidingDoorBlockEntityMixin implements SlidingDoorBlockEntityAccessor {

    @Shadow protected abstract boolean shouldRenderSpecial(BlockState state);

    @Shadow LerpedFloat animation;

    @Override
    public boolean copycats$shouldRenderSpecial(BlockState state) {
        return shouldRenderSpecial(state);
    }

    @Override
    public LerpedFloat copycats$animation() {
        return animation;
    }
}
