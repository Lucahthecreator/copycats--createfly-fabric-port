package com.copycatsplus.copycats.content.copycat.base.model.kinetic;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.content.copycat.base.ICopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.partial.CopycatPartialModel;
import com.copycatsplus.copycats.utility.ChatUtils;
import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.config.BackendType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.state.BlockState;

public record KineticCopycatRenderData(CopycatPartialModel partialModel, PartialModelState state, BlockState material) {
    public static KineticCopycatRenderData of(CopycatPartialModel partialModel, ICopycatBlockEntity be) {
        if (!CCConfigs.client().disableGraphicsWarnings.get()) {
            if (Backend.getBackendType() != BackendType.INSTANCING &&
                    Minecraft.getInstance().getBlockColors().getColor(be.getMaterial(), null, null, 0) != -1) {
                ChatUtils.sendWarningOnce(
                        "flywheel_block_color",
                        "Block colors may be incorrect due to the current Flywheel rendering backend. Please switch to the instancing backend to fix this."
                );
            }
        }
        return new KineticCopycatRenderData(partialModel, PartialModelState.fromBlockState(be.getBlockState(), partialModel.getProperties()), be.getMaterial());
    }
}
