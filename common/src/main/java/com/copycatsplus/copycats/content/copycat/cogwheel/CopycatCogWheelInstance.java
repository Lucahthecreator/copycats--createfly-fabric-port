package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.CCCopycatPartialModels;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlock;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderData;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderer;
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
        rotatingShaft = instancerProvider()
                .instancer(AllInstanceTypes.ROTATING, KineticCopycatRenderer.getInstancedModel(
                        CCCopycatPartialModels.SHAFT,
                        blockEntity,
                        CopycatCogWheelBlock.Part.SHAFT.getSerializedName()
                ))
                .createInstance()
                .rotateToFace(from, rotationAxis())
                .setup(blockEntity)
                .setPosition(getVisualPosition());
        rotatingCogwheel = instancerProvider()
                .instancer(AllInstanceTypes.ROTATING, KineticCopycatRenderer.getInstancedModel(
                        ICogWheel.isLargeCog(blockEntity.getBlockState()) ? CCCopycatPartialModels.LARGE_COGWHEEL : CCCopycatPartialModels.COGWHEEL,
                        blockEntity,
                        CopycatCogWheelBlock.Part.COGWHEEL.getSerializedName()
                ))
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
