package com.copycatsplus.copycats.compat.render;

import com.zurrtum.create.client.foundation.model.BakedModelHelper;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/** Resolves Create Fly's normal setter once, supporting both sides of the 26.2 build-2 change. */
public final class CreateFlyNormals {
    private static final Logger LOGGER = LoggerFactory.getLogger("Copycats+");
    private static final MethodHandle SET_NORMAL = findSetter();

    private CreateFlyNormals() {
    }

    private static MethodHandle findSetter() {
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        try {
            MethodHandle setter = lookup.findStatic(BakedModelHelper.class, "setNormals",
                    MethodType.methodType(void.class, BakedQuad.class, Vector3f.class));
            LOGGER.info("Create Fly normals compatibility: single normal API");
            return setter;
        } catch (NoSuchMethodException legacyApi) {
            try {
                MethodHandle setter = lookup.findStatic(BakedModelHelper.class, "setNormals",
                        MethodType.methodType(void.class, BakedQuad.class, Vector3f[].class));
                // Copycats already gives all four vertices the same computed face normal.
                // Expand that normal only on older Create Fly builds that require an array.
                MethodHandle repeat = MethodHandles.lookup().findStatic(CreateFlyNormals.class, "repeatNormal",
                        MethodType.methodType(Vector3f[].class, Vector3f.class));
                LOGGER.info("Create Fly normals compatibility: legacy four normals API");
                return MethodHandles.filterArguments(setter, 1, repeat);
            } catch (NoSuchMethodException | IllegalAccessException error) {
                error.addSuppressed(legacyApi);
                throw new ExceptionInInitializerError(error);
            }
        } catch (IllegalAccessException error) {
            throw new ExceptionInInitializerError(error);
        }
    }

    private static Vector3f[] repeatNormal(Vector3f normal) {
        return new Vector3f[]{normal, normal, normal, normal};
    }

    public static void setNormal(BakedQuad quad, Vector3f normal) {
        try {
            SET_NORMAL.invokeExact(quad, normal);
        } catch (RuntimeException | Error error) {
            throw error;
        } catch (Throwable error) {
            throw new IllegalStateException("Unable to assign Copycats quad normals", error);
        }
    }
}
