package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.CCCopycatPartialModels;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlock;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.*;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CopycatCogWheelVisual extends KineticBlockEntityVisual<CopycatCogWheelBlockEntity> {
    protected Direction from;
    protected CopycatInstanceManager<CopycatCogWheelBlockEntity, RotatingInstance> instances;

    public CopycatCogWheelVisual(VisualizationContext context, CopycatCogWheelBlockEntity blockEntity, float partialTick) {
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
    public CopycatCogWheelVisual(VisualizationContext context, CopycatCogWheelBlockEntity blockEntity, float partialTick, Direction from) {
        super(context, blockEntity, partialTick);
        this.from = from;
        this.instances = new CopycatInstanceManager<>(
                this,
                blockEntity,
                CopycatCogWheelBlock.Part.SHAFT.getSerializedName(),
                CopycatCogWheelBlock.Part.COGWHEEL.getSerializedName()
        ) {
            @Override
            public void updateInstance(CopycatInstance<RotatingInstance> instance) {
                instance.instance().setup(blockEntity).setChanged();
            }

            @Override
            public CopycatInstance<RotatingInstance> createInstance(String key) {
                ICopycatPartialModel partialModel;
                if (key.equals(CopycatCogWheelBlock.Part.SHAFT.getSerializedName())) {
                    partialModel = CCCopycatPartialModels.SHAFT;
                } else {
                    partialModel = ICogWheel.isLargeCog(blockEntity.getBlockState())
                            ? CCCopycatPartialModels.LARGE_COGWHEEL
                            : CCCopycatPartialModels.COGWHEEL;
                }
                return CopycatInstance.of(
                        KineticCopycatRenderData.of(partialModel, blockEntity, key),
                        instancerProvider()
                                .instancer(AllInstanceTypes.ROTATING, KineticCopycatRenderer.getInstancedModel(
                                        partialModel,
                                        blockEntity,
                                        key
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
