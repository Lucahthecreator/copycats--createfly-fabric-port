package com.copycatsplus.copycats.network;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.config.SyncConfigBase;
import com.copycatsplus.copycats.utility.Platform;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.config.ModConfig;

public record ConfigSyncPacket(CompoundTag config, ModConfig.Type type) implements PacketSystem.S2CPacket {

    public ConfigSyncPacket(FriendlyByteBuf buf) {
        this(buf.readAnySizeNbt(), buf.readEnum(ModConfig.Type.class));
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeNbt(config);
        buffer.writeEnum(type);
    }

    @Override
    public void handle(Minecraft mc) {
        Platform.Environment.CLIENT.runIfCurrent(() -> () -> {
            ConfigBase config = CCConfigs.byType(type());
            if (config instanceof SyncConfigBase syncConfig) {
                syncConfig.setSyncConfig(this.config);
                Copycats.LOGGER.debug("Sync Config: Received and applied server config {}", config.toString());
            } else {
                Copycats.LOGGER.warn("Sync Config: Received data for non-synchronized config, ignoring {}", config.toString());
            }
        });
    }
}
