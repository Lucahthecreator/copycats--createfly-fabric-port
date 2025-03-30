package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import dev.engine_room.flywheel.lib.instance.AbstractInstance;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Manages multiple instances for a copycat block entity and automatically refreshes them when the block entity changes.
 *
 * @param <B> The block entity type.
 * @param <R> The instance type.
 */
public abstract class CopycatInstanceManager<B extends BlockEntity, R extends AbstractInstance> {
    protected final AbstractBlockEntityVisual<B> visual;
    protected final B blockEntity;
    protected Map<String, CopycatInstance<R>> copycatInstances = new HashMap<>();

    private ICopycatBlockEntity singleStateBE;
    private IMultiStateCopycatBlockEntity multiStateBE;

    public CopycatInstanceManager(AbstractBlockEntityVisual<B> visual, B blockEntity, String... keys) {
        this.visual = visual;
        this.blockEntity = blockEntity;
        if (this.blockEntity instanceof IMultiStateCopycatBlockEntity multi) {
            this.multiStateBE = multi;
        } else if (this.blockEntity instanceof ICopycatBlockEntity single) {
            this.singleStateBE = single;
        } else {
            throw new IllegalArgumentException("Block entity must implement either IMultiStateCopycatBlockEntity or ICopycatBlockEntity");
        }
        for (String key : keys) {
            addModel(key);
        }
    }

    private void addModel(String key) {
        CopycatInstance<R> newInstance = createInstance(key);
        newInstance.instance().setChanged();
        copycatInstances.put(key, newInstance);
    }

    private BlockState getMaterial(String key) {
        if (singleStateBE != null) {
            return singleStateBE.getMaterial();
        } else {
            return multiStateBE.getMaterialItemStorage().getMaterialItem(key).material();
        }
    }

    public void update(float pt) {
        for (Map.Entry<String, CopycatInstance<R>> entry : copycatInstances.entrySet()) {
            KineticCopycatRenderData renderData = entry.getValue().renderData();
            if (!renderData.state().equalsState(blockEntity.getBlockState()) || !renderData.material().equals(getMaterial(entry.getKey()))) {
                entry.getValue().instance().delete();
                CopycatInstance<R> newInstance = createInstance(entry.getKey());
                entry.setValue(newInstance);
                newInstance.instance().setChanged();
                visual.updateLight(pt);
            } else {
                updateInstance(entry.getValue());
            }
        }
    }

    public void forEach(Consumer<CopycatInstance<R>> consumer) {
        copycatInstances.values().forEach(consumer);
    }

    public abstract void updateInstance(CopycatInstance<R> instance);

    public abstract CopycatInstance<R> createInstance(String key);
}
