package com.copycatsplus.copycats.config;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.network.CCPackets;
import com.copycatsplus.copycats.network.ConfigSyncPacket;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.config.ModConfig;

/**
 * Base class for all configs that require custom synchronization from server to clients.
 */
// TODO: clean up SyncConfigBase implementation w.r.t C:Connected's implementation
//   syncToAllPlayers seems to be unused?
public abstract class SyncConfigBase extends ConfigBase {

    public final CompoundTag getSyncConfig() {
        CompoundTag nbt = new CompoundTag();
        writeSyncConfig(nbt);
        if (children != null)
            for (ConfigBase child : children) {
                if (child instanceof SyncConfigBase syncChild) {
                    if (nbt.contains(child.getName()))
                        throw new RuntimeException("A sync config key starts with " + child.getName() + " but does not belong to the child");
                    nbt.put(child.getName(), syncChild.getSyncConfig());
                }
            }
        return nbt;
    }

    protected abstract ModConfig.Type type();

    /**
     * Serialize all configs into the provided `nbt` tag to be synced to clients.
     *
     * @param nbt Accepts any value.
     */
    protected void writeSyncConfig(CompoundTag nbt) {
    }

    public final void setSyncConfig(CompoundTag config) {
        if (children != null)
            for (ConfigBase child : children) {
                if (child instanceof SyncConfigBase syncChild) {
                    CompoundTag nbt = config.getCompound(child.getName());
                    syncChild.readSyncConfig(nbt);
                }
            }
        readSyncConfig(config);
    }

    /**
     * Deserialize all configs from the provided `nbt` tag to a custom data storage.
     * The implementing class is responsible for data storage. No storage/config overwrite mechanism is provided here.
     *
     * @param nbt The configs sent from server.
     */
    protected void readSyncConfig(CompoundTag nbt) {
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onReload() {
        super.onReload();
    }

    public void syncToPlayer(ServerPlayer player) {
        if (player == null) return;
        CCPackets.network().sendToClient(player, new ConfigSyncPacket(getSyncConfig(), type()));
        Copycats.LOGGER.debug("Sync Config: Sending server config to {}", player.getName().getString());
    }
}
