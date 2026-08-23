/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.AllBlocks
 *  com.zurrtum.create.api.behaviour.BlockEntityBehaviour
 *  com.zurrtum.create.client.foundation.model.BakedModelHelper
 *  com.zurrtum.create.client.infrastructure.model.WrapperBlockStateModel
 *  com.zurrtum.create.content.decoration.bracket.BracketedBlockEntityBehaviour
 *  com.zurrtum.create.content.fluids.FluidTransportBehaviour
 *  com.zurrtum.create.content.fluids.pipes.FluidPipeBlock
 *  com.zurrtum.create.foundation.blockEntity.behaviour.BehaviourType
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.block.BlockAndTintGetter
 *  net.minecraft.client.renderer.block.dispatch.BlockStateModel
 *  net.minecraft.client.renderer.block.dispatch.BlockStateModel$UnbakedRoot
 *  net.minecraft.client.renderer.block.dispatch.BlockStateModelPart
 *  net.minecraft.client.resources.model.ModelBaker
 *  net.minecraft.client.resources.model.ModelManager
 *  net.minecraft.client.resources.model.SimpleModelWrapper
 *  net.minecraft.client.resources.model.geometry.BakedQuad
 *  net.minecraft.client.resources.model.geometry.QuadCollection$Builder
 *  net.minecraft.client.resources.model.sprite.Material$Baked
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.BlockAndLightGetter
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.DoorBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 */
package com.copycatsplus.copycats.foundation.copycat.model;

import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatGlassFluidPipeBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModelCore;
import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlock;
import com.copycatsplus.copycats.compat.render.CopycatRenderFlags;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.BakedCopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.mixin.foundation.copycat.FabricBlockStateModelWrapperAccessor;
import com.copycatsplus.copycats.mixin.foundation.copycat.ColorLightTintedBakedModelAccessor;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.api.behaviour.BlockEntityBehaviour;
import com.zurrtum.create.client.foundation.model.BakedModelHelper;
import com.zurrtum.create.client.infrastructure.model.WrapperBlockStateModel;
import com.zurrtum.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.zurrtum.create.content.fluids.FluidTransportBehaviour;
import com.zurrtum.create.content.fluids.pipes.FluidPipeBlock;
import com.zurrtum.create.foundation.blockEntity.behaviour.BehaviourType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CreateFlyCopycatModel
extends WrapperBlockStateModel {
    private static final Map<BlockState, CreateFlyCopycatModel> BAKED_COPYCAT_MODELS = new ConcurrentHashMap<>();

    private ModelManager modelManager;
    private final CopycatModelCore core;
    private final List<CopycatModelCore.ModelEntry> entries = new ArrayList<CopycatModelCore.ModelEntry>();

    public CreateFlyCopycatModel(BlockState state, BlockStateModel.UnbakedRoot unbaked, CopycatModelCore core) {
        super(state, unbaked);
        this.core = core;
        if (core != null) {
            core.registerModels(this.entries);
        }
    }

    public BlockStateModel bake(BlockState state, ModelBaker baker) {
        this.modelManager = Minecraft.getInstance().getModelManager();
        BlockStateModel baked = super.bake(state, baker);
        BAKED_COPYCAT_MODELS.put(state, this);
        return baked;
    }

    /**
     * CreateFly's injected implementation uses one thread-local parts list. A model wrapper such as
     * Continuity can re-enter quad emission while processing a quad and clear that list underneath
     * the outer invocation. Keep Copycats emission re-entrant by owning the list for this call.
     */
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter world, BlockPos pos, BlockState state,
                          RandomSource random, Predicate<Direction> cullTest) {
        List<BlockStateModelPart> parts = new ArrayList<>();
        addPartsWithInfo(world, pos, state, random, parts);
        if (parts.isEmpty()) {
            return;
        }

        TriState ambientOcclusion = parts.getFirst().useAmbientOcclusion() ? TriState.DEFAULT : TriState.FALSE;
        // Snapshot before emission because quad transforms are allowed to render other models recursively.
        for (BlockStateModelPart part : List.copyOf(parts)) {
            emitPart(emitter, part, ambientOcclusion, cullTest);
        }
    }

    private static void emitPart(QuadEmitter emitter, BlockStateModelPart part, TriState ambientOcclusion,
                                 Predicate<Direction> cullTest) {
        for (Direction direction : Direction.values()) {
            if (cullTest.test(direction)) {
                continue;
            }
            for (BakedQuad quad : part.getQuads(direction)) {
                emitter.cullFace(direction);
                emitter.fromBakedQuad(quad);
                emitter.ambientOcclusion(ambientOcclusion);
                emitter.emit();
            }
        }
        if (cullTest.test(null)) {
            return;
        }
        for (BakedQuad quad : part.getQuads(null)) {
            emitter.fromBakedQuad(quad);
            emitter.ambientOcclusion(ambientOcclusion);
            emitter.emit();
        }
    }

    public void addPartsWithInfo(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> output) {
        ICopycatBlockEntity copycat;
        block12: {
            block11: {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                CopycatsDebug.log("model", () -> "addParts start pos=" + pos
                        + " state=" + state + " blockEntity=" + (blockEntity == null ? "null" : blockEntity.getClass().getName())
                        + " core=" + (this.core == null ? "null" : this.core.getClass().getName())
                        + " modelManagerReady=" + (this.modelManager != null));
                if (state.getBlock() instanceof CopycatSlidingDoorBlock) {
                    BlockPos lowerPos;
                    BlockPos blockPos = lowerPos = state.getValue((Property)DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
                    if (world.getBlockEntity(lowerPos) instanceof ICopycatBlockEntity) {
                        CopycatsDebug.log("model", () -> "skip sliding door upper render pos=" + pos
                                + " lowerPos=" + lowerPos + " state=" + state);
                        return;
                    }
                }
                if (!(blockEntity instanceof ICopycatBlockEntity)) break block11;
                copycat = (ICopycatBlockEntity)blockEntity;
                if (this.modelManager != null) break block12;
            }
            CopycatsDebug.log("model", () -> "collect vanilla parts pos=" + pos
                    + " state=" + state + " outputBefore=" + output.size());
            this.collectParts(random, output);
            CopycatsDebug.log("model", () -> "collect vanilla parts done pos=" + pos
                    + " state=" + state + " outputAfter=" + output.size());
            return;
        }
        if (this.core != null) {
            this.addCoreParts(world, pos, state, random, output, copycat, false);
            return;
        }
        BlockState material = CreateFlyCopycatModel.resolveMaterialForRender(world, pos, copycat.getMaterial());
        BlockStateModel materialModel = this.modelManager.getBlockStateModelSet().get(material);
        ArrayList<BlockStateModelPart> materialParts = new ArrayList<>();
        collectMaterialParts(materialModel, world, pos, material, random, materialParts);
        List boxes = state.getShape((BlockGetter)world, pos).toAabbs();
        if (boxes.isEmpty()) {
            CopycatsDebug.log("model", () -> "skip cropped render no boxes pos=" + pos
                    + " state=" + state + " material=" + material);
            return;
        }
        for (BlockStateModelPart part : materialParts) {
            QuadCollection.Builder builder = new QuadCollection.Builder();
            CreateFlyCopycatModel.addCroppedQuads(builder, part.getQuads(null), boxes);
            for (Direction direction : Direction.values()) {
                CreateFlyCopycatModel.addCroppedQuads(builder, part.getQuads(direction), boxes);
            }
            output.add((BlockStateModelPart)new SimpleModelWrapper(builder.build(), part.useAmbientOcclusion(), part.particleMaterial()));
        }
        CopycatsDebug.log("model", () -> "cropped render done pos=" + pos
                + " state=" + state + " material=" + material
                + " materialParts=" + materialParts.size() + " boxes=" + boxes.size());
    }

    private void addCoreParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> output, ICopycatBlockEntity copycat, boolean includeVirtual) {
        this.addCoreParts(world, pos, state, random, output, copycat, includeVirtual, this.core, this.entries, null);
    }

    private void addCoreParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> output, ICopycatBlockEntity copycat, boolean includeVirtual, CopycatModelCore renderCore, List<CopycatModelCore.ModelEntry> renderEntries, Map<BlockState, List<BlockStateModelPart>> groupedOutput) {
        CopycatsDebug.log("model", () -> "core render begin pos=" + pos
                + " state=" + state + " copycat=" + copycat.getClass().getName()
                + " renderCore=" + renderCore.getClass().getName()
                + " entries=" + renderEntries.size()
                + " includeVirtual=" + includeVirtual
                + " grouped=" + (groupedOutput != null));
        renderCore.prepareForRender();
        CreateFlyCopycatModel.prepareRenderData(world, pos, state, renderCore);
        HashMap<String, BlockState> materials = new HashMap<String, BlockState>();
        if (copycat instanceof IMultiStateCopycatBlockEntity) {
            IMultiStateCopycatBlockEntity multiState = (IMultiStateCopycatBlockEntity)copycat;
            multiState.getMaterialItemStorage().getMaterialMap().forEach((key, material) -> materials.put((String)key, CreateFlyCopycatModel.normalizeMaterial(material)));
            if (state.getBlock() instanceof CopycatCogWheelBlock) {
                materials.put("material", (BlockState)materials.get(CopycatCogWheelBlock.Part.SHAFT.getSerializedName()));
            }
        } else {
            materials.put("material", CreateFlyCopycatModel.resolveMaterialForRender(world, pos, copycat.getMaterial()));
        }
        CopycatsDebug.log("model", () -> "core materials pos=" + pos
                + " state=" + state + " materials=" + materials);
        for (CopycatModelCore.ModelEntry entry : renderEntries) {
            BlockState material2;
            BlockState rawMaterial = (BlockState)materials.get(entry.key());
            CopycatsDebug.log("model", () -> "entry check pos=" + pos
                    + " key=" + entry.key() + " type=" + entry.type()
                    + " part=" + entry.part()
                    + " rawMaterial=" + rawMaterial
                    + " useCopycatLogic=" + entry.type().useCopycatLogic()
                    + " onlyWhenVirtual=" + entry.type().onlyWhenVirtual());
            if (!entry.type().useCopycatLogic() || !includeVirtual && entry.type().onlyWhenVirtual() || (material2 = CreateFlyCopycatModel.resolveMaterialForEntry(world, pos, state, entry.key(), entry.materialMapper().map(state, rawMaterial))) == null) {
                CopycatsDebug.log("model", () -> "entry skipped pos=" + pos
                        + " key=" + entry.key() + " rawMaterial=" + rawMaterial);
                continue;
            }
            BlockState entryMaterial = material2;
            CopycatsDebug.log("model", () -> "entry render pos=" + pos
                    + " key=" + entry.key() + " material=" + entryMaterial);
            BlockStateModel materialModel = entry.model() == null ? this.modelManager.getBlockStateModelSet().get(material2) : entry.model().getModel(state, material2);
            ArrayList<BlockStateModelPart> materialParts = new ArrayList<>();
            materialModel = unwrapContinuityModel(materialModel);
            if (materialModel instanceof WrapperBlockStateModel) {
                WrapperBlockStateModel wrapper = (WrapperBlockStateModel)materialModel;
                CopycatsDebug.log("filter", () -> "material wrapper property=" + entry.key()
                        + " material=" + material2 + " modelClass=" + wrapper.getClass().getName());
                BlockAndTintGetter materialWorld = CreateFlyCopycatModel.createMaterialWorld(world, pos, state, entry, copycat);
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
            CopycatsDebug.log("model", () -> "entry material parts pos=" + pos
                    + " key=" + entry.key() + " material=" + entryMaterial
                    + " model=" + materialModelClass
                    + " parts=" + materialParts.size());
            for (BlockStateModelPart part : materialParts) {
                if (entry.part() == null) {
                    CopycatsDebug.log("model", () -> "entry passthrough part pos=" + pos
                            + " key=" + entry.key() + " material=" + entryMaterial
                            + " uncull=" + part.getQuads(null).size());
                    CreateFlyCopycatModel.addPart(output, groupedOutput, material2, part);
                    continue;
                }
                ArrayList<BakedCopycatRenderContext.SourceQuad> source = new ArrayList<BakedCopycatRenderContext.SourceQuad>();
                part.getQuads(null).forEach(quad -> source.add(new BakedCopycatRenderContext.SourceQuad((BakedQuad)quad, null)));
                for (Direction direction : Direction.values()) {
                    part.getQuads(direction).forEach(quad -> source.add(new BakedCopycatRenderContext.SourceQuad((BakedQuad)quad, direction)));
                }
                CopycatsDebug.log("model", () -> "entry source quads pos=" + pos
                        + " key=" + entry.key() + " material=" + entryMaterial
                        + " sourceTotal=" + source.size()
                        + " uncull=" + part.getQuads(null).size()
                        + " down=" + part.getQuads(Direction.DOWN).size()
                        + " up=" + part.getQuads(Direction.UP).size()
                        + " north=" + part.getQuads(Direction.NORTH).size()
                        + " south=" + part.getQuads(Direction.SOUTH).size()
                        + " west=" + part.getQuads(Direction.WEST).size()
                        + " east=" + part.getQuads(Direction.EAST).size());
                QuadCollection.Builder builder = new QuadCollection.Builder();
                BakedCopycatRenderContext context = new BakedCopycatRenderContext((List<BakedCopycatRenderContext.SourceQuad>)source, builder, entry.key());
                BlockState bottomSlabMaterial = materials.get(SlabType.BOTTOM.getSerializedName());
                BlockState topSlabMaterial = materials.get(SlabType.TOP.getSerializedName());
                boolean hideDoubleSlabSeam = CreateFlyCopycatModel.isSameMaterialDoubleSlab(state, bottomSlabMaterial, topSlabMaterial);
                CopycatsDebug.log("filter", () -> "double slab seam pos=" + pos
                        + " bottom=" + bottomSlabMaterial + " top=" + topSlabMaterial
                        + " hide=" + hideDoubleSlabSeam);
                CopycatRenderFlags.setRenderContext(hideDoubleSlabSeam, materials, entry.key());
                try {
                    CopycatsDebug.log("model", () -> "emit begin pos=" + pos
                            + " key=" + entry.key() + " material=" + entryMaterial
                            + " hideDoubleSlabSeam=" + hideDoubleSlabSeam);
                    entry.part().emitCopycatQuads(entry.key(), state, context, material2);
                    CopycatsDebug.log("model", () -> "emit done pos=" + pos
                            + " key=" + entry.key() + " material=" + entryMaterial);
                } finally {
                    CopycatRenderFlags.clear();
                }
                CreateFlyCopycatModel.addPart(output, groupedOutput, material2, (BlockStateModelPart)new SimpleModelWrapper(builder.build(), part.useAmbientOcclusion(), part.particleMaterial()));
            }
        }
        CopycatsDebug.log("model", () -> "core render done pos=" + pos
                + " state=" + state + " output=" + output.size()
                + " groupedMaterials=" + (groupedOutput == null ? 0 : groupedOutput.size()));
    }

    private static BlockAndTintGetter createMaterialWorld(BlockAndTintGetter world, BlockPos pos, BlockState state,
                                                           CopycatModelCore.ModelEntry entry, ICopycatBlockEntity copycat) {
        if (isCopycatPipe(state)) {
            BlockState material = copycat.getMaterial();
            CopycatsDebug.log("filter", () -> "pipe material world origin=" + pos
                    + " state=" + state + " material=" + material);
            return new FilteredBlockAndTintGetter(world, targetPos -> {
                if (targetPos.equals(pos)) {
                    return true;
                }
                BlockState targetState = world.getBlockState(targetPos);
                if (!isCopycatPipe(targetState)) {
                    return false;
                }
                BlockState targetMaterial = com.copycatsplus.copycats.foundation.copycat.ICopycatBlock.getMaterial(world, targetPos);
                boolean result = targetMaterial.getBlock() == material.getBlock();
                CopycatsDebug.log("filter", () -> "pipe material filter origin=" + pos
                        + " target=" + targetPos + " targetState=" + targetState
                        + " material=" + material + " targetMaterial=" + targetMaterial
                        + " result=" + result);
                return result;
            });
        }

        if (!(state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock)
                || !(copycat instanceof IMultiStateCopycatBlockEntity multistateEntity)
                || !copycatBlock.partExists(state, entry.key())) {
            return world;
        }

        if (state.getBlock() instanceof CopycatSlabBlock
                && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.DOUBLE) {
            BlockState bottom = multistateEntity.getMaterialItemStorage()
                    .getMaterialItem(SlabType.BOTTOM.getSerializedName()).material();
            BlockState top = multistateEntity.getMaterialItemStorage()
                    .getMaterialItem(SlabType.TOP.getSerializedName()).material();
            if (CreateFlyCopycatModel.isSameMaterialDoubleSlab(state, bottom, top)) {
                CopycatsDebug.log("filter", () -> "material world full double slab property=" + entry.key()
                        + " origin=" + pos + " material=" + bottom);
                return world;
            }
        }

        Vec3i inner = copycatBlock.getVectorFromProperty(state, entry.key());
        Vec3i scale = copycatBlock.vectorScale(state);
        var materialItem = multistateEntity.getMaterialItemStorage().getMaterialItem(entry.key());
        boolean ctEnabled = materialItem == null || materialItem.enableCT();
        ScaledBlockAndTintGetter unfiltered = new ScaledBlockAndTintGetter(
                entry.key(), world, pos, inner, scale, ignored -> true
        );
        ScaledBlockAndTintGetter scaledWorld = new ScaledBlockAndTintGetter(
                entry.key(), world, pos, inner, scale,
                targetPos -> {
                    boolean result = ctEnabled && copycatBlock.canConnectTexturesToward(
                            entry.key(), unfiltered, pos, targetPos, state
                    );
                    CopycatsDebug.log("filter", () -> "material filter property=" + entry.key()
                            + " origin=" + pos + " scaledTarget=" + targetPos
                            + " trueTarget=" + unfiltered.getTruePos(targetPos)
                            + " state=" + state + " ctEnabled=" + ctEnabled
                            + " result=" + result);
                    return result;
                }
        );
        CopycatsDebug.log("filter", () -> "material world property=" + entry.key()
                + " origin=" + pos + " inner=" + inner + " scale=" + scale
                + " ctEnabled=" + ctEnabled);
        return scaledWorld;
    }

    private static boolean isCopycatPipe(BlockState state) {
        return state.getBlock() instanceof CopycatFluidPipeBlock
                || state.getBlock() instanceof CopycatGlassFluidPipeBlock;
    }

    private static void addPart(List<BlockStateModelPart> output, Map<BlockState, List<BlockStateModelPart>> groupedOutput, BlockState material, BlockStateModelPart part) {
        if (groupedOutput == null) {
            output.add(part);
            return;
        }
        groupedOutput.computeIfAbsent(material, ignored -> new ArrayList()).add(part);
    }

    public void addAnimationParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> output, ICopycatBlockEntity copycat) {
        if (this.core != null && this.modelManager != null) {
            this.addCoreParts(world, pos, state, random, output, copycat, true);
        }
    }

    public void addAnimationParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> output, ICopycatBlockEntity copycat, CopycatModelCore animationCore) {
        if (animationCore == null || this.modelManager == null) {
            return;
        }
        ArrayList<CopycatModelCore.ModelEntry> animationEntries = new ArrayList<CopycatModelCore.ModelEntry>();
        animationCore.registerModels(animationEntries);
        this.addCoreParts(world, pos, state, random, output, copycat, true, animationCore, animationEntries, null);
    }

    public Map<BlockState, List<BlockStateModelPart>> getAnimationPartsByMaterial(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, ICopycatBlockEntity copycat) {
        HashMap<BlockState, List<BlockStateModelPart>> groupedOutput = new HashMap<BlockState, List<BlockStateModelPart>>();
        if (this.core != null && this.modelManager != null) {
            this.addCoreParts(world, pos, state, random, new ArrayList<BlockStateModelPart>(), copycat, true, this.core, this.entries, groupedOutput);
        }
        return groupedOutput;
    }

    /** Returns one kinetic section without merging sections that happen to use the same material. */
    public List<BlockStateModelPart> getAnimationPartsForProperty(BlockAndTintGetter world, BlockPos pos,
                                                                  BlockState state, RandomSource random,
                                                                  ICopycatBlockEntity copycat, String property) {
        if (this.core == null || this.modelManager == null) {
            return List.of();
        }
        List<CopycatModelCore.ModelEntry> selectedEntries = this.entries.stream()
                .filter(entry -> entry.key().equals(property))
                .toList();
        if (selectedEntries.isEmpty()) {
            return List.of();
        }
        List<BlockStateModelPart> output = new ArrayList<>();
        this.addCoreParts(world, pos, state, random, output, copycat, true,
                this.core, selectedEntries, null);
        return List.copyOf(output);
    }

    private void collectMaterialParts(BlockStateModel requestedModel, BlockAndTintGetter world, BlockPos pos,
                                      BlockState material, RandomSource random,
                                      List<BlockStateModelPart> output) {
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

    /**
     * Some optional render mods wrap every baked block model. Copycats needs the underlying
     * context-aware model when it renders a block-entity-selected material; otherwise a wrapped
     * CT model only exposes its static parts and the connection information is lost.
     */
    public static BlockStateModel unwrapContinuityModel(BlockStateModel model) {
        BlockStateModel current = model;
        for (int depth = 0; current != null && depth < 8; depth++) {
            BlockStateModel wrapped;
            if (current.getClass().getName().startsWith("me.pepperbell.continuity.")
                    && current instanceof FabricBlockStateModelWrapperAccessor accessor) {
                wrapped = accessor.copycats$getWrapped();
            } else if (current.getClass().getName().equals("me.mrhikmen.colorlight.core.render.TintedBakedModel")
                    && current instanceof ColorLightTintedBakedModelAccessor accessor) {
                wrapped = accessor.copycats$getWrapped();
            } else {
                break;
            }
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
            if (!(current instanceof FabricBlockStateModelWrapperAccessor accessor)) {
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

    /**
     * Optional renderer wrappers are not required to expose their delegate. Keep the model baked
     * for each state as an authoritative fallback for block-entity animation renderers.
     */
    @Nullable
    public static CreateFlyCopycatModel findCopycatModel(BlockStateModel model, BlockState state) {
        CreateFlyCopycatModel unwrapped = findCopycatModel(model);
        return unwrapped != null ? unwrapped : BAKED_COPYCAT_MODELS.get(state);
    }

    public boolean hasAnimationProperty(String property) {
        return this.modelManager != null && this.entries.stream().anyMatch(entry -> entry.key().equals(property));
    }

    private static void prepareRenderData(BlockAndTintGetter world, BlockPos pos, BlockState state, CopycatModelCore renderCore) {
        BracketedBlockEntityBehaviour bracket;
        if (!(renderCore instanceof CopycatFluidPipeModelCore)) {
            return;
        }
        CopycatFluidPipeModelCore fluidPipe = (CopycatFluidPipeModelCore)renderCore;
        CopycatFluidPipeModelCore.PipeModelData data = new CopycatFluidPipeModelCore.PipeModelData();
        FluidTransportBehaviour transport = (FluidTransportBehaviour)BlockEntityBehaviour.get((BlockGetter)world, (BlockPos)pos, (BehaviourType)FluidTransportBehaviour.TYPE);
        if (transport != null) {
            for (Direction direction : Direction.values()) {
                data.putAttachment(direction, transport.getRenderedRimAttachment((BlockAndLightGetter)world, pos, state, direction));
            }
        }
        if ((bracket = (BracketedBlockEntityBehaviour)BlockEntityBehaviour.get((BlockGetter)world, (BlockPos)pos, (BehaviourType)BracketedBlockEntityBehaviour.TYPE)) != null) {
            data.putBracket(bracket.getBracket());
        }
        data.setEncased(FluidPipeBlock.shouldDrawCasing((BlockAndLightGetter)world, (BlockPos)pos, (BlockState)state));
        fluidPipe.setData(data);
    }

    private static void addCroppedQuads(QuadCollection.Builder builder, List<BakedQuad> quads, List<AABB> boxes) {
        for (BakedQuad quad : quads) {
            Direction direction = quad.direction();
            for (AABB box : boxes) {
                AABB crop = CreateFlyCopycatModel.sourceFaceBox(box, direction);
                builder.addUnculledFace(BakedModelHelper.cropAndMove((BakedQuad)quad, (AABB)crop, (Vec3)CreateFlyCopycatModel.faceOffset(box, direction)));
            }
        }
    }

    private static AABB sourceFaceBox(AABB box, Direction direction) {
        double width = box.maxX - box.minX;
        double height = box.maxY - box.minY;
        double depth = box.maxZ - box.minZ;
        return switch (direction) {
            default -> throw new MatchException(null, null);
            case Direction.WEST -> new AABB(0.0, box.minY, box.minZ, width, box.maxY, box.maxZ);
            case Direction.EAST -> new AABB(1.0 - width, box.minY, box.minZ, 1.0, box.maxY, box.maxZ);
            case Direction.DOWN -> new AABB(box.minX, 0.0, box.minZ, box.maxX, height, box.maxZ);
            case Direction.UP -> new AABB(box.minX, 1.0 - height, box.minZ, box.maxX, 1.0, box.maxZ);
            case Direction.NORTH -> new AABB(box.minX, box.minY, 0.0, box.maxX, box.maxY, depth);
            case Direction.SOUTH -> new AABB(box.minX, box.minY, 1.0 - depth, box.maxX, box.maxY, 1.0);
        };
    }

    private static Vec3 faceOffset(AABB box, Direction direction) {
        return switch (direction) {
            default -> throw new MatchException(null, null);
            case Direction.WEST -> new Vec3(box.minX, 0.0, 0.0);
            case Direction.EAST -> new Vec3(box.maxX - 1.0, 0.0, 0.0);
            case Direction.DOWN -> new Vec3(0.0, box.minY, 0.0);
            case Direction.UP -> new Vec3(0.0, box.maxY - 1.0, 0.0);
            case Direction.NORTH -> new Vec3(0.0, 0.0, box.minZ);
            case Direction.SOUTH -> new Vec3(0.0, 0.0, box.maxZ - 1.0);
        };
    }

    public static BlockState normalizeMaterial(BlockState material) {
        if (material != null && material.hasProperty((Property)BlockStateProperties.SNOWY)) {
            return (BlockState)material.setValue((Property)BlockStateProperties.SNOWY, (Comparable)Boolean.valueOf(false));
        }
        return material;
    }

    public static BlockState resolveMaterialForRender(BlockAndTintGetter world, BlockPos pos, BlockState material) {
        material = CreateFlyCopycatModel.normalizeMaterial(material);
        if (world.getBlockState(pos).getBlock() instanceof CopycatCogWheelBlock) {
            return material;
        }
        if (material != null && material.is(Blocks.GRASS_BLOCK) && CreateFlyCopycatModel.hasGrassMaterial(world, pos.above())) {
            return Blocks.DIRT.defaultBlockState();
        }
        return material;
    }

    private static BlockState resolveMaterialForEntry(BlockAndTintGetter world, BlockPos pos, BlockState state, String key, BlockState material) {
        material = CreateFlyCopycatModel.normalizeMaterial(material);
        if (state.getBlock() instanceof CopycatCogWheelBlock && key.equals(CopycatCogWheelBlock.Part.COGWHEEL.getSerializedName())) {
            return material;
        }
        if (material != null && material.is(Blocks.GRASS_BLOCK) && CreateFlyCopycatModel.hasGrassMaterial(world, pos.above())) {
            return Blocks.DIRT.defaultBlockState();
        }
        return material;
    }

    private static boolean hasGrassMaterial(BlockAndTintGetter world, BlockPos pos) {
        if (world.getBlockState(pos).is(Blocks.GRASS_BLOCK)) {
            return true;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof IMultiStateCopycatBlockEntity) {
            IMultiStateCopycatBlockEntity multiState = (IMultiStateCopycatBlockEntity)blockEntity;
            return multiState.getMaterialItemStorage().getAllMaterials().stream().map(CreateFlyCopycatModel::normalizeMaterial).anyMatch(material -> material != null && material.is(Blocks.GRASS_BLOCK));
        }
        if (blockEntity instanceof ICopycatBlockEntity) {
            ICopycatBlockEntity copycat = (ICopycatBlockEntity)blockEntity;
            BlockState material2 = CreateFlyCopycatModel.normalizeMaterial(copycat.getMaterial());
            return material2 != null && material2.is(Blocks.GRASS_BLOCK);
        }
        return false;
    }

    private static boolean isSameMaterialDoubleSlab(BlockState state, BlockState bottom, BlockState top) {
        return state.getBlock() instanceof CopycatSlabBlock
                && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.DOUBLE
                && CopycatRenderFlags.sameMaterial(bottom, top);
    }

    public boolean needUpdateTerrainParticle() {
        return true;
    }

    public Material.Baked particleMaterialWithInfo(BlockAndTintGetter world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ICopycatBlockEntity) {
            ICopycatBlockEntity copycat = (ICopycatBlockEntity)blockEntity;
            if (this.modelManager != null) {
                return this.modelManager.getBlockStateModelSet().get(CreateFlyCopycatModel.resolveMaterialForRender(world, pos, copycat.getMaterial())).particleMaterial();
            }
        }
        return this.modelManager == null ? this.particleMaterial() : this.modelManager.getBlockStateModelSet().get(AllBlocks.COPYCAT_BASE.defaultBlockState()).particleMaterial();
    }
}

