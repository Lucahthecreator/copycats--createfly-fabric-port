package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.content.copycat.base.ICopycatBlockEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.redstone.RoseQuartzLampBlock;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MultiStateCopycatBlockEntity extends SmartBlockEntity implements IMultiStateCopycatBlockEntity {

    private MaterialItemStorage materialItemStorage;

    public MultiStateCopycatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        init();
    }

    public MaterialItemStorage getMaterialItemStorage() {
        return materialItemStorage;
    }

    @Override
    public void setMaterialItemStorageInternal(MaterialItemStorage storage) {
        materialItemStorage = storage;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        IMultiStateCopycatBlockEntity.read(this, tag, clientPacket);
    }

    @Override
    public void writeSafe(CompoundTag tag) {
        super.writeSafe(tag);
        IMultiStateCopycatBlockEntity.writeSafe(this, tag);
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        IMultiStateCopycatBlockEntity.write(this, tag, clientPacket);
    }

    public void migrateData(ICopycatBlockEntity copycatBlockEntity) {
        if (getBlockState().getBlock() instanceof MultiStateCopycatBlock mscb) {
            ResourceLocation blockId = copycatBlockEntity.getBlockState().getBlock().builtInRegistryHolder().key().location();
            Copycats.LOGGER.debug("Converting block({}) at @{} to a multistate copycat", blockId.toString(), copycatBlockEntity.getBlockPos().toShortString());
            //Set the first property available to have the item and mat.
            MaterialItemStorage.MaterialItem materialItem = materialItemStorage.getMaterialItem(getMaterialItemStorage().getAllProperties().stream().filter(prop -> mscb.partExists(getBlockState(), prop)).findFirst().get());
            materialItem.setMaterial(copycatBlockEntity.getMaterial());
            materialItem.setConsumedItem(copycatBlockEntity.getConsumedItem());

            //Sets only the material so that it looks the same as the old blocks but wont give you free items
            for (String property : mscb.storageProperties()) {
                if (mscb.partExists(getBlockState(), property)) {
                    if (!getMaterialItemStorage().hasCustomMaterial(property)) {
                        MaterialItemStorage.MaterialItem store = materialItemStorage.getMaterialItem(property);
                        store.setMaterial(copycatBlockEntity.getMaterial());
                        store.setConsumedItem(ItemStack.EMPTY);
                    }
                }
            }
            redraw();
        }
    }
}
