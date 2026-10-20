package com.copycatsplus.copycats;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCores;
import com.copycatsplus.copycats.foundation.copycat.model.CreateFlyCopycatModel;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.utility.LogicalSidedProvider;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.client.AllModels;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class CopycatsClient {
    private static final ThreadLocal<CopycatsClient.AnimatedTintContext> ANIMATED_TINT_CONTEXT = new ThreadLocal<>();

    public static void init() {
        LogicalSidedProvider.setClient(Minecraft::getInstance);
        CCBlocks.getAllRegisteredBlocksWithoutWrapped()
            .forEach(
                entry -> AllModels.register(entry.get(), (state, unbaked) -> new CreateFlyCopycatModel(state, unbaked, CopycatModelCores.create(entry.get())))
            );
        // NeoForge: block entity renderers and tints are registered from CopycatsNeoForgeClient
    }

    public static void registerBlockTints(RegisterColorHandlersEvent.BlockTintSources event) {
        BlockColors blockColors = event.getBlockColors();
        List<BlockTintSource> tintSources = List.of(
            new CopycatsClient.WrappedCopycatTint(blockColors, 0),
            new CopycatsClient.WrappedCopycatTint(blockColors, 1),
            new CopycatsClient.WrappedCopycatTint(blockColors, 2)
        );
        Block[] blocks = CCBlocks.getAllRegisteredBlocksWithoutWrapped().stream().map(entry -> entry.get()).toArray(Block[]::new);
        event.register(tintSources, blocks);
    }

    private static BlockState getTintMaterial(BlockAndTintGetter level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof IMultiStateCopycatBlockEntity multiState
            ? multiState.getMaterialItemStorage()
                .getAllMaterials()
                .stream()
                .filter(material -> !material.is(AllBlocks.COPYCAT_BASE))
                .findFirst()
                .orElseGet(multiState::getMaterial)
            : ICopycatBlock.getMaterial(level, pos);
    }

    public static <T> T withAnimatedTint(BlockState material, BlockAndTintGetter level, BlockPos pos, Supplier<T> action) {
        ANIMATED_TINT_CONTEXT.set(new CopycatsClient.AnimatedTintContext(CreateFlyCopycatModel.resolveMaterialForRender(level, pos, material), level, pos));

        Object var4;
        try {
            var4 = action.get();
        } finally {
            ANIMATED_TINT_CONTEXT.remove();
        }

        return (T)var4;
    }

    public static void invalidateCaches() {
    }

    private record AnimatedTintContext(BlockState material, BlockAndTintGetter level, BlockPos pos) {
    }

    private record WrappedCopycatTint(BlockColors blockColors, int tintIndex) implements BlockTintSource {
        @Override
        public int color(BlockState state) {
            return GrassColor.getDefaultColor();
        }

        @Override
        public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
            CopycatsClient.AnimatedTintContext context = CopycatsClient.ANIMATED_TINT_CONTEXT.get();
            BlockState material = context == null ? CopycatsClient.getTintMaterial(level, pos) : context.material();
            BlockAndTintGetter tintLevel = context == null ? level : context.level();
            BlockPos tintPos = context == null ? pos : context.pos();
            material = CreateFlyCopycatModel.resolveMaterialForRender(tintLevel, tintPos, material);
            List<BlockTintSource> materialTints = this.blockColors.getTintSources(material);
            return materialTints.size() <= this.tintIndex ? -1 : materialTints.get(this.tintIndex).colorInWorld(material, tintLevel, tintPos);
        }

        @Override
        public int colorAsTerrainParticle(BlockState state, BlockAndTintGetter level, BlockPos pos) {
            CopycatsClient.AnimatedTintContext context = CopycatsClient.ANIMATED_TINT_CONTEXT.get();
            BlockState material = context == null ? CopycatsClient.getTintMaterial(level, pos) : context.material();
            BlockAndTintGetter tintLevel = context == null ? level : context.level();
            BlockPos tintPos = context == null ? pos : context.pos();
            material = CreateFlyCopycatModel.resolveMaterialForRender(tintLevel, tintPos, material);
            List<BlockTintSource> materialTints = this.blockColors.getTintSources(material);
            return materialTints.size() <= this.tintIndex ? -1 : materialTints.get(this.tintIndex).colorAsTerrainParticle(material, tintLevel, tintPos);
        }
    }
}
