package com.copycatsplus.copycats.content.copycat.base.forge;

import com.copycatsplus.copycats.content.copycat.base.CCCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.model.forge.CopycatModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class CCCopycatBlockEntityForge extends CCCopycatBlockEntity {
    public CCCopycatBlockEntityForge(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void setConsumedItem(ItemStack stack) {
        consumedItem = ItemHandlerHelper.copyStackWithSize(stack, 1);
        setChanged();
    }

    @Override
    public void redraw() {
        if (!isVirtual())
            requestModelDataUpdate();
        if (hasLevel()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            level.getChunkSource()
                    .getLightEngine()
                    .checkBlock(worldPosition);
        }
    }

    @Override
    protected void write(CompoundTag tag, ItemStack stack, BlockState material) {
        tag.put("Item", stack.serializeNBT());
        tag.put("Material", NbtUtils.writeBlockState(material));
        tag.putBoolean("EnableCT", enableCT);
    }

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder()
                .with(CopycatModel.MATERIAL_PROPERTY, material)
                .build();
    }
}
