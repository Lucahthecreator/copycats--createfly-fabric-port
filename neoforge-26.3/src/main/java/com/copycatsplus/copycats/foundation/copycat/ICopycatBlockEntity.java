package com.copycatsplus.copycats.foundation.copycat;

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
import com.zurrtum.create.content.schematics.requirement.ItemRequirement.ItemUseType;
import com.zurrtum.create.foundation.blockEntity.IMergeableBE;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;

@ParametersAreNonnullByDefault
public interface ICopycatBlockEntity extends SpecialBlockEntityItemRequirement, TransformableBlockEntity, PartialSafeNBT, IMergeableBE {
    void notifyUpdate();

    Level getLevel();

    BlockPos getBlockPos();

    BlockState getBlockState();

    HolderGetter<Block> blockHolderGetter();

    void setBlockState(BlockState var1);

    void setLevel(Level var1);

    BlockState getMaterial();

    ItemStack getConsumedItem();

    boolean isCTEnabled();

    @OverrideOnly
    void setMaterialInternal(BlockState var1);

    @OverrideOnly
    void setConsumedItemInternal(ItemStack var1);

    @OverrideOnly
    void setCTEnabledInternal(boolean var1);

    default void init() {
        this.setMaterialInternal(AllBlocks.COPYCAT_BASE.defaultBlockState());
        this.setConsumedItemInternal(ItemStack.EMPTY);
        this.setCTEnabledInternal(true);
    }

    default ICopycatBlock getBlock() {
        return this.getBlockState().getBlock() instanceof ICopycatBlock copycatBlock ? copycatBlock : new ICopycatBlock() {
            {
                Objects.requireNonNull(ICopycatBlockEntity.this);
            }
        };
    }

    default boolean hasCustomMaterial() {
        return !this.getMaterial().is(AllBlocks.COPYCAT_BASE);
    }

    default void setMaterial(BlockState blockState) {
        BlockState wrapperState = this.getBlockState();
        if (!this.getMaterial().is(blockState.getBlock())) {
            for (Direction side : Iterate.directions) {
                BlockPos neighbour = this.getBlockPos().relative(side);
                BlockState neighbourState = this.getLevel().getBlockState(neighbour);
                if (neighbourState == wrapperState && this.getLevel().getBlockEntity(neighbour) instanceof ICopycatBlockEntity cbe) {
                    BlockState otherMaterial = cbe.getMaterial();
                    if (otherMaterial.is(blockState.getBlock())) {
                        blockState = otherMaterial;
                        break;
                    }
                }
            }
        }

        this.setMaterialInternal(blockState);
        BlockEntityUtils.redraw((BlockEntity)this);
    }

    default boolean cycleMaterial() {
        BlockState material = this.getMaterial();
        if (material.hasProperty(TrapDoorBlock.HALF) && material.getOptionalValue(TrapDoorBlock.OPEN).orElse(false)) {
            this.setMaterial(material.cycle(TrapDoorBlock.HALF));
        } else if (material.hasProperty(BlockStateProperties.FACING)) {
            this.setMaterial(material.cycle(BlockStateProperties.FACING));
        } else if (material.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            this.setMaterial(
                material.setValue(BlockStateProperties.HORIZONTAL_FACING, material.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise())
            );
        } else if (material.hasProperty(BlockStateProperties.AXIS)) {
            this.setMaterial(material.cycle(BlockStateProperties.AXIS));
        } else if (material.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            this.setMaterial(material.cycle(BlockStateProperties.HORIZONTAL_AXIS));
        } else if (material.hasProperty(BlockStateProperties.LIT)) {
            this.setMaterial(material.cycle(BlockStateProperties.LIT));
        } else {
            if (!material.hasProperty(RoseQuartzLampBlock.POWERING)) {
                return false;
            }

            this.setMaterial(material.cycle(RoseQuartzLampBlock.POWERING));
        }

        return true;
    }

    default void setConsumedItem(ItemStack stack) {
        this.setConsumedItemInternal(ItemUtils.copyStackWithSize(stack, 1));
        this.notifyUpdate();
    }

    default void setCTEnabled(boolean value) {
        this.setCTEnabledInternal(value);
        this.notifyUpdate();
    }

    default void invalidate() {
        CopycatMaterialStore.setMaterial(this.getLevel(), this.getBlockPos(), Blocks.AIR.defaultBlockState());
    }

    @Override
    default ItemRequirement getRequiredItems(BlockState state) {
        return this.getConsumedItem().isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemUseType.CONSUME, this.getConsumedItem());
    }

    @Override
    default void accept(BlockEntity other) {
        if (other instanceof ICopycatBlockEntity be) {
            this.setMaterial(be.getMaterial());
            this.setConsumedItem(be.getConsumedItem());
            this.setCTEnabled(be.isCTEnabled());
            BlockEntityUtils.redraw((BlockEntity)this);
        }
    }

    @Override
    default void transform(BlockEntity blockEntity, StructureTransform transform) {
        this.setMaterialInternal(transform.apply(this.getMaterial()));
        this.notifyUpdate();
    }

    static void read(ICopycatBlockEntity self, ValueInput view, boolean clientPacket) {
        self.setCTEnabled(view.getBooleanOr("EnableCT", true));
        self.setConsumedItem(view.<ItemStack>read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        BlockState prevMaterial = self.getMaterial();
        BlockState material = view.<BlockState>read("Material", BlockState.CODEC).orElse(null);
        if (material == null) {
            self.setConsumedItem(ItemStack.EMPTY);
        } else {
            self.setMaterialInternal(material);
            if (self.getMaterial() != null && !clientPacket) {
                BlockState blockState = self.getBlockState();
                if (blockState == null) {
                    return;
                }

                if (!(blockState.getBlock() instanceof ICopycatBlock cb)) {
                    return;
                }

                BlockState acceptedBlockState = cb.getAcceptedBlockState(self.getLevel(), self.getBlockPos(), self.getConsumedItem(), null);
                if (acceptedBlockState != null && self.getMaterial().is(acceptedBlockState.getBlock())) {
                    return;
                }

                self.setConsumedItem(ItemStack.EMPTY);
                self.setMaterialInternal(AllBlocks.COPYCAT_BASE.defaultBlockState());
            }

            if (prevMaterial != self.getMaterial()) {
                BlockEntityUtils.redraw((BlockEntity)self);
            }
        }
    }

    static void writeSafe(ICopycatBlockEntity self, ValueOutput view) {
        ItemStack stackWithoutComponents = new ItemStack(self.getConsumedItem().typeHolder(), self.getConsumedItem().getCount(), DataComponentPatch.EMPTY);
        write(view, stackWithoutComponents, self.getMaterial(), self.isCTEnabled());
    }

    static void write(ICopycatBlockEntity self, ValueOutput view, boolean clientPacket) {
        write(view, self.getConsumedItem(), self.getMaterial(), self.isCTEnabled());
    }

    @Internal
    static void write(ValueOutput view, ItemStack stack, BlockState material, boolean enableCT) {
        if (!stack.isEmpty()) {
            view.store("Item", ItemStack.CODEC, stack);
        }

        view.store("Material", BlockState.CODEC, material);
        view.putBoolean("EnableCT", enableCT);
    }
}
