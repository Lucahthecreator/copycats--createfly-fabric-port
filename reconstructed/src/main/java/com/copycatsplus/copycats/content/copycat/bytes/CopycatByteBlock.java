/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.mojang.math.OctahedralGroup
 *  com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement
 *  com.zurrtum.create.catnip.data.Iterate
 *  com.zurrtum.create.content.contraptions.StructureTransform
 *  com.zurrtum.create.content.equipment.wrench.IWrenchable
 *  com.zurrtum.create.content.schematics.requirement.ItemRequirement
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Position
 *  net.minecraft.core.Vec3i
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemInstance
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.pathfinder.PathComputationType
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.copycatsplus.copycats.utility.MathUtils;
import com.google.common.collect.ImmutableMap;
import com.mojang.math.OctahedralGroup;
import com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatByteBlock
extends WaterloggedMultiStateCopycatBlock
implements SpecialBlockItemRequirement {
    public static BooleanProperty TOP_NE = BooleanProperty.create((String)"top_northeast");
    public static BooleanProperty TOP_NW = BooleanProperty.create((String)"top_northwest");
    public static BooleanProperty TOP_SE = BooleanProperty.create((String)"top_southeast");
    public static BooleanProperty TOP_SW = BooleanProperty.create((String)"top_southwest");
    public static BooleanProperty BOTTOM_NE = BooleanProperty.create((String)"bottom_northeast");
    public static BooleanProperty BOTTOM_NW = BooleanProperty.create((String)"bottom_northwest");
    public static BooleanProperty BOTTOM_SE = BooleanProperty.create((String)"bottom_southeast");
    public static BooleanProperty BOTTOM_SW = BooleanProperty.create((String)"bottom_southwest");
    private final Function<BlockState, VoxelShape> shapesCache;
    private final ImmutableMap<FaceData, VoxelShape> partialFaceCache;
    public static final List<Byte> allBytes = new ArrayList<Byte>(8);
    public static final Map<String, Byte> byteMap;

    public CopycatByteBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue((Property)TOP_NE, (Comparable)Boolean.valueOf(false))).setValue((Property)TOP_NW, (Comparable)Boolean.valueOf(false))).setValue((Property)TOP_SE, (Comparable)Boolean.valueOf(false))).setValue((Property)TOP_SW, (Comparable)Boolean.valueOf(false))).setValue((Property)BOTTOM_NE, (Comparable)Boolean.valueOf(false))).setValue((Property)BOTTOM_NW, (Comparable)Boolean.valueOf(false))).setValue((Property)BOTTOM_SE, (Comparable)Boolean.valueOf(false))).setValue((Property)BOTTOM_SW, (Comparable)Boolean.valueOf(false)));
        this.shapesCache = this.getShapeForEachState(CopycatByteBlock::calculateMultiFaceShape);
        ImmutableMap.Builder builder = ImmutableMap.builder();
        BlockState state = (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue((Property)TOP_NE, (Comparable)Boolean.valueOf(true))).setValue((Property)TOP_NW, (Comparable)Boolean.valueOf(true))).setValue((Property)TOP_SE, (Comparable)Boolean.valueOf(true))).setValue((Property)TOP_SW, (Comparable)Boolean.valueOf(true))).setValue((Property)BOTTOM_NE, (Comparable)Boolean.valueOf(true))).setValue((Property)BOTTOM_NW, (Comparable)Boolean.valueOf(true))).setValue((Property)BOTTOM_SE, (Comparable)Boolean.valueOf(true))).setValue((Property)BOTTOM_SW, (Comparable)Boolean.valueOf(true));
        for (String property : this.storageProperties()) {
            for (Direction face : Direction.values()) {
                builder.put((Object)new FaceData(property, face), (Object)BlockFaceUtils.getPartialFaceShape(null, state, property, face));
            }
        }
        this.partialFaceCache = builder.build();
    }

    @Override
    public String defaultProperty() {
        return BOTTOM_NW.getName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return new Vec3i(2, 2, 2);
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        if (property.equals(TOP_NE.getName())) {
            return (Boolean)state.getValue((Property)TOP_NE);
        }
        if (property.equals(TOP_NW.getName())) {
            return (Boolean)state.getValue((Property)TOP_NW);
        }
        if (property.equals(TOP_SE.getName())) {
            return (Boolean)state.getValue((Property)TOP_SE);
        }
        if (property.equals(TOP_SW.getName())) {
            return (Boolean)state.getValue((Property)TOP_SW);
        }
        if (property.equals(BOTTOM_NE.getName())) {
            return (Boolean)state.getValue((Property)BOTTOM_NE);
        }
        if (property.equals(BOTTOM_NW.getName())) {
            return (Boolean)state.getValue((Property)BOTTOM_NW);
        }
        if (property.equals(BOTTOM_SE.getName())) {
            return (Boolean)state.getValue((Property)BOTTOM_SE);
        }
        if (property.equals(BOTTOM_SW.getName())) {
            return (Boolean)state.getValue((Property)BOTTOM_SW);
        }
        return false;
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(TOP_NE, TOP_NW, TOP_SE, TOP_SW, BOTTOM_NE, BOTTOM_NW, BOTTOM_SE, BOTTOM_SW).stream().map(Property::getName).collect(Collectors.toSet());
    }

    @Override
    public int getColorIndex(String property) {
        Byte bite = byteMap.get(property);
        return bite.x ^ bite.y ^ bite.z ? 1 : 0;
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        return CopycatByteBlock.byByte(hitLocation.getX() > 0, hitLocation.getY() > 0, hitLocation.getZ() > 0).getName();
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        Byte bite = byteMap.get(property);
        return new Vec3i(bite.x ? 1 : 0, bite.y ? 1 : 0, bite.z ? 1 : 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition((StateDefinition.Builder<Block, BlockState>)pBuilder.add(new Property[]{TOP_NE, TOP_NW, TOP_SE, TOP_SW, BOTTOM_NE, BOTTOM_NW, BOTTOM_SE, BOTTOM_SW}));
    }

    private static VoxelShape calculateMultiFaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        for (Byte bite : allBytes) {
            if (!((Boolean)pState.getValue((Property)CopycatByteBlock.byByte(bite))).booleanValue()) continue;
            int offsetX = bite.x ? 8 : 0;
            int offsetY = bite.y ? 8 : 0;
            int offsetZ = bite.z ? 8 : 0;
            shape = Shapes.or((VoxelShape)shape, (VoxelShape)Block.box((double)offsetX, (double)offsetY, (double)offsetZ, (double)(offsetX + 8), (double)(offsetY + 8), (double)(offsetZ + 8)));
        }
        return shape;
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Objects.requireNonNull(this.shapesCache.apply(pState));
    }

    @Override
    public VoxelShape getPartialFaceShape(BlockGetter level, BlockState state, String property, Direction face) {
        if (!this.partExists(state, property)) {
            return Shapes.empty();
        }
        return Objects.requireNonNull((VoxelShape)this.partialFaceCache.getOrDefault((Object)new FaceData(property, face), (Object)Shapes.empty()));
    }

    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return switch (pType) {
            case PathComputationType.LAND -> true;
            default -> false;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) {
            return null;
        }
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        Vec3 bias = Vec3.atLowerCornerOf((Vec3i)context.getClickedFace().getUnitVec3i()).scale(0.0625);
        Vec3 biasedLocation = context.getClickLocation().add(bias);
        if (!BlockPos.containing((Position)biasedLocation).equals((Object)context.getClickedPos())) {
            biasedLocation = MathUtils.clampToGrid(biasedLocation, (Vec3i)context.getClickedPos());
        }
        Byte bite = CopycatByteBlock.getByteFromVec(biasedLocation, context.getClickedPos());
        if (state.is((Object)this)) {
            if (!((Boolean)state.getValue((Property)CopycatByteBlock.byByte(bite))).booleanValue()) {
                return (BlockState)state.setValue((Property)CopycatByteBlock.byByte(bite), (Comparable)Boolean.valueOf(true));
            }
            if (!((Boolean)state.getValue((Property)CopycatByteBlock.byByte(bite = bite.relative(context.getClickedFace())))).booleanValue()) {
                return (BlockState)state.setValue((Property)CopycatByteBlock.byByte(bite), (Comparable)Boolean.valueOf(true));
            }
            Copycats.LOGGER.warn("Can't figure out where to place a byte! Please file an issue if you see this.");
            return state;
        }
        return (BlockState)stateForPlacement.setValue((Property)CopycatByteBlock.byByte(bite), (Comparable)Boolean.valueOf(true));
    }

    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        Byte bite;
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is((Object)this.asItem())) {
            return false;
        }
        Vec3 bias = Vec3.atLowerCornerOf((Vec3i)pUseContext.getClickedFace().getUnitVec3i()).scale(0.0625);
        Vec3 biasedLocation = pUseContext.getClickLocation().add(bias);
        if (!BlockPos.containing((Position)biasedLocation).equals((Object)pUseContext.getClickedPos())) {
            biasedLocation = MathUtils.clampToGrid(biasedLocation, (Vec3i)pUseContext.getClickedPos());
        }
        return (Boolean)pState.getValue((Property)CopycatByteBlock.byByte(bite = CopycatByteBlock.getByteFromVec(biasedLocation, pUseContext.getClickedPos()))) == false;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        this.onWrenched(state, context);
        int byteCount = 0;
        for (Byte bite : allBytes) {
            if (!((Boolean)state.getValue((Property)CopycatByteBlock.byByte(bite))).booleanValue()) continue;
            ++byteCount;
        }
        if (byteCount <= 1) {
            return super.onSneakWrenched(state, context);
        }
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        Vec3 bias = Vec3.atLowerCornerOf((Vec3i)context.getClickedFace().getUnitVec3i()).scale(-0.0625);
        Byte bite = CopycatByteBlock.getByteFromVec(context.getClickLocation().add(bias), context.getClickedPos());
        if (world instanceof ServerLevel) {
            if (player != null) {
                List drops = Block.getDrops((BlockState)((BlockState)this.defaultBlockState().setValue((Property)CopycatByteBlock.byByte(bite), (Comparable)Boolean.valueOf(true))), (ServerLevel)((ServerLevel)world), (BlockPos)pos, (BlockEntity)world.getBlockEntity(pos), (Entity)player, (ItemInstance)context.getItemInHand());
                if (!player.isCreative()) {
                    for (ItemStack drop : drops) {
                        player.getInventory().placeItemBackInInventory(drop);
                    }
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            world.setBlockAndUpdate(pos, (BlockState)state.setValue((Property)CopycatByteBlock.byByte(bite), (Comparable)Boolean.valueOf(false)));
            IWrenchable.playRemoveSound((Level)world, (BlockPos)pos);
        }
        return InteractionResult.SUCCESS;
    }

    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return IMultiStateCopycatBlock.getRequiredItemsForParts(state, TOP_NE, TOP_NW, TOP_SE, TOP_SW, BOTTOM_NE, BOTTOM_NW, BOTTOM_SE, BOTTOM_SW);
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return this.mapBytes(state, bite -> CopycatByteBlock.transformByte(transform, bite));
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        be.getMaterialItemStorage().remapStorage(key -> {
            Byte bite = byteMap.get(key);
            if (bite == null) {
                Copycats.LOGGER.debug("Can't find byte for key {} in {}. NBT data for this copycat byte might be corrupt.", key, (Object)state);
                return key;
            }
            return CopycatByteBlock.byByte(CopycatByteBlock.transformByte(transform, bite)).getName();
        });
    }

    private static Byte transformByte(StructureTransform transform, Byte bite) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            bite = bite.mirror(transform.mirror);
        }
        if (transform.rotationAxis != null) {
            bite = switch (transform.rotationAxis) {
                default -> throw new MatchException(null, null);
                case Direction.Axis.X -> bite.rotateX(transform.rotation);
                case Direction.Axis.Y -> bite.rotateY(transform.rotation);
                case Direction.Axis.Z -> bite.rotateZ(transform.rotation);
            };
        }
        return bite;
    }

    public static Byte getByteFromVec(Vec3 vec, BlockPos pos) {
        vec = vec.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        return CopycatByteBlock.bite(vec.x > 0.5, vec.y > 0.5, vec.z > 0.5);
    }

    private BlockState mapBytes(BlockState state, Function<Byte, Byte> mapper) {
        BlockState blockstate = state;
        for (Byte bite : allBytes) {
            blockstate = (BlockState)blockstate.setValue((Property)CopycatByteBlock.byByte(mapper.apply(bite)), (Comparable)((Boolean)state.getValue((Property)CopycatByteBlock.byByte(bite))));
        }
        return blockstate;
    }

    public static Byte bite(boolean x, boolean y, boolean z) {
        return new Byte(x, y, z);
    }

    public static BooleanProperty byByte(Byte bite) {
        return CopycatByteBlock.byByte(bite.x, bite.y, bite.z);
    }

    public static BooleanProperty byByte(boolean x, boolean y, boolean z) {
        if (y) {
            if (x) {
                if (z) {
                    return TOP_SE;
                }
                return TOP_NE;
            }
            if (z) {
                return TOP_SW;
            }
            return TOP_NW;
        }
        if (x) {
            if (z) {
                return BOTTOM_SE;
            }
            return BOTTOM_NE;
        }
        if (z) {
            return BOTTOM_SW;
        }
        return BOTTOM_NW;
    }

    static {
        for (boolean x : Iterate.falseAndTrue) {
            for (boolean y : Iterate.falseAndTrue) {
                for (boolean z : Iterate.falseAndTrue) {
                    allBytes.add(CopycatByteBlock.bite(x, y, z));
                }
            }
        }
        byteMap = allBytes.stream().collect(Collectors.toMap(b -> CopycatByteBlock.byByte(b).getName(), b -> b));
    }

    public record FaceData(String property, Direction direction) {
    }

    public record Byte(boolean x, boolean y, boolean z) {
        public Byte copy() {
            return new Byte(this.x, this.y, this.z);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Byte) {
                Byte other = (Byte)obj;
                return this.x == other.x && this.y == other.y && this.z == other.z;
            }
            return false;
        }

        public Byte set(Direction.Axis axis, boolean value) {
            return switch (axis) {
                default -> throw new MatchException(null, null);
                case Direction.Axis.X -> new Byte(value, this.y, this.z);
                case Direction.Axis.Y -> new Byte(this.x, value, this.z);
                case Direction.Axis.Z -> new Byte(this.x, this.y, value);
            };
        }

        public boolean get(Direction.Axis axis) {
            return switch (axis) {
                default -> throw new MatchException(null, null);
                case Direction.Axis.X -> this.x;
                case Direction.Axis.Y -> this.y;
                case Direction.Axis.Z -> this.z;
            };
        }

        public Byte relative(Direction direction) {
            return this.set(direction.getAxis(), !this.get(direction.getAxis()));
        }

        public Byte rotateX(Rotation rotation) {
            if (rotation == Rotation.CLOCKWISE_90) {
                return new Byte(this.x, this.z, !this.y);
            }
            if (rotation == Rotation.CLOCKWISE_180) {
                return new Byte(this.x, this.y, this.z);
            }
            if (rotation == Rotation.COUNTERCLOCKWISE_90) {
                return new Byte(this.x, !this.z, this.y);
            }
            return this;
        }

        public Byte rotateY(Rotation rotation) {
            if (rotation == Rotation.CLOCKWISE_90) {
                return new Byte(!this.z, this.y, this.x);
            }
            if (rotation == Rotation.CLOCKWISE_180) {
                return new Byte(!this.x, this.y, !this.z);
            }
            if (rotation == Rotation.COUNTERCLOCKWISE_90) {
                return new Byte(this.z, this.y, !this.x);
            }
            return this;
        }

        public Byte rotateZ(Rotation rotation) {
            if (rotation == Rotation.CLOCKWISE_90) {
                return new Byte(this.y, !this.x, this.z);
            }
            if (rotation == Rotation.CLOCKWISE_180) {
                return new Byte(this.x, this.y, this.z);
            }
            if (rotation == Rotation.COUNTERCLOCKWISE_90) {
                return new Byte(!this.y, this.x, this.z);
            }
            return this;
        }

        public Byte mirror(Mirror mirror) {
            boolean invertX = mirror.rotation() == OctahedralGroup.INVERT_X;
            boolean invertY = mirror.rotation() == OctahedralGroup.INVERT_Y;
            boolean invertZ = mirror.rotation() == OctahedralGroup.INVERT_Z;
            return new Byte(invertX != this.x, invertY != this.y, invertZ != this.z);
        }

        @Override
        public int hashCode() {
            return Byte.i(this.x) + Byte.i(this.y) << 1 + Byte.i(this.z) << 2;
        }

        private static int i(boolean b) {
            return b ? 1 : 0;
        }

        @Override
        public String toString() {
            return "(" + this.x + ", " + this.y + ", " + this.z + ")";
        }
    }
}

