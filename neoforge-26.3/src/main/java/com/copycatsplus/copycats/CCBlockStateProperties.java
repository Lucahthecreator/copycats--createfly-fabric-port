package com.copycatsplus.copycats;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class CCBlockStateProperties {
    public static final EnumProperty<CCBlockStateProperties.VerticalStairShape> VERTICAL_STAIR_SHAPE = EnumProperty.create(
        "vertical_stair_shape", CCBlockStateProperties.VerticalStairShape.class
    );
    public static final EnumProperty<CCBlockStateProperties.Side> SIDE = EnumProperty.create("side", CCBlockStateProperties.Side.class);
    public static final IntegerProperty BASE_TYPE = IntegerProperty.create("base_type", 0, 2);

    public static enum Side implements StringRepresentable {
        LEFT,
        RIGHT;

        public boolean isRight() {
            return this == RIGHT;
        }

        public CCBlockStateProperties.Side getOpposite() {
            return this == LEFT ? RIGHT : LEFT;
        }

        @NotNull
        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }

    public static enum VerticalStairShape implements StringRepresentable {
        STRAIGHT,
        OUTER_TOP,
        OUTER_BOTTOM,
        INNER_TOP,
        INNER_BOTTOM;

        @NotNull
        @Override
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
}
