package com.copycatsplus.copycats.compat.render;

import com.zurrtum.create.client.foundation.model.BakedModelHelper;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import org.joml.Vector3f;

import java.util.Arrays;

/** Checks the adapter against the real selected Create Fly JAR without requiring a game window. */
public final class CreateFlyNormalsLinkageCheck {
    public static void main(String[] args) throws Exception {
        String api;
        try {
            BakedModelHelper.class.getMethod("setNormals", BakedQuad.class, Vector3f.class);
            api = "single normal";
        } catch (NoSuchMethodException legacy) {
            BakedModelHelper.class.getMethod("setNormals", BakedQuad.class, Vector3f[].class);
            api = "legacy four normals";
        }
        // A null quad avoids requiring Fabric's runtime BakedQuad mixin. We expect Create's
        // setter itself to reject it, proving the adapter resolves and invokes the real API.
        try {
            CreateFlyNormals.setNormal(null, new Vector3f(0, 1, 0));
            throw new AssertionError("Create accepted a null quad unexpectedly");
        } catch (NullPointerException expected) {
            boolean reachedCreate = Arrays.stream(expected.getStackTrace()).anyMatch(frame ->
                    frame.getClassName().equals(BakedModelHelper.class.getName())
                            && frame.getMethodName().equals("setNormals"));
            if (!reachedCreate) {
                throw new AssertionError("Adapter failed before reaching Create's normal setter", expected);
            }
        }
        System.out.println("PASS: Copycats normal adapter links and invokes Create Fly's " + api + " API");
    }
}
