/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.client.foundation.item.TooltipModifier
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.utility;

import com.zurrtum.create.client.foundation.item.TooltipModifier;
import org.jetbrains.annotations.NotNull;

public class TooltipUtils {
    @NotNull
    public static TooltipModifier sequential(TooltipModifier ... modifiers) {
        return (tooltips, player) -> {
            for (TooltipModifier modifier : modifiers) {
                modifier.modify(tooltips, player);
            }
        };
    }
}

