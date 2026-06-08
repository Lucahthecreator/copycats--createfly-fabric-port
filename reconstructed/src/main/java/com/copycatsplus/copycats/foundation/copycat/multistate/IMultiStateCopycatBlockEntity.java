/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.AllBlocks
 *  com.zurrtum.create.catnip.data.Iterate
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.content.redstone.RoseQuartzLampBlock
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement$ItemUseType
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement$StackRequirement
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Vec3i
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.TrapDoorBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.storage.ValueInput
 *  net.minecraft.world.level.storage.ValueOutput
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.ApiStatus$OverrideOnly
 */
package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.redstone.RoseQuartzLampBlock;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.ApiStatus;

@ParametersAreNonnullByDefault
public interface IMultiStateCopycatBlockEntity
extends ICopycatBlockEntity {
    public MaterialItemStorage getMaterialItemStorage();

    @ApiStatus.OverrideOnly
    public void setMaterialItemStorageInternal(MaterialItemStorage var1);

    @Override
    default public void init() {
        Block block = this.getBlockState().getBlock();
        if (block instanceof IMultiStateCopycatBlock) {
            IMultiStateCopycatBlock copycatBlock = (IMultiStateCopycatBlock)block;
            Set<String> properties = copycatBlock.storageProperties();
            this.setMaterialItemStorageInternal(MaterialItemStorage.create(properties));
        } else {
            this.setMaterialItemStorageInternal(MaterialItemStorage.create(Set.of("block")));
        }
    }

    @Override
    default public BlockState getMaterial() {
        return this.getMaterialItemStorage().getMaterialItem(this.getBlock().defaultProperty()).material();
    }

    @Override
    default public ItemStack getConsumedItem() {
        return this.getMaterialItemStorage().getMaterialItem(this.getBlock().defaultProperty()).consumedItem();
    }

    @Override
    default public boolean isCTEnabled() {
        return this.getMaterialItemStorage().getMaterialItem(this.getBlock().defaultProperty()).enableCT();
    }

    @Override
    default public void setMaterialInternal(BlockState material) {
        this.getMaterialItemStorage().getMaterialItem(this.getBlock().defaultProperty()).setMaterial(material);
    }

    @Override
    default public void setConsumedItemInternal(ItemStack consumedItem) {
        this.getMaterialItemStorage().getMaterialItem(this.getBlock().defaultProperty()).setConsumedItem(consumedItem);
    }

    @Override
    default public void setCTEnabledInternal(boolean value) {
        this.getMaterialItemStorage().getMaterialItem(this.getBlock().defaultProperty()).setEnableCT(value);
    }

    @Override
    default public IMultiStateCopycatBlock getBlock() {
        Block block = this.getBlockState().getBlock();
        if (block instanceof IMultiStateCopycatBlock) {
            IMultiStateCopycatBlock copycatBlock = (IMultiStateCopycatBlock)block;
            return copycatBlock;
        }
        return new IMultiStateCopycatBlock(this){
            final /* synthetic */ IMultiStateCopycatBlockEntity this$0;
            {
                IMultiStateCopycatBlockEntity iMultiStateCopycatBlockEntity = this$0;
                Objects.requireNonNull(iMultiStateCopycatBlockEntity);
                this.this$0 = iMultiStateCopycatBlockEntity;
            }

            @Override
            public String defaultProperty() {
                return this.this$0.getMaterialItemStorage().getAllProperties().stream().findFirst().orElse("material");
            }

            @Override
            public Vec3i vectorScale(BlockState state) {
                return new Vec3i(1, 1, 1);
            }

            @Override
            public Set<String> storageProperties() {
                return Set.of(this.defaultProperty());
            }

            @Override
            public int getColorIndex(String property) {
                return 0;
            }

            @Override
            public boolean partExists(BlockState state, String property) {
                return false;
            }

            @Override
            public Vec3i getVectorFromProperty(BlockState state, String property) {
                return new Vec3i(0, 0, 0);
            }

            @Override
            public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
                return this.defaultProperty();
            }

            @Override
            public VoxelShape getPartialFaceShape(BlockGetter level, BlockState state, String property, Direction face) {
                return state.getFaceOcclusionShape(face);
            }

            @Override
            public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
            }

            @Override
            public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState) {
                return true;
            }

            @Override
            public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
                return false;
            }
        };
    }

    @Override
    default public boolean hasCustomMaterial() {
        return !this.getMaterialItemStorage().getAllMaterials().stream().allMatch(state -> state.is((Object)AllBlocks.COPYCAT_BASE));
    }

    default public void setMaterial(String property, BlockState blockState) {
        BlockState wrapperState = this.getBlockState();
        BlockState finalMaterial = blockState;
        if (!this.getMaterialItemStorage().getMaterialItem(property).material().is((Object)finalMaterial.getBlock())) {
            for (Direction side : Iterate.directions) {
                IMultiStateCopycatBlockEntity cbe;
                BlockState otherMaterial;
                BlockEntity blockEntity;
                BlockPos neighbour = this.getBlockPos().relative(side);
                BlockState neighbourState = this.getLevel().getBlockState(neighbour);
                if (neighbourState != wrapperState || !((blockEntity = this.getLevel().getBlockEntity(neighbour)) instanceof IMultiStateCopycatBlockEntity) || !(otherMaterial = (cbe = (IMultiStateCopycatBlockEntity)blockEntity).getMaterialItemStorage().getMaterialItem(property).material()).is((Object)blockState.getBlock())) continue;
                blockState = otherMaterial;
                break;
            }
        }
        MaterialItemStorage.MaterialItem materialItem = this.getMaterialItemStorage().getMaterialItem(property);
        materialItem.setMaterial(blockState);
        this.getMaterialItemStorage().storeMaterialItem(property, materialItem);
        BlockEntityUtils.redraw((BlockEntity)this);
    }

    default public boolean cycleMaterial(String property) {
        BlockState material = this.getMaterialItemStorage().getMaterialItem(property).material();
        if (material.hasProperty((Property)TrapDoorBlock.HALF) && material.getOptionalValue((Property)TrapDoorBlock.OPEN).orElse(false).booleanValue()) {
            this.setMaterial(property, (BlockState)material.cycle((Property)TrapDoorBlock.HALF));
        } else if (material.hasProperty((Property)BlockStateProperties.FACING)) {
            this.setMaterial(property, (BlockState)material.cycle((Property)BlockStateProperties.FACING));
        } else if (material.hasProperty((Property)BlockStateProperties.HORIZONTAL_FACING)) {
            this.setMaterial(property, (BlockState)material.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)((Direction)material.getValue((Property)BlockStateProperties.HORIZONTAL_FACING)).getClockWise()));
        } else if (material.hasProperty((Property)BlockStateProperties.AXIS)) {
            this.setMaterial(property, (BlockState)material.cycle((Property)BlockStateProperties.AXIS));
        } else if (material.hasProperty((Property)BlockStateProperties.HORIZONTAL_AXIS)) {
            this.setMaterial(property, (BlockState)material.cycle((Property)BlockStateProperties.HORIZONTAL_AXIS));
        } else if (material.hasProperty((Property)BlockStateProperties.LIT)) {
            this.setMaterial(property, (BlockState)material.cycle((Property)BlockStateProperties.LIT));
        } else if (material.hasProperty((Property)RoseQuartzLampBlock.POWERING)) {
            this.setMaterial(property, (BlockState)material.cycle((Property)RoseQuartzLampBlock.POWERING));
        } else {
            return false;
        }
        return true;
    }

    default public void setConsumedItem(String property, ItemStack itemStack) {
        this.getMaterialItemStorage().getMaterialItem(property).setConsumedItem(itemStack);
        this.notifyUpdate();
    }

    default public void setEnableCT(String property, boolean value) {
        this.getMaterialItemStorage().getMaterialItem(property).setEnableCT(value);
        this.notifyUpdate();
    }

    @Override
    default public ItemRequirement getRequiredItems(BlockState state) {
        return new ItemRequirement(this.getMaterialItemStorage().getAllConsumedItems().stream().map(stack -> new ItemRequirement.StackRequirement(stack, ItemRequirement.ItemUseType.CONSUME)).toList());
    }

    @Override
    default public void transform(BlockEntity blockEntity, StructureTransform transform) {
        this.getBlock().transformStorage(this.getBlockState(), this, transform);
        for (String key : this.getMaterialItemStorage().getAllProperties()) {
            this.getMaterialItemStorage().getMaterialItem(key).setMaterial(transform.apply(this.getMaterialItemStorage().getMaterialItem(key).material()));
        }
        this.notifyUpdate();
    }

    public static void read(IMultiStateCopycatBlockEntity self, ValueInput view, boolean clientPacket) {
        boolean anyUpdated;
        if (self.getBlockState().getBlock() instanceof IMultiStateCopycatBlock && (anyUpdated = self.getMaterialItemStorage().read(view.childOrEmpty("material_data")))) {
            BlockEntityUtils.redraw((BlockEntity)self);
        }
    }

    public static void writeSafe(IMultiStateCopycatBlockEntity self, ValueOutput view) {
        self.getMaterialItemStorage().write(view.child("material_data"), true);
    }

    public static void write(IMultiStateCopycatBlockEntity self, ValueOutput view, boolean clientPacket) {
        self.getMaterialItemStorage().write(view.child("material_data"), false);
    }
}

