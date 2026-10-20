package com.copycatsplus.copycats.utility;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.util.thread.BlockableEventLoop;

public class LogicalSidedProvider<T> {
    public static final LogicalSidedProvider<BlockableEventLoop<? super TickTask>> WORKQUEUE = new LogicalSidedProvider<>(Supplier::get, Supplier::get);
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
            return environment.equals(Platform.Environment.CLIENT) ? false : this.serverSide != null;
        } else {
            return this.clientSide != null;
        }
    }
}
