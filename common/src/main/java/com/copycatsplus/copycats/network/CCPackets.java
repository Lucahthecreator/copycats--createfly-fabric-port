package com.copycatsplus.copycats.network;

import com.copycatsplus.copycats.Copycats;
import net.createmod.catnip.net.base.BasePacketPayload;
import net.createmod.catnip.net.base.CatnipPacketRegistry;
import net.createmod.catnip.platform.CatnipServices;
import net.createmod.catnip.platform.services.NetworkHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Locale;

public enum CCPackets implements BasePacketPayload.PacketTypeProvider {
    CONFIG_SYNC(ConfigSyncPacket.class, ConfigSyncPacket.STREAM_CODEC),
    FILL_COPYCAT(FillCopycatPacket.class, FillCopycatPacket.STREAM_CODEC)
    ;
/*    //TODO: Increase version on updates
    public static final PacketSystem;PACKETS = PacketSystem.builder(Copycats.MODID, 2)
            .s2c(ConfigSyncPacket.class, ConfigSyncPacket::new)
            .c2s(FillCopycatPacket.class, FillCopycatPacket::new)
            .build();*/

    private final CatnipPacketRegistry.PacketType<?> type;

    <T extends BasePacketPayload> CCPackets(Class<T> clazz, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        String name = this.name().toLowerCase(Locale.ROOT);
        this.type = new CatnipPacketRegistry.PacketType<>(
                new CustomPacketPayload.Type<>(Copycats.asResource(name)),
                clazz, codec
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType() {
        return (CustomPacketPayload.Type<T>) this.type.type();
    }

    public static NetworkHelper network() {
        return CatnipServices.NETWORK;
    }

    public static void register() {
        CatnipPacketRegistry packetRegistry = new CatnipPacketRegistry(Copycats.MODID, 3);
        for (CCPackets packet : CCPackets.values()) {
            packetRegistry.registerPacket(packet.type);
        }
        packetRegistry.registerAllPackets();
    }
}
