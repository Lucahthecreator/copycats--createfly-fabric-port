package com.copycatsplus.copycats.content.copycat.shaft;

import com.copycatsplus.copycats.CCCopycatPartialModels;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlock;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlockEntity;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelInstance;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.CopycatRotatingInstance;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.ICopycatPartialModel;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderData;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CopycatShaftInstance extends KineticBlockEntityVisual<CopycatShaftBlockEntity> {
    protected CopycatRotatingInstance rotatingModel;
    protected Direction from;

    public CopycatShaftInstance(VisualizationContext context, CopycatShaftBlockEntity blockEntity, float partialTick) {
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
    public CopycatShaftInstance(VisualizationContext context, CopycatShaftBlockEntity blockEntity, float partialTick, Direction from) {
        super(context, blockEntity, partialTick);
        this.from = from;
        addModel();
    }

    private CopycatRotatingInstance createModel() {
        return CopycatRotatingInstance.of(
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

    private void addModel() {
        this.rotatingModel = createModel();
        this.rotatingModel.rotatingInstance().setChanged();
    }

    @Override
    public void update(float pt) {
        KineticCopycatRenderData renderData = rotatingModel.renderData();
        if (!renderData.state().equalsState(blockEntity.getBlockState()) || !renderData.material().equals(blockEntity.getMaterial())) {
            rotatingModel.rotatingInstance().delete();
            rotatingModel = createModel();
            rotatingModel.rotatingInstance().setChanged();
        } else {
            rotatingModel.rotatingInstance().setup(blockEntity).setChanged();
        }
    }

    @Override
    public void updateLight(float partialTick) {
        relight(rotatingModel.rotatingInstance());
    }

    @Override
    protected void _delete() {
        rotatingModel.rotatingInstance().delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(rotatingModel.rotatingInstance());
    }
}
