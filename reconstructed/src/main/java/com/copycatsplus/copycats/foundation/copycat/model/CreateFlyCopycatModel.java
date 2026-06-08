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
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModelCore;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.BakedCopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CreateFlyCopycatModel
extends WrapperBlockStateModel {
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
        return super.bake(state, baker);
    }

    public void addPartsWithInfo(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> output) {
        ICopycatBlockEntity copycat;
        block12: {
            block11: {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (state.getBlock() instanceof CopycatSlidingDoorBlock) {
                    BlockPos lowerPos;
                    BlockPos blockPos = lowerPos = state.getValue((Property)DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
                    if (world.getBlockEntity(lowerPos) instanceof ICopycatBlockEntity) {
                        return;
                    }
                }
                if (!(blockEntity instanceof ICopycatBlockEntity)) break block11;
                copycat = (ICopycatBlockEntity)blockEntity;
                if (this.modelManager != null) break block12;
            }
            this.collectParts(random, output);
            return;
        }
        if (this.core != null) {
            this.addCoreParts(world, pos, state, random, output, copycat, false);
            return;
        }
        BlockState material = CreateFlyCopycatModel.resolveMaterialForRender(world, pos, copycat.getMaterial());
        BlockStateModel materialModel = this.modelManager.getBlockStateModelSet().get(material);
        ArrayList materialParts = new ArrayList();
        if (materialModel instanceof WrapperBlockStateModel) {
            WrapperBlockStateModel wrapper = (WrapperBlockStateModel)materialModel;
            wrapper.addPartsWithInfo(world, pos, material, random, materialParts);
        } else {
            materialModel.collectParts(random, materialParts);
        }
        List boxes = state.getShape((BlockGetter)world, pos).toAabbs();
        if (boxes.isEmpty()) {
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
    }

    private void addCoreParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> output, ICopycatBlockEntity copycat, boolean includeVirtual) {
        this.addCoreParts(world, pos, state, random, output, copycat, includeVirtual, this.core, this.entries, null);
    }

    private void addCoreParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> output, ICopycatBlockEntity copycat, boolean includeVirtual, CopycatModelCore renderCore, List<CopycatModelCore.ModelEntry> renderEntries, Map<BlockState, List<BlockStateModelPart>> groupedOutput) {
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
        for (CopycatModelCore.ModelEntry entry : renderEntries) {
            BlockState material2;
            if (!entry.type().useCopycatLogic() || !includeVirtual && entry.type().onlyWhenVirtual() || (material2 = CreateFlyCopycatModel.resolveMaterialForEntry(world, pos, state, entry.key(), entry.materialMapper().map(state, (BlockState)materials.get(entry.key())))) == null) continue;
            BlockStateModel materialModel = entry.model() == null ? this.modelManager.getBlockStateModelSet().get(material2) : entry.model().getModel(state, material2);
            ArrayList materialParts = new ArrayList();
            if (materialModel instanceof WrapperBlockStateModel) {
                WrapperBlockStateModel wrapper = (WrapperBlockStateModel)materialModel;
                wrapper.addPartsWithInfo(world, pos, material2, random, materialParts);
            } else {
                materialModel.collectParts(random, materialParts);
            }
            for (BlockStateModelPart part : materialParts) {
                if (entry.part() == null) {
                    CreateFlyCopycatModel.addPart(output, groupedOutput, material2, part);
                    continue;
                }
                ArrayList<BakedCopycatRenderContext.SourceQuad> source = new ArrayList<BakedCopycatRenderContext.SourceQuad>();
                part.getQuads(null).forEach(quad -> source.add(new BakedCopycatRenderContext.SourceQuad((BakedQuad)quad, null)));
                for (Direction direction : Direction.values()) {
                    part.getQuads(direction).forEach(quad -> source.add(new BakedCopycatRenderContext.SourceQuad((BakedQuad)quad, direction)));
                }
                QuadCollection.Builder builder = new QuadCollection.Builder();
                BakedCopycatRenderContext context = new BakedCopycatRenderContext((List<BakedCopycatRenderContext.SourceQuad>)source, builder, entry.key());
                entry.part().emitCopycatQuads(entry.key(), state, context, material2);
                CreateFlyCopycatModel.addPart(output, groupedOutput, material2, (BlockStateModelPart)new SimpleModelWrapper(builder.build(), part.useAmbientOcclusion(), part.particleMaterial()));
            }
        }
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
        if (material != null && material.is((Object)Blocks.GRASS_BLOCK) && CreateFlyCopycatModel.hasGrassMaterial(world, pos.above())) {
            return Blocks.DIRT.defaultBlockState();
        }
        return material;
    }

    private static BlockState resolveMaterialForEntry(BlockAndTintGetter world, BlockPos pos, BlockState state, String key, BlockState material) {
        material = CreateFlyCopycatModel.normalizeMaterial(material);
        if (state.getBlock() instanceof CopycatCogWheelBlock && key.equals(CopycatCogWheelBlock.Part.COGWHEEL.getSerializedName())) {
            return material;
        }
        if (material != null && material.is((Object)Blocks.GRASS_BLOCK) && CreateFlyCopycatModel.hasGrassMaterial(world, pos.above())) {
            return Blocks.DIRT.defaultBlockState();
        }
        return material;
    }

    private static boolean hasGrassMaterial(BlockAndTintGetter world, BlockPos pos) {
        if (world.getBlockState(pos).is((Object)Blocks.GRASS_BLOCK)) {
            return true;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof IMultiStateCopycatBlockEntity) {
            IMultiStateCopycatBlockEntity multiState = (IMultiStateCopycatBlockEntity)blockEntity;
            return multiState.getMaterialItemStorage().getAllMaterials().stream().map(CreateFlyCopycatModel::normalizeMaterial).anyMatch(material -> material != null && material.is((Object)Blocks.GRASS_BLOCK));
        }
        if (blockEntity instanceof ICopycatBlockEntity) {
            ICopycatBlockEntity copycat = (ICopycatBlockEntity)blockEntity;
            BlockState material2 = CreateFlyCopycatModel.normalizeMaterial(copycat.getMaterial());
            return material2 != null && material2.is((Object)Blocks.GRASS_BLOCK);
        }
        return false;
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

