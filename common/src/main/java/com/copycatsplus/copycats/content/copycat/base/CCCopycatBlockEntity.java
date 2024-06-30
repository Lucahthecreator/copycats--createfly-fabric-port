package com.copycatsplus.copycats.content.copycat.base;

import java.util.List;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CCCopycatBlockEntity extends SmartBlockEntity implements ICopycatBlockEntity {

    protected BlockState material;
    protected ItemStack consumedItem;
    protected boolean enableCT;

    public CCCopycatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        ICopycatBlockEntity.super.init();
    }

    @Override
    public BlockState getMaterial() {
        return material;
    }

    @Override
    public ItemStack getConsumedItem() {
        return consumedItem;
    }

    @Override
    public boolean isCTEnabled() {
        return enableCT;
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
        enableCT = value;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        ICopycatBlockEntity.read(this, tag, clientPacket);
    }

    @Override
    public void writeSafe(CompoundTag tag) {
        super.writeSafe(tag);
        ICopycatBlockEntity.writeSafe(this, tag);
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        ICopycatBlockEntity.write(this, tag, clientPacket);
    }
}

