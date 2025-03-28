package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.CCCopycatPartialModels;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlock;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.ICopycatPartialModel;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderData;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class CopycatCogWheelInstance extends KineticBlockEntityVisual<CopycatCogWheelBlockEntity> {
    protected Map<String, CopycatRotatingInstance> rotatingModels = new HashMap<>();
    protected Direction from;

    public CopycatCogWheelInstance(VisualizationContext context, CopycatCogWheelBlockEntity blockEntity, float partialTick) {
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
    public CopycatCogWheelInstance(VisualizationContext context, CopycatCogWheelBlockEntity blockEntity, float partialTick, Direction from) {
        super(context, blockEntity, partialTick);
        this.from = from;
        addModel(CopycatCogWheelBlock.Part.SHAFT.getSerializedName());
        addModel(CopycatCogWheelBlock.Part.COGWHEEL.getSerializedName());
    }

    private CopycatRotatingInstance createModel(String key) {
        ICopycatPartialModel partialModel;
        if (key.equals(CopycatCogWheelBlock.Part.SHAFT.getSerializedName())) {
            partialModel = CCCopycatPartialModels.SHAFT;
        } else {
            partialModel = ICogWheel.isLargeCog(blockEntity.getBlockState())
                    ? CCCopycatPartialModels.LARGE_COGWHEEL
                    : CCCopycatPartialModels.COGWHEEL;
        }
        return CopycatRotatingInstance.of(
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

    private void addModel(String key) {
        CopycatRotatingInstance newInstance = createModel(key);
        newInstance.rotatingInstance.setChanged();
        rotatingModels.put(key, newInstance);
    }

    @Override
    public void update(float pt) {
        for (Map.Entry<String, CopycatRotatingInstance> entry : rotatingModels.entrySet()) {
            KineticCopycatRenderData renderData = entry.getValue().renderData;
            if (!renderData.state().equalsState(blockEntity.getBlockState()) || !renderData.material().equals(blockEntity.getMaterialItemStorage().getMaterialItem(entry.getKey()).material())) {
                entry.getValue().rotatingInstance().delete();
                CopycatRotatingInstance newInstance = createModel(entry.getKey());
                entry.setValue(newInstance);
                newInstance.rotatingInstance().setChanged();
            } else {
                entry.getValue().rotatingInstance().setup(blockEntity).setChanged();
            }
        }
    }

    @Override
    public void updateLight(float partialTick) {
        rotatingModels.forEach((k, v) -> relight(v.rotatingInstance()));
    }

    @Override
    protected void _delete() {
        rotatingModels.forEach((k, v) -> v.rotatingInstance().delete());
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        rotatingModels.forEach((k, v) -> consumer.accept(v.rotatingInstance()));
    }

    public static record CopycatRotatingInstance(KineticCopycatRenderData renderData,
                                                 RotatingInstance rotatingInstance) {
        public static CopycatRotatingInstance of(KineticCopycatRenderData renderData, RotatingInstance rotatingInstance) {
            return new CopycatRotatingInstance(renderData, rotatingInstance);
        }
    }
}
