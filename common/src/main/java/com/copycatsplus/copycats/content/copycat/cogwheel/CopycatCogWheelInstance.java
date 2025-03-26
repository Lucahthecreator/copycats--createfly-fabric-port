package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.CCCopycatPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.minecraft.core.Direction;

import java.util.function.Consumer;

public class CopycatCogWheelInstance extends KineticBlockEntityVisual<CopycatCogWheelBlockEntity> {
    protected final RotatingInstance rotatingShaft;
    protected final RotatingInstance rotatingCogwheel;

    public CopycatCogWheelInstance(VisualizationContext context, CopycatCogWheelBlockEntity blockEntity, float partialTick) {
        this(context, blockEntity, partialTick, Direction.UP);
    }

    /**
     * @param from  The source model orientation to rotate away from.
     */
    public CopycatCogWheelInstance(VisualizationContext context, CopycatCogWheelBlockEntity blockEntity, float partialTick, Direction from) {
        super(context, blockEntity, partialTick);
        rotatingShaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, CCCopycatPartialModels.SHAFT.getSimpleModel())
                .createInstance()
                .rotateToFace(from, rotationAxis())
                .setup(blockEntity)
                .setPosition(getVisualPosition());
        rotatingCogwheel = instancerProvider().instancer(AllInstanceTypes.ROTATING, ICogWheel.isLargeCog(blockEntity.getBlockState()) ? CCCopycatPartialModels.LARGE_COGWHEEL.getSimpleModel() : CCCopycatPartialModels.COGWHEEL.getSimpleModel())
                .createInstance()
                .rotateToFace(from, rotationAxis())
                .setup(blockEntity)
                .setPosition(getVisualPosition());

        rotatingShaft.setChanged();
        rotatingCogwheel.setChanged();
    }

    @Override
    public void update(float pt) {
        rotatingShaft.setup(blockEntity)
                .setChanged();
        rotatingCogwheel.setup(blockEntity)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(rotatingShaft);
        relight(rotatingCogwheel);
    }

    @Override
    protected void _delete() {
        rotatingShaft.delete();
        rotatingCogwheel.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(rotatingShaft);
        consumer.accept(rotatingCogwheel);
    }
}
