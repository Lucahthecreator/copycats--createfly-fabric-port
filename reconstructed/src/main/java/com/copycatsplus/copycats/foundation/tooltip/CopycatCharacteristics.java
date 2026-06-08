/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.StringRepresentable
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.foundation.tooltip;

import com.copycatsplus.copycats.CCKeys;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public final class CopycatCharacteristics
extends Enum<CopycatCharacteristics>
implements StringRepresentable {
    public static final /* enum */ CopycatCharacteristics COPYCAT = new CopycatCharacteristics("Copycat", "R-click with material to _assign_ and _rotate_. Wrench to _remove material_.", new Supplier[0]);
    public static final /* enum */ CopycatCharacteristics CT_TOGGLE = new CopycatCharacteristics("CT Toggle", "Shift-R-click with empty hand to _toggle connected textures_.", new Supplier[0]);
    public static final /* enum */ CopycatCharacteristics MULTI_STATE;
    public static final /* enum */ CopycatCharacteristics STACKABLE;
    public static final /* enum */ CopycatCharacteristics FUNCTIONAL;
    public static final /* enum */ CopycatCharacteristics GHOST;
    public static final /* enum */ CopycatCharacteristics PRE_ASSEMBLED;
    public static final /* enum */ CopycatCharacteristics COPY_CAT;
    private final String title;
    private final String description;
    private final Supplier<Object>[] args;
    private static final /* synthetic */ CopycatCharacteristics[] $VALUES;

    public static CopycatCharacteristics[] values() {
        return (CopycatCharacteristics[])$VALUES.clone();
    }

    public static CopycatCharacteristics valueOf(String name) {
        return Enum.valueOf(CopycatCharacteristics.class, name);
    }

    @SafeVarargs
    private CopycatCharacteristics(String title, String description, Supplier<Object> ... args) {
        this.title = title;
        this.description = description;
        this.args = args;
    }

    @NotNull
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
        return List.of(CopycatCharacteristics.values());
    }

    private static /* synthetic */ CopycatCharacteristics[] $values() {
        return new CopycatCharacteristics[]{COPYCAT, CT_TOGGLE, MULTI_STATE, STACKABLE, FUNCTIONAL, GHOST, PRE_ASSEMBLED, COPY_CAT};
    }

    static {
        Supplier[] supplierArray = new Supplier[1];
        supplierArray[0] = CCKeys.FILL_COPYCAT::getBoundKey;
        MULTI_STATE = new CopycatCharacteristics("Multi-state", "Put _multiple copies_ with different materials in the same block space. Hold _%s_ to fill all parts.", supplierArray);
        STACKABLE = new CopycatCharacteristics("Stackable", "R-click with the same copycat to _enlarge_.", new Supplier[0]);
        FUNCTIONAL = new CopycatCharacteristics("Functional", "_Same usage_ as non-copycat counterparts.", new Supplier[0]);
        GHOST = new CopycatCharacteristics("Ghost", "_No collision_ with entities.", new Supplier[0]);
        PRE_ASSEMBLED = new CopycatCharacteristics("Pre-assembled", "Disassemble _individual parts_ after placement.", new Supplier[0]);
        COPY_CAT = new CopycatCharacteristics("???", "R-click on a _Cat_.", new Supplier[0]);
        $VALUES = CopycatCharacteristics.$values();
    }
}

