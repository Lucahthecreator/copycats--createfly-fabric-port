package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.utility.ChatUtils;
import dev.engine_room.flywheel.api.backend.BackendManager;
import dev.engine_room.flywheel.backend.Backends;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * An interface with implementation for multi-state kinetic copycats with instancing support.
 * <p>
 * Implementors should create a field to store the render data and redirect calls of
 * {@link IMultiStateKineticCopycatBlockInstance#getRotatingMaterial},
 * {@link IMultiStateKineticCopycatBlockInstance#update},
 * {@link IMultiStateKineticCopycatBlockInstance#updateLight},
 * {@link IMultiStateKineticCopycatBlockInstance#remove},
 * {@link IMultiStateKineticCopycatBlockInstance#shouldReset} to this interface.
 */
//TODO: Fix this as RotatingData is gone and not sure what its been replaced with
public interface IMultiStateKineticCopycatBlockInstance {

    Map<String, KineticCopycatRenderData> getRenderData();

    @ApiStatus.OverrideOnly
    void setRenderDataInternal(Map<String, KineticCopycatRenderData> renderData);

    Map<String, RotatingData> getRotatingData();

    @ApiStatus.OverrideOnly
    void setRotatingDataInternal(Map<String, RotatingData> rotatingData);

    default void initializeData() {
        setRenderDataInternal(new HashMap<>());
        setRotatingDataInternal(new HashMap<>());
    }

    @ApiStatus.OverrideOnly
    RotatingData setupInternal(RotatingData key);

    @ApiStatus.OverrideOnly
    void updateRotationInternal(RotatingData instance);

    @ApiStatus.OverrideOnly
    void relightInternal(BlockPos pos, FlatLit<?>... models);

    default void initModel(ICopycatPartialModel model, String property) {
        getRotatingData().put(property, setupInternal(getModel(model, property).createInstance()));
    }

    MaterialManager getMaterialManager();

    IMultiStateCopycatBlockEntity getBlockEntity();

    default Material<RotatingData> getRotatingMaterial() {
        RenderType type = RenderType.translucent();
        RenderLayer layer = RenderLayer.getLayer(type);
        if (layer == null) layer = RenderLayer.TRANSPARENT;

        // workaround for flywheel crash when transparent layer is used in batching backend
        if (BackendManager.currentBackend() == Backends.INDIRECT && type == RenderType.translucent()) {
            type = RenderType.cutoutMipped();
            ChatUtils.sendWarningOnce(
                    "flywheel_batching_translucent",
                    "Translucent textures may appear slightly broken when using the Flywheel batching backend. Please switch to the instancing backend instead."
            );
        }

        return getMaterialManager().state(layer, type)
                .material(AllMaterialSpecs.ROTATING);
    }

    default Instancer<RotatingData> getModel(ICopycatPartialModel partialModel, String property) {
        KineticCopycatRenderData renderData = KineticCopycatRenderData.of(partialModel, getBlockEntity(), property);
        getRenderData().put(property, renderData);
        return getRotatingMaterial().model(renderData, () -> KineticCopycatRenderer.getInstanceModel(partialModel, getBlockEntity(), renderData));
    }

    default void update() {
        for (Map.Entry<String, RotatingData> entry : getRotatingData().entrySet()) {
            updateRotationInternal(entry.getValue());
        }
    }

    default void updateLight() {
        for (Map.Entry<String, RotatingData> entry : getRotatingData().entrySet()) {
            relightInternal(getBlockEntity().getBlockPos(), entry.getValue());
        }
    }

    default void remove() {
        for (Map.Entry<String, RotatingData> entry : getRotatingData().entrySet()) {
            entry.getValue().delete();
        }
    }

    default boolean shouldReset() {
        if (getRenderData() == null)
            return true;
        for (Map.Entry<String, KineticCopycatRenderData> entry : getRenderData().entrySet()) {
            if (!entry.getValue().material().equals(getBlockEntity().getMaterialItemStorage().getMaterialItem(entry.getKey()).material()))
                return true;
            if (!entry.getValue().state().equalsState(getBlockEntity().getBlockState()))
                return true;
        }
        return false;
    }
}
