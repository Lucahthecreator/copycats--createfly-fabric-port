package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import net.minecraft.util.Prediction;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.internal.create.AllTags;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.AllItems;
import com.zurrtum.create.api.contraption.transformable.TransformableBlock;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement.ItemUseType;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement.StackRequirement;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public interface ICopycatBlock extends IWrenchable, IStateType, TransformableBlock {
    @Nullable
    default ICopycatBlockEntity getCopycatBlockEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity == null) {
            return null;
        } else {
            return blockEntity instanceof ICopycatBlockEntity copycatBE ? copycatBE : null;
        }
    }

    default boolean canToggleCT(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    default boolean isCTEnabled(BlockState state, BlockGetter level, @Nullable BlockPos pos) {
        if (pos == null) {
            return false;
        } else if (level.getBlockEntity(pos) instanceof ICopycatBlockEntity fbe) {
            return !this.canToggleCT(state, level, pos) ? true : fbe.isCTEnabled();
        } else {
            return true;
        }
    }

    default InteractionResult toggleCT(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pPlayer.isShiftKeyDown() || !pPlayer.getItemInHand(pHand).equals(ItemStack.EMPTY)) {
            return InteractionResult.PASS;
        } else if (!this.canToggleCT(pState, pLevel, pPos)) {
            return InteractionResult.PASS;
        } else if (pLevel.getBlockEntity(pPos) instanceof ICopycatBlockEntity fbe) {
            if (!this.canToggleCT(pState, pLevel, pPos)) {
                return InteractionResult.PASS;
            } else {
                fbe.setCTEnabled(!fbe.isCTEnabled());
                BlockEntityUtils.redraw((BlockEntity)fbe);
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    default InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        this.onWrenched(state, context);
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    @Override
    default InteractionResult onWrenched(BlockState state, UseOnContext context) {
        ICopycatBlockEntity copycatBE = this.getCopycatBlockEntity(context.getLevel(), context.getClickedPos());
        if (copycatBE == null) {
            return InteractionResult.PASS;
        } else {
            ItemStack consumedItem = copycatBE.getConsumedItem();
            if (!copycatBE.hasCustomMaterial()) {
                return InteractionResult.PASS;
            } else {
                Player player = context.getPlayer();
                if (!player.isCreative()) {
                    player.getInventory().placeItemBackInInventory(consumedItem, Prediction.SERVER_ONLY);
                }

                context.getLevel().levelEvent(2001, context.getClickedPos(), Block.getId(getMaterial(context.getLevel(), context.getClickedPos())));
                copycatBE.setMaterial(AllBlocks.COPYCAT_BASE.defaultBlockState());
                copycatBE.setConsumedItem(ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Nullable
    default BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        if (item.getItem() instanceof BlockItem bi) {
            Block block = bi.getBlock();
            if (block instanceof ICopycatBlock) {
                return null;
            } else {
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
                        VoxelShape shape = appliedState.getShape(pLevel, pPos);
                        if (shape.isEmpty() || !shape.bounds().equals(Shapes.block().bounds())) {
                            return null;
                        }

                        VoxelShape collisionShape = appliedState.getCollisionShape(pLevel, pPos);
                        if (collisionShape.isEmpty()) {
                            return null;
                        }
                    }
                }

                if (face != null) {
                    Axis axis = face.getAxis();
                    if (appliedState.hasProperty(BlockStateProperties.FACING)) {
                        appliedState = appliedState.setValue(BlockStateProperties.FACING, face);
                    }

                    if (appliedState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && axis != Axis.Y) {
                        appliedState = appliedState.setValue(BlockStateProperties.HORIZONTAL_FACING, face);
                    }

                    if (appliedState.hasProperty(BlockStateProperties.AXIS)) {
                        appliedState = appliedState.setValue(BlockStateProperties.AXIS, axis);
                    }

                    if (appliedState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS) && axis != Axis.Y) {
                        appliedState = appliedState.setValue(BlockStateProperties.HORIZONTAL_AXIS, axis);
                    }
                }

                return appliedState;
            }
        } else {
            return null;
        }
    }

    default boolean isAcceptedRegardless(BlockState material) {
        return false;
    }

    default BlockState prepareMaterial(
        Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer, InteractionHand pHand, BlockHitResult pHit, BlockState material
    ) {
        return material;
    }

    default InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (player.getItemInHand(hand).is(AllTags.AllItemTags.WRENCH.tag)) {
            InteractionResult result = AllItems.WRENCH.useOn(new UseOnContext(player, hand, ray));
            if (result.consumesAction()) {
                return result;
            }
        }

        InteractionResult result = this.toggleCT(state, world, pos, player, hand, ray);
        if (result.consumesAction()) {
            return result;
        } else if (player != null && player.mayBuild()) {
            Direction face = ray.getDirection();
            ItemStack itemInHand = player.getItemInHand(hand);
            BlockState materialIn = this.getAcceptedBlockState(world, pos, itemInHand, face);
            if (materialIn != null) {
                materialIn = this.prepareMaterial(world, pos, state, player, hand, ray, materialIn);
            }

            if (materialIn == null) {
                return InteractionResult.PASS;
            } else {
                ICopycatBlockEntity copycatBE = this.getCopycatBlockEntity(world, pos);
                if (copycatBE == null) {
                    return InteractionResult.PASS;
                } else if (copycatBE.getMaterial().is(materialIn.getBlock())) {
                    if (!copycatBE.cycleMaterial()) {
                        return InteractionResult.PASS;
                    } else {
                        copycatBE.getLevel().playSound(null, copycatBE.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.75F, 0.95F);
                        return InteractionResult.SUCCESS;
                    }
                } else if (copycatBE.hasCustomMaterial()) {
                    return InteractionResult.PASS;
                } else if (world.isClientSide()) {
                    return InteractionResult.SUCCESS;
                } else {
                    copycatBE.setMaterial(materialIn);
                    copycatBE.setConsumedItem(itemInHand);
                    copycatBE.getLevel().playSound(null, copycatBE.getBlockPos(), materialIn.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 0.75F);
                    if (player.isCreative()) {
                        return InteractionResult.SUCCESS;
                    } else {
                        itemInHand.shrink(1);
                        if (itemInHand.isEmpty()) {
                            player.setItemInHand(hand, ItemStack.EMPTY);
                        }

                        return InteractionResult.SUCCESS;
                    }
                }
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    default void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer != null) {
            ItemStack offhandItem = placer.getItemInHand(InteractionHand.OFF_HAND);
            BlockState appliedState = this.getAcceptedBlockState(worldIn, pos, offhandItem, Direction.orderedByNearest(placer)[0]);
            if (appliedState != null) {
                ICopycatBlockEntity copycatBE = this.getCopycatBlockEntity(worldIn, pos);
                if (copycatBE != null) {
                    if (!copycatBE.hasCustomMaterial()) {
                        copycatBE.setMaterial(appliedState);
                        copycatBE.setConsumedItem(offhandItem);
                        if (!(placer instanceof Player player && player.isCreative())) {
                            offhandItem.shrink(1);
                            if (offhandItem.isEmpty()) {
                                placer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                            }
                        }
                    }
                }
            }
        }
    }

    default void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving, ICopycatBlock.OnRemoveHandler handler) {
        if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
            if (!isMoving) {
                ICopycatBlockEntity copycatBE = this.getCopycatBlockEntity(world, pos);
                if (copycatBE != null) {
                    Block.popResource(world, pos, copycatBE.getConsumedItem());
                }
            }

            handler.handle(state, world, pos, newState, isMoving);
            world.removeBlockEntity(pos);
        }
    }

    default BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (player.isCreative()) {
            ICopycatBlockEntity copycatBE = this.getCopycatBlockEntity(level, pos);
            if (copycatBE != null) {
                copycatBE.setConsumedItem(ItemStack.EMPTY);
            }
        }

        return state;
    }

    static BlockState getAppearance(
        ICopycatBlock block,
        BlockState state,
        BlockAndTintGetter level,
        BlockPos pos,
        Direction side,
        @Nullable BlockState queryState,
        @Nullable BlockPos queryPos
    ) {
        if (block.isIgnoredConnectivitySide(level, state, side, pos, queryPos, queryState)) {
            return state;
        } else {
            BlockState material = getMaterial(level, pos);
            return material.is(Blocks.AIR) ? AllBlocks.COPYCAT_BASE.defaultBlockState() : material;
        }
    }

    static BlockState getMaterial(BlockGetter reader, BlockPos targetPos) {
        return reader.getBlockEntity(targetPos) instanceof ICopycatBlockEntity cbe ? cbe.getMaterial() : Blocks.AIR.defaultBlockState();
    }

    static ItemRequirement getRequiredItemsForLayer(BlockState state, IntegerProperty property) {
        int count = state.getValue(property);
        return count == 0
            ? ItemRequirement.NONE
            : new ItemRequirement(
                IntStream.range(0, count).mapToObj($ -> new StackRequirement(new ItemStack(state.getBlock().asItem()), ItemUseType.CONSUME)).toList()
            );
    }

    @NotNull
    default BlockState rotateCopycat(@NotNull BlockState pState, Rotation pRot) {
        return this.transform(pState, new StructureTransform(BlockPos.ZERO, Axis.Y, pRot, Mirror.NONE));
    }

    @NotNull
    default BlockState mirrorCopycat(@NotNull BlockState pState, @NotNull Mirror pMirror) {
        return this.transform(pState, new StructureTransform(BlockPos.ZERO, null, Rotation.NONE, pMirror));
    }

    @Override
    default BlockState transform(BlockState state, StructureTransform transform) {
        Axis rotationAxis = transform.rotationAxis;
        Rotation rotation = transform.rotation;
        Mirror mirror = transform.mirror;
        Block block = state.getBlock();
        if (mirror != null) {
            state = state.mirror(mirror);
        }

        if (rotationAxis == Axis.Y) {
            if (block instanceof BellBlock) {
                if (state.getValue(BlockStateProperties.BELL_ATTACHMENT) == BellAttachType.DOUBLE_WALL) {
                    state = state.setValue(BlockStateProperties.BELL_ATTACHMENT, BellAttachType.SINGLE_WALL);
                }

                return state.setValue(BellBlock.FACING, rotation.rotate(state.getValue(BellBlock.FACING)));
            } else {
                return state.rotate(rotation);
            }
        } else if (block instanceof FaceAttachedHorizontalDirectionalBlock) {
            EnumProperty<Direction> facingProperty = FaceAttachedHorizontalDirectionalBlock.FACING;
            EnumProperty<AttachFace> faceProperty = FaceAttachedHorizontalDirectionalBlock.FACE;
            Direction stateFacing = state.getValue(facingProperty);
            AttachFace stateFace = state.getValue(faceProperty);
            boolean z = rotationAxis == Axis.Z;
            Direction forcedAxis = z ? Direction.WEST : Direction.SOUTH;
            if (stateFacing.getAxis() == rotationAxis && stateFace == AttachFace.WALL) {
                return state;
            } else {
                for (int i = 0; i < rotation.ordinal(); i++) {
                    stateFace = state.getValue(faceProperty);
                    stateFacing = state.getValue(facingProperty);
                    boolean b = state.getValue(faceProperty) == AttachFace.CEILING;
                    state = state.setValue(facingProperty, b ? forcedAxis : forcedAxis.getOpposite());
                    if (stateFace != AttachFace.WALL) {
                        state = state.setValue(faceProperty, AttachFace.WALL);
                    } else if (stateFacing.getAxisDirection() == (z ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE)) {
                        state = state.setValue(faceProperty, AttachFace.FLOOR);
                    } else {
                        state = state.setValue(faceProperty, AttachFace.CEILING);
                    }
                }

                return state;
            }
        } else {
            boolean halfTurn = rotation == Rotation.CLOCKWISE_180;
            if (!(block instanceof StairBlock)) {
                if (state.hasProperty(BlockStateProperties.FACING)) {
                    state = state.setValue(BlockStateProperties.FACING, transform.rotateFacing(state.getValue(BlockStateProperties.FACING)));
                } else if (state.hasProperty(BlockStateProperties.AXIS)) {
                    state = state.setValue(BlockStateProperties.AXIS, transform.rotateAxis(state.getValue(BlockStateProperties.AXIS)));
                } else if (halfTurn) {
                    if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                        Direction stateFacing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                        if (stateFacing.getAxis() == rotationAxis) {
                            return state;
                        }
                    }

                    state = state.rotate(rotation);
                    if (state.hasProperty(SlabBlock.TYPE) && state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE) {
                        state = state.setValue(SlabBlock.TYPE, state.getValue(SlabBlock.TYPE) == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM);
                    }
                }

                return state;
            } else {
                if (state.getValue(StairBlock.FACING).getAxis() != rotationAxis) {
                    for (int ix = 0; ix < rotation.ordinal(); ix++) {
                        Direction direction = state.getValue(StairBlock.FACING);
                        Half half = state.getValue(StairBlock.HALF);
                        if (direction.getAxisDirection() == AxisDirection.POSITIVE ^ half == Half.BOTTOM ^ direction.getAxis() == Axis.Z) {
                            state = state.cycle(StairBlock.HALF);
                        } else {
                            state = state.setValue(StairBlock.FACING, direction.getOpposite());
                        }
                    }
                } else if (halfTurn) {
                    state = state.cycle(StairBlock.HALF);
                }

                return state;
            }
        }
    }

    default boolean isIgnoredConnectivitySide(
        BlockAndTintGetter reader, BlockState fromState, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState
    ) {
        if (CopycatExternalContext.isForBlockingLogic()) {
            return false;
        } else if (toPos == null) {
            return true;
        } else {
            toState = reader.getBlockState(toPos);
            return !this.checkConnection(reader, toPos, fromPos, toState);
        }
    }

    default boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState fromState) {
        BlockState toState = reader.getBlockState(toPos);
        return toState.getBlock() instanceof ICopycatBlock ? true : this.checkConnection(reader, fromPos, toPos, fromState);
    }

    default boolean checkConnection(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState fromState) {
        Vec3i diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        } else {
            Direction facing = Direction.getApproximateNearest((float)diff.getX(), (float)diff.getY(), (float)diff.getZ());
            BlockState toState = reader.getBlockState(toPos);
            if (facing != null) {
                return BlockFaceUtils.faceMatch(reader, fromState, fromPos, toState, toPos, facing);
            } else if (diff.getX() != 0 && diff.getY() != 0 && diff.getZ() != 0) {
                return true;
            } else {
                for (Axis axis : Iterate.axes) {
                    if (diff.get(axis) != 0) {
                        Vec3i axisDiff = Vec3i.ZERO;
                        axisDiff = axisDiff.relative(axis, diff.get(axis));
                        BlockPos midPos = fromPos.offset(axisDiff);
                        BlockState midState = reader.getBlockState(midPos);
                        Vec3i remainingDiff = diff.subtract(axisDiff);
                        Direction remainingFacing = Direction.getApproximateNearest(
                            (float)remainingDiff.getX(), (float)remainingDiff.getY(), (float)remainingDiff.getZ()
                        );
                        if (!BlockFaceUtils.faceMatch(reader, midState, midPos, toState, toPos, remainingFacing)) {
                            return false;
                        }
                    }
                }

                return true;
            }
        }
    }

    default boolean canOcclude(BlockGetter level, BlockState state, BlockPos pos) {
        BlockState material = getMaterial(level, pos);
        return material.is(AllBlocks.COPYCAT_BASE) ? false : material.canOcclude();
    }

    default Optional<Boolean> shapeCanOccludeNeighbor(BlockGetter level, BlockPos pos, BlockState state, BlockPos neighborPos, Direction dir) {
        return Optional.empty();
    }

    static boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        BlockPos toPos = pos.relative(dir);
        if (level instanceof ScaledBlockAndTintGetter scaledLevel && state.getBlock() instanceof IMultiStateCopycatBlock) {
            CopycatExternalContext.setPropertyForAppearance(scaledLevel.getPropertyForRender(state, pos));
        } else {
            CopycatExternalContext.setPropertyForAppearance(null);
        }

        if (BlockFaceUtils.canOcclude(level, neighborState, toPos, state, pos, dir.getOpposite())) {
            BlockState material = state.getBlock() instanceof IMultiStateCopycatBlock
                ? IMultiStateCopycatBlock.getMaterial(level, pos, CopycatExternalContext.getPropertyForAppearance())
                : (state.getBlock() instanceof ICopycatBlock ? getMaterial(level, pos) : state);
            BlockState neighborMaterial = neighborState.getBlock() instanceof IMultiStateCopycatBlock && level instanceof ScaledBlockAndTintGetter scaledLevel
                ? IMultiStateCopycatBlock.getMaterial(level, toPos, scaledLevel.getPropertyForRender(neighborState, toPos))
                : (neighborState.getBlock() instanceof ICopycatBlock ? getMaterial(level, toPos) : neighborState);
            return material.skipRendering(neighborMaterial, dir.getOpposite());
        } else {
            return false;
        }
    }

    @FunctionalInterface
    public interface OnRemoveHandler {
        void handle(BlockState var1, Level var2, BlockPos var3, BlockState var4, boolean var5);
    }
}
