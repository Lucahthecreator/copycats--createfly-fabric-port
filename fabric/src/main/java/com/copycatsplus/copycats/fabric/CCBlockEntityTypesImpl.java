package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.content.copycat.base.CCCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.fabric.CCCopycatBlockEntityFabric;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.multistate.fabric.MultiStateCopycatBlockEntityFabric;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderMultiStateBlockEntity;
import com.copycatsplus.copycats.content.copycat.ladder.fabric.CopycatLadderMultiStateBlockEntityFabric;
import com.tterrag.registrate.builders.BlockEntityBuilder;

public class CCBlockEntityTypesImpl {

    public static BlockEntityBuilder.BlockEntityFactory<? extends CCCopycatBlockEntity> getPlatformCopycat() {
        return CCCopycatBlockEntityFabric::new;
    }

    public static BlockEntityBuilder.BlockEntityFactory<? extends MultiStateCopycatBlockEntity> getPlatformMultiState() {
        return MultiStateCopycatBlockEntityFabric::new;
    }

    public static BlockEntityBuilder.BlockEntityFactory<? extends CopycatLadderMultiStateBlockEntity> getPlatformMultiStateLadder() {
        return CopycatLadderMultiStateBlockEntityFabric::new;
    }
}
