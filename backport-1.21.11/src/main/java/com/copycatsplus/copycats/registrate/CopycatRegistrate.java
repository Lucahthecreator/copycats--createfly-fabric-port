/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.client.flywheel.api.visual.BlockEntityVisual
 *  com.zurrtum.create.client.flywheel.api.visualization.VisualizationContext
 *  net.minecraft.world.level.block.entity.BlockEntity
 */
package com.copycatsplus.copycats.registrate;

import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.zurrtum.create.client.flywheel.api.visual.BlockEntityVisual;
import com.zurrtum.create.client.flywheel.api.visualization.VisualizationContext;
import com.zurrtum.create.foundation.data.CreateBlockEntityBuilder;
import com.zurrtum.create.foundation.data.CreateRegistrate;
import java.util.function.Predicate;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CopycatRegistrate
extends CreateRegistrate {
    private static CopycatRegistrate instance;

    protected CopycatRegistrate(String modid) {
        super(modid);
        instance = this;
    }

    public static CopycatRegistrate create(String modid) {
        return new CopycatRegistrate(modid);
    }

    public <T extends BlockEntity> CopycatBlockEntityBuilder<T, CopycatRegistrate> copycatBlockEntity(String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return this.copycatBlockEntity(this, name, factory);
    }

    public <T extends BlockEntity, P> CopycatBlockEntityBuilder<T, P> copycatBlockEntity(P parent, String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return new CopycatBlockEntityBuilder<T, P>(this, parent, name, factory);
    }

    public static <Tab> CreateRegistrate setTab(Tab tab) {
        return instance;
    }

    public static CopycatRegistrate getInstance() {
        return instance;
    }

    public static class CopycatBlockEntityBuilder<T extends BlockEntity, P>
    extends CreateBlockEntityBuilder<T, P> {
        public CopycatBlockEntityBuilder(CopycatRegistrate owner, P parent, String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
            super(owner, parent, name, factory);
        }

        public CopycatBlockEntityBuilder<T, P> copycatVisual(NonNullSupplier<CopycatVisualFactory<T>> factory) {
            this.copycatVisual(factory, true);
            return this;
        }

        public CopycatBlockEntityBuilder<T, P> copycatVisual(NonNullSupplier<CopycatVisualFactory<T>> factory, boolean renderNormally) {
            this.copycatVisual(factory, be -> true);
            return this;
        }

        public CopycatBlockEntityBuilder<T, P> copycatVisual(NonNullSupplier<CopycatVisualFactory<T>> factory, Predicate<T> renderNormally) {
            CopycatBlockEntityBuilder.registerVisual(this, factory, renderNormally);
            return this;
        }

        public static <T extends BlockEntity, P> void registerVisual(CreateBlockEntityBuilder<T, P> builder, NonNullSupplier<CopycatVisualFactory<T>> factory, Predicate<T> renderNormally) {
        }

        @FunctionalInterface
        public static interface CopycatVisualFactory<T extends BlockEntity> {
            public BlockEntityVisual<? super T> create(VisualizationContext var1, T var2, float var3);
        }
    }
}

