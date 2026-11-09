package com.copycatsplus.copycats.utility;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionfc;

/** Pose stack rotation API for Minecraft 26.3. */
public final class CopycatPoseStackCompat {
    private CopycatPoseStackCompat() {
    }

    public static void rotate(PoseStack poseStack, Quaternionfc rotation) {
        poseStack.rotate(rotation);
    }
}
