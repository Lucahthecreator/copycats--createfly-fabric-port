package com.copycatsplus.copycats.foundation.tooltip;

import com.copycatsplus.copycats.CCKeys;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum CopycatCharacteristics implements StringRepresentable {
    COPYCAT("Copycat", "R-click with material to _assign_ and _rotate_. Wrench to _remove material_."),
    CT_TOGGLE("CT Toggle", "Shift-R-click with empty hand to _toggle connected textures_."),
    MULTI_STATE(
        "Multi-state", "Put _multiple copies_ with different materials in the same block space. Hold _%s_ to fill all parts.", CCKeys.FILL_COPYCAT::getBoundKey
    ),
    STACKABLE("Stackable", "R-click with the same copycat to _enlarge_."),
    FUNCTIONAL("Functional", "_Same usage_ as non-copycat counterparts."),
    GHOST("Ghost", "_No collision_ with entities."),
    PRE_ASSEMBLED("Pre-assembled", "Disassemble _individual parts_ after placement."),
    COPY_CAT("???", "R-click on a _Cat_.");

    private final String title;
    private final String description;
    private final Supplier<Object>[] args;

    @SafeVarargs
    private CopycatCharacteristics(String title, String description, Supplier<Object>... args) {
        this.title = title;
        this.description = description;
        this.args = args;
    }

    @NotNull
    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String getTitleKey() {
        return "tooltip.copycats.characteristics." + this.getSerializedName() + ".title";
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescriptionKey() {
        return "tooltip.copycats.characteristics." + this.getSerializedName() + ".description";
    }

    public String getDescription() {
        return this.description;
    }

    public Supplier<Object>[] getArgs() {
        return this.args;
    }

    public static List<CopycatCharacteristics> all() {
        return List.of(values());
    }
}
