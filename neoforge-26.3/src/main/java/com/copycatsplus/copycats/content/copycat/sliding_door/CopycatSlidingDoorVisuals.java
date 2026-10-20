package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.zurrtum.create.client.flywheel.api.instance.Instance;
import com.zurrtum.create.client.flywheel.api.visual.BlockEntityVisual;
import com.zurrtum.create.client.flywheel.api.visualization.BlockEntityVisualizer;
import com.zurrtum.create.client.flywheel.api.visualization.VisualizationContext;
import com.zurrtum.create.client.flywheel.api.visualization.VisualizationManager;
import com.zurrtum.create.client.flywheel.api.visualization.VisualizerRegistry;
import com.zurrtum.create.content.decoration.slidingDoor.SlidingDoorBlock;
import java.util.function.Consumer;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class CopycatSlidingDoorVisuals {
    private CopycatSlidingDoorVisuals() {
    }

    public static void register() {
        @SuppressWarnings("unchecked")
        BlockEntityType<CopycatSlidingDoorBlockEntity> type = (BlockEntityType<CopycatSlidingDoorBlockEntity>)(BlockEntityType<?>)CCBlockEntityTypes.COPYCAT_SLIDING_DOOR.get();
        VisualizerRegistry.setVisualizer(
            type,
            new BlockEntityVisualizer<CopycatSlidingDoorBlockEntity>() {
                public BlockEntityVisual<? super CopycatSlidingDoorBlockEntity> createVisual(
                    VisualizationContext context, CopycatSlidingDoorBlockEntity blockEntity, float partialTick
                ) {
                    return (BlockEntityVisual<? super CopycatSlidingDoorBlockEntity>)(CopycatSlidingDoorVisuals.shouldUseVisualization(blockEntity)
                        ? new CopycatSlidingDoorVisual(context, blockEntity, partialTick)
                        : new CopycatSlidingDoorVisuals.NoopVisual());
                }

                public boolean skipVanillaRender(CopycatSlidingDoorBlockEntity blockEntity) {
                    return CopycatSlidingDoorVisuals.shouldUseVisualization(blockEntity);
                }
            }
        );
    }

    public static boolean shouldUseVisualization(CopycatSlidingDoorBlockEntity blockEntity) {
        return blockEntity.getLevel() != null
            && VisualizationManager.supportsVisualization(blockEntity.getLevel())
            && blockEntity.getBlockState().getValue(SlidingDoorBlock.FACING).getAxis() != Axis.X
            && CopycatSlidingDoorVisual.canVisualize(blockEntity);
    }

    private static final class NoopVisual implements BlockEntityVisual<CopycatSlidingDoorBlockEntity> {
        @Override
        public void update(float partialTick) {
        }

        @Override
        public void collectCrumblingInstances(Consumer<Instance> consumer) {
        }

        @Override
        public void delete() {
        }
    }
}
