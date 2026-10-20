/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.Block
 */
package com.copycatsplus.copycats.foundation.copycat.model;

import com.copycatsplus.copycats.content.copycat.beam.CopycatBeamModelCore;
import com.copycatsplus.copycats.content.copycat.block.CopycatBlockModelCore;
import com.copycatsplus.copycats.content.copycat.board.CopycatMultiBoardModelCore;
import com.copycatsplus.copycats.content.copycat.button.CopycatButtonModelCore;
import com.copycatsplus.copycats.content.copycat.byte_panel.CopycatMultiBytePanelModelCore;
import com.copycatsplus.copycats.content.copycat.bytes.CopycatMultiByteModelCore;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelModelCore;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatLargeCogWheelModelCore;
import com.copycatsplus.copycats.content.copycat.corner_slice.CopycatCornerSliceModelCore;
import com.copycatsplus.copycats.content.copycat.door.CopycatDoorModelCore;
import com.copycatsplus.copycats.content.copycat.fence.CopycatFenceModelCore;
import com.copycatsplus.copycats.content.copycat.fence_gate.CopycatFenceGateModelCore;
import com.copycatsplus.copycats.content.copycat.flat_pane.CopycatFlatPaneModelCore;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModelCore;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatStraightPipeModelCore;
import com.copycatsplus.copycats.content.copycat.ghost_block.CopycatGhostBlockModelCore;
import com.copycatsplus.copycats.content.copycat.half_layer.CopycatMultiHalfLayerModelCore;
import com.copycatsplus.copycats.content.copycat.half_panel.CopycatHalfPanelModelCore;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderModelCore;
import com.copycatsplus.copycats.content.copycat.layer.CopycatLayerModelCore;
import com.copycatsplus.copycats.content.copycat.pane.CopycatPaneModelCore;
import com.copycatsplus.copycats.content.copycat.pressure_plate.CopycatPressurePlateModelCore;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftModelCore;
import com.copycatsplus.copycats.content.copycat.slab.CopycatMultiSlabModelCore;
import com.copycatsplus.copycats.content.copycat.slice.CopycatSliceModelCore;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatFoldingDoorModelCore;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorModelCore;
import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeModelCore;
import com.copycatsplus.copycats.content.copycat.slope_layer.CopycatSlopeLayerModelCore;
import com.copycatsplus.copycats.content.copycat.stacked_half_layer.CopycatStackedMultiHalfLayerModelCore;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsModelCore;
import com.copycatsplus.copycats.content.copycat.trapdoor.CopycatTrapdoorModelCore;
import com.copycatsplus.copycats.content.copycat.vertical_half_layer.CopycatVerticalMultiHalfLayerModelCore;
import com.copycatsplus.copycats.content.copycat.vertical_slice.CopycatVerticalSliceModelCore;
import com.copycatsplus.copycats.content.copycat.vertical_slope.CopycatVerticalSlopeModelCore;
import com.copycatsplus.copycats.content.copycat.vertical_stairs.CopycatVerticalStairsModelCore;
import com.copycatsplus.copycats.content.copycat.vertical_step.CopycatVerticalStepModelCore;
import com.copycatsplus.copycats.content.copycat.wall.CopycatWallModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import net.minecraft.world.level.block.Block;

public final class CopycatModelCores {
    private CopycatModelCores() {
    }

    public static CopycatModelCore create(Block block) {
        return switch (block.builtInRegistryHolder().key().identifier().getPath()) {
            case "copycat_block" -> new CopycatBlockModelCore();
            case "copycat_beam" -> new CopycatBeamModelCore();
            case "copycat_board" -> new CopycatMultiBoardModelCore();
            case "copycat_wooden_button", "copycat_stone_button" -> new CopycatButtonModelCore();
            case "copycat_byte" -> new CopycatMultiByteModelCore();
            case "copycat_byte_panel" -> new CopycatMultiBytePanelModelCore();
            case "copycat_fence" -> new CopycatFenceModelCore();
            case "copycat_fence_gate" -> new CopycatFenceGateModelCore();
            case "copycat_ghost_block" -> new CopycatGhostBlockModelCore();
            case "copycat_half_layer" -> new CopycatMultiHalfLayerModelCore();
            case "copycat_vertical_half_layer" -> new CopycatVerticalMultiHalfLayerModelCore();
            case "copycat_stacked_half_layer" -> new CopycatStackedMultiHalfLayerModelCore();
            case "copycat_half_panel" -> new CopycatHalfPanelModelCore();
            case "copycat_ladder" -> new CopycatLadderModelCore();
            case "copycat_layer" -> new CopycatLayerModelCore();
            case "copycat_wooden_pressure_plate", "copycat_stone_pressure_plate", "copycat_light_weighted_pressure_plate", "copycat_heavy_weighted_pressure_plate" -> new CopycatPressurePlateModelCore();
            case "copycat_slab" -> new CopycatMultiSlabModelCore();
            case "copycat_slice", "copycat_corner_slice" -> {
                if (block.builtInRegistryHolder().key().identifier().getPath().equals("copycat_corner_slice")) {
                    yield new CopycatCornerSliceModelCore();
                }
                yield new CopycatSliceModelCore();
            }
            case "copycat_stairs" -> new CopycatStairsModelCore();
            case "copycat_vertical_stairs" -> new CopycatVerticalStairsModelCore();
            case "copycat_trapdoor", "copycat_iron_trapdoor" -> new CopycatTrapdoorModelCore();
            case "copycat_vertical_slice" -> new CopycatVerticalSliceModelCore();
            case "copycat_vertical_step" -> new CopycatVerticalStepModelCore();
            case "copycat_wall" -> new CopycatWallModelCore();
            case "copycat_slope" -> new CopycatSlopeModelCore();
            case "copycat_vertical_slope" -> new CopycatVerticalSlopeModelCore();
            case "copycat_slope_layer" -> new CopycatSlopeLayerModelCore();
            case "copycat_door", "copycat_iron_door" -> new CopycatDoorModelCore();
            case "copycat_pane" -> new CopycatPaneModelCore();
            case "copycat_flat_pane" -> new CopycatFlatPaneModelCore();
            case "copycat_sliding_door" -> new CopycatSlidingDoorModelCore(false);
            case "copycat_folding_door" -> new CopycatFoldingDoorModelCore(false, false);
            case "copycat_fluid_pipe" -> new CopycatFluidPipeModelCore();
            case "copycat_glass_fluid_pipe" -> new CopycatStraightPipeModelCore();
            case "copycat_shaft" -> new CopycatShaftModelCore();
            case "copycat_cogwheel" -> CopycatModelCore.kinetic(new CopycatShaftModelCore(), new CopycatCogWheelModelCore());
            case "copycat_large_cogwheel" -> CopycatModelCore.kinetic(new CopycatShaftModelCore(), new CopycatLargeCogWheelModelCore());
            default -> null;
        };
    }
}

