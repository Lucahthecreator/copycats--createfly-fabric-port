package com.copycatsplus.copycats.fabric.mixin.copycat.base.multistate;

import com.copycatsplus.copycats.content.copycat.base.ICopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.simibubi.create.AllBlocks;
import io.github.fabricators_of_create.porting_lib.block.*;
import io.github.fabricators_of_create.porting_lib.enchant.EnchantmentBonusBlock;
import net.fabricmc.fabric.api.block.BlockPickInteractionAware;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(MultiStateCopycatBlock.class)
public abstract class MultiStateCopycatBlockMixin extends Block implements IMultiStateCopycatBlock,
        CustomFrictionBlock, CustomSoundTypeBlock, LightEmissiveBlock, ExplosionResistanceBlock,
        BlockPickInteractionAware, CustomLandingEffectsBlock, CustomRunningEffectsBlock, EnchantmentBonusBlock,
        ValidSpawnBlock {

    public MultiStateCopycatBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return ICopycatBlock.getMaterial(level, pos).getSoundType();
    }

    @Override
    public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        if (state.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
            AtomicReference<Float> bonus = new AtomicReference<>(0f);
            AtomicInteger count = new AtomicInteger(0);
            copycatBlock.withBlockEntityDo(level, pos, copycatBE -> copycatBE.getMaterialItemStorage()
                    .getAllMaterials().forEach(mat -> {
                        count.getAndIncrement();
                        bonus.accumulateAndGet(maybeMaterialAs(level, pos, CustomFrictionBlock.class,
                                mat, (material, frictionBlock) -> frictionBlock.getFriction(material, level, pos, entity),
                                (material) -> material.is(Blocks.AIR)
                                        ? state.getBlock().getFriction()
                                        : material.getBlock().getFriction()), Float::sum);
                    }));
            return bonus.get() / count.get();
        }
        return state.getBlock().getFriction();
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
            AtomicInteger light = new AtomicInteger(0);
            copycatBlock.withBlockEntityDo(level, pos, copycatBE -> {
                copycatBE.getMaterialItemStorage().getAllMaterials().forEach(bs -> {
                    light.accumulateAndGet(bs.getLightEmission(), Math::max);
                });
            });
            return light.get();
        }
        return 0;
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        if (state.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
            AtomicReference<Float> explosionResistance = new AtomicReference<>(state.getBlock().getExplosionResistance());
            copycatBlock.withBlockEntityDo(level, pos, copycatBE -> {
                copycatBE.getMaterialItemStorage().getAllMaterials().forEach(bs -> {
                    explosionResistance.accumulateAndGet(bs.getBlock().getExplosionResistance(), Math::max);
                });
            });
            return explosionResistance.get();
        }
        return state.getBlock().getExplosionResistance();
    }

    @Override
    public ItemStack getPickedStack(BlockState state, BlockGetter level, BlockPos pos, @Nullable Player player, @Nullable HitResult result) {
        if (state.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
            String property = result == null
                    ? null
                    : copycatBlock.getPropertyFromInteraction(state, level, pos, result.getLocation(), result instanceof BlockHitResult blockHit ? blockHit.getDirection() : Direction.UP, true);
            BlockState material = property == null ? ICopycatBlock.getMaterial(level, pos) : IMultiStateCopycatBlock.getMaterial(level, pos, property);
            if (AllBlocks.COPYCAT_BASE.has(material) || player != null && player.isShiftKeyDown())
                return new ItemStack(copycatBlock);
            return maybeMaterialAs(
                    level, pos, BlockPickInteractionAware.class, material,
                    (mat, block) -> block.getPickedStack(mat, level, pos, player, result),
                    mat -> mat.getBlock().getCloneItemStack(level, pos, mat)
            );
        }
        return new ItemStack(state.getBlock());
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        if (state1.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
            BlockHitResult hitResult = level.clip(new ClipContext(entity.position(), entity.position().add(0, -2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, entity));
            String property = copycatBlock.getPropertyFromInteraction(state1, level, pos, hitResult, true);
            AtomicReference<BlockState> mat = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
            copycatBlock.withBlockEntityDo(level, pos, copycatBE -> mat.set(copycatBE.getMaterialItemStorage().getMaterialItem(property).material()));
            return maybeMaterialAs(level, pos, CustomLandingEffectsBlock.class,
                    mat.get(), (material, frictionBlock) -> frictionBlock.addLandingEffects(material, level, pos, material, entity, numberOfParticles),
                    (material) -> false);
        }
        return false;
    }

    @Override
    public boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (state.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
            BlockHitResult hitResult = level.clip(new ClipContext(entity.position(), entity.position().add(0, -2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, entity));
            String property = copycatBlock.getPropertyFromInteraction(state, level, pos, hitResult, true);
            AtomicReference<BlockState> mat = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
            copycatBlock.withBlockEntityDo(level, pos, copycatBE -> mat.set(copycatBE.getMaterialItemStorage().getMaterialItem(property).material()));
            return maybeMaterialAs(level, pos, CustomRunningEffectsBlock.class,
                    mat.get(), (material, frictionBlock) -> frictionBlock.addRunningEffects(material, level, pos, entity),
                    (material) -> false);
        }
        return false;
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
            AtomicReference<Float> bonus = new AtomicReference<>(0f);
            copycatBlock.withBlockEntityDo(level, pos, copycatBE -> copycatBE.getMaterialItemStorage()
                    .getAllMaterials().forEach(mat -> {
                        bonus.set(bonus.get() + maybeMaterialAs(level, pos, EnchantmentBonusBlock.class,
                                mat, (material, enchantmentBlock) -> enchantmentBlock.getEnchantPowerBonus(material, level, pos),
                                (material) -> 0f));
                    }));
            return bonus.get();
        }
        return 0f;
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type,
                                EntityType<?> entityType) {
        return false;
    }

    @Override
    public void fallOn(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockPos pPos, @NotNull Entity pEntity, float p_152430_) {
        if (pState.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
            BlockHitResult hitResult = pLevel.clip(new ClipContext(pEntity.position(), pEntity.position().add(0, -2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, pEntity));
            String property = copycatBlock.getPropertyFromInteraction(pState, pLevel, pPos, hitResult, true);
            AtomicReference<BlockState> material = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
            copycatBlock.withBlockEntityDo(pLevel, pPos, copycatBE -> material.set(copycatBE.getMaterialItemStorage().getMaterialItem(property).material()));
            material.get().getBlock().fallOn(pLevel, material.get(), pPos, pEntity, p_152430_);
        }
    }

    @Override
    public float getDestroyProgress(@NotNull BlockState pState, @NotNull Player pPlayer, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        if (pState.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
            String property = copycatBlock.getPropertyFromInteraction(pState, pLevel, pPos, new BlockHitResult(Vec3.atCenterOf(pPos), Direction.UP, pPos, true), true);
            AtomicReference<BlockState> material = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
            copycatBlock.withBlockEntityDo(pLevel, pPos, copycatBE -> material.set(copycatBE.getMaterialItemStorage().getMaterialItem(property).material()));
            return material.get().getDestroyProgress(pPlayer, pLevel, pPos);
        }
        return pState.getDestroyProgress(pPlayer, pLevel, pPos);
    }

    @Unique
    private static <T, R> R maybeMaterialAs(BlockGetter level, BlockPos pos, Class<T> clazz, BlockState material,
                                            BiFunction<BlockState, T, R> ifType, Function<BlockState, R> ifNot) {
        Block block = material.getBlock();
        if (clazz.isInstance(block))
            return ifType.apply(material, clazz.cast(block));
        return ifNot.apply(material);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
        return IMultiStateCopycatBlock.getAppearance(this, state, renderView, pos, side, sourceState, sourcePos);
    }
}
