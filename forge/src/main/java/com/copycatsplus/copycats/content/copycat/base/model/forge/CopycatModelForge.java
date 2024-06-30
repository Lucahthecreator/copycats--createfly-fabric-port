package com.copycatsplus.copycats.content.copycat.base.model.forge;

import com.copycatsplus.copycats.content.copycat.base.ICopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.ICopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.forge.CopycatRenderContextForge;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.forge.ScaledBlockAndTintGetterForge;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.model.BakedModelWrapperWithData;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore.MATERIAL_KEY;
import static com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore.getModelOf;

public class CopycatModelForge extends BakedModelWrapperWithData {

    public static final ModelProperty<BlockState> MATERIAL_PROPERTY = new ModelProperty<>();
    public static final ModelProperty<Map<String, BlockState>> MATERIALS_PROPERTY = new ModelProperty<>();
    private static final ModelProperty<Map<String, OcclusionData>> OCCLUSION_PROPERTY = new ModelProperty<>();
    private static final ModelProperty<Map<String, ModelData>> WRAPPED_DATA_PROPERTY = new ModelProperty<>();

    private static final ChunkRenderTypeSet allRenderTypes = ChunkRenderTypeSet.of(RenderType.solid(), RenderType.cutout(), RenderType.cutoutMipped(), RenderType.translucent());

    protected final CopycatModelCore core;
    protected final List<CopycatModelCore.ModelEntry> entries = new ArrayList<>();

    public CopycatModelForge(BakedModel originalModel, CopycatModelCore core) {
        super(originalModel);
        this.core = core;
        core.registerModels(entries);
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        prepareModelCore(state, rand, data);
        ChunkRenderTypeSet renderTypes = allRenderTypes;
        Map<String, BlockState> materials = getMaterials(data);
        for (CopycatModelCore.ModelEntry entry : entries) {
            if (entry.model() == null) {
                renderTypes = ChunkRenderTypeSet.union(renderTypes, super.getRenderTypes(state, rand, data));
                continue;
            }
            BlockState material = materials.get(entry.key());
            if (material == null)
                continue;
            BakedModel model = entry.model().getModel(state, material);
            renderTypes = ChunkRenderTypeSet.union(renderTypes, model.getRenderTypes(state, rand, data));
        }
        return renderTypes;
    }

    @Override
    public ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state,
                                             ModelData blockEntityData) {
        Map<String, BlockState> materials = getMaterials(blockEntityData);
        if (materials.isEmpty()) {
            BlockState material = blockEntityData.get(MATERIAL_PROPERTY);
            if (material != null)
                materials = Map.of(MATERIAL_KEY, material);
        }
        if (materials.isEmpty())
            return builder;

        builder.with(MATERIALS_PROPERTY, new HashMap<>(materials));

        if (!(state.getBlock() instanceof ICopycatBlock copycatBlock))
            return builder;

        Map<String, OcclusionData> occlusionMap = materials.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> {
            OcclusionData occlusionData = new OcclusionData();
            gatherOcclusionData(world, pos, state, s.getValue(), occlusionData, copycatBlock);
            return occlusionData;
        }));
        builder.with(OCCLUSION_PROPERTY, occlusionMap);

        if (copycatBlock instanceof MultiStateCopycatBlock multiStateBlock) {
            Map<String, ModelData> wrappedDataMap = materials.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> {
                Vec3i inner = multiStateBlock.getVectorFromProperty(state, s.getKey());
                ScaledBlockAndTintGetterForge scaledWorld = new ScaledBlockAndTintGetterForge(s.getKey(), world, pos, inner, multiStateBlock.vectorScale(state), p -> true);
                ScaledBlockAndTintGetterForge filteredWorld = new ScaledBlockAndTintGetterForge(s.getKey(), world, pos, inner, multiStateBlock.vectorScale(state),
                        targetPos -> {
                            BlockEntity be = world.getBlockEntity(pos);
                            if (be instanceof MultiStateCopycatBlockEntity mscbe)
                                if (!mscbe.getMaterialItemStorage().getMaterialItem(s.getKey()).enableCT())
                                    return false;
                            return multiStateBlock.canConnectTexturesToward(s.getKey(), scaledWorld, pos, targetPos, state);
                        });
                return getModelOf(s.getValue()).getModelData(
                        filteredWorld,
                        pos, s.getValue(), ModelData.EMPTY);
            }));
            return builder.with(WRAPPED_DATA_PROPERTY, wrappedDataMap);
        } else {
            FilteredBlockAndTintGetterForge filteredWorld = new FilteredBlockAndTintGetterForge(world,
                    targetPos -> {
                        BlockEntity be = world.getBlockEntity(pos);
                        if (be instanceof ICopycatBlockEntity copycatBE)
                            if (!copycatBE.isCTEnabled()) return false;
                        return copycatBlock.canConnectTexturesToward(world, pos, targetPos, state);
                    });
            BlockState material = materials.get(MATERIAL_KEY);
            Map<String, ModelData> wrappedDataMap = Map.of(
                    MATERIAL_KEY,
                    getModelOf(material).getModelData(
                            filteredWorld,
                            pos, material, ModelData.EMPTY)
            );
            return builder.with(WRAPPED_DATA_PROPERTY, wrappedDataMap);
        }
    }

    private void gatherOcclusionData(BlockAndTintGetter world, BlockPos pos, BlockState state, BlockState material,
                                     CopycatModelForge.OcclusionData occlusionData, ICopycatBlock copycatBlock) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction face : Iterate.directions) {

            // Rubidium: Run an additional IForgeBlock.hidesNeighborFace check because it
            // seems to be missing in Block.shouldRenderFace
            BlockPos.MutableBlockPos neighbourPos = mutablePos.setWithOffset(pos, face);
            BlockState neighbourState = world.getBlockState(neighbourPos);
            if (state.supportsExternalFaceHiding()
                    && neighbourState.hidesNeighborFace(world, neighbourPos, state, face.getOpposite())) {
                occlusionData.occlude(face);
                continue;
            }

            if (!copycatBlock.canFaceBeOccluded(state, face))
                continue;
            if (!Block.shouldRenderFace(material, world, pos, face, neighbourPos))
                occlusionData.occlude(face);
        }
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side, @NotNull RandomSource rand, @NotNull ModelData data, RenderType renderType) {
        List<BakedQuad> croppedQuads = new ArrayList<>();
        Map<String, BlockState> materials = getMaterials(data);
        Map<String, OcclusionData> occlusionDataMap = getOcclusion(data);
        Map<String, ModelData> wrappedDataMap = getWrappedData(data);
        for (CopycatModelCore.ModelEntry entry : entries) {
            BlockState material = materials.get(entry.key());
            if (entry.useMaterial()) {
                if (material == null)
                    continue;

                // Rubidium: see below
                if (side != null && state.getBlock() instanceof ICopycatBlock ccb && ccb.shouldFaceAlwaysRender(state, side))
                    continue;

                BakedModel model = entry.model() == null ? null : entry.model().getModel(state, material);

                CopycatModelForge.OcclusionData occlusionData = occlusionDataMap.get(entry.key());
                if (occlusionData != null && occlusionData.isOccluded(side))
                    continue;

                ModelData wrappedData = wrappedDataMap.get(entry.key());
                if (wrappedData == null)
                    wrappedData = ModelData.EMPTY;
                if (renderType != null) {
                    if (model == null) {
                        if (!super.getRenderTypes(material, rand, wrappedData).contains(renderType))
                            continue;
                    } else {
                        if (!model.getRenderTypes(material, rand, wrappedData).contains(renderType))
                            continue;
                    }
                }

                List<BakedQuad> templateQuads = model == null
                        ? super.getQuads(material, side, rand, wrappedData, renderType)
                        : model.getQuads(material, side, rand, wrappedData, renderType);
                croppedQuads.addAll(getCroppedQuads(entry, state, templateQuads, material));

                // Rubidium: render side!=null versions of the base material during side==null,
                // to avoid getting culled away
                if (side == null && state.getBlock() instanceof ICopycatBlock ccb) {
                    for (Direction nonOcclusionSide : Iterate.directions)
                        if (ccb.shouldFaceAlwaysRender(state, nonOcclusionSide)) {
                            List<BakedQuad> nonOcclusionTemplateQuads = model == null
                                    ? super.getQuads(material, nonOcclusionSide, rand, wrappedData, renderType)
                                    : model.getQuads(material, nonOcclusionSide, rand, wrappedData, renderType);
                            croppedQuads.addAll(getCroppedQuads(entry, state, nonOcclusionTemplateQuads, material));
                        }
                }
            } else {
                BakedModel model = entry.model() == null ? null : entry.model().getModel(state, material);
                if (renderType != null) {
                    if (model == null) {
                        if (!super.getRenderTypes(state, rand, data).contains(renderType))
                            continue;
                    } else {
                        if (!model.getRenderTypes(state, rand, data).contains(renderType))
                            continue;
                    }
                }
                List<BakedQuad> templateQuads = model == null
                        ? super.getQuads(material, side, rand, data, renderType)
                        : model.getQuads(material, side, rand, data, renderType);
                croppedQuads.addAll(getCroppedQuads(entry, state, templateQuads, material));
            }
        }

        return croppedQuads;
    }

    private List<BakedQuad> getCroppedQuads(CopycatModelCore.ModelEntry entry, BlockState state, List<BakedQuad> templateQuads, BlockState material) {
        if (entry.part() == null)
            return templateQuads;
        List<BakedQuad> quads = new ArrayList<>();
        CopycatRenderContextForge context = new CopycatRenderContextForge(templateQuads, quads);
        entry.part().emitCopycatQuads(entry.key(), state, context, material);
        return quads;
    }

    protected void prepareModelCore(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        core.prepareForRender();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        @NotNull Map<String, BlockState> material = getMaterials(data);

        if (material.isEmpty())
            return super.getParticleIcon(data);

        Map.Entry<String, BlockState> key = material.entrySet().stream().findFirst().get();

        return getModelOf(key.getValue()).getParticleIcon(getWrappedData(data).get(key.getKey()));
    }

    public static @NotNull BlockState getMaterial(ModelData data) {
        BlockState material = data == null ? null : data.get(MATERIAL_PROPERTY);
        return material == null ? AllBlocks.COPYCAT_BASE.getDefaultState() : material;
    }

    public static @NotNull Map<String, BlockState> getMaterials(ModelData data) {
        Map<String, BlockState> materials = data == null ? null : data.get(MATERIALS_PROPERTY);
        return materials == null ? Map.of() : materials;
    }

    public static @NotNull Map<String, OcclusionData> getOcclusion(ModelData data) {
        Map<String, OcclusionData> occlusions = data == null ? null : data.get(OCCLUSION_PROPERTY);
        return occlusions == null ? Map.of() : occlusions;
    }

    public static @NotNull Map<String, ModelData> getWrappedData(ModelData data) {
        Map<String, ModelData> wrappedData = data == null ? null : data.get(WRAPPED_DATA_PROPERTY);
        return wrappedData == null ? Map.of() : wrappedData;
    }

    public static class OcclusionData {
        private final boolean[] occluded;

        public OcclusionData() {
            occluded = new boolean[6];
        }

        public void occlude(Direction face) {
            occluded[face.get3DDataValue()] = true;
        }

        public boolean isOccluded(Direction face) {
            return face != null && occluded[face.get3DDataValue()];
        }
    }

}
