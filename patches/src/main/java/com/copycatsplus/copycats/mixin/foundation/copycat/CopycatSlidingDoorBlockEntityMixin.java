package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.AnimatedDoorBreakContext;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import com.zurrtum.create.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

/** Gives the single sliding-door block entity two independently paid material slots. */
@Mixin(CopycatSlidingDoorBlockEntity.class)
public abstract class CopycatSlidingDoorBlockEntityMixin implements IMultiStateCopycatBlockEntity {
    @Unique private static final String COPYCATS$LOWER = "lower";
    @Unique private static final String COPYCATS$UPPER = "upper";

    @Shadow protected BlockState material;
    @Shadow protected ItemStack consumedItem;
    @Shadow protected boolean enableCT;
    @Unique private MaterialItemStorage copycats$materials;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void copycats$initSeparateMaterials(CallbackInfo ci) {
        copycats$materials = MaterialItemStorage.create(Set.of(COPYCATS$LOWER, COPYCATS$UPPER));
    }

    @Override public MaterialItemStorage getMaterialItemStorage() { return copycats$materials; }
    @Override public void setMaterialItemStorageInternal(MaterialItemStorage storage) { copycats$materials = storage; }

    @Override public BlockState getMaterial() {
        return copycats$materials == null ? material : copycats$materials.getMaterialItem(COPYCATS$LOWER).material();
    }
    @Override public ItemStack getConsumedItem() {
        return copycats$materials == null ? consumedItem : copycats$materials.getMaterialItem(COPYCATS$LOWER).consumedItem();
    }
    @Override public boolean isCTEnabled() {
        return copycats$materials == null ? enableCT : copycats$materials.getMaterialItem(COPYCATS$LOWER).enableCT();
    }
    @Override public void setMaterialInternal(BlockState value) {
        if (copycats$materials == null) material = value;
        else copycats$materials.getMaterialItem(COPYCATS$LOWER).setMaterial(value);
    }
    @Override public void setConsumedItemInternal(ItemStack value) {
        if (AnimatedDoorBreakContext.preserveMaterials() && value.isEmpty())
            return;
        if (copycats$materials == null) consumedItem = value;
        else copycats$materials.getMaterialItem(COPYCATS$LOWER).setConsumedItem(value);
    }
    @Override public void setCTEnabledInternal(boolean value) {
        if (copycats$materials == null) enableCT = value;
        else copycats$materials.getMaterialItem(COPYCATS$LOWER).setEnableCT(value);
    }

    @Inject(method = "read", at = @At("TAIL"))
    private void copycats$readSeparateMaterials(ValueInput view, boolean clientPacket, CallbackInfo ci) {
        IMultiStateCopycatBlockEntity.read(this, view, clientPacket);
    }
    @Inject(method = "writeSafe", at = @At("TAIL"))
    private void copycats$writeSafeSeparateMaterials(ValueOutput view, CallbackInfo ci) {
        IMultiStateCopycatBlockEntity.writeSafe(this, view);
    }
    @Inject(method = "write", at = @At("TAIL"))
    private void copycats$writeSeparateMaterials(ValueOutput view, boolean clientPacket, CallbackInfo ci) {
        IMultiStateCopycatBlockEntity.write(this, view, clientPacket);
    }

    @Inject(method = "preRemoveSideEffects", at = @At("HEAD"))
    private void copycats$dropSeparateMaterials(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (copycats$materials == null)
            return;
        Level level = ((net.minecraft.world.level.block.entity.BlockEntity) (Object) this).getLevel();
        if (level == null || level.isClientSide())
            return;
        copycats$materials.getAllConsumedItems().forEach(stack -> Block.popResource(level, pos, stack));
        copycats$materials.getAllProperties().forEach(property -> copycats$materials.getMaterialItem(property).setConsumedItem(ItemStack.EMPTY));
    }

}
