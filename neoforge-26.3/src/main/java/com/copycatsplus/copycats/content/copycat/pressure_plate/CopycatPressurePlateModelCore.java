package com.copycatsplus.copycats.content.copycat.pressure_plate;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.copycatsplus.copycats.utility.BlockUtils;
import java.util.List;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatPressurePlateModelCore extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(
            new CopycatModelCore.ModelEntry(
                "material", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatPressurePlateModelCore::prepareMaterial, CopycatModelCore.EntryType.COPYCAT
            )
        );
    }

    private static BlockState prepareMaterial(BlockState state, BlockState mat) {
        if (mat == null) {
            return null;
        } else if (!(mat.getBlock() instanceof BasePressurePlateBlock)) {
            return mat;
        } else {
            BlockState renderState = BlockUtils.tryCopyProperties(state, mat);
            if (renderState.hasProperty(WeightedPressurePlateBlock.POWER) && state.hasProperty(PressurePlateBlock.POWERED)) {
                renderState = renderState.setValue(WeightedPressurePlateBlock.POWER, state.getValue(PressurePlateBlock.POWERED) ? 15 : 0);
            } else if (renderState.hasProperty(PressurePlateBlock.POWERED) && state.hasProperty(WeightedPressurePlateBlock.POWER)) {
                renderState = renderState.setValue(PressurePlateBlock.POWERED, state.getValue(WeightedPressurePlateBlock.POWER) > 0);
            }

            return renderState;
        }
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof BasePressurePlateBlock) {
            context.assembleAll();
        } else {
            boolean powered = state.getOptionalValue(PressurePlateBlock.POWERED)
                .or(() -> state.getOptionalValue(WeightedPressurePlateBlock.POWER).map(power -> power > 0))
                .orElse(false);
            if (powered) {
                context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    CopycatRenderContext.vec3(1.0, 0.0, 1.0),
                    CopycatRenderContext.aabb(7.0, 0.5, 7.0).move(0.0, 0.0, 0.0),
                    CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
                context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    CopycatRenderContext.vec3(8.0, 0.0, 8.0),
                    CopycatRenderContext.aabb(7.0, 0.5, 7.0).move(9.0, 0.0, 9.0),
                    CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.WEST)
                );
                context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    CopycatRenderContext.vec3(8.0, 0.0, 1.0),
                    CopycatRenderContext.aabb(7.0, 0.5, 7.0).move(9.0, 0.0, 0.0),
                    CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.SOUTH)
                );
                context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    CopycatRenderContext.vec3(1.0, 0.0, 8.0),
                    CopycatRenderContext.aabb(7.0, 0.5, 7.0).move(0.0, 0.0, 9.0),
                    CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST)
                );
            } else {
                context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    CopycatRenderContext.vec3(1.0, 0.0, 1.0),
                    CopycatRenderContext.aabb(7.0, 1.0, 7.0).move(0.0, 0.0, 0.0),
                    CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
                context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    CopycatRenderContext.vec3(8.0, 0.0, 8.0),
                    CopycatRenderContext.aabb(7.0, 1.0, 7.0).move(9.0, 0.0, 9.0),
                    CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.WEST)
                );
                context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    CopycatRenderContext.vec3(8.0, 0.0, 1.0),
                    CopycatRenderContext.aabb(7.0, 1.0, 7.0).move(9.0, 0.0, 0.0),
                    CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.SOUTH)
                );
                context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    CopycatRenderContext.vec3(1.0, 0.0, 8.0),
                    CopycatRenderContext.aabb(7.0, 1.0, 7.0).move(0.0, 0.0, 9.0),
                    CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST)
                );
            }
        }
    }
}
