/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.AllBlocks
 *  com.zurrtum.create.AllItems
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement$ItemUseType
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement$StackRequirement
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Vec3i
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.copycatsplus.copycats.foundation.copycat.CopycatExternalContext;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.foundation.copycat.StateType;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.AllItems;
import com.zurrtum.create.AllTags;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
public interface IMultiStateCopycatBlock
extends ICopycatBlock,
IStateType {
    @Override
    default public StateType stateType() {
        return StateType.MULTI;
    }

    @Override
    @Nullable
    default public IMultiStateCopycatBlockEntity getCopycatBlockEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity == null) {
            return null;
        }
        if (!(blockEntity instanceof IMultiStateCopycatBlockEntity)) {
            return null;
        }
        IMultiStateCopycatBlockEntity copycatBE = (IMultiStateCopycatBlockEntity)blockEntity;
        return copycatBE;
    }

    public String defaultProperty();

    public Vec3i vectorScale(BlockState var1);

    public Set<String> storageProperties();

    public int getColorIndex(String var1);

    public boolean partExists(BlockState var1, String var2);

    public Vec3i getVectorFromProperty(BlockState var1, String var2);

    public String getPropertyFromInteraction(BlockState var1, BlockGetter var2, Vec3i var3, BlockPos var4, Direction var5, Vec3 var6);

    default public String getPropertyFromInteraction(BlockState state, BlockGetter level, BlockPos pos, Vec3 hitVec, Direction face, boolean targetingSolid) {
        hitVec = targetingSolid ? hitVec.subtract(Vec3.atLowerCornerOf((Vec3i)face.getUnitVec3i()).scale(0.05)) : hitVec.add(Vec3.atLowerCornerOf((Vec3i)face.getUnitVec3i()).scale(0.05));
        Vec3 unscaledHit = hitVec = hitVec.add((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()));
        Vec3i scale = this.vectorScale(state);
        hitVec = hitVec.multiply((double)scale.getX(), (double)scale.getY(), (double)scale.getZ());
        BlockPos location = new BlockPos((int)hitVec.x(), (int)hitVec.y(), (int)hitVec.z());
        return this.getPropertyFromInteraction(state, level, (Vec3i)location, pos, face, unscaledHit);
    }

    default public String getPropertyFromInteraction(BlockState state, BlockGetter level, BlockPos pos, BlockHitResult hit, boolean targetingSolid) {
        Vec3 hitVec = hit.getLocation();
        return this.getPropertyFromInteraction(state, level, pos, hitVec, hit.getDirection(), targetingSolid);
    }

    default public String getPropertyFromRender(String renderingProperty, BlockState state, BlockGetter level, Vec3i vector, BlockPos blockPos) {
        Vec3i scale = this.vectorScale(state);
        return this.getPropertyFromInteraction(state, level, vector, blockPos, Direction.UP, Vec3.atLowerCornerOf((Vec3i)vector).multiply(1.0 / (double)scale.getX(), 1.0 / (double)scale.getY(), 1.0 / (double)scale.getZ()));
    }

    @Override
    default public InteractionResult toggleCT(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isShiftKeyDown() && player.getItemInHand(hand).equals(ItemStack.EMPTY)) {
            if (!this.canToggleCT(state, (BlockGetter)level, pos)) {
                return InteractionResult.PASS;
            }
            String property = this.getPropertyFromInteraction(state, (BlockGetter)level, pos, hit, true);
            IMultiStateCopycatBlockEntity be = this.getCopycatBlockEntity((BlockGetter)level, pos);
            if (be == null) {
                return InteractionResult.PASS;
            }
            be.setEnableCT(property, !be.getMaterialItemStorage().getMaterialItem(property).enableCT());
            BlockEntityUtils.redraw((BlockEntity)be);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    default public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        IMultiStateCopycatBlockEntity copycatBE = this.getCopycatBlockEntity((BlockGetter)context.getLevel(), context.getClickedPos());
        if (copycatBE == null) {
            return InteractionResult.PASS;
        }
        String property = this.getPropertyFromInteraction(state, (BlockGetter)context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), true);
        if (!this.partExists(state, property)) {
            return InteractionResult.PASS;
        }
        MaterialItemStorage.MaterialItem material = copycatBE.getMaterialItemStorage().getMaterialItem(property);
        ItemStack consumedItem = material.consumedItem();
        if (!consumedItem.isEmpty()) {
            for (String prop : copycatBE.getMaterialItemStorage().getAllProperties()) {
                MaterialItemStorage.MaterialItem materialItem;
                if (prop.equals(property) || !(materialItem = copycatBE.getMaterialItemStorage().getMaterialItem(prop)).material().getBlock().equals(material.material().getBlock()) || !materialItem.consumedItem().isEmpty()) continue;
                copycatBE.setConsumedItem(prop, consumedItem);
                consumedItem = ItemStack.EMPTY;
                break;
            }
        }
        if (!copycatBE.getMaterialItemStorage().hasCustomMaterial(property)) {
            return InteractionResult.PASS;
        }
        Player player = context.getPlayer();
        if (!player.isCreative()) {
            player.getInventory().placeItemBackInInventory(consumedItem);
        }
        context.getLevel().levelEvent(2001, context.getClickedPos(), Block.getId((BlockState)material.material()));
        copycatBE.setMaterial(property, AllBlocks.COPYCAT_BASE.defaultBlockState());
        copycatBE.setConsumedItem(property, ItemStack.EMPTY);
        return InteractionResult.SUCCESS;
    }

    @Nullable
    default public BlockState getAcceptedBlockState(String property, Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        return this.getAcceptedBlockState(pLevel, pPos, item, face);
    }

    @Override
    default public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult result;
        if (player.getItemInHand(hand).is(AllTags.AllItemTags.WRENCH.tag) && (result = AllItems.WRENCH.useOn(new UseOnContext(player, hand, hit))).consumesAction()) {
            return result;
        }
        result = this.toggleCT(state, level, pos, player, hand, hit);
        if (result.consumesAction()) {
            return result;
        }
        if (player == null || !player.mayBuild()) {
            return InteractionResult.PASS;
        }
        String property = this.getPropertyFromInteraction(state, (BlockGetter)level, pos, hit, true);
        Direction face = hit.getDirection();
        ItemStack itemInHand = player.getItemInHand(hand);
        BlockState material = this.getAcceptedBlockState(property, level, pos, itemInHand, face);
        if (material != null) {
            material = this.prepareMaterial(level, pos, state, player, hand, hit, material);
        }
        if (material == null) {
            return InteractionResult.PASS;
        }
        IMultiStateCopycatBlockEntity copycatBE = this.getCopycatBlockEntity((BlockGetter)level, pos);
        if (copycatBE == null) {
            return InteractionResult.PASS;
        }
        if (!this.partExists(state, property)) {
            return InteractionResult.PASS;
        }
        if (copycatBE.getMaterialItemStorage().getMaterialItem(property).material().is((Object)material.getBlock())) {
            if (!copycatBE.cycleMaterial(property)) {
                return InteractionResult.PASS;
            }
            copycatBE.getLevel().playSound(null, copycatBE.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.75f, 0.95f);
            return InteractionResult.SUCCESS;
        }
        if (copycatBE.getMaterialItemStorage().hasCustomMaterial(property)) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        boolean freeToApply = copycatBE.getMaterialItemStorage().getAllConsumedItems().stream().anyMatch(s -> s.getItem() == itemInHand.getItem());
        copycatBE.setMaterial(property, material);
        if (!freeToApply) {
            copycatBE.setConsumedItem(property, itemInHand);
        }
        copycatBE.getLevel().playSound(null, copycatBE.getBlockPos(), material.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0f, 0.75f);
        if (player.isCreative()) {
            return InteractionResult.SUCCESS;
        }
        if (!freeToApply) {
            itemInHand.shrink(1);
        }
        if (itemInHand.isEmpty()) {
            player.setItemInHand(hand, ItemStack.EMPTY);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    default public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer == null) {
            return;
        }
        ItemStack offhandItem = placer.getItemInHand(InteractionHand.OFF_HAND);
        BlockState appliedState = this.getAcceptedBlockState(level, pos, offhandItem, Direction.orderedByNearest((Entity)placer)[0]);
        if (appliedState == null) {
            return;
        }
        IMultiStateCopycatBlockEntity copycatBE = this.getCopycatBlockEntity((BlockGetter)level, pos);
        if (copycatBE == null) {
            return;
        }
        for (String property : this.storageProperties()) {
            Player player;
            if (!this.partExists(state, property) || copycatBE.getMaterialItemStorage().hasCustomMaterial(property)) continue;
            boolean freeToApply = copycatBE.getMaterialItemStorage().getAllConsumedItems().stream().anyMatch(s -> s.getItem() == offhandItem.getItem());
            copycatBE.setMaterial(property, appliedState);
            if (!freeToApply) {
                copycatBE.setConsumedItem(property, offhandItem);
            }
            if (placer instanceof Player && (player = (Player)placer).isCreative()) continue;
            if (!freeToApply) {
                offhandItem.shrink(1);
            }
            if (!offhandItem.isEmpty()) continue;
            placer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            break;
        }
    }

    @Override
    default public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving, ICopycatBlock.OnRemoveHandler handler) {
        IMultiStateCopycatBlockEntity copycatBE;
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock()) {
            return;
        }
        if (!isMoving && (copycatBE = this.getCopycatBlockEntity((BlockGetter)level, pos)) != null) {
            copycatBE.getMaterialItemStorage().getAllConsumedItems().forEach(stack -> Block.popResource((Level)level, (BlockPos)pos, (ItemStack)stack));
        }
        handler.handle(state, level, pos, newState, isMoving);
        level.removeBlockEntity(pos);
    }

    @Override
    default public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        IMultiStateCopycatBlockEntity copycatBE;
        if (player.isCreative() && (copycatBE = this.getCopycatBlockEntity((BlockGetter)level, pos)) != null) {
            copycatBE.getMaterialItemStorage().getAllProperties().forEach(key -> copycatBE.getMaterialItemStorage().getMaterialItem((String)key).setConsumedItem(ItemStack.EMPTY));
        }
        return state;
    }

    default public void fillEmptyParts(Level level, BlockPos pos, BlockState state, BlockState material) {
        IMultiStateCopycatBlockEntity copycatBE = this.getCopycatBlockEntity((BlockGetter)level, pos);
        if (copycatBE == null) {
            return;
        }
        for (String property : copycatBE.getMaterialItemStorage().getAllProperties()) {
            if (copycatBE.getMaterialItemStorage().hasCustomMaterial(property) || !this.partExists(state, property)) continue;
            copycatBE.setMaterial(property, material);
        }
    }

    public static BlockState getAppearance(IMultiStateCopycatBlock block, BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, @Nullable BlockState queryState, @Nullable BlockPos queryPos) {
        BlockAndTintGetter reader = level;
        if (reader instanceof ScaledBlockAndTintGetter scaledLevel && state.getBlock() instanceof IMultiStateCopycatBlock) {
            CopycatExternalContext.setPropertyForAppearance(scaledLevel.getPropertyForRender(state, pos));
        } else {
            CopycatExternalContext.setPropertyForAppearance(block.defaultProperty());
        }
        if (block.isIgnoredConnectivitySide(reader, state, side, pos, queryPos, queryState)) {
            return state;
        }
        String property = CopycatExternalContext.getPropertyForAppearance();
        if (property == null) {
            property = block.defaultProperty();
        }
        BlockState material = IMultiStateCopycatBlock.getMaterial(reader, pos, property);
        return material.is(Blocks.AIR) ? AllBlocks.COPYCAT_BASE.defaultBlockState() : material;
    }

    public VoxelShape getPartialFaceShape(BlockGetter var1, BlockState var2, String var3, Direction var4);

    public static BlockState getMaterial(BlockGetter reader, BlockPos targetPos, String property) {
        IMultiStateCopycatBlockEntity cbe;
        BlockEntity blockEntity = reader.getBlockEntity(targetPos);
        if (blockEntity instanceof IMultiStateCopycatBlockEntity && (cbe = (IMultiStateCopycatBlockEntity)blockEntity).getMaterialItemStorage().getMaterialItem(property) != null) {
            return cbe.getMaterialItemStorage().getMaterialItem(property).material();
        }
        return Blocks.AIR.defaultBlockState();
    }

    public void transformStorage(BlockState var1, IMultiStateCopycatBlockEntity var2, StructureTransform var3);

    public static ItemRequirement getRequiredItemsForParts(BlockState state, BooleanProperty ... property) {
        int count = 0;
        for (BooleanProperty part : property) {
            if (!((Boolean)state.getValue((Property)part)).booleanValue()) continue;
            ++count;
        }
        if (count == 0) {
            return ItemRequirement.NONE;
        }
        return new ItemRequirement(IntStream.range(0, count).mapToObj($ -> new ItemRequirement.StackRequirement(new ItemStack((ItemLike)state.getBlock().asItem()), ItemRequirement.ItemUseType.CONSUME)).toList());
    }

    default public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return this.canConnectTexturesToward(reader, fromPos, toPos, state);
    }

    @Override
    default public boolean canOcclude(BlockGetter level, BlockState state, BlockPos pos) {
        if (level instanceof ScaledBlockAndTintGetter) {
            ScaledBlockAndTintGetter scaledWorld = (ScaledBlockAndTintGetter)level;
            return this.canOcclude(scaledWorld.getPropertyForRender(state, pos), level, state, pos);
        }
        return false;
    }

    default public boolean canOcclude(String property, BlockGetter level, BlockState state, BlockPos pos) {
        BlockState material = IMultiStateCopycatBlock.getMaterial(level, pos, property);
        if (material.is((Object)AllBlocks.COPYCAT_BASE)) {
            return false;
        }
        return material.canOcclude();
    }

    @Override
    default public Optional<Boolean> shapeCanOccludeNeighbor(BlockGetter level, BlockPos pos, BlockState state, BlockPos neighborPos, Direction dir) {
        BlockState neighborState = level.getBlockState(neighborPos);
        return Optional.of(BlockFaceUtils.canOcclude(level, neighborState, neighborPos, state, pos, dir.getOpposite()));
    }
}
