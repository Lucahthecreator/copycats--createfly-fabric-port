package com.copycatsplus.copycats.content.copycat.base.model.forge;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelPart;
import com.copycatsplus.copycats.content.copycat.base.model.ToggleableCopycatModel;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import java.util.List;

public class ToggleableCopycatModelImpl extends CopycatModel implements ToggleableCopycatModel {

    private final BakedModel base;
    private final BakedModel enhanced;

    public ToggleableCopycatModelImpl(BakedModel originalModel, BakedModel base, BakedModel enhanced) {
        super(originalModel);
        this.base = base;
        this.enhanced = enhanced;
    }

    public static NonNullFunction<BakedModel, ? extends BakedModel> with(CopycatModelPart base, CopycatModelPart enhanced) {
        return model -> new ToggleableCopycatModelImpl(model, CopycatModelPart.create(model, base), CopycatModelPart.create(model, enhanced));
    }

    @Override
    public List<BakedQuad> getCroppedQuads(BlockState state, ModelData data, Direction side, RandomSource rand, BlockState material, ModelData wrappedData, RenderType renderType) {
        return ((CopycatModel) (CCConfigs.client().useEnhancedModels.get() ? enhanced : base)).getCroppedQuads(state, data, side, rand, material, wrappedData, renderType);
    }
}
