package com.copycatsplus.copycats.utility;

import com.simibubi.create.content.contraptions.StructureTransform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockUtils {
    public static BlockState tryCopyProperties(BlockState from, BlockState to) {
        for (Property<?> property : from.getProperties()) {
            to = tryCopyProperty(from, to, property);
        }
        return to;
    }

    public static <T extends Comparable<T>> BlockState tryCopyProperty(BlockState from, BlockState to, Property<T> property) {
        if (from.hasProperty(property) && to.hasProperty(property)) {
            return to.setValue(property, from.getValue(property));
        }
        return to;
    }

    public static Direction transformFacing(StructureTransform transform, Direction facing) {
        if (transform.mirror != null)
            facing = transform.mirrorFacing(facing);
        if (transform.rotationAxis != null)
            facing = transform.rotateFacing(facing);
        return facing;
    }
}
