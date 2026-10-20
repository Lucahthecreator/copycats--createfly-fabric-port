/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.AllBlocks
 *  com.zurrtum.create.client.AllModels
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
 *  net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.color.block.BlockColors
 *  net.minecraft.client.color.block.BlockTintSource
 *  net.minecraft.world.level.BlockAndTintGetter
 *  net.minecraft.client.renderer.block.model.BlockStateModel$UnbakedRoot
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.GrassColor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.copycatsplus.copycats;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorRenderBridge;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatKineticRenderBridge;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCores;
import com.copycatsplus.copycats.foundation.copycat.model.CreateFlyCopycatModel;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.utility.LogicalSidedProvider;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.client.AllModels;
import java.util.List;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatsClient {
    private static final ThreadLocal<AnimatedTintContext> ANIMATED_TINT_CONTEXT = new ThreadLocal();

    public static void init() {
        LogicalSidedProvider.setClient(Minecraft::getInstance);
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> CopycatsClient.registerBlockTints(client.getBlockColors()));
        CCBlocks.getAllRegisteredBlocksWithoutWrapped().forEach(entry -> AllModels.register((Block)((Block)entry.get()), (state, unbaked) -> new CreateFlyCopycatModel((BlockState)state, (BlockStateModel.UnbakedRoot)unbaked, CopycatModelCores.create((Block)entry.get()))));
        BlockEntityType slidingDoorType = (BlockEntityType)CCBlockEntityTypes.COPYCAT_SLIDING_DOOR.get();
        BlockEntityRendererRegistry.register((BlockEntityType)slidingDoorType, CopycatSlidingDoorRenderBridge::new);
        CopycatsClient.registerKineticRenderer((BlockEntityType)CCBlockEntityTypes.COPYCAT_SHAFT.get());
        CopycatsClient.registerKineticRenderer((BlockEntityType)CCBlockEntityTypes.COPYCAT_COGWHEEL.get());
    }

    private static void registerBlockTints(BlockColors blockColors) {
        Block[] blocks = (Block[])CCBlocks.getAllRegisteredBlocksWithoutWrapped().stream().map(entry -> (Block)entry.get()).toArray(Block[]::new);
        blockColors.register(new WrappedCopycatTint(blockColors), blocks);
    }

    private static BlockState getTintMaterial(BlockAndTintGetter level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof IMultiStateCopycatBlockEntity) {
            IMultiStateCopycatBlockEntity multiState = (IMultiStateCopycatBlockEntity)blockEntity;
            return multiState.getMaterialItemStorage().getAllMaterials().stream().filter(material -> !material.is(AllBlocks.COPYCAT_BASE)).findFirst().orElseGet(multiState::getMaterial);
        }
        return ICopycatBlock.getMaterial((BlockGetter)level, pos);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static <T> T withAnimatedTint(BlockState material, BlockAndTintGetter level, BlockPos pos, Supplier<T> action) {
        ANIMATED_TINT_CONTEXT.set(new AnimatedTintContext(CreateFlyCopycatModel.resolveMaterialForRender(level, pos, material), level, pos));
        try {
            T t = action.get();
            return t;
        }
        finally {
            ANIMATED_TINT_CONTEXT.remove();
        }
    }

    private static void registerKineticRenderer(BlockEntityType<?> type) {
        BlockEntityRendererRegistry.register((BlockEntityType)type, (net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider)(CopycatKineticRenderBridge::new));
    }

    public static void invalidateCaches() {
    }

    private record WrappedCopycatTint(BlockColors blockColors) implements BlockColor
    {
        public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
            if (level == null || pos == null) {
                return GrassColor.getDefaultColor();
            }
            AnimatedTintContext context = ANIMATED_TINT_CONTEXT.get();
            BlockState material = context == null ? CopycatsClient.getTintMaterial(level, pos) : context.material();
            BlockAndTintGetter tintLevel = context == null ? level : context.level();
            BlockPos tintPos = context == null ? pos : context.pos();
            material = CreateFlyCopycatModel.resolveMaterialForRender(tintLevel, tintPos, material);
            return this.blockColors.getColor(material, tintLevel, tintPos, tintIndex);
        }
    }

    private record AnimatedTintContext(BlockState material, BlockAndTintGetter level, BlockPos pos) {
    }
}

