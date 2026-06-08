/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.content.fluids.pipes.StraightPipeBlockEntity
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.storage.ValueInput
 *  net.minecraft.world.level.storage.ValueOutput
 */
package com.copycatsplus.copycats.content.copycat.fluid_pipe;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.zurrtum.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class CopycatStraightPipeBlockEntity
extends StraightPipeBlockEntity
implements ICopycatBlockEntity {
    protected BlockState material;
    protected ItemStack consumedItem;
    protected boolean enableCT;

    public CopycatStraightPipeBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(pos, state);
        this.type = typeIn;
        ICopycatBlockEntity.super.init();
    }

    @Override
    public BlockState getMaterial() {
        return this.material;
    }

    @Override
    public ItemStack getConsumedItem() {
        return this.consumedItem;
    }

    @Override
    public boolean isCTEnabled() {
        return this.enableCT;
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
        this.enableCT = value;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        ICopycatBlockEntity.super.invalidate();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        return ICopycatBlockEntity.super.getRequiredItems(state).union(super.getRequiredItems(state));
    }

    public void preRemoveSideEffects(BlockPos pos, BlockState oldState) {
        super.preRemoveSideEffects(pos, oldState);
        Block.popResource((Level)this.level, (BlockPos)pos, (ItemStack)this.consumedItem);
    }

    protected void read(ValueInput view, boolean clientPacket) {
        super.read(view, clientPacket);
        ICopycatBlockEntity.read(this, view, clientPacket);
    }

    public void writeSafe(ValueOutput view) {
        super.writeSafe(view);
        ICopycatBlockEntity.writeSafe(this, view);
    }

    protected void write(ValueOutput view, boolean clientPacket) {
        super.write(view, clientPacket);
        ICopycatBlockEntity.write(this, view, clientPacket);
    }
}

