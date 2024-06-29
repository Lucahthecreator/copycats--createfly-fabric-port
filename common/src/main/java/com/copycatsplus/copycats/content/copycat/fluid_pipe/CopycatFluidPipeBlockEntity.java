package com.copycatsplus.copycats.content.copycat.fluid_pipe;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.content.copycat.base.functional.ICopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


public abstract class CopycatFluidPipeBlockEntity extends FluidPipeBlockEntity implements ICopycatBlockEntity {

    protected CopycatBlockEntity copycatBlockEntity;

    public CopycatFluidPipeBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        copycatBlockEntity = new CopycatBlockEntity(CCBlockEntityTypes.COPYCAT.get(), pos, state);
    }

    @Override
    public CopycatBlockEntity getCopycatBlockEntity() {
        return copycatBlockEntity;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        ICopycatBlockEntity.super.setLevel(level);
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        ICopycatBlockEntity.super.read(compound, clientPacket);
    }

    @Override
    public void writeSafe(CompoundTag tag) {
        ICopycatBlockEntity.super.writeSafe(tag);
        super.writeSafe(tag);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        ICopycatBlockEntity.super.write(compound, clientPacket);
    }

    @Override
    public void callRedraw() {
        ICopycatBlockEntity.super.callRedraw();
    }
}

