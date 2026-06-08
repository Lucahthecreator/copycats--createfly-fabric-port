/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.StringRepresentable
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.IntegerProperty
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class CCBlockStateProperties {
    public static final EnumProperty<VerticalStairShape> VERTICAL_STAIR_SHAPE = EnumProperty.create((String)"vertical_stair_shape", VerticalStairShape.class);
    public static final EnumProperty<Side> SIDE = EnumProperty.create((String)"side", Side.class);
    public static final IntegerProperty BASE_TYPE = IntegerProperty.create((String)"base_type", (int)0, (int)2);

    public static enum VerticalStairShape implements StringRepresentable
    {
        STRAIGHT,
        OUTER_TOP,
        OUTER_BOTTOM,
        INNER_TOP,
        INNER_BOTTOM;


        @NotNull
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public boolean isOuter() {
            return this == OUTER_TOP || this == OUTER_BOTTOM;
        }

        public boolean isTop() {
            return this == OUTER_TOP || this == INNER_TOP;
        }
    }

    public static enum Side implements StringRepresentable
    {
        LEFT,
        RIGHT;


        public boolean isRight() {
            return this == RIGHT;
        }

        public Side getOpposite() {
            return this == LEFT ? RIGHT : LEFT;
        }

        @NotNull
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}

