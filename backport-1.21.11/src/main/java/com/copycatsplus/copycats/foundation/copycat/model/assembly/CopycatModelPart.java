/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface CopycatModelPart {
    public void emitCopycatQuads(String var1, BlockState var2, CopycatRenderContext var3, BlockState var4);
}

