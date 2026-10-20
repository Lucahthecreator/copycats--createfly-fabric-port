package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.redstone.RoseQuartzLampBlock;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement.ItemUseType;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement.StackRequirement;
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
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;

@ParametersAreNonnullByDefault
public interface IMultiStateCopycatBlockEntity extends ICopycatBlockEntity {
    MaterialItemStorage getMaterialItemStorage();

    @OverrideOnly
    void setMaterialItemStorageInternal(MaterialItemStorage var1);

    @Override
    default void init() {
        if (this.getBlockState().getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            Set<String> properties = copycatBlock.storageProperties();
            this.setMaterialItemStorageInternal(MaterialItemStorage.create(properties));
        } else {
            this.setMaterialItemStorageInternal(MaterialItemStorage.create(Set.of("block")));
        }
    }

    @Override
    default BlockState getMaterial() {
        return this.getMaterialItemStorage().getMaterialItem(this.getBlock().defaultProperty()).material();
    }

    @Override
    default ItemStack getConsumedItem() {
        return this.getMaterialItemStorage().getMaterialItem(this.getBlock().defaultProperty()).consumedItem();
    }

    @Override
    default boolean isCTEnabled() {
        return this.getMaterialItemStorage().getMaterialItem(this.getBlock().defaultProperty()).enableCT();
    }

    @Override
    default void setMaterialInternal(BlockState material) {
        this.getMaterialItemStorage().getMaterialItem(this.getBlock().defaultProperty()).setMaterial(material);
    }

    @Override
    default void setConsumedItemInternal(ItemStack consumedItem) {
        this.getMaterialItemStorage().getMaterialItem(this.getBlock().defaultProperty()).setConsumedItem(consumedItem);
    }

    @Override
    default void setCTEnabledInternal(boolean value) {
        this.getMaterialItemStorage().getMaterialItem(this.getBlock().defaultProperty()).setEnableCT(value);
    }

    default IMultiStateCopycatBlock getBlock() {
        return this.getBlockState().getBlock() instanceof IMultiStateCopycatBlock copycatBlock
            ? copycatBlock
            : new IMultiStateCopycatBlock() {
                {
                    Objects.requireNonNull(IMultiStateCopycatBlockEntity.this);
                }

                @Override
                public String defaultProperty() {
                    return IMultiStateCopycatBlockEntity.this.getMaterialItemStorage().getAllProperties().stream().findFirst().orElse("material");
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
                public String getPropertyFromInteraction(
                    BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit
                ) {
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
                public boolean isIgnoredConnectivitySide(
                    BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState
                ) {
                    return true;
                }

                @Override
                public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
                    return false;
                }
            };
    }

    @Override
    default boolean hasCustomMaterial() {
        return !this.getMaterialItemStorage().getAllMaterials().stream().allMatch(state -> state.is(AllBlocks.COPYCAT_BASE));
    }

    default void setMaterial(String property, BlockState blockState) {
        BlockState wrapperState = this.getBlockState();
        if (!this.getMaterialItemStorage().getMaterialItem(property).material().is(blockState.getBlock())) {
            for (Direction side : Iterate.directions) {
                BlockPos neighbour = this.getBlockPos().relative(side);
                BlockState neighbourState = this.getLevel().getBlockState(neighbour);
                if (neighbourState == wrapperState && this.getLevel().getBlockEntity(neighbour) instanceof IMultiStateCopycatBlockEntity cbe) {
                    BlockState otherMaterial = cbe.getMaterialItemStorage().getMaterialItem(property).material();
                    if (otherMaterial.is(blockState.getBlock())) {
                        blockState = otherMaterial;
                        break;
                    }
                }
            }
        }

        MaterialItemStorage.MaterialItem materialItem = this.getMaterialItemStorage().getMaterialItem(property);
        materialItem.setMaterial(blockState);
        this.getMaterialItemStorage().storeMaterialItem(property, materialItem);
        BlockEntityUtils.redraw((BlockEntity)this);
    }

    default boolean cycleMaterial(String property) {
        BlockState material = this.getMaterialItemStorage().getMaterialItem(property).material();
        if (material.hasProperty(TrapDoorBlock.HALF) && material.getOptionalValue(TrapDoorBlock.OPEN).orElse(false)) {
            this.setMaterial(property, material.cycle(TrapDoorBlock.HALF));
        } else if (material.hasProperty(BlockStateProperties.FACING)) {
            this.setMaterial(property, material.cycle(BlockStateProperties.FACING));
        } else if (material.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            this.setMaterial(
                property, material.setValue(BlockStateProperties.HORIZONTAL_FACING, material.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise())
            );
        } else if (material.hasProperty(BlockStateProperties.AXIS)) {
            this.setMaterial(property, material.cycle(BlockStateProperties.AXIS));
        } else if (material.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            this.setMaterial(property, material.cycle(BlockStateProperties.HORIZONTAL_AXIS));
        } else if (material.hasProperty(BlockStateProperties.LIT)) {
            this.setMaterial(property, material.cycle(BlockStateProperties.LIT));
        } else {
            if (!material.hasProperty(RoseQuartzLampBlock.POWERING)) {
                return false;
            }

            this.setMaterial(property, material.cycle(RoseQuartzLampBlock.POWERING));
        }

        return true;
    }

    default void setConsumedItem(String property, ItemStack itemStack) {
        this.getMaterialItemStorage().getMaterialItem(property).setConsumedItem(itemStack);
        this.notifyUpdate();
    }

    default void setEnableCT(String property, boolean value) {
        this.getMaterialItemStorage().getMaterialItem(property).setEnableCT(value);
        this.notifyUpdate();
    }

    @Override
    default ItemRequirement getRequiredItems(BlockState state) {
        return new ItemRequirement(
            this.getMaterialItemStorage().getAllConsumedItems().stream().map(stack -> new StackRequirement(stack, ItemUseType.CONSUME)).toList()
        );
    }

    @Override
    default void transform(BlockEntity blockEntity, StructureTransform transform) {
        this.getBlock().transformStorage(this.getBlockState(), this, transform);

        for (String key : this.getMaterialItemStorage().getAllProperties()) {
            this.getMaterialItemStorage().getMaterialItem(key).setMaterial(transform.apply(this.getMaterialItemStorage().getMaterialItem(key).material()));
        }

        this.notifyUpdate();
    }

    static void read(IMultiStateCopycatBlockEntity self, ValueInput view, boolean clientPacket) {
        if (self.getBlockState().getBlock() instanceof IMultiStateCopycatBlock) {
            boolean anyUpdated = self.getMaterialItemStorage().read(view.childOrEmpty("material_data"));
            if (anyUpdated) {
                BlockEntityUtils.redraw((BlockEntity)self);
            }
        }
    }

    static void writeSafe(IMultiStateCopycatBlockEntity self, ValueOutput view) {
        self.getMaterialItemStorage().write(view.child("material_data"), true);
    }

    static void write(IMultiStateCopycatBlockEntity self, ValueOutput view, boolean clientPacket) {
        self.getMaterialItemStorage().write(view.child("material_data"), false);
    }
}
