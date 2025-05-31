package com.copycatsplus.copycats.network;

import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fill every part of a multi-state copycat with the given material.
 * The consumed item must be empty.
 * <p>
 * Every part except the specified one will be filled. The specified part will be filled by the server-bound
 * item use packet.
 */
public record FillCopycatPacket(BlockPos pos, BlockState material, String property) implements ServerboundPacketPayload {

    public static final StreamCodec<ByteBuf, FillCopycatPacket> STREAM_CODEC = StreamCodec.composite(
            CatnipStreamCodecs.NULLABLE_BLOCK_POS, FillCopycatPacket::pos,
            CatnipStreamCodecs.BLOCK_STATE, FillCopycatPacket::material,
            CatnipStreamCodecBuilders.nullable(ByteBufCodecs.stringUtf8(256)), FillCopycatPacket::property,
            FillCopycatPacket::new
    );

    @Override
    public void handle(ServerPlayer sender) {
        Level level = sender.level();
        if (!level.isLoaded(pos)) return;
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock)) return;
        BlockState prevMaterial = IMultiStateCopycatBlock.getMaterial(level, pos, property);
        copycatBlock.fillEmptyParts(level, pos, state, material);
        copycatBlock.getCopycatBlockEntity(level, pos).setMaterial(property, prevMaterial);
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return CCPackets.FILL_COPYCAT;
    }
}
