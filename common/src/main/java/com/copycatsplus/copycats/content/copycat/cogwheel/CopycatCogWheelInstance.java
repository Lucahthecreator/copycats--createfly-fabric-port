package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.content.copycat.base.model.kinetic.IMultiStateKineticCopycatBlockInstance;
import com.copycatsplus.copycats.content.copycat.base.model.kinetic.KineticCopycatRenderData;
import com.copycatsplus.copycats.content.copycat.base.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.partial.CopycatPartialModel;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import net.minecraft.core.BlockPos;

import java.util.Map;

public class CopycatCogWheelInstance extends KineticBlockEntityInstance<BracketedKineticBlockEntity> implements IMultiStateKineticCopycatBlockInstance {
    protected Map<String, KineticCopycatRenderData> renderData;
    protected Map<String, RotatingData> rotatingData;

    public CopycatCogWheelInstance(MaterialManager materialManager, BracketedKineticBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        initializeData();
    }

    @Override
    public Map<String, KineticCopycatRenderData> getRenderData() {
        return renderData;
    }

    @Override
    public void setRenderDataInternal(Map<String, KineticCopycatRenderData> renderData) {
        this.renderData = renderData;
    }

    @Override
    public Map<String, RotatingData> getRotatingData() {
        return rotatingData;
    }

    @Override
    public void setRotatingDataInternal(Map<String, RotatingData> rotatingData) {
        this.rotatingData = rotatingData;
    }

    @Override
    public RotatingData setupInternal(RotatingData key) {
        return super.setup(key);
    }

    @Override
    public void updateRotationInternal(RotatingData instance) {
        super.updateRotation(instance);
    }

    @Override
    public void relightInternal(BlockPos pos, FlatLit<?>... models) {
        super.relight(pos, models);
    }

    @Override
    public MaterialManager getMaterialManager() {
        return materialManager;
    }

    @Override
    public IMultiStateCopycatBlockEntity getBlockEntity() {
        return (IMultiStateCopycatBlockEntity) blockEntity;
    }

    @Override
    public void init() {
        super.init();
        init(CopycatPartialModel.SHAFT, CopycatPartialModel.COGWHEEL);
    }

    @Override
    public Material<RotatingData> getRotatingMaterial() {
        return IMultiStateKineticCopycatBlockInstance.super.getRotatingMaterial();
    }

    @Override
    public void update() {
        super.update();
        IMultiStateKineticCopycatBlockInstance.super.update();
    }

    @Override
    public void updateLight() {
        super.updateLight();
        IMultiStateKineticCopycatBlockInstance.super.updateLight();
    }

    @Override
    public void remove() {
        IMultiStateKineticCopycatBlockInstance.super.remove();
    }

    @Override
    public boolean shouldReset() {
        return IMultiStateKineticCopycatBlockInstance.super.shouldReset();
    }
}
