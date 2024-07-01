package com.copycatsplus.copycats.content.copycat.base.model;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatModelPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.content.copycat.base.multistate.IMultiStateCopycatBlock;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Block-specific but platform-independent model generation logic for copycats.
 */
public abstract class CopycatModelCore implements CopycatModelPart {

    protected static final ModelEntry SUPER = new ModelEntry("super", null, null, false);

    /**
     * Model key for the copied material in simple copycats.
     */
    public static final String MATERIAL_KEY = "material";
    protected final ModelEntry MATERIAL = new ModelEntry(MATERIAL_KEY, (state, mat) -> getModelOf(mat), this, true);

    /**
     * Whether this model core should render enhanced models.
     */
    protected boolean enhanced = true;

    /**
     * Register all models required to render this copycat.
     * <p>
     * By default, the only required model is the one of the copied material.
     * This should be sufficient for most simple copycats. Only override this method if the copycat is multi-state or
     * if extra models are required.
     *
     * @param entries The list to register the models to.
     */
    public void registerModels(List<ModelEntry> entries) {
        entries.add(MATERIAL);
    }

    /**
     * Helper method to register models for all copied materials in a multi-state copycat.
     * <p>
     * When implementing a model core for multi-state copycats, override {@link CopycatModelCore#registerModels}
     * and call this method without calling super.
     *
     * @param entries The list to register the models to.
     * @param block   The multi-state copycat that this model core is meant for.
     */
    protected final void registerForMultiState(List<ModelEntry> entries, IMultiStateCopycatBlock block) {
        for (String property : block.storageProperties()) {
            entries.add(new ModelEntry(property, (state, mat) -> getModelOf(mat), this, true));
        }
    }

    /**
     * Called before rendering to gather external data, such as configs, and prepare the model core for rendering.
     * <p>
     * This method is likely to be called on the render thread and might be called multiple times in each render.
     */
    public void prepareForRender() {
        enhanced = CCConfigs.client().useEnhancedModels.get();
    }

    /**
     * For a default implementation of {@link CopycatModelCore#registerModels}, this method is called to assemble quads
     * from the copied material, with the key being equal to {@link CopycatModelCore#MATERIAL_KEY}.
     * <p>
     * For multi-state copycats using {@link CopycatModelCore#registerForMultiState}, this method is called for each
     * storage property of the copycat.
     *
     * @param key      The key of the model provided by the context. For simple copycats, the key of the copied material is {@link CopycatModelCore#MATERIAL_KEY}. For multi-state copycats, the keys come from the storage properties of the copycat.
     * @param state    The block state of the copycat.
     * @param context  The context to assemble the quads with.
     * @param material The block state of the copied material. This matches the key provided for both simple and multi-state copycats.
     */
    @Override
    public abstract void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material);

    /**
     * Helper method to get the model of a block state.
     *
     * @param state The block state to get the model of.
     * @return The baked model of the block state.
     */
    public static BakedModel getModelOf(BlockState state) {
        return Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);
    }

    /**
     * Create a platform-specific {@link BakedModel} implementation for a copycat which wraps the original model and
     * renders with the provided core.
     *
     * @param original The original model to wrap.
     * @param core     The core to render the model with.
     */
    @ExpectPlatform
    @NotNull
    public static BakedModel createModel(BakedModel original, CopycatModelCore core) {
        //noinspection DataFlowIssue
        return null;
    }

    /**
     * Create a platform-specific {@link BakedModel} implementation for a copycat which wraps the original model and
     * renders with the provided core.
     * <p>
     * Ambient occlusion is disabled for this model, which is ideal for partial models and kinetic copycat models.
     *
     * @param original The original model to wrap.
     * @param core     The core to render the model with.
     */
    @ExpectPlatform
    @NotNull
    public static BakedModel createModelWithoutAO(BakedModel original, CopycatModelCore core) {
        //noinspection DataFlowIssue
        return null;
    }

    /**
     * A model core that requires extra data for rendering. The stored data is thread-local and should not be retained between renders.
     *
     * @param <T> The type of data required for rendering.
     */
    public static abstract class WithData<T> extends CopycatModelCore {
        private final ThreadLocal<T> data = new ThreadLocal<>();

        /**
         * Set the data required for rendering. This is intended to be called around the same time as
         * {@link CopycatModelCore#prepareForRender}, which guarantees that the data is set before model getters are invoked.
         */
        public void setData(T data) {
            this.data.set(data);
        }

        /**
         * Get the data required for rendering. The data is thread-local and should not be retained between renders.
         */
        public T getData() {
            return this.data.get();
        }
    }

    /**
     * A model entry to be rendered by a {@link CopycatModelCore}.
     *
     * @param key             A custom key to identify the model entry during rendering.
     * @param model           A getter that returns a {@link BakedModel} to be rendered for this entry, invoked for each render. Set to null to render the original model as specified by the copycat's block state file.
     * @param part            A {@link CopycatModelPart} to assemble the model quads with. Set to null if the model should be rendered without modifications.
     * @param useCopycatLogic Whether to use copycat logic for occlusion, culling and connected textures instead of the original model's logic.
     */
    public record ModelEntry(String key, @Nullable ModelGetter model, @Nullable CopycatModelPart part,
                             boolean useCopycatLogic) {
    }

    /**
     * A functional interface to get the {@link BakedModel} when rendering a {@link ModelEntry}. For model cores with
     * extra data, the data should be set before invoking this getter, so that the getter is safe to access the data.
     */
    @FunctionalInterface
    public interface ModelGetter {
        /**
         * Get the model for the given block state and material.
         *
         * @param state    The block state of the copycat.
         * @param material The block state of the copied material.
         */
        BakedModel getModel(BlockState state, BlockState material);
    }
}
