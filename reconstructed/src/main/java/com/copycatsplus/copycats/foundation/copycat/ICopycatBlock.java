/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.AllBlocks
 *  com.zurrtum.create.AllItems
 *  com.zurrtum.create.api.contraption.transformable.TransformableBlock
 *  com.zurrtum.create.catnip.data.Iterate
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.content.equipment.wrench.IWrenchable
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement$ItemUseType
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement$StackRequirement
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.core.Vec3i
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.BellBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.EntityBlock
 *  net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.SlabBlock
 *  net.minecraft.world.level.block.StairBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.AttachFace
 *  net.minecraft.world.level.block.state.properties.BellAttachType
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Half
 *  net.minecraft.world.level.block.state.properties.IntegerProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.block.state.properties.SlabType
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.CopycatExternalContext;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.AllItems;
import com.zurrtum.create.AllTags;
import com.zurrtum.create.api.contraption.transformable.TransformableBlock;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import java.util.Optional;
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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public interface ICopycatBlock
extends IWrenchable,
IStateType,
TransformableBlock {
    @Nullable
    default public ICopycatBlockEntity getCopycatBlockEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity == null) {
            return null;
        }
        if (!(blockEntity instanceof ICopycatBlockEntity)) {
            return null;
        }
        ICopycatBlockEntity copycatBE = (ICopycatBlockEntity)blockEntity;
        return copycatBE;
    }

    default public boolean canToggleCT(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    default public boolean isCTEnabled(BlockState state, BlockGetter level, @Nullable BlockPos pos) {
        if (pos == null) {
            return false;
        }
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ICopycatBlockEntity)) {
            return true;
        }
        ICopycatBlockEntity fbe = (ICopycatBlockEntity)be;
        if (!this.canToggleCT(state, level, pos)) {
            return true;
        }
        return fbe.isCTEnabled();
    }

    default public InteractionResult toggleCT(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.isShiftKeyDown() && pPlayer.getItemInHand(pHand).equals(ItemStack.EMPTY)) {
            if (!this.canToggleCT(pState, (BlockGetter)pLevel, pPos)) {
                return InteractionResult.PASS;
            }
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (!(be instanceof ICopycatBlockEntity)) {
                return InteractionResult.PASS;
            }
            ICopycatBlockEntity fbe = (ICopycatBlockEntity)be;
            if (!this.canToggleCT(pState, (BlockGetter)pLevel, pPos)) {
                return InteractionResult.PASS;
            }
            fbe.setCTEnabled(!fbe.isCTEnabled());
            BlockEntityUtils.redraw((BlockEntity)fbe);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    default public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        this.onWrenched(state, context);
        return super.onSneakWrenched(state, context);
    }

    default public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        ICopycatBlockEntity copycatBE = this.getCopycatBlockEntity((BlockGetter)context.getLevel(), context.getClickedPos());
        if (copycatBE == null) {
            return InteractionResult.PASS;
        }
        ItemStack consumedItem = copycatBE.getConsumedItem();
        if (!copycatBE.hasCustomMaterial()) {
            return InteractionResult.PASS;
        }
        Player player = context.getPlayer();
        if (!player.isCreative()) {
            player.getInventory().placeItemBackInInventory(consumedItem);
        }
        context.getLevel().levelEvent(2001, context.getClickedPos(), Block.getId((BlockState)ICopycatBlock.getMaterial((BlockGetter)context.getLevel(), context.getClickedPos())));
        copycatBE.setMaterial(AllBlocks.COPYCAT_BASE.defaultBlockState());
        copycatBE.setConsumedItem(ItemStack.EMPTY);
        return InteractionResult.SUCCESS;
    }

    @Nullable
    default public BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        Item item2 = item.getItem();
        if (!(item2 instanceof BlockItem)) {
            return null;
        }
        BlockItem bi = (BlockItem)item2;
        Block block = bi.getBlock();
        if (block instanceof ICopycatBlock) {
            return null;
        }
        BlockState appliedState = block.defaultBlockState();
        boolean hardCodedAllow = this.isAcceptedRegardless(appliedState);
        if (!AllTags.AllBlockTags.COPYCAT_ALLOW.matches(block) && !hardCodedAllow) {
            if (AllTags.AllBlockTags.COPYCAT_DENY.matches(block)) {
                return null;
            }
            if (block instanceof EntityBlock) {
                return null;
            }
            if (block instanceof StairBlock) {
                return null;
            }
            if (pLevel != null) {
                VoxelShape shape = appliedState.getShape((BlockGetter)pLevel, pPos);
                if (shape.isEmpty() || !shape.bounds().equals((Object)Shapes.block().bounds())) {
                    return null;
                }
                VoxelShape collisionShape = appliedState.getCollisionShape((BlockGetter)pLevel, pPos);
                if (collisionShape.isEmpty()) {
                    return null;
                }
            }
        }
        if (face != null) {
            Direction.Axis axis = face.getAxis();
            if (appliedState.hasProperty((Property)BlockStateProperties.FACING)) {
                appliedState = (BlockState)appliedState.setValue((Property)BlockStateProperties.FACING, (Comparable)face);
            }
            if (appliedState.hasProperty((Property)BlockStateProperties.HORIZONTAL_FACING) && axis != Direction.Axis.Y) {
                appliedState = (BlockState)appliedState.setValue((Property)BlockStateProperties.HORIZONTAL_FACING, (Comparable)face);
            }
            if (appliedState.hasProperty((Property)BlockStateProperties.AXIS)) {
                appliedState = (BlockState)appliedState.setValue((Property)BlockStateProperties.AXIS, (Comparable)axis);
            }
            if (appliedState.hasProperty((Property)BlockStateProperties.HORIZONTAL_AXIS) && axis != Direction.Axis.Y) {
                appliedState = (BlockState)appliedState.setValue((Property)BlockStateProperties.HORIZONTAL_AXIS, (Comparable)axis);
            }
        }
        return appliedState;
    }

    default public boolean isAcceptedRegardless(BlockState material) {
        return false;
    }

    default public BlockState prepareMaterial(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer, InteractionHand pHand, BlockHitResult pHit, BlockState material) {
        return material;
    }

    default public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        InteractionResult result;
        if (player.getItemInHand(hand).is(AllTags.AllItemTags.WRENCH.tag) && (result = AllItems.WRENCH.useOn(new UseOnContext(player, hand, ray))).consumesAction()) {
            return result;
        }
        result = this.toggleCT(state, world, pos, player, hand, ray);
        if (result.consumesAction()) {
            return result;
        }
        if (player == null || !player.mayBuild()) {
            return InteractionResult.PASS;
        }
        Direction face = ray.getDirection();
        ItemStack itemInHand = player.getItemInHand(hand);
        BlockState materialIn = this.getAcceptedBlockState(world, pos, itemInHand, face);
        if (materialIn != null) {
            materialIn = this.prepareMaterial(world, pos, state, player, hand, ray, materialIn);
        }
        if (materialIn == null) {
            return InteractionResult.PASS;
        }
        BlockState material = materialIn;
        ICopycatBlockEntity copycatBE = this.getCopycatBlockEntity((BlockGetter)world, pos);
        if (copycatBE == null) {
            return InteractionResult.PASS;
        }
        if (copycatBE.getMaterial().is((Object)material.getBlock())) {
            if (!copycatBE.cycleMaterial()) {
                return InteractionResult.PASS;
            }
            copycatBE.getLevel().playSound(null, copycatBE.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.75f, 0.95f);
            return InteractionResult.SUCCESS;
        }
        if (copycatBE.hasCustomMaterial()) {
            return InteractionResult.PASS;
        }
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        copycatBE.setMaterial(material);
        copycatBE.setConsumedItem(itemInHand);
        copycatBE.getLevel().playSound(null, copycatBE.getBlockPos(), material.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0f, 0.75f);
        if (player.isCreative()) {
            return InteractionResult.SUCCESS;
        }
        itemInHand.shrink(1);
        if (itemInHand.isEmpty()) {
            player.setItemInHand(hand, ItemStack.EMPTY);
        }
        return InteractionResult.SUCCESS;
    }

    default public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Player player;
        if (placer == null) {
            return;
        }
        ItemStack offhandItem = placer.getItemInHand(InteractionHand.OFF_HAND);
        BlockState appliedState = this.getAcceptedBlockState(worldIn, pos, offhandItem, Direction.orderedByNearest((Entity)placer)[0]);
        if (appliedState == null) {
            return;
        }
        ICopycatBlockEntity copycatBE = this.getCopycatBlockEntity((BlockGetter)worldIn, pos);
        if (copycatBE == null) {
            return;
        }
        if (copycatBE.hasCustomMaterial()) {
            return;
        }
        copycatBE.setMaterial(appliedState);
        copycatBE.setConsumedItem(offhandItem);
        if (placer instanceof Player && (player = (Player)placer).isCreative()) {
            return;
        }
        offhandItem.shrink(1);
        if (offhandItem.isEmpty()) {
            placer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    default public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving, OnRemoveHandler handler) {
        ICopycatBlockEntity copycatBE;
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock()) {
            return;
        }
        if (!isMoving && (copycatBE = this.getCopycatBlockEntity((BlockGetter)world, pos)) != null) {
            Block.popResource((Level)world, (BlockPos)pos, (ItemStack)copycatBE.getConsumedItem());
        }
        handler.handle(state, world, pos, newState, isMoving);
        world.removeBlockEntity(pos);
    }

    default public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        ICopycatBlockEntity copycatBE;
        if (player.isCreative() && (copycatBE = this.getCopycatBlockEntity((BlockGetter)level, pos)) != null) {
            copycatBE.setConsumedItem(ItemStack.EMPTY);
        }
        return state;
    }

    public static BlockState getAppearance(ICopycatBlock block, BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, @Nullable BlockState queryState, @Nullable BlockPos queryPos) {
        if (block.isIgnoredConnectivitySide(level, state, side, pos, queryPos, queryState)) {
            return state;
        }
        BlockState material = ICopycatBlock.getMaterial((BlockGetter)level, pos);
        return material.is((Object)Blocks.AIR) ? AllBlocks.COPYCAT_BASE.defaultBlockState() : material;
    }

    public static BlockState getMaterial(BlockGetter reader, BlockPos targetPos) {
        BlockEntity blockEntity = reader.getBlockEntity(targetPos);
        if (blockEntity instanceof ICopycatBlockEntity) {
            ICopycatBlockEntity cbe = (ICopycatBlockEntity)blockEntity;
            return cbe.getMaterial();
        }
        return Blocks.AIR.defaultBlockState();
    }

    public static ItemRequirement getRequiredItemsForLayer(BlockState state, IntegerProperty property) {
        int count = (Integer)state.getValue((Property)property);
        if (count == 0) {
            return ItemRequirement.NONE;
        }
        return new ItemRequirement(IntStream.range(0, count).mapToObj($ -> new ItemRequirement.StackRequirement(new ItemStack((ItemLike)state.getBlock().asItem()), ItemRequirement.ItemUseType.CONSUME)).toList());
    }

    @NotNull
    default public BlockState rotateCopycat(@NotNull BlockState pState, Rotation pRot) {
        return this.transform(pState, new StructureTransform(BlockPos.ZERO, Direction.Axis.Y, pRot, Mirror.NONE));
    }

    @NotNull
    default public BlockState mirrorCopycat(@NotNull BlockState pState, @NotNull Mirror pMirror) {
        return this.transform(pState, new StructureTransform(BlockPos.ZERO, null, Rotation.NONE, pMirror));
    }

    default public BlockState transform(BlockState state, StructureTransform transform) {
        boolean halfTurn;
        Direction.Axis rotationAxis = transform.rotationAxis;
        Rotation rotation = transform.rotation;
        Mirror mirror = transform.mirror;
        Block block = state.getBlock();
        if (mirror != null) {
            state = state.mirror(mirror);
        }
        if (rotationAxis == Direction.Axis.Y) {
            if (block instanceof BellBlock) {
                if (state.getValue((Property)BlockStateProperties.BELL_ATTACHMENT) == BellAttachType.DOUBLE_WALL) {
                    state = (BlockState)state.setValue((Property)BlockStateProperties.BELL_ATTACHMENT, (Comparable)BellAttachType.SINGLE_WALL);
                }
                return (BlockState)state.setValue((Property)BellBlock.FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)BellBlock.FACING)));
            }
            return state.rotate(rotation);
        }
        if (block instanceof FaceAttachedHorizontalDirectionalBlock) {
            Direction forcedAxis;
            EnumProperty facingProperty = FaceAttachedHorizontalDirectionalBlock.FACING;
            EnumProperty faceProperty = FaceAttachedHorizontalDirectionalBlock.FACE;
            Direction stateFacing = (Direction)state.getValue((Property)facingProperty);
            AttachFace stateFace = (AttachFace)state.getValue((Property)faceProperty);
            boolean z = rotationAxis == Direction.Axis.Z;
            Direction direction = forcedAxis = z ? Direction.WEST : Direction.SOUTH;
            if (stateFacing.getAxis() == rotationAxis && stateFace == AttachFace.WALL) {
                return state;
            }
            for (int i = 0; i < rotation.ordinal(); ++i) {
                stateFace = (AttachFace)state.getValue((Property)faceProperty);
                stateFacing = (Direction)state.getValue((Property)facingProperty);
                boolean b = state.getValue((Property)faceProperty) == AttachFace.CEILING;
                state = (BlockState)state.setValue((Property)facingProperty, (Comparable)(b ? forcedAxis : forcedAxis.getOpposite()));
                state = stateFace != AttachFace.WALL ? (BlockState)state.setValue((Property)faceProperty, (Comparable)AttachFace.WALL) : (stateFacing.getAxisDirection() == (z ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE) ? (BlockState)state.setValue((Property)faceProperty, (Comparable)AttachFace.FLOOR) : (BlockState)state.setValue((Property)faceProperty, (Comparable)AttachFace.CEILING));
            }
            return state;
        }
        boolean bl = halfTurn = rotation == Rotation.CLOCKWISE_180;
        if (block instanceof StairBlock) {
            if (((Direction)state.getValue((Property)StairBlock.FACING)).getAxis() != rotationAxis) {
                for (int i = 0; i < rotation.ordinal(); ++i) {
                    Direction direction = (Direction)state.getValue((Property)StairBlock.FACING);
                    Half half = (Half)state.getValue((Property)StairBlock.HALF);
                    state = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ^ half == Half.BOTTOM ^ direction.getAxis() == Direction.Axis.Z ? (BlockState)state.cycle((Property)StairBlock.HALF) : (BlockState)state.setValue((Property)StairBlock.FACING, (Comparable)direction.getOpposite());
                }
            } else if (halfTurn) {
                state = (BlockState)state.cycle((Property)StairBlock.HALF);
            }
            return state;
        }
        if (state.hasProperty((Property)BlockStateProperties.FACING)) {
            state = (BlockState)state.setValue((Property)BlockStateProperties.FACING, (Comparable)transform.rotateFacing((Direction)state.getValue((Property)BlockStateProperties.FACING)));
        } else if (state.hasProperty((Property)BlockStateProperties.AXIS)) {
            state = (BlockState)state.setValue((Property)BlockStateProperties.AXIS, (Comparable)transform.rotateAxis((Direction.Axis)state.getValue((Property)BlockStateProperties.AXIS)));
        } else if (halfTurn) {
            Direction stateFacing;
            if (state.hasProperty((Property)BlockStateProperties.HORIZONTAL_FACING) && (stateFacing = (Direction)state.getValue((Property)BlockStateProperties.HORIZONTAL_FACING)).getAxis() == rotationAxis) {
                return state;
            }
            if ((state = state.rotate(rotation)).hasProperty((Property)SlabBlock.TYPE) && state.getValue((Property)SlabBlock.TYPE) != SlabType.DOUBLE) {
                state = (BlockState)state.setValue((Property)SlabBlock.TYPE, (Comparable)(state.getValue((Property)SlabBlock.TYPE) == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM));
            }
        }
        return state;
    }

    default public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState fromState, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState) {
        if (CopycatExternalContext.isForBlockingLogic()) {
            return false;
        }
        if (toPos == null) {
            return true;
        }
        toState = reader.getBlockState(toPos);
        return !this.checkConnection(reader, toPos, fromPos, toState);
    }

    default public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState fromState) {
        BlockState toState = reader.getBlockState(toPos);
        if (toState.getBlock() instanceof ICopycatBlock) {
            return true;
        }
        return this.checkConnection(reader, fromPos, toPos, fromState);
    }

    default public boolean checkConnection(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState fromState) {
        BlockPos diff = toPos.subtract((Vec3i)fromPos);
        if (diff.equals((Object)Vec3i.ZERO)) {
            return true;
        }
        Direction facing = Direction.getApproximateNearest((float)diff.getX(), (float)diff.getY(), (float)diff.getZ());
        BlockState toState = reader.getBlockState(toPos);
        if (facing != null) {
            return BlockFaceUtils.faceMatch((BlockGetter)reader, fromState, fromPos, toState, toPos, facing);
        }
        if (diff.getX() != 0 && diff.getY() != 0 && diff.getZ() != 0) {
            return true;
        }
        for (Direction.Axis axis : Iterate.axes) {
            Vec3i remainingDiff;
            Direction remainingFacing;
            if (diff.get(axis) == 0) continue;
            Vec3i axisDiff = Vec3i.ZERO;
            BlockPos midPos = fromPos.offset(axisDiff = axisDiff.relative(axis, diff.get(axis)));
            BlockState midState = reader.getBlockState(midPos);
            if (BlockFaceUtils.faceMatch((BlockGetter)reader, midState, midPos, toState, toPos, remainingFacing = Direction.getApproximateNearest((float)(remainingDiff = diff.subtract(axisDiff)).getX(), (float)remainingDiff.getY(), (float)remainingDiff.getZ()))) continue;
            return false;
        }
        return true;
    }

    default public boolean canOcclude(BlockGetter level, BlockState state, BlockPos pos) {
        BlockState material = ICopycatBlock.getMaterial(level, pos);
        if (material.is((Object)AllBlocks.COPYCAT_BASE)) {
            return false;
        }
        return material.canOcclude();
    }

    default public Optional<Boolean> shapeCanOccludeNeighbor(BlockGetter level, BlockPos pos, BlockState state, BlockPos neighborPos, Direction dir) {
        return Optional.empty();
    }

    public static boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        BlockPos toPos = pos.relative(dir);
        if (level instanceof ScaledBlockAndTintGetter scaledLevel && state.getBlock() instanceof IMultiStateCopycatBlock) {
            CopycatExternalContext.setPropertyForAppearance(scaledLevel.getPropertyForRender(state, pos));
        } else {
            CopycatExternalContext.setPropertyForAppearance(null);
        }
        if (BlockFaceUtils.canOcclude(level, neighborState, toPos, state, pos, dir.getOpposite())) {
            BlockState material = state.getBlock() instanceof IMultiStateCopycatBlock
                    ? IMultiStateCopycatBlock.getMaterial(level, pos, CopycatExternalContext.getPropertyForAppearance())
                    : state.getBlock() instanceof ICopycatBlock ? ICopycatBlock.getMaterial(level, pos) : state;
            BlockState neighborMaterial = neighborState.getBlock() instanceof IMultiStateCopycatBlock && level instanceof ScaledBlockAndTintGetter scaledLevel
                    ? IMultiStateCopycatBlock.getMaterial(level, toPos, scaledLevel.getPropertyForRender(neighborState, toPos))
                    : neighborState.getBlock() instanceof ICopycatBlock ? ICopycatBlock.getMaterial(level, toPos) : neighborState;
            return material.skipRendering(neighborMaterial, dir.getOpposite());
        }
        return false;
    }

    @FunctionalInterface
    public static interface OnRemoveHandler {
        public void handle(BlockState var1, Level var2, BlockPos var3, BlockState var4, boolean var5);
    }
}
