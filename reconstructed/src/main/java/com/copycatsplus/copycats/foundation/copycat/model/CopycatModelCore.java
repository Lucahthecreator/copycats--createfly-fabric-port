/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.annotation.ParametersAreNonnullByDefault
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.block.dispatch.BlockStateModel
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.foundation.copycat.model;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatModelPart;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockUtils;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class CopycatModelCore
implements CopycatModelPart {
    protected static final ModelEntry SUPER = new ModelEntry("super", null, null, EntryType.STATIC);
    public static final String MATERIAL_KEY = "material";
    protected final ModelEntry MATERIAL = new ModelEntry("material", ModelGetter.MATERIAL, this, EntryType.COPYCAT);
    public boolean enhanced = true;
    public boolean colorize = false;

    public void registerModels(List<ModelEntry> entries) {
        entries.add(this.MATERIAL);
    }

    protected final void registerForMultiState(List<ModelEntry> entries, IMultiStateCopycatBlock block, boolean isKinetic) {
        for (String property : block.storageProperties()) {
            this.registerMultiStatePart(entries, property, isKinetic);
        }
    }

    protected final void registerMultiStatePart(List<ModelEntry> entries, String property, boolean isKinetic) {
        entries.add(new ModelEntry(property, (state, mat) -> CopycatModelCore.getModelOf(mat), this, isKinetic ? EntryType.KINETIC_COPYCAT : EntryType.COPYCAT));
    }

    public void prepareForRender() {
        this.enhanced = true;
        this.colorize = false;
    }

    @Override
    public abstract void emitCopycatQuads(String var1, BlockState var2, CopycatRenderContext var3, BlockState var4);

    public static BlockStateModel getModelOf(BlockState state) {
        return Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
    }

    public static MaterialMapper updatePropertiesIfMatch(Class<?> clazz) {
        return (state, mat) -> {
            if (mat == null) {
                return null;
            }
            if (clazz.isInstance(mat.getBlock())) {
                return BlockUtils.tryCopyProperties(state, mat);
            }
            return mat;
        };
    }

    @NotNull
    public static BlockStateModel createModel(BlockStateModel original, CopycatModelCore core) {
        return original;
    }

    @NotNull
    public static BlockStateModel createKineticModel(BlockStateModel original, CopycatModelCore core) {
        return original;
    }

    public static CopycatModelCore kinetic(final CopycatModelCore ... cores) {
        return new CopycatModelCore(){

            @Override
            public void registerModels(List<ModelEntry> entries) {
                for (CopycatModelCore core : cores) {
                    core.registerModels(entries);
                }
                entries.add(SUPER);
            }

            @Override
            public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
            }
        };
    }

    @ParametersAreNonnullByDefault
    public record ModelEntry(String key, @Nullable ModelGetter model, @Nullable CopycatModelPart part, MaterialMapper materialMapper, EntryType type) {
        public ModelEntry(String key, @Nullable ModelGetter model, @Nullable CopycatModelPart part, EntryType type) {
            this(key, model, part, MaterialMapper.IDENTITY, type);
        }
    }

    @FunctionalInterface
    public static interface ModelGetter {
        public static final ModelGetter MATERIAL = (state, material) -> CopycatModelCore.getModelOf(material);

        public BlockStateModel getModel(BlockState var1, BlockState var2);
    }

    public static enum EntryType {
        STATIC(false, false),
        COPYCAT(true, false),
        KINETIC(false, true),
        KINETIC_COPYCAT(true, true);

        private final boolean useCopycatLogic;
        private final boolean onlyWhenVirtual;

        private EntryType(boolean useCopycatLogic, boolean onlyWhenVirtual) {
            this.useCopycatLogic = useCopycatLogic;
            this.onlyWhenVirtual = onlyWhenVirtual;
        }

        public boolean useCopycatLogic() {
            return this.useCopycatLogic;
        }

        public boolean onlyWhenVirtual() {
            return this.onlyWhenVirtual;
        }
    }

    @FunctionalInterface
    public static interface MaterialMapper {
        public static final MaterialMapper IDENTITY = (state, material) -> material;

        public BlockState map(BlockState var1, BlockState var2);
    }

    public static abstract class WithData<T>
    extends CopycatModelCore {
        private final ThreadLocal<T> data = new ThreadLocal();

        public void setData(T data) {
            this.data.set(data);
        }

        public T getData() {
            return this.data.get();
        }
    }
}

