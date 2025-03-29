package com.copycatsplus.copycats.content.copycat.shaft;

import com.copycatsplus.copycats.CCCopycatPartialModels;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.CopycatInstance;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.CopycatInstanceManager;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderData;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.minecraft.core.Direction;

import java.util.function.Consumer;

public class CopycatShaftVisual extends KineticBlockEntityVisual<CopycatShaftBlockEntity> {
    protected Direction from;
    protected CopycatInstanceManager<CopycatShaftBlockEntity, RotatingInstance> instances;

    public CopycatShaftVisual(VisualizationContext context, CopycatShaftBlockEntity blockEntity, float partialTick) {
        this(
                context, blockEntity, partialTick,
                Direction.fromAxisAndDirection(
                        blockEntity.getBlockState().getValue(CopycatShaftBlock.AXIS),
                        Direction.AxisDirection.POSITIVE
                )
        );
    }

    /**
     * @param from The source model orientation to rotate away from.
     */
    public CopycatShaftVisual(VisualizationContext context, CopycatShaftBlockEntity blockEntity, float partialTick, Direction from) {
        super(context, blockEntity, partialTick);
        this.from = from;
        this.instances = new CopycatInstanceManager<>(this, blockEntity, CopycatModelCore.MATERIAL_KEY) {

            @Override
            public void updateInstance(CopycatInstance<RotatingInstance> instance) {
                instance.instance().setup(blockEntity).setChanged();
            }

            @Override
            public CopycatInstance<RotatingInstance> createInstance(String key) {
                return CopycatInstance.of(
                        KineticCopycatRenderData.of(CCCopycatPartialModels.SHAFT, blockEntity),
                        instancerProvider()
                                .instancer(AllInstanceTypes.ROTATING, KineticCopycatRenderer.getInstancedModel(
                                        CCCopycatPartialModels.SHAFT,
                                        blockEntity
                                ))
                                .createInstance()
                                .rotateToFace(from, rotationAxis())
                                .setup(blockEntity)
                                .setPosition(getVisualPosition())
                );
            }
        };
    }

    @Override
    public void update(float pt) {
        instances.update(pt);
    }

    @Override
    public void updateLight(float partialTick) {
        instances.forEach(i -> relight(i.instance()));
    }

    @Override
    protected void _delete() {
        instances.forEach(i -> i.instance().delete());
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        instances.forEach(i -> consumer.accept(i.instance()));
    }
}
