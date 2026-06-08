/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.AllBlocks
 *  com.zurrtum.create.api.contraption.transformable.TransformableBlockEntity
 *  com.zurrtum.create.api.schematic.nbt.PartialSafeNBT
 *  com.zurrtum.create.api.schematic.requirement.SpecialBlockEntityItemRequirement
 *  com.zurrtum.create.catnip.data.Iterate
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.content.redstone.RoseQuartzLampBlock
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement$ItemUseType
 *  com.zurrtum.create.foundation.blockEntity.IMergeableBE
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.HolderGetter
 *  net.minecraft.core.component.DataComponentPatch
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.TrapDoorBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.storage.ValueInput
 *  net.minecraft.world.level.storage.ValueOutput
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.ApiStatus$OverrideOnly
 */
package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.CopycatMaterialStore;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.copycatsplus.copycats.utility.ItemUtils;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.api.contraption.transformable.TransformableBlockEntity;
import com.zurrtum.create.api.schematic.nbt.PartialSafeNBT;
import com.zurrtum.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.redstone.RoseQuartzLampBlock;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import com.zurrtum.create.foundation.blockEntity.IMergeableBE;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.ApiStatus;

@ParametersAreNonnullByDefault
public interface ICopycatBlockEntity
extends SpecialBlockEntityItemRequirement,
TransformableBlockEntity,
PartialSafeNBT,
IMergeableBE {
    public void notifyUpdate();

    public Level getLevel();

    public BlockPos getBlockPos();

    public BlockState getBlockState();

    public HolderGetter<Block> blockHolderGetter();

    public void setBlockState(BlockState var1);

    public void setLevel(Level var1);

    public BlockState getMaterial();

    public ItemStack getConsumedItem();

    public boolean isCTEnabled();

    @ApiStatus.OverrideOnly
    public void setMaterialInternal(BlockState var1);

    @ApiStatus.OverrideOnly
    public void setConsumedItemInternal(ItemStack var1);

    @ApiStatus.OverrideOnly
    public void setCTEnabledInternal(boolean var1);

    default public void init() {
        this.setMaterialInternal(AllBlocks.COPYCAT_BASE.defaultBlockState());
        this.setConsumedItemInternal(ItemStack.EMPTY);
        this.setCTEnabledInternal(true);
    }

    default public ICopycatBlock getBlock() {
        Block block = this.getBlockState().getBlock();
        if (block instanceof ICopycatBlock) {
            ICopycatBlock copycatBlock = (ICopycatBlock)block;
            return copycatBlock;
        }
        return new ICopycatBlock(this){
            {
                Objects.requireNonNull(this$0);
            }
        };
    }

    default public boolean hasCustomMaterial() {
        return !this.getMaterial().is((Object)AllBlocks.COPYCAT_BASE);
    }

    default public void setMaterial(BlockState blockState) {
        BlockState wrapperState = this.getBlockState();
        if (!this.getMaterial().is((Object)blockState.getBlock())) {
            for (Direction side : Iterate.directions) {
                ICopycatBlockEntity cbe;
                BlockState otherMaterial;
                BlockEntity blockEntity;
                BlockPos neighbour = this.getBlockPos().relative(side);
                BlockState neighbourState = this.getLevel().getBlockState(neighbour);
                if (neighbourState != wrapperState || !((blockEntity = this.getLevel().getBlockEntity(neighbour)) instanceof ICopycatBlockEntity) || !(otherMaterial = (cbe = (ICopycatBlockEntity)blockEntity).getMaterial()).is((Object)blockState.getBlock())) continue;
                blockState = otherMaterial;
                break;
            }
        }
        this.setMaterialInternal(blockState);
        BlockEntityUtils.redraw((BlockEntity)this);
    }

    default public boolean cycleMaterial() {
        BlockState material = this.getMaterial();
        if (material.hasProperty((Property)TrapDoorBlock.HALF) && material.getOptionalValue((Property)TrapDoorBlock.OPEN).orElse(false).booleanValue()) {
            this.setMaterial((BlockState)material.cycle((Property)TrapDoorBlock.HALF));
        } else if (material.hasProperty((Property)BlockStateProperties.FACING)) {
            this.setMaterial((BlockState)material.cycle((Property)BlockStateProperties.FACING));
        } else if (material.hasProperty((Property)BlockStateProperties.HORIZONTAL_FACING)) {
            this.setMaterial((BlockState)material.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)((Direction)material.getValue((Property)BlockStateProperties.HORIZONTAL_FACING)).getClockWise()));
        } else if (material.hasProperty((Property)BlockStateProperties.AXIS)) {
            this.setMaterial((BlockState)material.cycle((Property)BlockStateProperties.AXIS));
        } else if (material.hasProperty((Property)BlockStateProperties.HORIZONTAL_AXIS)) {
            this.setMaterial((BlockState)material.cycle((Property)BlockStateProperties.HORIZONTAL_AXIS));
        } else if (material.hasProperty((Property)BlockStateProperties.LIT)) {
            this.setMaterial((BlockState)material.cycle((Property)BlockStateProperties.LIT));
        } else if (material.hasProperty((Property)RoseQuartzLampBlock.POWERING)) {
            this.setMaterial((BlockState)material.cycle((Property)RoseQuartzLampBlock.POWERING));
        } else {
            return false;
        }
        return true;
    }

    default public void setConsumedItem(ItemStack stack) {
        this.setConsumedItemInternal(ItemUtils.copyStackWithSize(stack, 1));
        this.notifyUpdate();
    }

    default public void setCTEnabled(boolean value) {
        this.setCTEnabledInternal(value);
        this.notifyUpdate();
    }

    default public void invalidate() {
        CopycatMaterialStore.setMaterial((BlockGetter)this.getLevel(), this.getBlockPos(), Blocks.AIR.defaultBlockState());
    }

    default public ItemRequirement getRequiredItems(BlockState state) {
        if (this.getConsumedItem().isEmpty()) {
            return ItemRequirement.NONE;
        }
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, this.getConsumedItem());
    }

    default public void accept(BlockEntity other) {
        if (other instanceof ICopycatBlockEntity) {
            ICopycatBlockEntity be = (ICopycatBlockEntity)other;
            this.setMaterial(be.getMaterial());
            this.setConsumedItem(be.getConsumedItem());
            this.setCTEnabled(be.isCTEnabled());
            BlockEntityUtils.redraw((BlockEntity)this);
        }
    }

    default public void transform(BlockEntity blockEntity, StructureTransform transform) {
        this.setMaterialInternal(transform.apply(this.getMaterial()));
        this.notifyUpdate();
    }

    public static void read(ICopycatBlockEntity self, ValueInput view, boolean clientPacket) {
        self.setCTEnabled(view.getBooleanOr("EnableCT", true));
        self.setConsumedItem(view.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        BlockState prevMaterial = self.getMaterial();
        BlockState material = view.read("Material", BlockState.CODEC).orElse(null);
        if (material == null) {
            self.setConsumedItem(ItemStack.EMPTY);
            return;
        }
        self.setMaterialInternal(material);
        if (self.getMaterial() != null && !clientPacket) {
            BlockState blockState = self.getBlockState();
            if (blockState == null) {
                return;
            }
            Block block = blockState.getBlock();
            if (!(block instanceof ICopycatBlock)) {
                return;
            }
            ICopycatBlock cb = (ICopycatBlock)block;
            BlockState acceptedBlockState = cb.getAcceptedBlockState(self.getLevel(), self.getBlockPos(), self.getConsumedItem(), null);
            if (acceptedBlockState != null && self.getMaterial().is((Object)acceptedBlockState.getBlock())) {
                return;
            }
            self.setConsumedItem(ItemStack.EMPTY);
            self.setMaterialInternal(AllBlocks.COPYCAT_BASE.defaultBlockState());
        }
        if (prevMaterial != self.getMaterial()) {
            BlockEntityUtils.redraw((BlockEntity)self);
        }
    }

    public static void writeSafe(ICopycatBlockEntity self, ValueOutput view) {
        ItemStack stackWithoutComponents = new ItemStack(self.getConsumedItem().typeHolder(), self.getConsumedItem().getCount(), DataComponentPatch.EMPTY);
        ICopycatBlockEntity.write(view, stackWithoutComponents, self.getMaterial(), self.isCTEnabled());
    }

    public static void write(ICopycatBlockEntity self, ValueOutput view, boolean clientPacket) {
        ICopycatBlockEntity.write(view, self.getConsumedItem(), self.getMaterial(), self.isCTEnabled());
    }

    @ApiStatus.Internal
    public static void write(ValueOutput view, ItemStack stack, BlockState material, boolean enableCT) {
        if (!stack.isEmpty()) {
            view.store("Item", ItemStack.CODEC, (Object)stack);
        }
        view.store("Material", BlockState.CODEC, (Object)material);
        view.putBoolean("EnableCT", enableCT);
    }
}

