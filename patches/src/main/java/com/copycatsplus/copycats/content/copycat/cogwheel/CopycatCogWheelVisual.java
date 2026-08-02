package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.foundation.copycat.model.CreateFlyCopycatModel;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import com.zurrtum.create.client.content.kinetics.base.KineticBlockEntityVisual;
import com.zurrtum.create.client.content.kinetics.base.RotatingInstance;
import com.zurrtum.create.client.flywheel.api.instance.Instance;
import com.zurrtum.create.client.flywheel.api.model.Model;
import com.zurrtum.create.client.flywheel.api.visualization.VisualizationContext;
import com.zurrtum.create.client.flywheel.lib.model.baked.BakedModelBuilder;
import com.zurrtum.create.client.foundation.render.AllInstanceTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/** Two independently refreshed rotating instances for the shaft and cogwheel material slots. */
public class CopycatCogWheelVisual extends KineticBlockEntityVisual<CopycatCogWheelBlockEntity> {
    private final Map<String, PartInstance> instances = new LinkedHashMap<>();

    public CopycatCogWheelVisual(VisualizationContext context, CopycatCogWheelBlockEntity blockEntity,
                                 float partialTick) {
        super(context, blockEntity, partialTick);
        addPart(CopycatCogWheelBlock.Part.SHAFT.getSerializedName());
        addPart(CopycatCogWheelBlock.Part.COGWHEEL.getSerializedName());
    }

    private void addPart(String property) {
        PartInstance part = createPart(property);
        if (part != null) {
            instances.put(property, part);
        }
    }

    private PartInstance createPart(String property) {
        BlockState material = material(property);
        Model model = createModel(property, material);
        if (model == null) {
            return null;
        }
        RotatingInstance instance = instancerProvider()
                .instancer(AllInstanceTypes.ROTATING, model)
                .createInstance()
                .rotateToFace(from(), rotationAxis())
                .setup(blockEntity)
                .setPosition(getVisualPosition());
        instance.setChanged();
        return new PartInstance(blockEntity.getBlockState(), material, instance);
    }

    private Model createModel(String property, BlockState material) {
        BlockState state = blockEntity.getBlockState();
        BlockStateModel registeredModel = Minecraft.getInstance().getModelManager()
                .getBlockStateModelSet().get(state);
        CreateFlyCopycatModel copycatModel = CreateFlyCopycatModel.findCopycatModel(registeredModel, state);
        List<BlockStateModelPart> parts = copycatModel == null
                ? List.of()
                : copycatModel.getAnimationPartsForProperty(
                        (BlockAndTintGetter) level,
                        blockEntity.getBlockPos(),
                        state,
                        RandomSource.create(42L),
                        blockEntity,
                        property
                );
        if (parts.isEmpty()) {
            return null;
        }
        FixedPartsModel fixedModel = new FixedPartsModel(parts);
        return CopycatsClient.withAnimatedTint(material, (BlockAndTintGetter) level,
                blockEntity.getBlockPos(), () -> new BakedModelBuilder(fixedModel)
                        .level((BlockAndTintGetter) level)
                        .pos(blockEntity.getBlockPos())
                        .build());
    }

    private BlockState material(String property) {
        MaterialItemStorage.MaterialItem materialItem = blockEntity.getMaterialItemStorage()
                .getMaterialItem(property);
        if (materialItem == null) {
            throw new IllegalStateException("Missing cogwheel material property '" + property + "'");
        }
        return materialItem.material();
    }

    public static boolean canVisualize(CopycatCogWheelBlockEntity blockEntity) {
        if (blockEntity.getLevel() == null) {
            return false;
        }
        String shaft = CopycatCogWheelBlock.Part.SHAFT.getSerializedName();
        String cogwheel = CopycatCogWheelBlock.Part.COGWHEEL.getSerializedName();
        if (blockEntity.getMaterialItemStorage().getMaterialItem(shaft) == null
                || blockEntity.getMaterialItemStorage().getMaterialItem(cogwheel) == null) {
            return false;
        }
        BlockState state = blockEntity.getBlockState();
        BlockStateModel registeredModel = Minecraft.getInstance().getModelManager()
                .getBlockStateModelSet().get(state);
        CreateFlyCopycatModel copycatModel = CreateFlyCopycatModel.findCopycatModel(registeredModel, state);
        return copycatModel != null
                && copycatModel.hasAnimationProperty(shaft)
                && copycatModel.hasAnimationProperty(cogwheel);
    }

    private Direction from() {
        return Direction.fromAxisAndDirection(
                blockEntity.getBlockState().getValue(CopycatCogWheelBlock.AXIS),
                Direction.AxisDirection.POSITIVE
        );
    }

    @Override
    public void update(float partialTick) {
        for (Map.Entry<String, PartInstance> entry : instances.entrySet()) {
            String property = entry.getKey();
            PartInstance current = entry.getValue();
            BlockState material = material(property);
            if (current.state() != blockEntity.getBlockState() || current.material() != material) {
                PartInstance replacement = createPart(property);
                if (replacement != null) {
                    current.instance().delete();
                    entry.setValue(replacement);
                    relight(replacement.instance());
                } else {
                    current.instance().setup(blockEntity).setChanged();
                }
            } else {
                current.instance().setup(blockEntity).setChanged();
            }
        }
    }

    @Override
    public void updateLight(float partialTick) {
        instances.values().forEach(part -> relight(part.instance()));
    }

    @Override
    protected void _delete() {
        instances.values().forEach(part -> part.instance().delete());
        instances.clear();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        instances.values().forEach(part -> consumer.accept(part.instance()));
    }

    private record PartInstance(BlockState state, BlockState material, RotatingInstance instance) {
    }

    private record FixedPartsModel(List<BlockStateModelPart> parts) implements BlockStateModel {
        @Override
        public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
            output.addAll(parts);
        }

        @Override
        public Material.Baked particleMaterial() {
            return parts.getFirst().particleMaterial();
        }

        @Override
        public int materialFlags() {
            return parts.stream().mapToInt(BlockStateModelPart::materialFlags)
                    .reduce(0, (left, right) -> left | right);
        }
    }
}
