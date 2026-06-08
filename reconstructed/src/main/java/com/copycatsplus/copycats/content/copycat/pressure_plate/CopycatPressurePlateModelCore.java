/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.BasePressurePlateBlock
 *  net.minecraft.world.level.block.PressurePlateBlock
 *  net.minecraft.world.level.block.WeightedPressurePlateBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 */
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
import net.minecraft.world.level.block.state.properties.Property;

public class CopycatPressurePlateModelCore
extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(new CopycatModelCore.ModelEntry("material", CopycatModelCore.ModelGetter.MATERIAL, this, CopycatPressurePlateModelCore::prepareMaterial, CopycatModelCore.EntryType.COPYCAT));
    }

    private static BlockState prepareMaterial(BlockState state, BlockState mat) {
        if (mat == null) {
            return null;
        }
        if (mat.getBlock() instanceof BasePressurePlateBlock) {
            BlockState renderState = BlockUtils.tryCopyProperties(state, mat);
            if (renderState.hasProperty((Property)WeightedPressurePlateBlock.POWER) && state.hasProperty((Property)PressurePlateBlock.POWERED)) {
                renderState = (BlockState)renderState.setValue((Property)WeightedPressurePlateBlock.POWER, (Comparable)Integer.valueOf((Boolean)state.getValue((Property)PressurePlateBlock.POWERED) != false ? 15 : 0));
            } else if (renderState.hasProperty((Property)PressurePlateBlock.POWERED) && state.hasProperty((Property)WeightedPressurePlateBlock.POWER)) {
                renderState = (BlockState)renderState.setValue((Property)PressurePlateBlock.POWERED, (Comparable)Boolean.valueOf((Integer)state.getValue((Property)WeightedPressurePlateBlock.POWER) > 0));
            }
            return renderState;
        }
        return mat;
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof BasePressurePlateBlock) {
            context.assembleAll();
            return;
        }
        boolean powered = state.getOptionalValue((Property)PressurePlateBlock.POWERED).or(() -> state.getOptionalValue((Property)WeightedPressurePlateBlock.POWER).map(power -> power > 0)).orElse(false);
        if (powered) {
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(1.0, 0.0, 1.0), CopycatRenderContext.aabb(7.0, 0.5, 7.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(8.0, 0.0, 8.0), CopycatRenderContext.aabb(7.0, 0.5, 7.0).move(9.0, 0.0, 9.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.WEST));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(8.0, 0.0, 1.0), CopycatRenderContext.aabb(7.0, 0.5, 7.0).move(9.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.SOUTH));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(1.0, 0.0, 8.0), CopycatRenderContext.aabb(7.0, 0.5, 7.0).move(0.0, 0.0, 9.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST));
        } else {
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(1.0, 0.0, 1.0), CopycatRenderContext.aabb(7.0, 1.0, 7.0).move(0.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(8.0, 0.0, 8.0), CopycatRenderContext.aabb(7.0, 1.0, 7.0).move(9.0, 0.0, 9.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.WEST));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(8.0, 0.0, 1.0), CopycatRenderContext.aabb(7.0, 1.0, 7.0).move(9.0, 0.0, 0.0), CopycatRenderContext.cull(MutableCullFace.WEST | MutableCullFace.SOUTH));
            context.assemblePiece(AssemblyTransform.IDENTITY, CopycatRenderContext.vec3(1.0, 0.0, 8.0), CopycatRenderContext.aabb(7.0, 1.0, 7.0).move(0.0, 0.0, 9.0), CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST));
        }
    }
}

