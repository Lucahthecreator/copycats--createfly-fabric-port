package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.copycatsplus.copycats.foundation.copycat.CopycatExternalContext;
import net.minecraft.util.Prediction;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.foundation.copycat.StateType;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.internal.create.AllTags;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.AllItems;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement.ItemUseType;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement.StackRequirement;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
public interface IMultiStateCopycatBlock extends ICopycatBlock, IStateType {
    @Override
    default StateType stateType() {
        return StateType.MULTI;
    }

    @Nullable
    default IMultiStateCopycatBlockEntity getCopycatBlockEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity == null) {
            return null;
        } else {
            return blockEntity instanceof IMultiStateCopycatBlockEntity copycatBE ? copycatBE : null;
        }
    }

    String defaultProperty();

    Vec3i vectorScale(BlockState var1);

    Set<String> storageProperties();

    int getColorIndex(String var1);

    boolean partExists(BlockState var1, String var2);

    Vec3i getVectorFromProperty(BlockState var1, String var2);

    String getPropertyFromInteraction(BlockState var1, BlockGetter var2, Vec3i var3, BlockPos var4, Direction var5, Vec3 var6);

    default String getPropertyFromInteraction(BlockState state, BlockGetter level, BlockPos pos, Vec3 hitVec, Direction face, boolean targetingSolid) {
        if (targetingSolid) {
            hitVec = hitVec.subtract(Vec3.atLowerCornerOf(face.getUnitVec3i()).scale(0.05));
        } else {
            hitVec = hitVec.add(Vec3.atLowerCornerOf(face.getUnitVec3i()).scale(0.05));
        }

        hitVec = hitVec.add(-pos.getX(), -pos.getY(), -pos.getZ());
        Vec3i scale = this.vectorScale(state);
        Vec3 var12 = hitVec.multiply(scale.getX(), scale.getY(), scale.getZ());
        BlockPos location = new BlockPos((int)var12.x(), (int)var12.y(), (int)var12.z());
        return this.getPropertyFromInteraction(state, level, location, pos, face, hitVec);
    }

    default String getPropertyFromInteraction(BlockState state, BlockGetter level, BlockPos pos, BlockHitResult hit, boolean targetingSolid) {
        Vec3 hitVec = hit.getLocation();
        return this.getPropertyFromInteraction(state, level, pos, hitVec, hit.getDirection(), targetingSolid);
    }

    default String getPropertyFromRender(String renderingProperty, BlockState state, BlockGetter level, Vec3i vector, BlockPos blockPos) {
        Vec3i scale = this.vectorScale(state);
        return this.getPropertyFromInteraction(
            state, level, vector, blockPos, Direction.UP, Vec3.atLowerCornerOf(vector).multiply(1.0 / scale.getX(), 1.0 / scale.getY(), 1.0 / scale.getZ())
        );
    }

    @Override
    default InteractionResult toggleCT(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.isShiftKeyDown() || !player.getItemInHand(hand).equals(ItemStack.EMPTY)) {
            return InteractionResult.PASS;
        } else if (!this.canToggleCT(state, level, pos)) {
            return InteractionResult.PASS;
        } else {
            String property = this.getPropertyFromInteraction(state, level, pos, hit, true);
            IMultiStateCopycatBlockEntity be = this.getCopycatBlockEntity(level, pos);
            if (be == null) {
                return InteractionResult.PASS;
            } else {
                be.setEnableCT(property, !be.getMaterialItemStorage().getMaterialItem(property).enableCT());
                BlockEntityUtils.redraw((BlockEntity)be);
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    default InteractionResult onWrenched(BlockState state, UseOnContext context) {
        IMultiStateCopycatBlockEntity copycatBE = this.getCopycatBlockEntity(context.getLevel(), context.getClickedPos());
        if (copycatBE == null) {
            return InteractionResult.PASS;
        } else {
            String property = this.getPropertyFromInteraction(
                state, context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), true
            );
            if (!this.partExists(state, property)) {
                return InteractionResult.PASS;
            } else {
                MaterialItemStorage.MaterialItem material = copycatBE.getMaterialItemStorage().getMaterialItem(property);
                ItemStack consumedItem = material.consumedItem();
                if (!consumedItem.isEmpty()) {
                    for (String prop : copycatBE.getMaterialItemStorage().getAllProperties()) {
                        if (!prop.equals(property)) {
                            MaterialItemStorage.MaterialItem materialItem = copycatBE.getMaterialItemStorage().getMaterialItem(prop);
                            if (materialItem.material().getBlock().equals(material.material().getBlock()) && materialItem.consumedItem().isEmpty()) {
                                copycatBE.setConsumedItem(prop, consumedItem);
                                consumedItem = ItemStack.EMPTY;
                                break;
                            }
                        }
                    }
                }

                if (!copycatBE.getMaterialItemStorage().hasCustomMaterial(property)) {
                    return InteractionResult.PASS;
                } else {
                    Player player = context.getPlayer();
                    if (!player.isCreative()) {
                        player.getInventory().placeItemBackInInventory(consumedItem, Prediction.SERVER_ONLY);
                    }

                    context.getLevel().levelEvent(2001, context.getClickedPos(), Block.getId(material.material()));
                    copycatBE.setMaterial(property, AllBlocks.COPYCAT_BASE.defaultBlockState());
                    copycatBE.setConsumedItem(property, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }

    @Nullable
    default BlockState getAcceptedBlockState(String property, Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        return this.getAcceptedBlockState(pLevel, pPos, item, face);
    }

    @Override
    default InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.getItemInHand(hand).is(AllTags.AllItemTags.WRENCH.tag)) {
            InteractionResult result = AllItems.WRENCH.useOn(new UseOnContext(player, hand, hit));
            if (result.consumesAction()) {
                return result;
            }
        }

        InteractionResult result = this.toggleCT(state, level, pos, player, hand, hit);
        if (result.consumesAction()) {
            return result;
        } else if (player != null && player.mayBuild()) {
            String property = this.getPropertyFromInteraction(state, level, pos, hit, true);
            Direction face = hit.getDirection();
            ItemStack itemInHand = player.getItemInHand(hand);
            BlockState material = this.getAcceptedBlockState(property, level, pos, itemInHand, face);
            if (material != null) {
                material = this.prepareMaterial(level, pos, state, player, hand, hit, material);
            }

            if (material == null) {
                return InteractionResult.PASS;
            } else {
                IMultiStateCopycatBlockEntity copycatBE = this.getCopycatBlockEntity(level, pos);
                if (copycatBE == null) {
                    return InteractionResult.PASS;
                } else if (!this.partExists(state, property)) {
                    return InteractionResult.PASS;
                } else if (!copycatBE.getMaterialItemStorage().getMaterialItem(property).material().is(material.getBlock())) {
                    if (copycatBE.getMaterialItemStorage().hasCustomMaterial(property)) {
                        return InteractionResult.PASS;
                    } else if (level.isClientSide()) {
                        return InteractionResult.SUCCESS;
                    } else {
                        boolean freeToApply = copycatBE.getMaterialItemStorage()
                            .getAllConsumedItems()
                            .stream()
                            .anyMatch(s -> s.getItem() == itemInHand.getItem());
                        copycatBE.setMaterial(property, material);
                        if (!freeToApply) {
                            copycatBE.setConsumedItem(property, itemInHand);
                        }

                        copycatBE.getLevel().playSound(null, copycatBE.getBlockPos(), material.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 0.75F);
                        if (player.isCreative()) {
                            return InteractionResult.SUCCESS;
                        } else {
                            if (!freeToApply) {
                                itemInHand.shrink(1);
                            }

                            if (itemInHand.isEmpty()) {
                                player.setItemInHand(hand, ItemStack.EMPTY);
                            }

                            return InteractionResult.SUCCESS;
                        }
                    }
                } else if (!copycatBE.cycleMaterial(property)) {
                    return InteractionResult.PASS;
                } else {
                    copycatBE.getLevel().playSound(null, copycatBE.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.75F, 0.95F);
                    return InteractionResult.SUCCESS;
                }
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    default void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer != null) {
            ItemStack offhandItem = placer.getItemInHand(InteractionHand.OFF_HAND);
            BlockState appliedState = this.getAcceptedBlockState(level, pos, offhandItem, Direction.orderedByNearest(placer)[0]);
            if (appliedState != null) {
                IMultiStateCopycatBlockEntity copycatBE = this.getCopycatBlockEntity(level, pos);
                if (copycatBE != null) {
                    for (String property : this.storageProperties()) {
                        if (this.partExists(state, property) && !copycatBE.getMaterialItemStorage().hasCustomMaterial(property)) {
                            boolean freeToApply = copycatBE.getMaterialItemStorage()
                                .getAllConsumedItems()
                                .stream()
                                .anyMatch(s -> s.getItem() == offhandItem.getItem());
                            copycatBE.setMaterial(property, appliedState);
                            if (!freeToApply) {
                                copycatBE.setConsumedItem(property, offhandItem);
                            }

                            if (!(placer instanceof Player player && player.isCreative())) {
                                if (!freeToApply) {
                                    offhandItem.shrink(1);
                                }

                                if (offhandItem.isEmpty()) {
                                    placer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    default void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving, ICopycatBlock.OnRemoveHandler handler) {
        if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
            if (!isMoving) {
                IMultiStateCopycatBlockEntity copycatBE = this.getCopycatBlockEntity(level, pos);
                if (copycatBE != null) {
                    copycatBE.getMaterialItemStorage().getAllConsumedItems().forEach(stack -> Block.popResource(level, pos, stack));
                }
            }

            handler.handle(state, level, pos, newState, isMoving);
            level.removeBlockEntity(pos);
        }
    }

    @Override
    default BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (player.isCreative()) {
            IMultiStateCopycatBlockEntity copycatBE = this.getCopycatBlockEntity(level, pos);
            if (copycatBE != null) {
                copycatBE.getMaterialItemStorage()
                    .getAllProperties()
                    .forEach(key -> copycatBE.getMaterialItemStorage().getMaterialItem(key).setConsumedItem(ItemStack.EMPTY));
            }
        }

        return state;
    }

    default void fillEmptyParts(Level level, BlockPos pos, BlockState state, BlockState material) {
        IMultiStateCopycatBlockEntity copycatBE = this.getCopycatBlockEntity(level, pos);
        if (copycatBE != null) {
            for (String property : copycatBE.getMaterialItemStorage().getAllProperties()) {
                if (!copycatBE.getMaterialItemStorage().hasCustomMaterial(property) && this.partExists(state, property)) {
                    copycatBE.setMaterial(property, material);
                }
            }
        }
    }

    static BlockState getAppearance(
        IMultiStateCopycatBlock block,
        BlockState state,
        BlockAndTintGetter level,
        BlockPos pos,
        Direction side,
        @Nullable BlockState queryState,
        @Nullable BlockPos queryPos
    ) {
        if (level instanceof ScaledBlockAndTintGetter scaledLevel && state.getBlock() instanceof IMultiStateCopycatBlock) {
            CopycatExternalContext.setPropertyForAppearance(scaledLevel.getPropertyForRender(state, pos));
        } else {
            CopycatExternalContext.setPropertyForAppearance(block.defaultProperty());
        }

        if (block.isIgnoredConnectivitySide(level, state, side, pos, queryPos, queryState)) {
            return state;
        } else {
            String property = CopycatExternalContext.getPropertyForAppearance();
            if (property == null) {
                property = block.defaultProperty();
            }

            BlockState material = getMaterial(level, pos, property);
            return material.is(Blocks.AIR) ? AllBlocks.COPYCAT_BASE.defaultBlockState() : material;
        }
    }

    VoxelShape getPartialFaceShape(BlockGetter var1, BlockState var2, String var3, Direction var4);

    static BlockState getMaterial(BlockGetter reader, BlockPos targetPos, String property) {
        return reader.getBlockEntity(targetPos) instanceof IMultiStateCopycatBlockEntity cbe && cbe.getMaterialItemStorage().getMaterialItem(property) != null
            ? cbe.getMaterialItemStorage().getMaterialItem(property).material()
            : Blocks.AIR.defaultBlockState();
    }

    void transformStorage(BlockState var1, IMultiStateCopycatBlockEntity var2, StructureTransform var3);

    static ItemRequirement getRequiredItemsForParts(BlockState state, BooleanProperty... property) {
        int count = 0;

        for (BooleanProperty part : property) {
            if (state.getValue(part)) {
                count++;
            }
        }

        return count == 0
            ? ItemRequirement.NONE
            : new ItemRequirement(
                IntStream.range(0, count).mapToObj($ -> new StackRequirement(new ItemStack(state.getBlock().asItem()), ItemUseType.CONSUME)).toList()
            );
    }

    default boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return this.canConnectTexturesToward(reader, fromPos, toPos, state);
    }

    @Override
    default boolean canOcclude(BlockGetter level, BlockState state, BlockPos pos) {
        return level instanceof ScaledBlockAndTintGetter scaledWorld ? this.canOcclude(scaledWorld.getPropertyForRender(state, pos), level, state, pos) : false;
    }

    default boolean canOcclude(String property, BlockGetter level, BlockState state, BlockPos pos) {
        BlockState material = getMaterial(level, pos, property);
        return material.is(AllBlocks.COPYCAT_BASE) ? false : material.canOcclude();
    }

    @Override
    default Optional<Boolean> shapeCanOccludeNeighbor(BlockGetter level, BlockPos pos, BlockState state, BlockPos neighborPos, Direction dir) {
        BlockState neighborState = level.getBlockState(neighborPos);
        return Optional.of(BlockFaceUtils.canOcclude(level, neighborState, neighborPos, state, pos, dir.getOpposite()));
    }
}
