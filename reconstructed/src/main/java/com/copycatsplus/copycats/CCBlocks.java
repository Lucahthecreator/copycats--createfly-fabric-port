/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.AllInteractionBehaviours
 *  com.zurrtum.create.AllMovementBehaviours
 *  com.zurrtum.create.api.behaviour.interaction.MovingInteractionBehaviour
 *  com.zurrtum.create.api.behaviour.movement.MovementBehaviour
 *  com.zurrtum.create.api.contraption.BlockMovementChecks
 *  com.zurrtum.create.api.contraption.BlockMovementChecks$CheckResult
 *  com.zurrtum.create.content.contraptions.behaviour.DoorMovingInteraction
 *  com.zurrtum.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour
 *  com.zurrtum.create.content.kinetics.simpleRelays.CogwheelBlockItem
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.properties.BlockSetType
 *  net.minecraft.world.level.material.MapColor
 */
package com.copycatsplus.copycats;

import com.copycatsplus.copycats.CCBuilderTransformers;
import com.copycatsplus.copycats.CCTags;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CStress;
import com.copycatsplus.copycats.config.FeatureCategory;
import com.copycatsplus.copycats.config.FeatureToggle;
import com.copycatsplus.copycats.content.copycat.beam.CopycatBeamBlock;
import com.copycatsplus.copycats.content.copycat.block.CopycatBlockBlock;
import com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock;
import com.copycatsplus.copycats.content.copycat.button.CopycatButtonBlock;
import com.copycatsplus.copycats.content.copycat.byte_panel.CopycatBytePanelBlock;
import com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlock;
import com.copycatsplus.copycats.content.copycat.corner_slice.CopycatCornerSliceBlock;
import com.copycatsplus.copycats.content.copycat.door.CopycatDoorBlock;
import com.copycatsplus.copycats.content.copycat.fence.CopycatFenceBlock;
import com.copycatsplus.copycats.content.copycat.fence_gate.CopycatFenceGateBlock;
import com.copycatsplus.copycats.content.copycat.flat_pane.CopycatFlatPaneBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatGlassFluidPipeBlock;
import com.copycatsplus.copycats.content.copycat.ghost_block.CopycatGhostBlock;
import com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock;
import com.copycatsplus.copycats.content.copycat.half_panel.CopycatHalfPanelBlock;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderBlock;
import com.copycatsplus.copycats.content.copycat.layer.CopycatLayerBlock;
import com.copycatsplus.copycats.content.copycat.pane.CopycatPaneBlock;
import com.copycatsplus.copycats.content.copycat.pressure_plate.CopycatPressurePlateBlock;
import com.copycatsplus.copycats.content.copycat.pressure_plate.CopycatWeightedPressurePlate;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlock;
import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlock;
import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeBlock;
import com.copycatsplus.copycats.content.copycat.slope_layer.CopycatSlopeLayerBlock;
import com.copycatsplus.copycats.content.copycat.stacked_half_layer.CopycatStackedHalfLayerBlock;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsBlock;
import com.copycatsplus.copycats.content.copycat.trapdoor.CopycatTrapdoorBlock;
import com.copycatsplus.copycats.content.copycat.vertical_half_layer.CopycatVerticalHalfLayerBlock;
import com.copycatsplus.copycats.content.copycat.vertical_slice.CopycatVerticalSliceBlock;
import com.copycatsplus.copycats.content.copycat.vertical_slope.CopycatVerticalSlopeBlock;
import com.copycatsplus.copycats.content.copycat.vertical_stairs.CopycatVerticalStairBlock;
import com.copycatsplus.copycats.content.copycat.vertical_step.CopycatVerticalStepBlock;
import com.copycatsplus.copycats.content.copycat.wall.CopycatWallBlock;
import com.copycatsplus.copycats.foundation.copycat.CopycatBaseBlock;
import com.copycatsplus.copycats.foundation.copycat.WrappedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.tooltip.CopycatCharacteristics;
import com.copycatsplus.copycats.foundation.tooltip.CopycatDescription;
import com.copycatsplus.copycats.registrate.CopycatRegistrate;
import com.copycatsplus.copycats.utility.Platform;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.zurrtum.create.AllInteractionBehaviours;
import com.zurrtum.create.AllMovementBehaviours;
import com.zurrtum.create.AllTags;
import com.zurrtum.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.zurrtum.create.api.behaviour.movement.MovementBehaviour;
import com.zurrtum.create.api.contraption.BlockMovementChecks;
import com.zurrtum.create.content.contraptions.behaviour.DoorMovingInteraction;
import com.zurrtum.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour;
import com.zurrtum.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.zurrtum.create.foundation.data.BuilderTransformers;
import com.zurrtum.create.foundation.data.ModelGen;
import com.zurrtum.create.foundation.data.SharedProperties;
import com.zurrtum.create.foundation.data.TagGen;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;

public class CCBlocks {
    private static final CopycatRegistrate REGISTRATE = Copycats.getRegistrate();
    public static final BlockEntry<WrappedCopycatBlock> WRAPPED_COPYCAT = REGISTRATE.block("wrapped_copycat", WrappedCopycatBlock::new).transform(BuilderTransformers.copycat()).register();
    public static final BlockEntry<CopycatBaseBlock> COPYCAT_BASE = REGISTRATE.block("copycat_base", CopycatBaseBlock::new).initialProperties(SharedProperties::softMetal).properties(p -> p.mapColor(MapColor.GLOW_LICHEN).noOcclusion()).tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag).transform(TagGen.pickaxeOnly()).register();
    public static final BlockEntry<CopycatBlockBlock> COPYCAT_BLOCK = ((BlockBuilder)REGISTRATE.block("copycat_block", CopycatBlockBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register())).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.COPY_CAT)).tag(CCTags.Items.COPYCAT_BLOCK.tag).transform(ModelGen.customItemModel("copycat_base", "block")).register();
    public static final BlockEntry<CopycatBeamBlock> COPYCAT_BEAM = ((BlockBuilder)REGISTRATE.block("copycat_beam", CopycatBeamBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register())).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE)).tag(CCTags.Items.COPYCAT_BEAM.tag).transform(ModelGen.customItemModel("copycat_base", "beam")).register();
    public static final BlockEntry<CopycatBoardBlock> COPYCAT_BOARD = ((BlockBuilder)REGISTRATE.block("copycat_board", CopycatBoardBlock::new).transform(CCBuilderTransformers.multiCopycat()).transform(FeatureToggle.register(FeatureCategory.MULTISTATES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.MULTI_STATE)).tag(CCTags.Items.COPYCAT_BOARD.tag).transform(ModelGen.customItemModel("copycat_base", "board")).register();
    public static final BlockEntry<CopycatButtonBlock> COPYCAT_WOODEN_BUTTON = ((BlockBuilder)REGISTRATE.block("copycat_wooden_button", p -> new CopycatButtonBlock((BlockBehaviour.Properties)p, BlockSetType.OAK, 30, true)).transform(CCBuilderTransformers.copycat()).properties(p -> p.isValidSpawn((state, level, pos, entity) -> false).noCollision()).tag(BlockTags.BUTTONS).tag(BlockTags.WOODEN_BUTTONS).transform(FeatureToggle.register(FeatureCategory.REDSTONE, FeatureCategory.FUNCTIONAL))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "button")).register();
    public static final BlockEntry<CopycatButtonBlock> COPYCAT_STONE_BUTTON = ((BlockBuilder)REGISTRATE.block("copycat_stone_button", p -> new CopycatButtonBlock((BlockBehaviour.Properties)p, BlockSetType.STONE, 20, false)).transform(CCBuilderTransformers.copycat()).properties(p -> p.isValidSpawn((state, level, pos, entity) -> false).noCollision()).tag(BlockTags.BUTTONS).tag(BlockTags.STONE_BUTTONS).transform(FeatureToggle.register(FeatureCategory.REDSTONE, FeatureCategory.FUNCTIONAL))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "button")).register();
    public static final BlockEntry<CopycatByteBlock> COPYCAT_BYTE = ((BlockBuilder)REGISTRATE.block("copycat_byte", CopycatByteBlock::new).transform(CCBuilderTransformers.multiCopycat()).transform(FeatureToggle.register(FeatureCategory.MULTISTATES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.MULTI_STATE)).transform(ModelGen.customItemModel("copycat_base", "byte")).register();
    public static final BlockEntry<CopycatBytePanelBlock> COPYCAT_BYTE_PANEL = ((BlockBuilder)REGISTRATE.block("copycat_byte_panel", CopycatBytePanelBlock::new).transform(CCBuilderTransformers.multiCopycat()).transform(FeatureToggle.register(FeatureCategory.MULTISTATES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.MULTI_STATE)).transform(ModelGen.customItemModel("copycat_base", "byte_panel")).register();
    public static final BlockEntry<CopycatFenceBlock> COPYCAT_FENCE = ((BlockBuilder)REGISTRATE.block("copycat_fence", CopycatFenceBlock::new).transform(CCBuilderTransformers.copycat()).tag(BlockTags.FENCES, CCTags.commonBlockTag("fences")).transform(FeatureToggle.register())).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE)).tag(CCTags.Items.COPYCAT_FENCE.tag).transform(ModelGen.customItemModel("copycat_base", "fence")).register();
    public static final BlockEntry<CopycatFenceGateBlock> COPYCAT_FENCE_GATE = ((BlockBuilder)REGISTRATE.block("copycat_fence_gate", CopycatFenceGateBlock::new).transform(CCBuilderTransformers.copycat()).properties(BlockBehaviour.Properties::forceSolidOn).tag(BlockTags.FENCE_GATES, CCTags.commonBlockTag("fence_gates"), BlockTags.UNSTABLE_BOTTOM_CENTER, AllTags.AllBlockTags.MOVABLE_EMPTY_COLLIDER.tag).transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.FUNCTIONAL)).tag(CCTags.Items.COPYCAT_FENCE_GATE.tag).transform(ModelGen.customItemModel("copycat_base", "fence_gate")).register();
    public static final BlockEntry<CopycatGhostBlock> COPYCAT_GHOST_BLOCK = ((BlockBuilder)REGISTRATE.block("copycat_ghost_block", CopycatGhostBlock::new).transform(CCBuilderTransformers.copycat()).properties(p -> p.isValidSpawn((state, level, pos, entity) -> false).noCollision()).transform(FeatureToggle.register())).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.GHOST)).transform(ModelGen.customItemModel("copycat_base", "ghost_block")).register();
    public static final BlockEntry<CopycatHalfLayerBlock> COPYCAT_HALF_LAYER = ((BlockBuilder)REGISTRATE.block("copycat_half_layer", CopycatHalfLayerBlock::new).transform(CCBuilderTransformers.multiCopycat()).transform(FeatureToggle.register(FeatureCategory.MULTISTATES, FeatureCategory.STACKABLES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.MULTI_STATE, CopycatCharacteristics.STACKABLE)).transform(ModelGen.customItemModel("copycat_base", "half_layer")).register();
    public static final BlockEntry<CopycatVerticalHalfLayerBlock> COPYCAT_VERTICAL_HALF_LAYER = ((BlockBuilder)REGISTRATE.block("copycat_vertical_half_layer", CopycatVerticalHalfLayerBlock::new).transform(CCBuilderTransformers.multiCopycat()).transform(FeatureToggle.register(FeatureCategory.MULTISTATES, FeatureCategory.STACKABLES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.MULTI_STATE, CopycatCharacteristics.STACKABLE)).transform(ModelGen.customItemModel("copycat_base", "vertical_half_layer")).register();
    public static final BlockEntry<CopycatStackedHalfLayerBlock> COPYCAT_STACKED_HALF_LAYER = ((BlockBuilder)REGISTRATE.block("copycat_stacked_half_layer", CopycatStackedHalfLayerBlock::new).transform(CCBuilderTransformers.multiCopycat()).transform(FeatureToggle.register(FeatureCategory.MULTISTATES, FeatureCategory.STACKABLES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.MULTI_STATE, CopycatCharacteristics.STACKABLE)).transform(ModelGen.customItemModel("copycat_base", "stacked_half_layer")).register();
    public static final BlockEntry<CopycatHalfPanelBlock> COPYCAT_HALF_PANEL = ((BlockBuilder)REGISTRATE.block("copycat_half_panel", CopycatHalfPanelBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register())).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE)).transform(ModelGen.customItemModel("copycat_base", "half_panel")).register();
    public static final BlockEntry<CopycatLadderBlock> COPYCAT_LADDER = ((BlockBuilder)REGISTRATE.block("copycat_ladder", CopycatLadderBlock::new).transform(CCBuilderTransformers.copycat()).properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)).tag(BlockTags.CLIMBABLE).transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "ladder")).register();
    public static final BlockEntry<CopycatLayerBlock> COPYCAT_LAYER = ((BlockBuilder)REGISTRATE.block("copycat_layer", CopycatLayerBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.STACKABLES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.STACKABLE)).transform(ModelGen.customItemModel("copycat_base", "layer")).register();
    public static final BlockEntry<CopycatPressurePlateBlock> COPYCAT_WOODEN_PRESSURE_PLATE = ((BlockBuilder)REGISTRATE.block("copycat_wooden_pressure_plate", p -> new CopycatPressurePlateBlock((BlockBehaviour.Properties)p, BlockSetType.OAK)).transform(CCBuilderTransformers.copycat()).properties(p -> p.isValidSpawn((state, level, pos, entity) -> false).noCollision()).tag(BlockTags.PRESSURE_PLATES).tag(BlockTags.WOODEN_PRESSURE_PLATES).transform(FeatureToggle.register(FeatureCategory.REDSTONE, FeatureCategory.FUNCTIONAL))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "pressure_plate")).register();
    public static final BlockEntry<CopycatPressurePlateBlock> COPYCAT_STONE_PRESSURE_PLATE = ((BlockBuilder)REGISTRATE.block("copycat_stone_pressure_plate", p -> new CopycatPressurePlateBlock((BlockBehaviour.Properties)p, BlockSetType.STONE)).transform(CCBuilderTransformers.copycat()).properties(p -> p.isValidSpawn((state, level, pos, entity) -> false).noCollision()).tag(BlockTags.PRESSURE_PLATES).tag(BlockTags.STONE_PRESSURE_PLATES).transform(FeatureToggle.register(FeatureCategory.REDSTONE, FeatureCategory.FUNCTIONAL))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "pressure_plate")).register();
    public static final BlockEntry<CopycatWeightedPressurePlate> COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE = ((BlockBuilder)REGISTRATE.block("copycat_heavy_weighted_pressure_plate", p -> new CopycatWeightedPressurePlate(150, (BlockBehaviour.Properties)p, BlockSetType.IRON)).transform(CCBuilderTransformers.copycat()).properties(p -> p.isValidSpawn((state, level, pos, entity) -> false).noCollision()).tag(BlockTags.PRESSURE_PLATES).transform(FeatureToggle.register(FeatureCategory.REDSTONE, FeatureCategory.FUNCTIONAL))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "pressure_plate")).register();
    public static final BlockEntry<CopycatWeightedPressurePlate> COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE = ((BlockBuilder)REGISTRATE.block("copycat_light_weighted_pressure_plate", p -> new CopycatWeightedPressurePlate(15, (BlockBehaviour.Properties)p, BlockSetType.GOLD)).transform(CCBuilderTransformers.copycat()).properties(p -> p.isValidSpawn((state, level, pos, entity) -> false).noCollision()).tag(BlockTags.PRESSURE_PLATES).transform(FeatureToggle.register(FeatureCategory.REDSTONE, FeatureCategory.FUNCTIONAL))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "pressure_plate")).register();
    public static final BlockEntry<CopycatSlabBlock> COPYCAT_SLAB = ((BlockBuilder)REGISTRATE.block("copycat_slab", CopycatSlabBlock::new).transform(CCBuilderTransformers.multiCopycat()).tag(BlockTags.SLABS).transform(FeatureToggle.register(FeatureCategory.MULTISTATES))).item().tag(CCTags.Items.COPYCAT_SLAB.tag).onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.MULTI_STATE)).transform(ModelGen.customItemModel("copycat_base", "slab")).register();
    public static final BlockEntry<CopycatSliceBlock> COPYCAT_SLICE = ((BlockBuilder)REGISTRATE.block("copycat_slice", CopycatSliceBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.STACKABLES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.STACKABLE)).transform(ModelGen.customItemModel("copycat_base", "slice")).register();
    public static final BlockEntry<CopycatCornerSliceBlock> COPYCAT_CORNER_SLICE = ((BlockBuilder)REGISTRATE.block("copycat_corner_slice", CopycatCornerSliceBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.STACKABLES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.STACKABLE)).transform(ModelGen.customItemModel("copycat_base", "corner_slice")).register();
    public static final BlockEntry<CopycatStairsBlock> COPYCAT_STAIRS = ((BlockBuilder)REGISTRATE.block("copycat_stairs", CopycatStairsBlock::new).transform(CCBuilderTransformers.copycat()).tag(BlockTags.STAIRS).transform(FeatureToggle.register())).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE)).tag(CCTags.Items.COPYCAT_STAIRS.tag).transform(ModelGen.customItemModel("copycat_base", "stairs")).register();
    public static final BlockEntry<CopycatVerticalStairBlock> COPYCAT_VERTICAL_STAIRS = ((BlockBuilder)REGISTRATE.block("copycat_vertical_stairs", CopycatVerticalStairBlock::new).transform(CCBuilderTransformers.copycat()).tag(BlockTags.STAIRS).transform(FeatureToggle.register())).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE)).tag(CCTags.Items.COPYCAT_STAIRS.tag).transform(ModelGen.customItemModel("copycat_base", "vertical_stairs")).register();
    public static final BlockEntry<CopycatTrapdoorBlock> COPYCAT_TRAPDOOR = ((BlockBuilder)REGISTRATE.block("copycat_trapdoor", p -> new CopycatTrapdoorBlock((BlockBehaviour.Properties)p, BlockSetType.OAK)).transform(CCBuilderTransformers.copycat()).properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)).tag(BlockTags.TRAPDOORS).tag(BlockTags.WOODEN_TRAPDOORS).transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "trapdoor")).register();
    public static final BlockEntry<CopycatTrapdoorBlock> COPYCAT_IRON_TRAPDOOR = ((BlockBuilder)REGISTRATE.block("copycat_iron_trapdoor", p -> new CopycatTrapdoorBlock((BlockBehaviour.Properties)p, BlockSetType.IRON)).transform(CCBuilderTransformers.copycat()).properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)).tag(BlockTags.TRAPDOORS).transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "trapdoor")).register();
    public static final BlockEntry<CopycatVerticalSliceBlock> COPYCAT_VERTICAL_SLICE = ((BlockBuilder)REGISTRATE.block("copycat_vertical_slice", CopycatVerticalSliceBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.STACKABLES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.STACKABLE)).transform(ModelGen.customItemModel("copycat_base", "vertical_slice")).register();
    public static final BlockEntry<CopycatVerticalStepBlock> COPYCAT_VERTICAL_STEP = ((BlockBuilder)REGISTRATE.block("copycat_vertical_step", CopycatVerticalStepBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register())).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE)).tag(CCTags.Items.COPYCAT_VERTICAL_STEP.tag).transform(ModelGen.customItemModel("copycat_base", "vertical_step")).register();
    public static final BlockEntry<CopycatWallBlock> COPYCAT_WALL = ((BlockBuilder)REGISTRATE.block("copycat_wall", CopycatWallBlock::new).transform(CCBuilderTransformers.copycat()).properties(BlockBehaviour.Properties::forceSolidOn).tag(BlockTags.WALLS).transform(FeatureToggle.register())).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE)).tag(CCTags.Items.COPYCAT_WALL.tag).transform(ModelGen.customItemModel("copycat_base", "wall")).register();
    public static final BlockEntry<CopycatSlopeBlock> COPYCAT_SLOPE = ((BlockBuilder)REGISTRATE.block("copycat_slope", CopycatSlopeBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.SLOPES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE)).transform(ModelGen.customItemModel("copycat_base", "slope")).register();
    public static final BlockEntry<CopycatVerticalSlopeBlock> COPYCAT_VERTICAL_SLOPE = ((BlockBuilder)REGISTRATE.block("copycat_vertical_slope", CopycatVerticalSlopeBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.SLOPES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE)).transform(ModelGen.customItemModel("copycat_base", "vertical_slope")).register();
    public static final BlockEntry<CopycatSlopeLayerBlock> COPYCAT_SLOPE_LAYER = ((BlockBuilder)REGISTRATE.block("copycat_slope_layer", CopycatSlopeLayerBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.SLOPES, FeatureCategory.STACKABLES))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.STACKABLE)).transform(ModelGen.customItemModel("copycat_base", "slope_layer")).register();
    public static final BlockEntry<CopycatShaftBlock> COPYCAT_SHAFT = ((BlockBuilder)REGISTRATE.block("copycat_shaft", CopycatShaftBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL, FeatureCategory.CREATE))).transform(CStress.setNoImpact()).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "shaft")).register();
    public static final BlockEntry<CopycatCogWheelBlock> COPYCAT_COGWHEEL = ((BlockBuilder)REGISTRATE.block("copycat_cogwheel", CopycatCogWheelBlock::small).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.MULTISTATES, FeatureCategory.FUNCTIONAL, FeatureCategory.CREATE))).transform(CStress.setNoImpact()).item(CogwheelBlockItem::new).onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.MULTI_STATE, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "cogwheel")).register();
    public static final BlockEntry<CopycatCogWheelBlock> COPYCAT_LARGE_COGWHEEL = ((BlockBuilder)REGISTRATE.block("copycat_large_cogwheel", CopycatCogWheelBlock::large).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.MULTISTATES, FeatureCategory.FUNCTIONAL, FeatureCategory.CREATE))).transform(CStress.setNoImpact()).item(CogwheelBlockItem::new).onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.MULTI_STATE, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "large_cogwheel")).register();
    public static final BlockEntry<CopycatFluidPipeBlock> COPYCAT_FLUID_PIPE = ((BlockBuilder)REGISTRATE.block("copycat_fluid_pipe", CopycatFluidPipeBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL, FeatureCategory.CREATE))).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "fluid_pipe")).register();
    public static final BlockEntry<CopycatGlassFluidPipeBlock> COPYCAT_GLASS_FLUID_PIPE = REGISTRATE.block("copycat_glass_fluid_pipe", CopycatGlassFluidPipeBlock::new).transform(CCBuilderTransformers.copycat()).register();
    public static final BlockEntry<CopycatDoorBlock> COPYCAT_DOOR = ((BlockBuilder)REGISTRATE.block("copycat_door", p -> new CopycatDoorBlock((BlockBehaviour.Properties)p, BlockSetType.OAK)).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))).onRegister(block -> AllInteractionBehaviours.register((MovingInteractionBehaviour)new DoorMovingInteraction(), (Block[])new Block[]{block})).onRegister(b -> BlockMovementChecks.registerBrittleCheck(state -> state.getBlock() == b ? BlockMovementChecks.CheckResult.SUCCESS : BlockMovementChecks.CheckResult.PASS)).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.FUNCTIONAL)).tag(ItemTags.DOORS).tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag).transform(ModelGen.customItemModel("copycat_base", "door")).register();
    public static final BlockEntry<CopycatDoorBlock> COPYCAT_IRON_DOOR = ((BlockBuilder)REGISTRATE.block("copycat_iron_door", p -> new CopycatDoorBlock((BlockBehaviour.Properties)p, BlockSetType.IRON)).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))).onRegister(block -> AllInteractionBehaviours.register((MovingInteractionBehaviour)new DoorMovingInteraction(), (Block[])new Block[]{block})).onRegister(b -> BlockMovementChecks.registerBrittleCheck(state -> state.getBlock() == b ? BlockMovementChecks.CheckResult.SUCCESS : BlockMovementChecks.CheckResult.PASS)).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.FUNCTIONAL)).transform(ModelGen.customItemModel("copycat_base", "door")).register();
    public static final BlockEntry<CopycatPaneBlock> COPYCAT_PANE = ((BlockBuilder)REGISTRATE.block("copycat_pane", CopycatPaneBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register())).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE)).transform(ModelGen.customItemModel("copycat_base", "pane")).register();
    public static final BlockEntry<CopycatSlidingDoorBlock> COPYCAT_SLIDING_DOOR = ((BlockBuilder)REGISTRATE.block("copycat_sliding_door", p -> CopycatSlidingDoorBlock.metal(p, false)).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))).onRegister(block -> AllInteractionBehaviours.register((MovingInteractionBehaviour)new DoorMovingInteraction(), (Block[])new Block[]{block})).onRegister(block -> AllMovementBehaviours.register((MovementBehaviour)new SlidingDoorMovementBehaviour(), (Block[])new Block[]{block})).tag(BlockTags.DOORS).tag(BlockTags.WOODEN_DOORS).tag(AllTags.AllBlockTags.NON_DOUBLE_DOOR.tag).onRegister(b -> BlockMovementChecks.registerBrittleCheck(state -> state.getBlock() == b ? BlockMovementChecks.CheckResult.SUCCESS : BlockMovementChecks.CheckResult.PASS)).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.FUNCTIONAL)).tag(ItemTags.DOORS).tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag).transform(ModelGen.customItemModel("copycat_base", "sliding_door")).register();
    public static final BlockEntry<CopycatSlidingDoorBlock> COPYCAT_FOLDING_DOOR = ((BlockBuilder)REGISTRATE.block("copycat_folding_door", p -> CopycatSlidingDoorBlock.metal(p, true)).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))).onRegister(block -> AllInteractionBehaviours.register((MovingInteractionBehaviour)new DoorMovingInteraction(), (Block[])new Block[]{block})).onRegister(block -> AllMovementBehaviours.register((MovementBehaviour)new SlidingDoorMovementBehaviour(), (Block[])new Block[]{block})).tag(BlockTags.DOORS).tag(BlockTags.WOODEN_DOORS).tag(AllTags.AllBlockTags.NON_DOUBLE_DOOR.tag).onRegister(b -> BlockMovementChecks.registerBrittleCheck(state -> state.getBlock() == b ? BlockMovementChecks.CheckResult.SUCCESS : BlockMovementChecks.CheckResult.PASS)).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.FUNCTIONAL)).tag(ItemTags.DOORS).tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag).transform(ModelGen.customItemModel("copycat_base", "folding_door")).register();
    public static final BlockEntry<CopycatFlatPaneBlock> COPYCAT_FLAT_PANE = ((BlockBuilder)REGISTRATE.block("copycat_flat_pane", CopycatFlatPaneBlock::new).transform(CCBuilderTransformers.copycat()).transform(FeatureToggle.register())).item().onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE)).transform(ModelGen.customItemModel("copycat_base", "flat_pane")).register();

    public static void getWrappedBlockState(DataGenContext<Block, ? extends Block> c, RegistrateBlockstateProvider p, String name) {
    }

    public static <T> NonNullConsumer<? super T> onClient(Supplier<NonNullConsumer<? super T>> supplier) {
        return Platform.Environment.CLIENT.getIfCurrent(supplier, b -> {});
    }

    public static void register() {
    }

    public static Set<RegistryEntry<Block>> getAllRegisteredBlocks() {
        return new HashSet<RegistryEntry<Block>>(REGISTRATE.getAll(BuiltInRegistries.BLOCK.key()));
    }

    public static Set<RegistryEntry<Block>> getAllRegisteredBlocksWithoutWrapped() {
        return new HashSet(REGISTRATE.getAll(BuiltInRegistries.BLOCK.key())).stream().filter(entry -> !entry.getId().getPath().startsWith("wrapped")).collect(Collectors.toSet());
    }

    public static Set<RegistryEntry<Block>> getAllRegisteredMultiStateBlocks() {
        return new HashSet(REGISTRATE.getAll(BuiltInRegistries.BLOCK.key())).stream().filter(entry -> entry.get() instanceof IMultiStateCopycatBlock).collect(Collectors.toSet());
    }
}

