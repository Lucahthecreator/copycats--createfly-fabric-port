package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.zurrtum.create.api.behaviour.BlockEntityBehaviour;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import com.zurrtum.create.foundation.blockEntity.SmartBlockEntity;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

@ParametersAreNonnullByDefault
public class MultiStateCopycatBlockEntity extends SmartBlockEntity implements IMultiStateCopycatBlockEntity {
    private MaterialItemStorage materialItemStorage;

    public MultiStateCopycatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.init();
    }

    @Override
    public MaterialItemStorage getMaterialItemStorage() {
        return this.materialItemStorage;
    }

    @Override
    public void setMaterialItemStorageInternal(MaterialItemStorage storage) {
        this.materialItemStorage = storage;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        IMultiStateCopycatBlockEntity.super.invalidate();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour<?>> behaviours) {
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        return IMultiStateCopycatBlockEntity.super.getRequiredItems(state);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState oldState) {
        super.preRemoveSideEffects(pos, oldState);
        this.materialItemStorage.getAllConsumedItems().forEach(stack -> Block.popResource(this.level, pos, stack));
    }

    @Override
    protected void read(ValueInput view, boolean clientPacket) {
        super.read(view, clientPacket);
        IMultiStateCopycatBlockEntity.read(this, view, clientPacket);
    }

    @Override
    public void writeSafe(ValueOutput view) {
        super.writeSafe(view);
        IMultiStateCopycatBlockEntity.writeSafe(this, view);
    }

    @Override
    protected void write(ValueOutput view, boolean clientPacket) {
        super.write(view, clientPacket);
        IMultiStateCopycatBlockEntity.write(this, view, clientPacket);
    }

    public void migrateData(ICopycatBlockEntity copycatBlockEntity) {
        if (this.getBlockState().getBlock() instanceof MultiStateCopycatBlock mscb) {
            Identifier blockId = copycatBlockEntity.getBlockState().getBlock().builtInRegistryHolder().key().identifier();
            Copycats.LOGGER.debug("Converting block({}) at @{} to a multistate copycat", blockId.toString(), copycatBlockEntity.getBlockPos().toShortString());
            String firstProperty = this.getMaterialItemStorage()
                .getAllProperties()
                .stream()
                .filter(prop -> mscb.partExists(this.getBlockState(), prop))
                .findFirst()
                .orElse(null);
            if (firstProperty == null) {
                Copycats.LOGGER
                    .error(
                        "Failed to convert block({}) at @{} to a multistate copycat: no valid properties found",
                        blockId.toString(),
                        copycatBlockEntity.getBlockPos().toShortString()
                    );
                BlockEntityUtils.redraw(this);
                return;
            }

            MaterialItemStorage.MaterialItem materialItem = this.materialItemStorage.getMaterialItem(firstProperty);
            materialItem.setMaterial(copycatBlockEntity.getMaterial());
            materialItem.setConsumedItem(copycatBlockEntity.getConsumedItem());

            for (String property : mscb.storageProperties()) {
                if (mscb.partExists(this.getBlockState(), property) && !this.getMaterialItemStorage().hasCustomMaterial(property)) {
                    MaterialItemStorage.MaterialItem store = this.materialItemStorage.getMaterialItem(property);
                    store.setMaterial(copycatBlockEntity.getMaterial());
                    store.setConsumedItem(ItemStack.EMPTY);
                }
            }

            BlockEntityUtils.redraw(this);
        }
    }
}
