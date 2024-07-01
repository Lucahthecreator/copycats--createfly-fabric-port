package com.copycatsplus.copycats.content.copycat.partial;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * An enum containing all partial models which are assembled from models of copied materials using a {@link CopycatModelCore}.
 * These models are intended to be reused across different copycats, thus their implementation must not be specific to a single block.
 * <p>
 * Multi-state copycats should use a separate partial model for each part for efficient caching. For example, a Copycat
 * Cogwheel should use separate partial models for the shaftless cogwheel and the shaft, so that the shaft model can be reused by
 * Copycat Shafts.
 * <p>
 * Use {@link com.jozufozu.flywheel.core.PartialModel} instead if dynamic assembly is not required.
 */
public enum CopycatPartialModel {
    SHAFT(new CopycatShaftModelPart(), BlockStateProperties.AXIS);

    /**
     * Creates a new partial model with the given core and properties.
     * <p>
     * The model core must assemble the model using only properties listed in the properties array to ensure correct caching.
     * <p>
     * Note that copycat partial models have no block state files, so a SUPER model entry in the {@link CopycatModelCore} will be empty.
     *
     * @param core       The core of the model.
     * @param properties The properties used to assemble the model.
     */
    CopycatPartialModel(CopycatModelCore core, Property<?>... properties) {
        this.model = modelOf(core);
        this.properties = properties;
    }

    private final BakedModel model;
    private final Property<?>[] properties;

    public BakedModel getModel() {
        return model;
    }

    public Property<?>[] getProperties() {
        return properties;
    }

    private static BakedModel modelOf(CopycatModelCore core) {
        return CopycatModelCore.createModelWithoutAO(
                Minecraft
                        .getInstance()
                        .getBlockRenderer()
                        .getBlockModel(Blocks.AIR.defaultBlockState()),
                core
        );
    }
}
