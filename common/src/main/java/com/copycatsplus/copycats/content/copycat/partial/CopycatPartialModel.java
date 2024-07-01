package com.copycatsplus.copycats.content.copycat.partial;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

public enum CopycatPartialModel {
    SHAFT(new CopycatShaftModelPart(), BlockStateProperties.AXIS);

    private final BakedModel model;
    private final Property<?>[] properties;

    CopycatPartialModel(CopycatModelCore core, Property<?>... properties) {
        this.model = modelOf(core);
        this.properties = properties;
    }

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
