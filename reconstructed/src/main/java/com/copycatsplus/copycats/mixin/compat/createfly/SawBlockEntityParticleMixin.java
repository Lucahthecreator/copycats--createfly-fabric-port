/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.math.VecHelper
 *  com.zurrtum.create.content.kinetics.saw.SawBlockEntity
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.particles.ItemParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.ItemStackTemplate
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jspecify.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.copycatsplus.copycats.mixin.compat.createfly;

import com.zurrtum.create.catnip.math.VecHelper;
import com.zurrtum.create.content.kinetics.saw.SawBlockEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={SawBlockEntity.class})
public class SawBlockEntityParticleMixin {
    @Inject(method={"spawnEventParticles"}, at={@At(value="HEAD")}, cancellable=true)
    private void copycats$spawnBlockItemEventParticles(@Nullable ItemStack stack, CallbackInfo ci) {
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof BlockItem)) {
            return;
        }
        SawBlockEntity saw = (SawBlockEntity)this;
        Level level = saw.getLevel();
        if (level == null) {
            return;
        }
        ItemParticleOption particle = SawBlockEntityParticleMixin.copycats$particleFor(stack);
        RandomSource random = level.getRandom();
        Vec3 center = VecHelper.getCenterOf((Vec3i)saw.getBlockPos()).add(0.0, 0.3125, 0.0);
        for (int i = 0; i < 10; ++i) {
            Vec3 motion = VecHelper.offsetRandomly((Vec3)new Vec3(0.0, 0.25, 0.0), (RandomSource)random, (float)0.125f);
            level.addParticle((ParticleOptions)particle, center.x, center.y, center.z, motion.x, motion.y, motion.z);
        }
        ci.cancel();
    }

    @Inject(method={"spawnParticles"}, at={@At(value="HEAD")}, cancellable=true)
    private void copycats$spawnBlockItemParticles(@Nullable ItemStack stack, CallbackInfo ci) {
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof BlockItem)) {
            return;
        }
        SawBlockEntity saw = (SawBlockEntity)this;
        Level level = saw.getLevel();
        if (level == null) {
            return;
        }
        Vec3 movement = saw.getItemMovementVec();
        Vec3 center = VecHelper.getCenterOf((Vec3i)saw.getBlockPos());
        float offset = saw.inventory.recipeDuration != 0.0f ? saw.inventory.remainingTime / saw.inventory.recipeDuration : 0.0f;
        offset /= 2.0f;
        if (saw.inventory.appliedRecipe) {
            offset -= 0.5f;
        }
        float speed = 0.125f;
        level.addParticle((ParticleOptions)SawBlockEntityParticleMixin.copycats$particleFor(stack), center.x - movement.x * (double)offset, center.y + (double)0.45f, center.z - movement.z * (double)offset, -movement.x * (double)speed, (double)(level.getRandom().nextFloat() * speed), -movement.z * (double)speed);
        ci.cancel();
    }

    private static ItemParticleOption copycats$particleFor(ItemStack stack) {
        return new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack((ItemStack)stack));
    }
}

