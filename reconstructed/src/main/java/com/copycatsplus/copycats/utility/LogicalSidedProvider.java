/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.TickTask
 *  net.minecraft.util.thread.BlockableEventLoop
 */
package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.utility.Platform;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.util.thread.BlockableEventLoop;

public class LogicalSidedProvider<T> {
    public static final LogicalSidedProvider<BlockableEventLoop<? super TickTask>> WORKQUEUE = new LogicalSidedProvider<BlockableEventLoop>(Supplier::get, Supplier::get);
    private static Supplier<Minecraft> client;
    private static Supplier<MinecraftServer> server;
    private final Function<Supplier<Minecraft>, T> clientSide;
    private final Function<Supplier<MinecraftServer>, T> serverSide;

    public static void setClient(Supplier<Minecraft> client) {
        LogicalSidedProvider.client = client;
    }

    public static void setServer(Supplier<MinecraftServer> server) {
        LogicalSidedProvider.server = server;
    }

    private LogicalSidedProvider(Function<Supplier<Minecraft>, T> clientSide, Function<Supplier<MinecraftServer>, T> serverSide) {
        this.clientSide = clientSide;
        this.serverSide = serverSide;
    }

    public T get(Platform.Environment side) {
        return side == Platform.Environment.CLIENT ? this.clientSide.apply(client) : this.serverSide.apply(server);
    }

    public boolean isPresent(Platform.Environment environment) {
        if (Platform.Environment.SERVER.isCurrent()) {
            if (environment.equals((Object)Platform.Environment.CLIENT)) {
                return false;
            }
            return this.serverSide != null;
        }
        return this.clientSide != null;
    }
}

