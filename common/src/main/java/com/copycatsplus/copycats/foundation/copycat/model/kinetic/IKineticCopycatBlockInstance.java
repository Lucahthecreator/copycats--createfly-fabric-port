package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.utility.ChatUtils;
import dev.engine_room.flywheel.api.backend.BackendManager;
import dev.engine_room.flywheel.backend.Backends;
import net.minecraft.client.renderer.RenderType;

import javax.annotation.Nullable;

/**
 * An interface with implementation for kinetic copycats with instancing support.
 * <p>
 * Implementors should create a field to store the render data and redirect calls of
 * {@link IKineticCopycatBlockInstance#getRotatingMaterial},
 * {@link IKineticCopycatBlockInstance#getModel} and
 * {@link IKineticCopycatBlockInstance#shouldReset} to this interface.
 */
//TODO: Fix this as RotatingData is gone and not sure what its been replaced with
public interface IKineticCopycatBlockInstance {

    @Nullable
    KineticCopycatRenderData getRenderData();

    MaterialManager getMaterialManager();

    void setRenderData(KineticCopycatRenderData renderData);

    ICopycatBlockEntity getBlockEntity();

    default Material<RotatingData> getRotatingMaterial() {
        RenderType type = RenderType.translucent();
        RenderLayer layer = RenderLayer.getLayer(type);
        if (layer == null) layer = RenderLayer.TRANSPARENT;

        // workaround for flywheel crash when transparent layer is used in batching backend
        if (BackendManager == Backends.INDIRECT && type == RenderType.translucent()) {
            type = RenderType.cutoutMipped();
            ChatUtils.sendWarningOnce(
                    "flywheel_batching_translucent",
                    "Translucent textures may appear slightly broken when using the Flywheel batching backend. Please switch to the instancing backend instead."
            );
        }

        return getMaterialManager().state(layer, type)
                .material(AllMaterialSpecs.ROTATING);
    }

    default Instancer<RotatingData> getModel(ICopycatPartialModel partialModel) {
        KineticCopycatRenderData renderData = KineticCopycatRenderData.of(partialModel, getBlockEntity());
        setRenderData(renderData);
        return getRotatingMaterial().model(renderData, () -> KineticCopycatRenderer.getInstanceModel(partialModel, getBlockEntity(), renderData));
    }

    default boolean shouldReset() {
        if (getRenderData() == null)
            return true;
        if (!getRenderData().material().equals(getBlockEntity().getMaterial()))
            return true;
        if (!getRenderData().state().equalsState(getBlockEntity().getBlockState()))
            return true;
        return false;
    }
}
