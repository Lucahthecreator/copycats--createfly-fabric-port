package com.copycatsplus.copycats;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction.Axis;

import java.util.EnumMap;
import java.util.Map;

public class CCPartialModels {

    public static final Map<Axis, PartialModel> GLASS_PIPES =
            new EnumMap<>(Axis.class);

    static {
        for (Axis axis : Iterate.axes) {
            GLASS_PIPES.put(axis, block("fluid_pipe/window_" + axis.getSerializedName()));
        }
    }

    public static void init() {
        // init static fields
    }

    private static PartialModel block(String path) {
        return new PartialModel(Copycats.asResource("block/" + path));
    }
}
