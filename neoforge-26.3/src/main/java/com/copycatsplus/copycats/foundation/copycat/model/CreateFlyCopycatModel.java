package com.copycatsplus.copycats.foundation.copycat.model;

import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.compat.render.CopycatRenderFlags;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModelCore;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatGlassFluidPipeBlock;
import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import com.copycatsplus.copycats.mixin.foundation.copycat.DelegateBlockStateModelAccessor;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.api.behaviour.BlockEntityBehaviour;
import com.zurrtum.create.client.foundation.model.BakedModelHelper;
import com.zurrtum.create.client.infrastructure.model.WrapperBlockStateModel;
import com.zurrtum.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.zurrtum.create.content.fluids.FluidTransportBehaviour;
import com.zurrtum.create.content.fluids.pipes.FluidPipeBlock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel.UnbakedRoot;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection.Builder;
import net.minecraft.client.resources.model.sprite.Material.Baked;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CreateFlyCopycatModel extends WrapperBlockStateModel {
    private static final Map<BlockState, CreateFlyCopycatModel> BAKED_COPYCAT_MODELS = new ConcurrentHashMap<>();
    private static final Map<BlockPos, BlockState> PARTICLE_MATERIALS = new LinkedHashMap<>();
    private ModelManager modelManager;
    private final CopycatModelCore core;
    private final List<CopycatModelCore.ModelEntry> entries = new ArrayList<>();

    public CreateFlyCopycatModel(BlockState state, UnbakedRoot unbaked, CopycatModelCore core) {
        super(state, unbaked);
        this.core = core;
        if (core != null) {
            core.registerModels(this.entries);
        }
    }

    @Override
    public BlockStateModel bake(BlockState state, ModelBaker baker) {
        this.modelManager = Minecraft.getInstance().getModelManager();
        BlockStateModel baked = super.bake(state, baker);
        BAKED_COPYCAT_MODELS.put(state, this);
        return baked;
    }

    // NeoForge: the FRAPI emitQuads path is gone. NeoForge and Sodium both render through addPartsWithInfo.

    @Override
    public void addPartsWithInfo(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> output) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ICopycatBlockEntity copycat) {
            rememberParticleMaterial(pos, copycat.getMaterial());
        }
        CopycatsDebug.log(
            "model",
            () -> "addParts start pos="
                + pos
                + " state="
                + state
                + " blockEntity="
                + (blockEntity == null ? "null" : blockEntity.getClass().getName())
                + " core="
                + (this.core == null ? "null" : this.core.getClass().getName())
                + " modelManagerReady="
                + (this.modelManager != null)
        );
        if (state.getBlock() instanceof CopycatSlidingDoorBlock) {
            BlockPos lowerPos = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
            if (world.getBlockEntity(lowerPos) instanceof ICopycatBlockEntity) {
                CopycatsDebug.log("model", () -> "skip sliding door upper render pos=" + pos + " lowerPos=" + lowerPos + " state=" + state);
                return;
            }
        }

        if (!(blockEntity instanceof ICopycatBlockEntity copycat && this.modelManager != null)) {
            CopycatsDebug.log("model", () -> "collect vanilla parts pos=" + pos + " state=" + state + " outputBefore=" + output.size());
            this.collectParts(random, output);
            CopycatsDebug.log("model", () -> "collect vanilla parts done pos=" + pos + " state=" + state + " outputAfter=" + output.size());
        } else if (this.core != null) {
            this.addCoreParts(world, pos, state, random, output, copycat, false);
        } else {
            BlockState material = resolveMaterialForRender(world, pos, copycat.getMaterial());
            BlockStateModel materialModel = this.modelManager.getBlockStateModelSet().get(material);
            ArrayList<BlockStateModelPart> materialParts = new ArrayList<>();
            this.collectMaterialParts(materialModel, world, pos, material, random, materialParts);
            List boxes = state.getShape(world, pos).toAabbs();
            if (boxes.isEmpty()) {
                CopycatsDebug.log("model", () -> "skip cropped render no boxes pos=" + pos + " state=" + state + " material=" + material);
            } else {
                for (BlockStateModelPart part : materialParts) {
                    Builder builder = new Builder();
                    addCroppedQuads(builder, part.getQuads(null), boxes);

                    for (Direction direction : Direction.values()) {
                        addCroppedQuads(builder, part.getQuads(direction), boxes);
                    }

                    output.add(new SimpleModelWrapper(builder.build(), part.useAmbientOcclusion(), part.particleMaterial()));
                }

                CopycatsDebug.log(
                    "model",
                    () -> "cropped render done pos="
                        + pos
                        + " state="
                        + state
                        + " material="
                        + material
                        + " materialParts="
                        + materialParts.size()
                        + " boxes="
                        + boxes.size()
                );
            }
        }
    }

    private void addCoreParts(
        BlockAndTintGetter world,
        BlockPos pos,
        BlockState state,
        RandomSource random,
        List<BlockStateModelPart> output,
        ICopycatBlockEntity copycat,
        boolean includeVirtual
    ) {
        this.addCoreParts(world, pos, state, random, output, copycat, includeVirtual, this.core, this.entries, null);
    }

    private void addCoreParts(
        BlockAndTintGetter world,
        BlockPos pos,
        BlockState state,
        RandomSource random,
        List<BlockStateModelPart> output,
        ICopycatBlockEntity copycat,
        boolean includeVirtual,
        CopycatModelCore renderCore,
        List<CopycatModelCore.ModelEntry> renderEntries,
        Map<BlockState, List<BlockStateModelPart>> groupedOutput
    ) {
        CopycatsDebug.log(
            "model",
            () -> "core render begin pos="
                + pos
                + " state="
                + state
                + " copycat="
                + copycat.getClass().getName()
                + " renderCore="
                + renderCore.getClass().getName()
                + " entries="
                + renderEntries.size()
                + " includeVirtual="
                + includeVirtual
                + " grouped="
                + (groupedOutput != null)
        );
        renderCore.prepareForRender();
        prepareRenderData(world, pos, state, renderCore);
        HashMap<String, BlockState> materials = new HashMap<>();
        if (copycat instanceof IMultiStateCopycatBlockEntity multiState) {
            multiState.getMaterialItemStorage().getMaterialMap().forEach((key, material) -> materials.put(key, normalizeMaterial(material)));
            if (state.getBlock() instanceof CopycatCogWheelBlock) {
                materials.put("material", materials.get(CopycatCogWheelBlock.Part.SHAFT.getSerializedName()));
            }
        } else {
            materials.put("material", resolveMaterialForRender(world, pos, copycat.getMaterial()));
        }

        CopycatsDebug.log("model", () -> "core materials pos=" + pos + " state=" + state + " materials=" + materials);

        for (CopycatModelCore.ModelEntry entry : renderEntries) {
            BlockState rawMaterial = materials.get(entry.key());
            CopycatsDebug.log(
                "model",
                () -> "entry check pos="
                    + pos
                    + " key="
                    + entry.key()
                    + " type="
                    + entry.type()
                    + " part="
                    + entry.part()
                    + " rawMaterial="
                    + rawMaterial
                    + " useCopycatLogic="
                    + entry.type().useCopycatLogic()
                    + " onlyWhenVirtual="
                    + entry.type().onlyWhenVirtual()
            );
            BlockState material2;
            if (entry.type().useCopycatLogic()
                && (includeVirtual || !entry.type().onlyWhenVirtual())
                && (material2 = resolveMaterialForEntry(world, pos, state, entry.key(), entry.materialMapper().map(state, rawMaterial))) != null) {
                BlockState entryMaterial = material2;
                CopycatsDebug.log("model", () -> "entry render pos=" + pos + " key=" + entry.key() + " material=" + material2);
                BlockStateModel materialModel = entry.model() == null
                    ? this.modelManager.getBlockStateModelSet().get(material2)
                    : entry.model().getModel(state, material2);
                ArrayList<BlockStateModelPart> materialParts = new ArrayList<>();
                materialModel = unwrapContinuityModel(materialModel);
                if (materialModel instanceof WrapperBlockStateModel wrapper) {
                    CopycatsDebug.log(
                        "filter", () -> "material wrapper property=" + entry.key() + " material=" + material2 + " modelClass=" + wrapper.getClass().getName()
                    );
                    BlockAndTintGetter materialWorld = createMaterialWorld(world, pos, state, entry, copycat);
                    wrapper.addPartsWithInfo(materialWorld, pos, material2, random, materialParts);
                } else {
                    materialModel.collectParts(random, materialParts);
                }

                if (materialParts.isEmpty()) {
                    BlockStateModel missingModel = this.modelManager.getBlockStateModelSet().missingModel();
                    if (materialModel != missingModel) {
                        missingModel.collectParts(random, materialParts);
                    }
                }

                String materialModelClass = materialModel == null ? "null" : materialModel.getClass().getName();
                CopycatsDebug.log(
                    "model",
                    () -> "entry material parts pos="
                        + pos
                        + " key="
                        + entry.key()
                        + " material="
                        + material2
                        + " model="
                        + materialModelClass
                        + " parts="
                        + materialParts.size()
                );

                for (BlockStateModelPart part : materialParts) {
                    if (entry.part() == null) {
                        CopycatsDebug.log(
                            "model",
                            () -> "entry passthrough part pos="
                                + pos
                                + " key="
                                + entry.key()
                                + " material="
                                + entryMaterial
                                + " uncull="
                                + part.getQuads(null).size()
                        );
                        addPart(output, groupedOutput, material2, part);
                    } else {
                        ArrayList<BakedCopycatRenderContext.SourceQuad> source = new ArrayList<>();
                        part.getQuads(null).forEach(quad -> source.add(new BakedCopycatRenderContext.SourceQuad(quad, null)));

                        for (Direction direction : Direction.values()) {
                            part.getQuads(direction).forEach(quad -> source.add(new BakedCopycatRenderContext.SourceQuad(quad, direction)));
                        }

                        CopycatsDebug.log(
                            "model",
                            () -> "entry source quads pos="
                                + pos
                                + " key="
                                + entry.key()
                                + " material="
                                + entryMaterial
                                + " sourceTotal="
                                + source.size()
                                + " uncull="
                                + part.getQuads(null).size()
                                + " down="
                                + part.getQuads(Direction.DOWN).size()
                                + " up="
                                + part.getQuads(Direction.UP).size()
                                + " north="
                                + part.getQuads(Direction.NORTH).size()
                                + " south="
                                + part.getQuads(Direction.SOUTH).size()
                                + " west="
                                + part.getQuads(Direction.WEST).size()
                                + " east="
                                + part.getQuads(Direction.EAST).size()
                        );
                        Builder builder = new Builder();
                        BakedCopycatRenderContext context = new BakedCopycatRenderContext(source, builder, entry.key());
                        BlockState bottomSlabMaterial = materials.get(SlabType.BOTTOM.getSerializedName());
                        BlockState topSlabMaterial = materials.get(SlabType.TOP.getSerializedName());
                        boolean hideDoubleSlabSeam = isSameMaterialDoubleSlab(state, bottomSlabMaterial, topSlabMaterial);
                        CopycatsDebug.log(
                            "filter",
                            () -> "double slab seam pos=" + pos + " bottom=" + bottomSlabMaterial + " top=" + topSlabMaterial + " hide=" + hideDoubleSlabSeam
                        );
                        CopycatRenderFlags.setRenderContext(hideDoubleSlabSeam, materials, entry.key());

                        try {
                            CopycatsDebug.log(
                                "model",
                                () -> "emit begin pos="
                                    + pos
                                    + " key="
                                    + entry.key()
                                    + " material="
                                    + entryMaterial
                                    + " hideDoubleSlabSeam="
                                    + hideDoubleSlabSeam
                            );
                            entry.part().emitCopycatQuads(entry.key(), state, context, material2);
                            CopycatsDebug.log("model", () -> "emit done pos=" + pos + " key=" + entry.key() + " material=" + entryMaterial);
                        } finally {
                            CopycatRenderFlags.clear();
                        }

                        addPart(output, groupedOutput, material2, new SimpleModelWrapper(builder.build(), part.useAmbientOcclusion(), part.particleMaterial()));
                    }
                }
            } else {
                CopycatsDebug.log("model", () -> "entry skipped pos=" + pos + " key=" + entry.key() + " rawMaterial=" + rawMaterial);
            }
        }

        CopycatsDebug.log(
            "model",
            () -> "core render done pos="
                + pos
                + " state="
                + state
                + " output="
                + output.size()
                + " groupedMaterials="
                + (groupedOutput == null ? 0 : groupedOutput.size())
        );
    }

    private static BlockAndTintGetter createMaterialWorld(
        BlockAndTintGetter world, BlockPos pos, BlockState state, CopycatModelCore.ModelEntry entry, ICopycatBlockEntity copycat
    ) {
        if (isCopycatPipe(state)) {
            BlockState material = copycat.getMaterial();
            CopycatsDebug.log("filter", () -> "pipe material world origin=" + pos + " state=" + state + " material=" + material);
            return new FilteredBlockAndTintGetter(
                world,
                targetPos -> {
                    if (targetPos.equals(pos)) {
                        return true;
                    } else {
                        BlockState targetState = world.getBlockState(targetPos);
                        if (!isCopycatPipe(targetState)) {
                            return false;
                        } else {
                            BlockState targetMaterial = ICopycatBlock.getMaterial(world, targetPos);
                            boolean result = targetMaterial.getBlock() == material.getBlock();
                            CopycatsDebug.log(
                                "filter",
                                () -> "pipe material filter origin="
                                    + pos
                                    + " target="
                                    + targetPos
                                    + " targetState="
                                    + targetState
                                    + " material="
                                    + material
                                    + " targetMaterial="
                                    + targetMaterial
                                    + " result="
                                    + result
                            );
                            return result;
                        }
                    }
                }
            );
        } else if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock
            && copycat instanceof IMultiStateCopycatBlockEntity multistateEntity
            && copycatBlock.partExists(state, entry.key())) {
            if (state.getBlock() instanceof CopycatSlabBlock && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.DOUBLE) {
                BlockState bottom = multistateEntity.getMaterialItemStorage().getMaterialItem(SlabType.BOTTOM.getSerializedName()).material();
                BlockState top = multistateEntity.getMaterialItemStorage().getMaterialItem(SlabType.TOP.getSerializedName()).material();
                if (isSameMaterialDoubleSlab(state, bottom, top)) {
                    CopycatsDebug.log("filter", () -> "material world full double slab property=" + entry.key() + " origin=" + pos + " material=" + bottom);
                    return world;
                }
            }

            Vec3i inner = copycatBlock.getVectorFromProperty(state, entry.key());
            Vec3i scale = copycatBlock.vectorScale(state);
            MaterialItemStorage.MaterialItem materialItem = multistateEntity.getMaterialItemStorage().getMaterialItem(entry.key());
            boolean ctEnabled = materialItem == null || materialItem.enableCT();
            ScaledBlockAndTintGetter unfiltered = new ScaledBlockAndTintGetter(entry.key(), world, pos, inner, scale, ignored -> true);
            ScaledBlockAndTintGetter scaledWorld = new ScaledBlockAndTintGetter(
                entry.key(),
                world,
                pos,
                inner,
                scale,
                targetPos -> {
                    boolean result = ctEnabled && copycatBlock.canConnectTexturesToward(entry.key(), unfiltered, pos, targetPos, state);
                    CopycatsDebug.log(
                        "filter",
                        () -> "material filter property="
                            + entry.key()
                            + " origin="
                            + pos
                            + " scaledTarget="
                            + targetPos
                            + " trueTarget="
                            + unfiltered.getTruePos(targetPos)
                            + " state="
                            + state
                            + " ctEnabled="
                            + ctEnabled
                            + " result="
                            + result
                    );
                    return result;
                }
            );
            CopycatsDebug.log(
                "filter", () -> "material world property=" + entry.key() + " origin=" + pos + " inner=" + inner + " scale=" + scale + " ctEnabled=" + ctEnabled
            );
            return scaledWorld;
        } else {
            return world;
        }
    }

    private static boolean isCopycatPipe(BlockState state) {
        return state.getBlock() instanceof CopycatFluidPipeBlock || state.getBlock() instanceof CopycatGlassFluidPipeBlock;
    }

    private static void addPart(
        List<BlockStateModelPart> output, Map<BlockState, List<BlockStateModelPart>> groupedOutput, BlockState material, BlockStateModelPart part
    ) {
        if (groupedOutput == null) {
            output.add(part);
        } else {
            groupedOutput.computeIfAbsent(material, ignored -> new ArrayList<>()).add(part);
        }
    }

    public void addAnimationParts(
        BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> output, ICopycatBlockEntity copycat
    ) {
        if (this.core != null && this.modelManager != null) {
            this.addCoreParts(world, pos, state, random, output, copycat, true);
        }
    }

    public void addAnimationParts(
        BlockAndTintGetter world,
        BlockPos pos,
        BlockState state,
        RandomSource random,
        List<BlockStateModelPart> output,
        ICopycatBlockEntity copycat,
        CopycatModelCore animationCore
    ) {
        if (animationCore != null && this.modelManager != null) {
            ArrayList<CopycatModelCore.ModelEntry> animationEntries = new ArrayList<>();
            animationCore.registerModels(animationEntries);
            this.addCoreParts(world, pos, state, random, output, copycat, true, animationCore, animationEntries, null);
        }
    }

    public Map<BlockState, List<BlockStateModelPart>> getAnimationPartsByMaterial(
        BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, ICopycatBlockEntity copycat
    ) {
        HashMap<BlockState, List<BlockStateModelPart>> groupedOutput = new HashMap<>();
        if (this.core != null && this.modelManager != null) {
            this.addCoreParts(world, pos, state, random, new ArrayList<>(), copycat, true, this.core, this.entries, groupedOutput);
        }

        return groupedOutput;
    }

    public List<BlockStateModelPart> getAnimationPartsForProperty(
        BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, ICopycatBlockEntity copycat, String property
    ) {
        if (this.core != null && this.modelManager != null) {
            List<CopycatModelCore.ModelEntry> selectedEntries = this.entries.stream().filter(entry -> entry.key().equals(property)).toList();
            if (selectedEntries.isEmpty()) {
                return List.of();
            } else {
                List<BlockStateModelPart> output = new ArrayList<>();
                this.addCoreParts(world, pos, state, random, output, copycat, true, this.core, selectedEntries, null);
                return List.copyOf(output);
            }
        } else {
            return List.of();
        }
    }

    private void collectMaterialParts(
        BlockStateModel requestedModel, BlockAndTintGetter world, BlockPos pos, BlockState material, RandomSource random, List<BlockStateModelPart> output
    ) {
        BlockStateModel materialModel = unwrapContinuityModel(requestedModel);
        if (materialModel instanceof WrapperBlockStateModel wrapper) {
            wrapper.addPartsWithInfo(world, pos, material, random, output);
        } else if (materialModel != null) {
            materialModel.collectParts(random, output);
        }

        if (output.isEmpty() && this.modelManager != null) {
            BlockStateModel missingModel = this.modelManager.getBlockStateModelSet().missingModel();
            if (materialModel != missingModel) {
                missingModel.collectParts(random, output);
            }
        }
    }

    // NeoForge: upstream unwrapped Continuity and ColorLight (Fabric-only). Model wrappers on NeoForge extend
    // DelegateBlockStateModel.
    public static BlockStateModel unwrapContinuityModel(BlockStateModel model) {
        BlockStateModel current = model;

        for (int depth = 0; current instanceof DelegateBlockStateModelAccessor accessor && depth < 8; depth++) {
            BlockStateModel wrapped = accessor.copycats$getWrapped();
            if (wrapped == null || wrapped == current) {
                break;
            }

            current = wrapped;
        }

        return current;
    }

    @Nullable
    public static CreateFlyCopycatModel findCopycatModel(BlockStateModel model) {
        BlockStateModel current = model;

        for (int depth = 0; current != null && depth < 8; depth++) {
            if (current instanceof CreateFlyCopycatModel copycatModel) {
                return copycatModel;
            }

            if (!(current instanceof DelegateBlockStateModelAccessor accessor)) {
                break;
            }

            BlockStateModel wrapped = accessor.copycats$getWrapped();
            if (wrapped == null || wrapped == current) {
                break;
            }

            current = wrapped;
        }

        return null;
    }

    @Nullable
    public static CreateFlyCopycatModel findCopycatModel(BlockStateModel model, BlockState state) {
        CreateFlyCopycatModel unwrapped = findCopycatModel(model);
        return unwrapped != null ? unwrapped : BAKED_COPYCAT_MODELS.get(state);
    }

    public boolean hasAnimationProperty(String property) {
        return this.modelManager != null && this.entries.stream().anyMatch(entry -> entry.key().equals(property));
    }

    private static void prepareRenderData(BlockAndTintGetter world, BlockPos pos, BlockState state, CopycatModelCore renderCore) {
        if (renderCore instanceof CopycatFluidPipeModelCore fluidPipe) {
            CopycatFluidPipeModelCore.PipeModelData data = new CopycatFluidPipeModelCore.PipeModelData();
            FluidTransportBehaviour transport = BlockEntityBehaviour.get(world, pos, FluidTransportBehaviour.TYPE);
            if (transport != null) {
                for (Direction direction : Direction.values()) {
                    data.putAttachment(direction, transport.getRenderedRimAttachment(world, pos, state, direction));
                }
            }

            BracketedBlockEntityBehaviour bracket;
            if ((bracket = BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE)) != null) {
                data.putBracket(bracket.getBracket());
            }

            data.setEncased(FluidPipeBlock.shouldDrawCasing(world, pos, state));
            fluidPipe.setData(data);
        }
    }

    private static void addCroppedQuads(Builder builder, List<BakedQuad> quads, List<AABB> boxes) {
        for (BakedQuad quad : quads) {
            Direction direction = quad.direction();

            for (AABB box : boxes) {
                AABB crop = sourceFaceBox(box, direction);
                builder.addUnculledFace(BakedModelHelper.cropAndMove(quad, crop, faceOffset(box, direction)));
            }
        }
    }

    private static AABB sourceFaceBox(AABB box, Direction direction) {
        double width = box.maxX - box.minX;
        double height = box.maxY - box.minY;
        double depth = box.maxZ - box.minZ;

        return switch (direction) {
            case WEST -> new AABB(0.0, box.minY, box.minZ, width, box.maxY, box.maxZ);
            case EAST -> new AABB(1.0 - width, box.minY, box.minZ, 1.0, box.maxY, box.maxZ);
            case DOWN -> new AABB(box.minX, 0.0, box.minZ, box.maxX, height, box.maxZ);
            case UP -> new AABB(box.minX, 1.0 - height, box.minZ, box.maxX, 1.0, box.maxZ);
            case NORTH -> new AABB(box.minX, box.minY, 0.0, box.maxX, box.maxY, depth);
            case SOUTH -> new AABB(box.minX, box.minY, 1.0 - depth, box.maxX, box.maxY, 1.0);
        };
    }

    private static Vec3 faceOffset(AABB box, Direction direction) {
        return switch (direction) {
            case WEST -> new Vec3(box.minX, 0.0, 0.0);
            case EAST -> new Vec3(box.maxX - 1.0, 0.0, 0.0);
            case DOWN -> new Vec3(0.0, box.minY, 0.0);
            case UP -> new Vec3(0.0, box.maxY - 1.0, 0.0);
            case NORTH -> new Vec3(0.0, 0.0, box.minZ);
            case SOUTH -> new Vec3(0.0, 0.0, box.maxZ - 1.0);
        };
    }

    public static BlockState normalizeMaterial(BlockState material) {
        return material != null && material.hasProperty(BlockStateProperties.SNOWY) ? material.setValue(BlockStateProperties.SNOWY, false) : material;
    }

    public static BlockState resolveMaterialForRender(BlockAndTintGetter world, BlockPos pos, BlockState material) {
        material = normalizeMaterial(material);
        if (world.getBlockState(pos).getBlock() instanceof CopycatCogWheelBlock) {
            return material;
        } else {
            return material != null && material.is(Blocks.GRASS_BLOCK) && hasGrassMaterial(world, pos.above()) ? Blocks.DIRT.defaultBlockState() : material;
        }
    }

    private static BlockState resolveMaterialForEntry(BlockAndTintGetter world, BlockPos pos, BlockState state, String key, BlockState material) {
        material = normalizeMaterial(material);
        if (state.getBlock() instanceof CopycatCogWheelBlock && key.equals(CopycatCogWheelBlock.Part.COGWHEEL.getSerializedName())) {
            return material;
        } else {
            return material != null && material.is(Blocks.GRASS_BLOCK) && hasGrassMaterial(world, pos.above()) ? Blocks.DIRT.defaultBlockState() : material;
        }
    }

    private static boolean hasGrassMaterial(BlockAndTintGetter world, BlockPos pos) {
        if (world.getBlockState(pos).is(Blocks.GRASS_BLOCK)) {
            return true;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof IMultiStateCopycatBlockEntity multiState) {
                return multiState.getMaterialItemStorage()
                    .getAllMaterials()
                    .stream()
                    .map(CreateFlyCopycatModel::normalizeMaterial)
                    .anyMatch(material -> material != null && material.is(Blocks.GRASS_BLOCK));
            } else if (!(blockEntity instanceof ICopycatBlockEntity copycat)) {
                return false;
            } else {
                BlockState material2 = normalizeMaterial(copycat.getMaterial());
                return material2 != null && material2.is(Blocks.GRASS_BLOCK);
            }
        }
    }

    private static boolean isSameMaterialDoubleSlab(BlockState state, BlockState bottom, BlockState top) {
        return state.getBlock() instanceof CopycatSlabBlock
            && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.DOUBLE
            && CopycatRenderFlags.sameMaterial(bottom, top);
    }

    @Override
    public boolean needUpdateTerrainParticle() {
        return true;
    }

    @Override
    public Baked particleMaterialWithInfo(BlockAndTintGetter world, BlockPos pos, BlockState state) {
        if (this.modelManager == null) {
            return this.particleMaterial();
        }

        BlockState material = world.getBlockEntity(pos) instanceof ICopycatBlockEntity copycat
            ? copycat.getMaterial()
            : rememberedParticleMaterial(pos);
        if (material == null) {
            material = AllBlocks.COPYCAT_BASE.defaultBlockState();
        }
        material = resolveMaterialForRender(world, pos, material);
        return this.modelManager.getBlockStateModelSet().get(material).particleMaterial();
    }

    private static void rememberParticleMaterial(BlockPos pos, BlockState material) {
        synchronized (PARTICLE_MATERIALS) {
            PARTICLE_MATERIALS.put(pos.immutable(), material);
            if (PARTICLE_MATERIALS.size() > 8192) {
                PARTICLE_MATERIALS.remove(PARTICLE_MATERIALS.keySet().iterator().next());
            }
        }
    }

    @Nullable
    private static BlockState rememberedParticleMaterial(BlockPos pos) {
        synchronized (PARTICLE_MATERIALS) {
            return PARTICLE_MATERIALS.get(pos);
        }
    }
}
