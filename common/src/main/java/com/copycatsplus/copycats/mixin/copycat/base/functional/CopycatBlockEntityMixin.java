package com.copycatsplus.copycats.mixin.copycat.base.functional;

import com.copycatsplus.copycats.content.copycat.base.functional.ICopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CopycatBlockEntity.class)
public abstract class CopycatBlockEntityMixin implements ICopycatBlockEntity {
    @Override
    public CopycatBlockEntity getCopycatBlockEntity() {
        return (CopycatBlockEntity) (Object) this;
    }
}
