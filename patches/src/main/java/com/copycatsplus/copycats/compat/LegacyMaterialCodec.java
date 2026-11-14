package com.copycatsplus.copycats.compat;

import com.google.gson.JsonNull;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.block.state.BlockState;

/** Reads material states saved before the block-state codec changed in 26.3. */
public final class LegacyMaterialCodec {
    private static final Codec<BlockState> OLD_FORMAT = Codec.PASSTHROUGH.comapFlatMap(
            input -> BlockState.CODEC.parse(input.renameField("Name", "id").renameField("Properties", "properties")),
            ignored -> new Dynamic<>(JsonOps.INSTANCE, JsonNull.INSTANCE));
    public static final Codec<BlockState> CODEC = Codec.withAlternative(BlockState.CODEC, OLD_FORMAT);

    private LegacyMaterialCodec() {
    }
}
