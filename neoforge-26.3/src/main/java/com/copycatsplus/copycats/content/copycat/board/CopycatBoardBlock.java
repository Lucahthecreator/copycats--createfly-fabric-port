package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.CCShapes;
import net.minecraft.util.Prediction;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.zurrtum.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.content.contraptions.StructureTransform;
import com.zurrtum.create.content.equipment.wrench.IWrenchable;
import com.zurrtum.create.content.schematics.requirement.ItemRequirement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class CopycatBoardBlock extends WaterloggedMultiStateCopycatBlock implements ICustomCTBlocking, SpecialBlockItemRequirement {
    public static BooleanProperty UP = BlockStateProperties.UP;
    public static BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static BooleanProperty EAST = BlockStateProperties.EAST;
    public static BooleanProperty WEST = BlockStateProperties.WEST;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    private final Function<BlockState, VoxelShape> shapesCache;
    private final ImmutableMap<CopycatBoardBlock.FaceData, VoxelShape> partialFaceCache;

    public CopycatBoardBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.defaultBlockState()
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
        );
        this.shapesCache = this.getShapeForEachState(CopycatBoardBlock::calculateMultifaceShape);
        Builder<CopycatBoardBlock.FaceData, VoxelShape> builder = ImmutableMap.builder();
        BlockState state = this.defaultBlockState()
            .setValue(UP, true)
            .setValue(DOWN, true)
            .setValue(NORTH, true)
            .setValue(SOUTH, true)
            .setValue(EAST, true)
            .setValue(WEST, true);

        for (String property : this.storageProperties()) {
            for (Direction face : Direction.values()) {
                builder.put(new CopycatBoardBlock.FaceData(property, face), BlockFaceUtils.getPartialFaceShape(null, state, property, face));
            }
        }

        this.partialFaceCache = builder.build();
    }

    @Override
    public String defaultProperty() {
        return UP.getName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return new Vec3i(1, 1, 1);
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(UP, DOWN, NORTH, EAST, SOUTH, WEST).stream().map(Property::getName).collect(Collectors.toSet());
    }

    @Override
    public int getColorIndex(String property) {
        if (property.equals(UP.getName())) {
            return 0;
        } else if (property.equals(DOWN.getName())) {
            return 0;
        } else if (property.equals(NORTH.getName())) {
            return 1;
        } else if (property.equals(SOUTH.getName())) {
            return 1;
        } else if (property.equals(EAST.getName())) {
            return 2;
        } else {
            return property.equals(WEST.getName()) ? 2 : 0;
        }
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        if (property.equals(UP.getName())) {
            return state.getValue(UP);
        } else if (property.equals(DOWN.getName())) {
            return state.getValue(DOWN);
        } else if (property.equals(NORTH.getName())) {
            return state.getValue(NORTH);
        } else if (property.equals(SOUTH.getName())) {
            return state.getValue(SOUTH);
        } else if (property.equals(EAST.getName())) {
            return state.getValue(EAST);
        } else {
            return property.equals(WEST.getName()) ? state.getValue(WEST) : false;
        }
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        facing = Direction.fromAxisAndDirection(facing.getAxis(), unscaledHit.get(facing.getAxis()) > 0.5 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE);
        BooleanProperty face = byDirection(facing);
        return face.getName();
    }

    @Override
    public String getPropertyFromRender(String renderingProperty, BlockState state, BlockGetter level, Vec3i vector, BlockPos blockPos) {
        return renderingProperty;
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return Vec3i.ZERO;
    }

    @Override
    protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(UP, DOWN, NORTH, SOUTH, EAST, WEST));
    }

    @Override
    public boolean isIgnoredConnectivitySide(
        BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos, @Nullable BlockState toState
    ) {
        return toPos == null ? true : !reader.getBlockState(toPos).is(this);
    }

    @Override
    public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return reader.getBlockState(toPos).is(this);
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        return !reader.getBlockState(ctPos).is(this) ? Optional.empty() : Optional.of(false);
    }

    @Override
    public boolean isPathfindable(BlockState pState, PathComputationType pType) {
        return switch (pType) {
            case LAND -> !pState.getValue(UP) && pState.getValue(DOWN) || pState.getValue(UP);
            default -> false;
        };
    }

    private static VoxelShape calculateMultifaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();

        for (Direction direction : Iterate.directions) {
            if (pState.getValue(byDirection(direction))) {
                shape = Shapes.joinUnoptimized(shape, CCShapes.BOARD.get(direction).toShape(), BooleanOp.OR);
            }
        }

        return shape.optimize();
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
            : Objects.requireNonNull((VoxelShape)this.partialFaceCache.getOrDefault(new CopycatBoardBlock.FaceData(property, face), Shapes.empty()));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) {
            return null;
        } else {
            BlockPos blockPos = context.getClickedPos();
            BlockState state = context.getLevel().getBlockState(blockPos);
            if (state.is(this)) {
                return !state.getValue(byDirection(context.getClickedFace().getOpposite()))
                    ? state.setValue(byDirection(context.getClickedFace().getOpposite()), true)
                    : state.setValue(byDirection(context.getClickedFace()), true);
            } else {
                return stateForPlacement.setValue(byDirection(context.getClickedFace().getOpposite()), true);
            }
        }
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is(this.asItem())) {
            return false;
        } else {
            if (!pState.getValue(byDirection(pUseContext.getClickedFace().getOpposite()))) {
                Direction direction = pUseContext.getClickedFace().getOpposite();
                double pos = getByAxis(pUseContext.getClickedPos(), direction.getAxis());
                if (getByAxis(direction.getUnitVec3i(), direction.getAxis()) > 0) {
                    pos++;
                }

                double loc = getByAxis(pUseContext.getClickLocation(), direction.getAxis());
                if (Math.abs(pos - loc) < 0.125) {
                    return true;
                }
            }

            if (!pState.getValue(byDirection(pUseContext.getClickedFace()))) {
                double hitLoc = getByAxis(pUseContext.getClickLocation(), pUseContext.getClickedFace().getAxis());
                int directionx = getByAxis(pUseContext.getClickedFace().getUnitVec3i(), pUseContext.getClickedFace().getAxis());
                double offset = hitLoc - Math.round(hitLoc);
                if (Mth.sign(directionx) == Mth.sign(offset) && Math.abs(offset) < 0.125) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        this.onWrenched(state, context);
        int faceCount = 0;

        for (Direction direction : Iterate.directions) {
            if (state.getValue(byDirection(direction))) {
                faceCount++;
            }
        }

        if (faceCount <= 1) {
            return super.onSneakWrenched(state, context);
        } else {
            List<Direction> options = new ArrayList<>(6);

            for (Direction directionx : Iterate.directions) {
                if (state.getValue(byDirection(directionx))) {
                    double pos = getByAxis(context.getClickedPos(), directionx.getAxis());
                    if (getByAxis(directionx.getUnitVec3i(), directionx.getAxis()) > 0) {
                        pos++;
                    }

                    double loc = getByAxis(context.getClickLocation(), directionx.getAxis());
                    if (Math.abs(pos - loc) < 0.125) {
                        options.add(directionx);
                    }
                }
            }

            if (options.size() > 1) {
                Direction backup = options.get(0);
                options.removeIf(d -> d.getAxis() != context.getClickedFace().getAxis());
                if (options.size() == 0) {
                    options.add(backup);
                }
            }

            if (options.size() == 0) {
                return super.onSneakWrenched(state, context);
            } else {
                Level world = context.getLevel();
                BlockPos posx = context.getClickedPos();
                Player player = context.getPlayer();
                if (world instanceof ServerLevel) {
                    if (player != null) {
                        List<ItemStack> drops = Block.getDrops(
                            this.defaultBlockState().setValue(byDirection(options.get(0)), true),
                            (ServerLevel)world,
                            posx,
                            world.getBlockEntity(posx),
                            player,
                            context.getItemInHand()
                        );
                        if (!player.isCreative()) {
                            for (ItemStack drop : drops) {
                                player.getInventory().placeItemBackInInventory(drop, Prediction.SERVER_ONLY);
                            }
                        }
                    }

                    BlockPos up = posx.relative(Direction.UP);
                    world.setBlockAndUpdate(posx, state.setValue(byDirection(options.get(0)), false));
                    IWrenchable.playRemoveSound(world, posx);
                }

                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return IMultiStateCopycatBlock.getRequiredItemsForParts(state, UP, DOWN, NORTH, SOUTH, EAST, WEST);
    }

    private static int getByAxis(Vec3i pos, Axis axis) {
        return switch (axis) {
            case X -> pos.getX();
            case Y -> pos.getY();
            case Z -> pos.getZ();
        };
    }

    private static double getByAxis(Position pos, Axis axis) {
        return switch (axis) {
            case X -> pos.x();
            case Y -> pos.y();
            case Z -> pos.z();
        };
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return this.mapDirections(state, dir -> BlockUtils.transformFacing(transform, dir));
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        be.getMaterialItemStorage().remapStorage(key -> directionToProperty(BlockUtils.transformFacing(transform, propertyToDirection(key))));
    }

    private static Direction propertyToDirection(String property) {
        return switch (property) {
            case "up" -> Direction.UP;
            case "down" -> Direction.DOWN;
            case "north" -> Direction.NORTH;
            case "south" -> Direction.SOUTH;
            case "east" -> Direction.EAST;
            case "west" -> Direction.WEST;
            default -> throw new IllegalStateException("Unexpected value: " + property);
        };
    }

    public static String directionToProperty(Direction direction) {
        return direction.getName().toLowerCase(Locale.ROOT);
    }

    private BlockState mapDirections(BlockState pState, Function<Direction, Direction> pDirectionalFunction) {
        BlockState blockstate = pState;

        for (Direction direction : Iterate.directions) {
            blockstate = blockstate.setValue(byDirection(pDirectionalFunction.apply(direction)), pState.getValue(byDirection(direction)));
        }

        return blockstate;
    }

    public static BooleanProperty byDirection(Direction direction) {
        return PROPERTY_BY_DIRECTION.get(direction);
    }

    public record FaceData(String property, Direction direction) {
    }
}
