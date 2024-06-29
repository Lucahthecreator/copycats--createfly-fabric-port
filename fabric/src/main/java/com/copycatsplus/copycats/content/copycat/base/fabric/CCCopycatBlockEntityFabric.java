package com.copycatsplus.copycats.content.copycat.base.fabric;

import com.copycatsplus.copycats.content.copycat.base.CCCopycatBlockEntity;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import io.github.fabricators_of_create.porting_lib.util.NBTSerializer;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class CCCopycatBlockEntityFabric extends CCCopycatBlockEntity implements RenderAttachmentBlockEntity {
    public CCCopycatBlockEntityFabric(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void setConsumedItem(ItemStack stack) {
        consumedItem = ItemHandlerHelper.copyStackWithSize(stack, 1);
        setChanged();
    }

    @Override
    public void redraw() {
        // fabric: no need for requestModelDataUpdate
        if (hasLevel()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            level.getChunkSource()
                    .getLightEngine()
                    .checkBlock(worldPosition);
        }
    }

    @Override
    protected void write(CompoundTag tag, ItemStack stack, BlockState material) {
        tag.put("Item", NBTSerializer.serializeNBT(stack));
        tag.put("Material", NbtUtils.writeBlockState(material));
        tag.putBoolean("EnableCT", enableCT);
    }

    @Override
    public @Nullable Object getRenderAttachmentData() {
        return material;
    }
}
