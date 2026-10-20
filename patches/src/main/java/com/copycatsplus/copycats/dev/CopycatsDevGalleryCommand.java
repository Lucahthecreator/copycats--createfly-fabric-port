package com.copycatsplus.copycats.dev;

import com.copycatsplus.copycats.CCItems;
import com.copycatsplus.copycats.Copycats;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.AttachFace;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

/** Places every public Copycats block near a developer without replacing existing blocks. */
public final class CopycatsDevGalleryCommand {
    private static final int COLUMNS = 8;
    private static final int COLUMN_SPACING = 3;
    private static final int ROW_SPACING = 4;

    private CopycatsDevGalleryCommand() {
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(Commands.literal("copycats_test")
                        .executes(context -> placeAll(context.getSource().getPlayerOrException()))));
    }

    private static int placeAll(ServerPlayer player) throws CommandSyntaxException {
        ServerLevel level = player.level();
        List<Block> blocks = BuiltInRegistries.BLOCK.stream()
                .filter(block -> Copycats.MODID.equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace()))
                .filter(block -> block.asItem() != Items.AIR)
                .filter(block -> !BuiltInRegistries.BLOCK.getKey(block).getPath().startsWith("wrapped"))
                .sorted(Comparator.comparing(block -> BuiltInRegistries.BLOCK.getKey(block).toString()))
                .toList();

        Direction forward = player.getDirection();
        Direction right = forward.getClockWise();
        BlockPos start = player.blockPosition().relative(forward, 5).relative(right, -10);
        BlockPos storage = start.relative(right, -3);
        int elevation = 0;
        while (elevation <= 32 && !isClear(level, start.above(elevation), storage.above(elevation), forward, right, blocks)) {
            elevation++;
        }
        if (elevation > 32) {
            player.sendSystemMessage(Component.literal("No clear gallery space found within 32 blocks above you. Move to an open area."));
            return 0;
        }

        start = start.above(elevation);
        storage = storage.above(elevation);
        int placed = 0;
        List<String> failed = new ArrayList<>();
        for (int index = 0; index < blocks.size(); index++) {
            Block block = blocks.get(index);
            BlockPos pos = position(start, forward, right, index);
            addSupport(level, pos);
            BlockState state = block.defaultBlockState();
            if (block instanceof ButtonBlock) {
                state = state.setValue(ButtonBlock.FACE, AttachFace.FLOOR);
            }
            if (block instanceof LadderBlock) {
                state = state.setValue(LadderBlock.FACING, forward.getOpposite());
                BlockPos backing = pos.relative(forward);
                if (level.isEmptyBlock(backing)) {
                    level.setBlock(backing, Blocks.SMOOTH_STONE.defaultBlockState(), 3);
                }
            }
            if (block instanceof DoorBlock) {
                level.setBlock(pos, state.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER), 3);
                level.setBlock(pos.above(), state.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 3);
            } else {
                level.setBlock(pos, state, 3);
            }
            boolean complete = level.getBlockState(pos).is(block)
                    && (!(block instanceof DoorBlock) || level.getBlockState(pos.above()).is(block));
            if (complete) {
                placed++;
            } else {
                failed.add(BuiltInRegistries.BLOCK.getKey(block).getPath());
            }
        }

        addSupport(level, storage);
        level.setBlock(storage, Blocks.BARREL.defaultBlockState(), 3);
        if (level.getBlockEntity(storage) instanceof Container container) {
            container.setItem(0, new ItemStack(CCItems.COPYCAT_BOX.get()));
            container.setItem(1, new ItemStack(CCItems.COPYCAT_CATWALK.get()));
            container.setItem(2, new ItemStack(Blocks.GRASS_BLOCK));
            container.setItem(3, new ItemStack(Blocks.STONE));
            container.setItem(4, new ItemStack(Blocks.GLASS));
            container.setChanged();
        }
        player.sendSystemMessage(Component.literal("Placed " + placed + "/" + blocks.size() + " Copycats blocks near "
                + start.toShortString() + ". The barrel holds item-only components and test materials."));
        if (!failed.isEmpty()) {
            player.sendSystemMessage(Component.literal("Could not place: " + String.join(", ", failed)));
        }
        return placed;
    }

    private static boolean isClear(ServerLevel level, BlockPos start, BlockPos storage,
                                   Direction forward, Direction right, List<Block> blocks) {
        if (!clearCell(level, storage)) return false;
        for (int index = 0; index < blocks.size(); index++) {
            BlockPos pos = position(start, forward, right, index);
            if (!clearCell(level, pos)) return false;
            if (blocks.get(index) instanceof LadderBlock && !level.isEmptyBlock(pos.relative(forward))) return false;
        }
        return true;
    }

    private static boolean clearCell(ServerLevel level, BlockPos pos) {
        return level.isEmptyBlock(pos) && level.isEmptyBlock(pos.above());
    }

    private static BlockPos position(BlockPos start, Direction forward, Direction right, int index) {
        return start.relative(right, index % COLUMNS * COLUMN_SPACING)
                .relative(forward, index / COLUMNS * ROW_SPACING);
    }

    private static void addSupport(ServerLevel level, BlockPos pos) {
        if (level.isEmptyBlock(pos.below())) {
            level.setBlock(pos.below(), Blocks.SMOOTH_STONE.defaultBlockState(), 3);
        }
    }
}
