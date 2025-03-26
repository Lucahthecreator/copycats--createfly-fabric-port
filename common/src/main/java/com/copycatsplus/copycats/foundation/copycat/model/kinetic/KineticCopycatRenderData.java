package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.utility.ChatUtils;
import dev.engine_room.flywheel.api.backend.BackendManager;
import dev.engine_room.flywheel.backend.Backends;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Contains data required to cache a rendered kinetic copycat model.
 *
 * @param partialModel The partial model to render.
 * @param state        The state of the partial model. Should only contain states required by the partial model.
 * @param material     The material of the copycat.
 */
public record KineticCopycatRenderData(ICopycatPartialModel partialModel, PartialModelState state,
                                       BlockState material) {
    /**
     * Create a new render data object from the given partial model rendered by the given block entity.
     *
     * @param partialModel The partial model to render.
     * @param be           The block entity that is rendering the model.
     */
    public static KineticCopycatRenderData of(ICopycatPartialModel partialModel, ICopycatBlockEntity be) {
        BlockState material = be.getMaterial();
        return of(partialModel, be, material);
    }

    /**
     * Create a new render data object from the given partial model rendered by the given multi-state block entity.
     * <p>
     * The partial model should only represent one part of the multi-state block entity, such that one material state
     * is enough to render the model.
     *
     * @param partialModel The partial model to render.
     * @param be           The block entity that is rendering the model.
     * @param property     The property of the block entity that determines the material state.
     */
    public static KineticCopycatRenderData of(ICopycatPartialModel partialModel, IMultiStateCopycatBlockEntity be, String property) {
        BlockState material = be.getMaterialItemStorage().getMaterialItem(property).material();
        return of(partialModel, be, material);
    }

    private static KineticCopycatRenderData of(ICopycatPartialModel partialModel, ICopycatBlockEntity be, BlockState material) {
        return new KineticCopycatRenderData(partialModel, PartialModelState.fromBlockState(be.getBlockState(), partialModel.getProperties()), material);
    }
}
