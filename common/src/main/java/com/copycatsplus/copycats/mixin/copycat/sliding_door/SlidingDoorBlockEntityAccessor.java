package com.copycatsplus.copycats.mixin.copycat.sliding_door;

import com.zurrtum.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.zurrtum.create.catnip.animation.LerpedFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SlidingDoorBlockEntity.class)
public interface SlidingDoorBlockEntityAccessor {
    @Accessor
    LerpedFloat getAnimation();
}
