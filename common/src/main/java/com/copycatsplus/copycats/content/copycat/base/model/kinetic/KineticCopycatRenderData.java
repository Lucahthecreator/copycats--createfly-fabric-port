package com.copycatsplus.copycats.content.copycat.base.model.kinetic;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.content.copycat.base.ICopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.partial.CopycatPartialModel;
import com.copycatsplus.copycats.utility.ChatUtils;
import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.config.BackendType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Contains data required to cache a rendered kinetic copycat model.
 *
 * @param partialModel The partial model to render.
 * @param state        The state of the partial model. Should only contain states required by the partial model.
 * @param material     The material of the copycat.
 */
public record KineticCopycatRenderData(CopycatPartialModel partialModel, PartialModelState state, BlockState material) {
    /**
     * Create a new render data object from the given partial model rendered by the given block entity.
     *
     * @param partialModel The partial model to render.
     * @param be           The block entity that is rendering the model.
     */
    public static KineticCopycatRenderData of(CopycatPartialModel partialModel, ICopycatBlockEntity be) {
        BlockState material = be instanceof IMultiStateCopycatBlockEntity multiState
                ? multiState.getMaterialItemStorage().getMaterialItem(partialModel.getKey()).material()
                : be.getMaterial();
        if (!CCConfigs.client().disableGraphicsWarnings.get()) {
            if (Backend.getBackendType() != BackendType.INSTANCING &&
                    Minecraft.getInstance().getBlockColors().getColor(material, null, null, 0) != -1) {
                ChatUtils.sendWarningOnce(
                        "flywheel_block_color",
                        "Block colors may be incorrect due to the current Flywheel rendering backend. Please switch to the instancing backend to fix this."
                );
            }
        }
        return new KineticCopycatRenderData(partialModel, PartialModelState.fromBlockState(be.getBlockState(), partialModel.getProperties()), material);
    }

    /**
     * Create a new render data object from the given partial model rendered by the given multi-state block entity.
     * <p>
     * Note that each part of a kinetic multi-state copycat must be rendered separately. Multi-state caching is not supported
     * for kinetic copycats.
     *
     * @param partialModel The partial model to render.
     * @param be           The block entity that is rendering the model.
     * @param property     The multi-state part property to use.
     */
    public static KineticCopycatRenderData of(CopycatPartialModel partialModel, IMultiStateCopycatBlockEntity be, String property) {
        if (!CCConfigs.client().disableGraphicsWarnings.get()) {
            if (Backend.getBackendType() != BackendType.INSTANCING &&
                    Minecraft.getInstance().getBlockColors().getColor(be.getMaterial(), null, null, 0) != -1) {
                ChatUtils.sendWarningOnce(
                        "flywheel_block_color",
                        "Block colors may be incorrect due to the current Flywheel rendering backend. Please switch to the instancing backend to fix this."
                );
            }
        }
        return new KineticCopycatRenderData(partialModel, PartialModelState.fromBlockState(be.getBlockState(), partialModel.getProperties()), be.getMaterialItemStorage().getMaterialItem(property).material());
    }
}
