package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.zurrtum.create.content.decoration.copycat.CopycatBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Gives CreateFly's copycat block entity the same CT/material interface as Copycats+ block entities.
 */
@Mixin(value = CopycatBlockEntity.class, remap = false)
public abstract class CopycatBlockEntityMixin implements ICopycatBlockEntity {
    @Shadow
    private BlockState material;

    @Shadow
    private ItemStack consumedItem;

    @Unique
    private boolean copycats$enableCT = true;

    @Override
    public boolean isCTEnabled() {
        return copycats$enableCT;
    }

    @Override
    public void setMaterialInternal(BlockState material) {
        this.material = material;
    }

    @Override
    public void setConsumedItemInternal(ItemStack consumedItem) {
        this.consumedItem = consumedItem;
    }

    @Override
    public void setCTEnabledInternal(boolean value) {
        this.copycats$enableCT = value;
    }

    @Inject(
            at = @At("HEAD"),
            method = "write(Lnet/minecraft/world/level/storage/ValueOutput;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;)V"
    )
    private void copycats$writeCT(ValueOutput view, ItemStack stack, BlockState material, CallbackInfo ci) {
        view.putBoolean("EnableCT", copycats$enableCT);
    }

    @Inject(
            at = @At("HEAD"),
            method = "read(Lnet/minecraft/world/level/storage/ValueInput;Z)V"
    )
    private void copycats$readCT(ValueInput view, boolean clientPacket, CallbackInfo ci) {
        copycats$enableCT = view.getBooleanOr("EnableCT", true);
    }
}
