/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.storage.ValueInput
 *  net.minecraft.world.level.storage.ValueOutput
 */
package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

@ParametersAreNonnullByDefault
public class CopycatCogWheelBlockEntity
extends BracketedKineticBlockEntity
implements IMultiStateCopycatBlockEntity {
    private MaterialItemStorage storage;

    public CopycatCogWheelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(pos, state);
        this.type = type;
        this.init();
    }

    @Override
    public MaterialItemStorage getMaterialItemStorage() {
        return this.storage;
    }

    @Override
    public void setMaterialItemStorageInternal(MaterialItemStorage storage) {
        this.storage = storage;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        IMultiStateCopycatBlockEntity.super.invalidate();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        return IMultiStateCopycatBlockEntity.super.getRequiredItems(state).union(super.getRequiredItems(state));
    }

    public void preRemoveSideEffects(BlockPos pos, BlockState oldState) {
        super.preRemoveSideEffects(pos, oldState);
        this.storage.getAllConsumedItems().forEach(stack -> Block.popResource((Level)this.level, (BlockPos)pos, (ItemStack)stack));
    }

    @Override
    public void transform(BlockEntity blockEntity, StructureTransform transform) {
        super.transform(blockEntity, transform);
        IMultiStateCopycatBlockEntity.super.transform(blockEntity, transform);
    }

    protected void read(ValueInput view, boolean clientPacket) {
        super.read(view, clientPacket);
        IMultiStateCopycatBlockEntity.read(this, view, clientPacket);
    }

    public void writeSafe(ValueOutput view) {
        super.writeSafe(view);
        IMultiStateCopycatBlockEntity.writeSafe(this, view);
    }

    protected void write(ValueOutput view, boolean clientPacket) {
        super.write(view, clientPacket);
        IMultiStateCopycatBlockEntity.write(this, view, clientPacket);
    }
}

