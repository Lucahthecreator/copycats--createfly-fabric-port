package com.copycatsplus.copycats.registrate;

import com.copycatsplus.copycats.internal.create.foundation.data.CreateBlockEntityBuilder;
import com.copycatsplus.copycats.internal.create.foundation.data.CreateRegistrate;
import com.copycatsplus.copycats.internal.registrate.builders.BlockEntityBuilder;
import com.copycatsplus.copycats.internal.registrate.util.nullness.NonNullSupplier;
import com.zurrtum.create.client.flywheel.api.visual.BlockEntityVisual;
import com.zurrtum.create.client.flywheel.api.visualization.VisualizationContext;
import java.util.function.Predicate;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CopycatRegistrate extends CreateRegistrate {
    private static CopycatRegistrate instance;

    protected CopycatRegistrate(String modid) {
        super(modid);
        instance = this;
    }

    public static CopycatRegistrate create(String modid) {
        return new CopycatRegistrate(modid);
    }

    public <T extends BlockEntity> CopycatRegistrate.CopycatBlockEntityBuilder<T, CopycatRegistrate> copycatBlockEntity(
        String name, BlockEntityBuilder.BlockEntityFactory<T> factory
    ) {
        return this.copycatBlockEntity(this, name, factory);
    }

    public <T extends BlockEntity, P> CopycatRegistrate.CopycatBlockEntityBuilder<T, P> copycatBlockEntity(
        P parent, String name, BlockEntityBuilder.BlockEntityFactory<T> factory
    ) {
        return new CopycatRegistrate.CopycatBlockEntityBuilder<>(this, parent, name, factory);
    }

    public static <Tab> CreateRegistrate setTab(Tab tab) {
        return instance;
    }

    public static CopycatRegistrate getInstance() {
        return instance;
    }

    public static class CopycatBlockEntityBuilder<T extends BlockEntity, P> extends CreateBlockEntityBuilder<T, P> {
        public CopycatBlockEntityBuilder(CopycatRegistrate owner, P parent, String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
            super(owner, parent, name, factory);
        }

        public CopycatRegistrate.CopycatBlockEntityBuilder<T, P> copycatVisual(
            NonNullSupplier<CopycatRegistrate.CopycatBlockEntityBuilder.CopycatVisualFactory<T>> factory
        ) {
            this.copycatVisual(factory, true);
            return this;
        }

        public CopycatRegistrate.CopycatBlockEntityBuilder<T, P> copycatVisual(
            NonNullSupplier<CopycatRegistrate.CopycatBlockEntityBuilder.CopycatVisualFactory<T>> factory, boolean renderNormally
        ) {
            this.copycatVisual(factory, be -> true);
            return this;
        }

        public CopycatRegistrate.CopycatBlockEntityBuilder<T, P> copycatVisual(
            NonNullSupplier<CopycatRegistrate.CopycatBlockEntityBuilder.CopycatVisualFactory<T>> factory, Predicate<T> renderNormally
        ) {
            registerVisual(this, factory, renderNormally);
            return this;
        }

        public static <T extends BlockEntity, P> void registerVisual(
            CreateBlockEntityBuilder<T, P> builder,
            NonNullSupplier<CopycatRegistrate.CopycatBlockEntityBuilder.CopycatVisualFactory<T>> factory,
            Predicate<T> renderNormally
        ) {
        }

        @FunctionalInterface
        public interface CopycatVisualFactory<T extends BlockEntity> {
            BlockEntityVisual<? super T> create(VisualizationContext var1, T var2, float var3);
        }
    }
}
