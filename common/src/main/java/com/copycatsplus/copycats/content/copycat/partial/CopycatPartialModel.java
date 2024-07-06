package com.copycatsplus.copycats.content.copycat.partial;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import static com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore.MATERIAL_KEY;

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
    SHAFT("shaft", new CopycatShaftModelCore(), BlockStateProperties.AXIS),
    COGWHEEL("cogwheel", new CopycatCogWheelModelCore(), BlockStateProperties.AXIS);

    /**
     * Creates a new partial model with the given core and block state properties.
     * <p>
     * To ensure correct caching, the model core must assemble the model using only information listed in the blockStateProperties array.
     * It must also render with only the single material block state recorded by {@link com.copycatsplus.copycats.content.copycat.base.model.kinetic.KineticCopycatRenderData}.
     * In other words, multi-state rendering is not allowed in a single copycat partial model, but it is possible to
     * render multiple partial models with different materials in a single multi-state copycat.
     * <p>
     * Note that copycat partial models have no block state files, so a SUPER model entry in the {@link CopycatModelCore} will be empty.
     *
     * @param key                  If the model is rendered as part of a multi-state copycat, the key that determines the material.
     * @param core                 The core of the model.
     * @param blockStateProperties The block state properties used to assemble the model.
     */
    CopycatPartialModel(String key, CopycatModelCore core, Property<?>... blockStateProperties) {
        this.key = key;
        this.model = modelOf(core, key);
        this.properties = blockStateProperties;
    }

    private final String key;
    private final BakedModel model;
    private final Property<?>[] properties;

    public String getKey() {
        return key;
    }

    public BakedModel getModel() {
        return model;
    }

    public Property<?>[] getProperties() {
        return properties;
    }

    private static BakedModel modelOf(CopycatModelCore core, String property) {
        return CopycatModelCore.createKineticModel(
                Minecraft
                        .getInstance()
                        .getBlockRenderer()
                        .getBlockModel(Blocks.AIR.defaultBlockState()),
                core,
                s -> s.equals(property) ? MATERIAL_KEY : s
        );
    }
}
