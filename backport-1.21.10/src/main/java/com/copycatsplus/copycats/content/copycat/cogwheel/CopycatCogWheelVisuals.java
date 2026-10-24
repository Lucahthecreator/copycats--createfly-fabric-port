package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.zurrtum.create.client.flywheel.api.visual.BlockEntityVisual;
import com.zurrtum.create.client.flywheel.api.visualization.BlockEntityVisualizer;
import com.zurrtum.create.client.flywheel.api.visualization.VisualizationContext;
import com.zurrtum.create.client.flywheel.api.visualization.VisualizationManager;
import com.zurrtum.create.client.flywheel.api.visualization.VisualizerRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class CopycatCogWheelVisuals {
    private CopycatCogWheelVisuals() {
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register() {
        BlockEntityType<CopycatCogWheelBlockEntity> type = (BlockEntityType) CCBlockEntityTypes.COPYCAT_COGWHEEL.get();
        VisualizerRegistry.setVisualizer(type, new BlockEntityVisualizer<>() {
            @Override
            public BlockEntityVisual<? super CopycatCogWheelBlockEntity> createVisual(
                    VisualizationContext context, CopycatCogWheelBlockEntity blockEntity, float partialTick) {
                return new CopycatCogWheelVisual(context, blockEntity, partialTick);
            }

            @Override
            public boolean skipVanillaRender(CopycatCogWheelBlockEntity blockEntity) {
                return shouldUseVisualization(blockEntity);
            }
        });
    }

    public static boolean shouldUseVisualization(CopycatCogWheelBlockEntity blockEntity) {
        return blockEntity.getLevel() != null
                && VisualizationManager.supportsVisualization(blockEntity.getLevel())
                && CopycatCogWheelVisual.canVisualize(blockEntity);
    }
}
