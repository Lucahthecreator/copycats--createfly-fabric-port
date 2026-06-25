package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.CopycatExternalContext;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.content.copycat.door.CopycatDoorBlock;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlock;
import com.copycatsplus.copycats.compat.debug.CopycatsDebug;
import com.copycatsplus.copycats.compat.render.connectivity.CopycatConnectivity;
import com.copycatsplus.copycats.compat.render.CopycatRenderMaterial;
import com.copycatsplus.copycats.foundation.copycat.model.FilteredBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.zurrtum.create.client.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Teaches CreateFly connected textures how copycat blocks block and connect.
 */
@Mixin(value = ConnectedTextureBehaviour.class, remap = false)
public class ConnectedTextureBehaviourMixin {
    @Inject(
            at = @At("HEAD"),
            method = "getCTBlockState",
            cancellable = true
    )
    private void copycats$getCTBlockState(BlockAndTintGetter reader, BlockState reference, Direction face, BlockPos fromPos, BlockPos toPos, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = reader.getBlockState(toPos);
        BlockState fromState = reader.getBlockState(fromPos);
        boolean fromDoor = copycats$isDoor(fromState);
        boolean toDoor = copycats$isDoor(state);
        if (fromState.getBlock() instanceof ICopycatBlock || state.getBlock() instanceof ICopycatBlock) {
            CopycatsDebug.log("ct", () -> "query reader=" + reader.getClass().getName()
                    + " fromPos=" + fromPos + " from=" + fromState
                    + " toPos=" + toPos + " to=" + state
                    + " face=" + face + " reference=" + reference);
        }
        if ((fromDoor || toDoor) && fromState.getBlock() != state.getBlock()) {
            BlockState rejected = toDoor ? state : fromState;
            CopycatsDebug.log("door", () -> "door CT rejected from=" + fromPos + " " + fromState
                    + " to=" + toPos + " " + state + " face=" + face);
            cir.setReturnValue(rejected);
            return;
        }
        if (state.getBlock() instanceof ICopycatBlock copycatBlock) {
            boolean ctEnabled = CopycatRenderMaterial.isCTEnabled(reader, toPos, state)
                    && (!(fromState.getBlock() instanceof ICopycatBlock)
                    || CopycatRenderMaterial.isCTEnabled(reader, fromPos, fromState));
            boolean ignored = copycatBlock.isIgnoredConnectivitySide(reader, state, face, toPos, fromPos, fromState);
            boolean familyOverride = CopycatConnectivity.canConnectForCT(reader, fromPos, toPos, fromState, state, reference, face);
            boolean connect = ctEnabled && (!ignored || familyOverride);
            BlockState resolved = connect
                    ? CopycatRenderMaterial.resolveForConnectedTexture(reader, fromPos, toPos, fromState, state, reference, face)
                    : state;
            String debugCategory = fromDoor || toDoor ? "door" : "ct";
            CopycatsDebug.log(debugCategory, () -> "reader=" + reader.getClass().getSimpleName()
                    + " fromPos=" + fromPos + " from=" + fromState
                    + " toPos=" + toPos + " to=" + state
                    + " face=" + face + " reference=" + reference + " resolved=" + resolved
                    + " enabled=" + ctEnabled + " ignored=" + ignored
                    + " familyOverride=" + familyOverride
                    + " connect=" + connect);
            cir.setReturnValue(resolved);
        }
    }

    private static boolean copycats$isDoor(BlockState state) {
        return state.getBlock() instanceof CopycatDoorBlock
                || state.getBlock() instanceof CopycatSlidingDoorBlock;
    }

    @Inject(
            at = @At("HEAD"),
            method = "isBeingBlocked",
            cancellable = true
    )
    private void copycats$isCopycatBlockable(BlockState state, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face, CallbackInfoReturnable<Boolean> cir) {
        BlockAndTintGetter originalReader = reader;
        if (reader instanceof FilteredBlockAndTintGetter accessor) {
            reader = accessor.wrapped;
        }
        if (reader instanceof ScaledBlockAndTintGetter accessor) {
            reader = accessor.getWrapped();
        }

        BlockAndTintGetter unwrappedReader = reader;
        BlockState selfState = reader.getBlockState(pos);
        BlockPos blockingPos = otherPos.relative(face);
        CopycatsDebug.log("blocking", () -> "check stateArg=" + state
                + " originalReader=" + originalReader.getClass().getName()
                + " unwrappedReader=" + unwrappedReader.getClass().getName()
                + " pos=" + pos + " self=" + selfState
                + " otherPos=" + otherPos + " other=" + unwrappedReader.getBlockState(otherPos)
                + " blockingPos=" + blockingPos + " blocking=" + unwrappedReader.getBlockState(blockingPos)
                + " face=" + face);
        if (selfState.getBlock() instanceof ICustomCTBlocking customBlocking) {
            Optional<Boolean> blocked = customBlocking.isCTBlocked(reader, selfState, pos, otherPos, blockingPos, face);
            if (blocked.isPresent()) {
                CopycatsDebug.log("blocking", () -> "self custom result pos=" + pos
                        + " otherPos=" + otherPos + " blockingPos=" + blockingPos
                        + " face=" + face + " blocked=" + blocked.get());
                cir.setReturnValue(blocked.get());
                return;
            }
            CopycatsDebug.log("blocking", () -> "self custom empty pos=" + pos
                    + " otherPos=" + otherPos + " blockingPos=" + blockingPos
                    + " face=" + face);
        }

        BlockState blockingState = reader.getBlockState(blockingPos);
        if (blockingState.getBlock() instanceof ICustomCTBlocking customBlocker) {
            Optional<Boolean> blocking = customBlocker.blockCTTowards(reader, blockingState, blockingPos, pos, otherPos, face.getOpposite());
            CopycatsDebug.log("blocking", () -> "blocker custom result pos=" + pos
                    + " otherPos=" + otherPos + " blockingPos=" + blockingPos
                    + " blockingState=" + blockingState + " face=" + face
                    + " result=" + blocking);
            blocking.ifPresent(cir::setReturnValue);
        } else {
            CopycatsDebug.log("blocking", () -> "vanilla blocking fallback pos=" + pos
                    + " otherPos=" + otherPos + " blockingPos=" + blockingPos
                    + " blockingState=" + blockingState + " face=" + face);
        }
    }

    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;isFaceFull(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/core/Direction;)Z", remap = true),
            method = "isBeingBlocked"
    )
    private boolean copycats$isFaceFull(VoxelShape shape, Direction face, Operation<Boolean> original, BlockState state, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face2) {
        BlockPos blockingPos = otherPos.relative(face2);
        BlockState otherState = reader.getBlockState(otherPos);
        BlockState blockingState = reader.getBlockState(blockingPos);
        if (blockingState.getBlock() instanceof ICopycatBlock) {
            boolean result = BlockFaceUtils.faceMatch(reader, otherState, otherPos, blockingState, blockingPos, face2);
            CopycatsDebug.log("blocking", () -> "copycat faceMatch otherPos=" + otherPos
                    + " other=" + otherState + " blockingPos=" + blockingPos
                    + " blocking=" + blockingState + " face=" + face2
                    + " result=" + result);
            return result;
        }
        boolean result = original.call(shape, face);
        CopycatsDebug.log("blocking", () -> "vanilla isFaceFull blockingPos=" + blockingPos
                + " blocking=" + blockingState + " face=" + face
                + " methodFace=" + face2 + " result=" + result);
        return result;
    }

    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lcom/zurrtum/create/client/foundation/block/connected/ConnectedTextureBehaviour;getCTBlockState(Lnet/minecraft/client/renderer/block/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"),
            method = "isBeingBlocked"
    )
    private BlockState copycats$getAppearanceForBlockingLogic(ConnectedTextureBehaviour instance, BlockAndTintGetter reader, BlockState reference, Direction face, BlockPos fromPos, BlockPos toPos, Operation<BlockState> original) {
        CopycatExternalContext.setForBlockingLogic(true);
        BlockState state = original.call(instance, reader, reference, face, fromPos, toPos);
        CopycatExternalContext.setForBlockingLogic(false);
        CopycatsDebug.log("blocking", () -> "blocking appearance fromPos=" + fromPos
                + " toPos=" + toPos + " face=" + face
                + " reference=" + reference + " result=" + state);
        return state;
    }
}
