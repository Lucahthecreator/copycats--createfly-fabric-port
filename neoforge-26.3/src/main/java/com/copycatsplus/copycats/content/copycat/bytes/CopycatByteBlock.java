package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.Copycats;
import net.minecraft.util.Prediction;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.copycatsplus.copycats.utility.MathUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
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
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatByteBlock extends WaterloggedMultiStateCopycatBlock implements SpecialBlockItemRequirement {
    public static BooleanProperty TOP_NE = BooleanProperty.create("top_northeast");
    public static BooleanProperty TOP_NW = BooleanProperty.create("top_northwest");
    public static BooleanProperty TOP_SE = BooleanProperty.create("top_southeast");
    public static BooleanProperty TOP_SW = BooleanProperty.create("top_southwest");
    public static BooleanProperty BOTTOM_NE = BooleanProperty.create("bottom_northeast");
    public static BooleanProperty BOTTOM_NW = BooleanProperty.create("bottom_northwest");
    public static BooleanProperty BOTTOM_SE = BooleanProperty.create("bottom_southeast");
    public static BooleanProperty BOTTOM_SW = BooleanProperty.create("bottom_southwest");
    private final Function<BlockState, VoxelShape> shapesCache;
    private final ImmutableMap<CopycatByteBlock.FaceData, VoxelShape> partialFaceCache;
    public static final List<CopycatByteBlock.Byte> allBytes = new ArrayList<>(8);
    public static final Map<String, CopycatByteBlock.Byte> byteMap;

    public CopycatByteBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.defaultBlockState()
                .setValue(TOP_NE, false)
                .setValue(TOP_NW, false)
                .setValue(TOP_SE, false)
                .setValue(TOP_SW, false)
                .setValue(BOTTOM_NE, false)
                .setValue(BOTTOM_NW, false)
                .setValue(BOTTOM_SE, false)
                .setValue(BOTTOM_SW, false)
        );
        this.shapesCache = this.getShapeForEachState(CopycatByteBlock::calculateMultiFaceShape);
        Builder<CopycatByteBlock.FaceData, VoxelShape> builder = ImmutableMap.builder();
        BlockState state = this.defaultBlockState()
            .setValue(TOP_NE, true)
            .setValue(TOP_NW, true)
            .setValue(TOP_SE, true)
            .setValue(TOP_SW, true)
            .setValue(BOTTOM_NE, true)
            .setValue(BOTTOM_NW, true)
            .setValue(BOTTOM_SE, true)
            .setValue(BOTTOM_SW, true);

        for (String property : this.storageProperties()) {
            for (Direction face : Direction.values()) {
                builder.put(new CopycatByteBlock.FaceData(property, face), BlockFaceUtils.getPartialFaceShape(null, state, property, face));
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
            return state.getValue(TOP_NE);
        } else if (property.equals(TOP_NW.getName())) {
            return state.getValue(TOP_NW);
        } else if (property.equals(TOP_SE.getName())) {
            return state.getValue(TOP_SE);
        } else if (property.equals(TOP_SW.getName())) {
            return state.getValue(TOP_SW);
        } else if (property.equals(BOTTOM_NE.getName())) {
            return state.getValue(BOTTOM_NE);
        } else if (property.equals(BOTTOM_NW.getName())) {
            return state.getValue(BOTTOM_NW);
        } else if (property.equals(BOTTOM_SE.getName())) {
            return state.getValue(BOTTOM_SE);
        } else {
            return property.equals(BOTTOM_SW.getName()) ? state.getValue(BOTTOM_SW) : false;
        }
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(TOP_NE, TOP_NW, TOP_SE, TOP_SW, BOTTOM_NE, BOTTOM_NW, BOTTOM_SE, BOTTOM_SW).stream().map(Property::getName).collect(Collectors.toSet());
    }

    @Override
    public int getColorIndex(String property) {
        CopycatByteBlock.Byte bite = byteMap.get(property);
        return bite.x ^ bite.y ^ bite.z ? 1 : 0;
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        return byByte(hitLocation.getX() > 0, hitLocation.getY() > 0, hitLocation.getZ() > 0).getName();
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        CopycatByteBlock.Byte bite = byteMap.get(property);
        return new Vec3i(bite.x ? 1 : 0, bite.y ? 1 : 0, bite.z ? 1 : 0);
    }

    @Override
    protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(TOP_NE, TOP_NW, TOP_SE, TOP_SW, BOTTOM_NE, BOTTOM_NW, BOTTOM_SE, BOTTOM_SW));
    }

    private static VoxelShape calculateMultiFaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();

        for (CopycatByteBlock.Byte bite : allBytes) {
            if (pState.getValue(byByte(bite))) {
                int offsetX = bite.x ? 8 : 0;
                int offsetY = bite.y ? 8 : 0;
                int offsetZ = bite.z ? 8 : 0;
                shape = Shapes.or(shape, Block.box(offsetX, offsetY, offsetZ, offsetX + 8, offsetY + 8, offsetZ + 8));
            }
        }

        return shape;
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Objects.requireNonNull(this.shapesCache.apply(pState));
    }

    @Override
    public VoxelShape getPartialFaceShape(BlockGetter level, BlockState state, String property, Direction face) {
        return !this.partExists(state, property)
            ? Shapes.empty()
            : Objects.requireNonNull((VoxelShape)this.partialFaceCache.getOrDefault(new CopycatByteBlock.FaceData(property, face), Shapes.empty()));
    }

    @Override
    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return switch (pType) {
            case LAND -> true;
            default -> false;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) {
            return null;
        } else {
            BlockPos blockPos = context.getClickedPos();
            BlockState state = context.getLevel().getBlockState(blockPos);
            Vec3 bias = Vec3.atLowerCornerOf(context.getClickedFace().getUnitVec3i()).scale(0.0625);
            Vec3 biasedLocation = context.getClickLocation().add(bias);
            if (!BlockPos.containing(biasedLocation).equals(context.getClickedPos())) {
                biasedLocation = MathUtils.clampToGrid(biasedLocation, context.getClickedPos());
            }

            CopycatByteBlock.Byte bite = getByteFromVec(biasedLocation, context.getClickedPos());
            if (state.is(this)) {
                if (!state.getValue(byByte(bite))) {
                    return state.setValue(byByte(bite), true);
                } else {
                    bite = bite.relative(context.getClickedFace());
                    if (!state.getValue(byByte(bite))) {
                        return state.setValue(byByte(bite), true);
                    } else {
                        Copycats.LOGGER.warn("Can't figure out where to place a byte! Please file an issue if you see this.");
                        return state;
                    }
                }
            } else {
                return stateForPlacement.setValue(byByte(bite), true);
            }
        }
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is(this.asItem())) {
            return false;
        } else {
            Vec3 bias = Vec3.atLowerCornerOf(pUseContext.getClickedFace().getUnitVec3i()).scale(0.0625);
            Vec3 biasedLocation = pUseContext.getClickLocation().add(bias);
            if (!BlockPos.containing(biasedLocation).equals(pUseContext.getClickedPos())) {
                biasedLocation = MathUtils.clampToGrid(biasedLocation, pUseContext.getClickedPos());
            }

            CopycatByteBlock.Byte bite = getByteFromVec(biasedLocation, pUseContext.getClickedPos());
            return !pState.getValue(byByte(bite));
        }
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        this.onWrenched(state, context);
        int byteCount = 0;

        for (CopycatByteBlock.Byte bite : allBytes) {
            if (state.getValue(byByte(bite))) {
                byteCount++;
            }
        }

        if (byteCount <= 1) {
            return super.onSneakWrenched(state, context);
        } else {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            Player player = context.getPlayer();
            Vec3 bias = Vec3.atLowerCornerOf(context.getClickedFace().getUnitVec3i()).scale(-0.0625);
            CopycatByteBlock.Byte bitex = getByteFromVec(context.getClickLocation().add(bias), context.getClickedPos());
            if (world instanceof ServerLevel) {
                if (player != null) {
                    List<ItemStack> drops = Block.getDrops(
                        this.defaultBlockState().setValue(byByte(bitex), true),
                        (ServerLevel)world,
                        pos,
                        world.getBlockEntity(pos),
                        player,
                        context.getItemInHand()
                    );
                    if (!player.isCreative()) {
                        for (ItemStack drop : drops) {
                            player.getInventory().placeItemBackInInventory(drop, Prediction.SERVER_ONLY);
                        }
                    }
                }

                BlockPos up = pos.relative(Direction.UP);
                world.setBlockAndUpdate(pos, state.setValue(byByte(bitex), false));
                IWrenchable.playRemoveSound(world, pos);
            }

            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return IMultiStateCopycatBlock.getRequiredItemsForParts(state, TOP_NE, TOP_NW, TOP_SE, TOP_SW, BOTTOM_NE, BOTTOM_NW, BOTTOM_SE, BOTTOM_SW);
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return this.mapBytes(state, bite -> transformByte(transform, bite));
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        be.getMaterialItemStorage().remapStorage(key -> {
            CopycatByteBlock.Byte bite = byteMap.get(key);
            if (bite == null) {
                Copycats.LOGGER.debug("Can't find byte for key {} in {}. NBT data for this copycat byte might be corrupt.", key, state);
                return key;
            } else {
                return byByte(transformByte(transform, bite)).getName();
            }
        });
    }

    private static CopycatByteBlock.Byte transformByte(StructureTransform transform, CopycatByteBlock.Byte bite) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            bite = bite.mirror(transform.mirror);
        }

        if (transform.rotationAxis != null) {
            bite = switch (transform.rotationAxis) {
                case X -> bite.rotateX(transform.rotation);
                case Y -> bite.rotateY(transform.rotation);
                case Z -> bite.rotateZ(transform.rotation);
            };
        }

        return bite;
    }

    public static CopycatByteBlock.Byte getByteFromVec(Vec3 vec, BlockPos pos) {
        vec = vec.subtract(pos.getX(), pos.getY(), pos.getZ());
        return bite(vec.x > 0.5, vec.y > 0.5, vec.z > 0.5);
    }

    private BlockState mapBytes(BlockState state, Function<CopycatByteBlock.Byte, CopycatByteBlock.Byte> mapper) {
        BlockState blockstate = state;

        for (CopycatByteBlock.Byte bite : allBytes) {
            blockstate = blockstate.setValue(byByte(mapper.apply(bite)), state.getValue(byByte(bite)));
        }

        return blockstate;
    }

    public static CopycatByteBlock.Byte bite(boolean x, boolean y, boolean z) {
        return new CopycatByteBlock.Byte(x, y, z);
    }

    public static BooleanProperty byByte(CopycatByteBlock.Byte bite) {
        return byByte(bite.x, bite.y, bite.z);
    }

    public static BooleanProperty byByte(boolean x, boolean y, boolean z) {
        if (y) {
            if (x) {
                return z ? TOP_SE : TOP_NE;
            } else {
                return z ? TOP_SW : TOP_NW;
            }
        } else if (x) {
            return z ? BOTTOM_SE : BOTTOM_NE;
        } else {
            return z ? BOTTOM_SW : BOTTOM_NW;
        }
    }

    static {
        for (boolean x : Iterate.falseAndTrue) {
            for (boolean y : Iterate.falseAndTrue) {
                for (boolean z : Iterate.falseAndTrue) {
                    allBytes.add(bite(x, y, z));
                }
            }
        }

        byteMap = allBytes.stream().collect(Collectors.toMap(b -> byByte(b).getName(), b -> (CopycatByteBlock.Byte)b));
    }

    public record Byte(boolean x, boolean y, boolean z) {
        public CopycatByteBlock.Byte copy() {
            return new CopycatByteBlock.Byte(this.x, this.y, this.z);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else {
                return !(obj instanceof CopycatByteBlock.Byte other) ? false : this.x == other.x && this.y == other.y && this.z == other.z;
            }
        }

        public CopycatByteBlock.Byte set(Axis axis, boolean value) {
            return switch (axis) {
                case X -> new CopycatByteBlock.Byte(value, this.y, this.z);
                case Y -> new CopycatByteBlock.Byte(this.x, value, this.z);
                case Z -> new CopycatByteBlock.Byte(this.x, this.y, value);
            };
        }

        public boolean get(Axis axis) {
            return switch (axis) {
                case X -> this.x;
                case Y -> this.y;
                case Z -> this.z;
            };
        }

        public CopycatByteBlock.Byte relative(Direction direction) {
            return this.set(direction.getAxis(), !this.get(direction.getAxis()));
        }

        public CopycatByteBlock.Byte rotateX(Rotation rotation) {
            if (rotation == Rotation.CLOCKWISE_90) {
                return new CopycatByteBlock.Byte(this.x, this.z, !this.y);
            } else if (rotation == Rotation.CLOCKWISE_180) {
                return new CopycatByteBlock.Byte(this.x, this.y, this.z);
            } else {
                return rotation == Rotation.COUNTERCLOCKWISE_90 ? new CopycatByteBlock.Byte(this.x, !this.z, this.y) : this;
            }
        }

        public CopycatByteBlock.Byte rotateY(Rotation rotation) {
            if (rotation == Rotation.CLOCKWISE_90) {
                return new CopycatByteBlock.Byte(!this.z, this.y, this.x);
            } else if (rotation == Rotation.CLOCKWISE_180) {
                return new CopycatByteBlock.Byte(!this.x, this.y, !this.z);
            } else {
                return rotation == Rotation.COUNTERCLOCKWISE_90 ? new CopycatByteBlock.Byte(this.z, this.y, !this.x) : this;
            }
        }

        public CopycatByteBlock.Byte rotateZ(Rotation rotation) {
            if (rotation == Rotation.CLOCKWISE_90) {
                return new CopycatByteBlock.Byte(this.y, !this.x, this.z);
            } else if (rotation == Rotation.CLOCKWISE_180) {
                return new CopycatByteBlock.Byte(this.x, this.y, this.z);
            } else {
                return rotation == Rotation.COUNTERCLOCKWISE_90 ? new CopycatByteBlock.Byte(!this.y, this.x, this.z) : this;
            }
        }

        public CopycatByteBlock.Byte mirror(Mirror mirror) {
            boolean invertX = mirror.rotation() == OctahedralGroup.INVERT_X;
            boolean invertY = mirror.rotation() == OctahedralGroup.INVERT_Y;
            boolean invertZ = mirror.rotation() == OctahedralGroup.INVERT_Z;
            return new CopycatByteBlock.Byte(invertX != this.x, invertY != this.y, invertZ != this.z);
        }

        @Override
        public int hashCode() {
            return i(this.x) + i(this.y) << 1 + i(this.z) << 2;
        }

        private static int i(boolean b) {
            return b ? 1 : 0;
        }

        @Override
        public String toString() {
            return "(" + this.x + ", " + this.y + ", " + this.z + ")";
        }
    }

    public record FaceData(String property, Direction direction) {
    }
}
