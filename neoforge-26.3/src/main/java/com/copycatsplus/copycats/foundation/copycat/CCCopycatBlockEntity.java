package com.copycatsplus.copycats.foundation.copycat;

import com.zurrtum.create.api.behaviour.BlockEntityBehaviour;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import com.zurrtum.create.foundation.blockEntity.SmartBlockEntity;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

@ParametersAreNonnullByDefault
public class CCCopycatBlockEntity extends SmartBlockEntity implements ICopycatBlockEntity {
    protected BlockState material;
    protected ItemStack consumedItem;
    protected boolean enableCT;

    public CCCopycatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        ICopycatBlockEntity.super.init();
    }

    @Override
    public Level getLevel() {
        return super.getLevel();
    }

    @Override
    public BlockPos getBlockPos() {
        return super.getBlockPos();
    }

    @Override
    public BlockState getBlockState() {
        return super.getBlockState();
    }

    @Override
    public HolderGetter<Block> blockHolderGetter() {
        return super.blockHolderGetter();
    }

    @Override
    public void setBlockState(BlockState blockState) {
        super.setBlockState(blockState);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
    }

    @Override
    public BlockState getMaterial() {
        return this.material;
    }

    @Override
    public ItemStack getConsumedItem() {
        return this.consumedItem;
    }

    @Override
    public boolean isCTEnabled() {
        return this.enableCT;
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
        this.enableCT = value;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        ICopycatBlockEntity.super.invalidate();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour<?>> behaviours) {
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        return ICopycatBlockEntity.super.getRequiredItems(state);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState oldState) {
        super.preRemoveSideEffects(pos, oldState);
        Block.popResource(this.level, pos, this.consumedItem);
    }

    @Override
    protected void read(ValueInput view, boolean clientPacket) {
        super.read(view, clientPacket);
        ICopycatBlockEntity.read(this, view, clientPacket);
    }

    @Override
    public void writeSafe(ValueOutput view) {
        super.writeSafe(view);
        ICopycatBlockEntity.writeSafe(this, view);
    }

    @Override
    protected void write(ValueOutput view, boolean clientPacket) {
        super.write(view, clientPacket);
        ICopycatBlockEntity.write(this, view, clientPacket);
    }
}
