package com.copycatsplus.copycats.content.copycat.base.model;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatModelPart;
import com.copycatsplus.copycats.content.copycat.base.multistate.IMultiStateCopycatBlock;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CopycatModelCore implements CopycatModelPart {

    protected static final ModelEntry SUPER = new ModelEntry("super", null, null, false);
    public static final String MATERIAL_KEY = "material";
    protected final ModelEntry MATERIAL = new ModelEntry(MATERIAL_KEY, (state, mat) -> getModelOf(mat), this, true);
    protected boolean enhanced = true;

    public void registerModels(List<ModelEntry> entries) {
        entries.add(MATERIAL);
    }

    protected void registerForMultiState(List<ModelEntry> entries, IMultiStateCopycatBlock block) {
        for (String property : block.storageProperties()) {
            entries.add(new ModelEntry(property, (state, mat) -> getModelOf(mat), this, true));
        }
    }

    public void prepareForRender() {
        enhanced = CCConfigs.client().useEnhancedModels.get();
    }

    public static BakedModel getModelOf(BlockState state) {
        return Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);
    }

    @ExpectPlatform
    @NotNull
    public static BakedModel createModel(BakedModel original, CopycatModelCore core) {
        //noinspection DataFlowIssue
        return null;
    }

    @ExpectPlatform
    @NotNull
    public static BakedModel createModelWithoutAO(BakedModel original, CopycatModelCore core) {
        //noinspection DataFlowIssue
        return null;
    }

    public static abstract class WithData<T> extends CopycatModelCore {
        private final ThreadLocal<T> data = new ThreadLocal<>();

        public void setData(T data) {
            this.data.set(data);
        }

        public T getData() {
            return this.data.get();
        }
    }

    public record ModelEntry(String key, @Nullable ModelGetter model, @Nullable CopycatModelPart part,
                             boolean useMaterial) {
    }

    @FunctionalInterface
    public interface ModelGetter {
        BakedModel getModel(BlockState state, BlockState material);
    }
}
