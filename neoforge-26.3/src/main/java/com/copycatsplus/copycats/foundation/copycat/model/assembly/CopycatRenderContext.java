package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadManualCull;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadRotate;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadScale;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadShear;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadSlope;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadTranslate;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadUVRotate;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadUVScale;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadUVTranslate;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadUVUpdate;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@ParametersAreNonnullByDefault
public interface CopycatRenderContext {
    void assemblePiece(@NotNull AssemblyTransform var1, MutableVec3 var2, MutableAABB var3, MutableCullFace var4);

    void assemblePiece(@NotNull AssemblyTransform var1, MutableVec3 var2, MutableAABB var3, MutableCullFace var4, QuadTransform... var5);

    void assembleAll();

    @Internal
    void assembleRaw(AABB var1, Vec3 var2);

    @Internal
    void assembleRaw(AABB var1, Vec3 var2, QuadTransform... var3);

    static MutableCullFace cull(int mask) {
        return new MutableCullFace(mask);
    }

    static MutableVec3 vec3(double x, double y, double z) {
        return new MutableVec3(x / 16.0, y / 16.0, z / 16.0);
    }

    static MutableVec3.AsPivot pivot(double x, double y, double z) {
        return new MutableVec3.AsPivot(x / 16.0, y / 16.0, z / 16.0);
    }

    static MutableVec3.AsAngle angle(double x, double y, double z) {
        return new MutableVec3.AsAngle(x, y, z);
    }

    static MutableVec3.AsScale scale(double x, double y, double z) {
        return new MutableVec3.AsScale(x, y, z);
    }

    static MutableAABB aabb(double sizeX, double sizeY, double sizeZ) {
        return new MutableAABB(sizeX / 16.0, sizeY / 16.0, sizeZ / 16.0);
    }

    static QuadRotate rotate(MutableVec3.AsPivot pivot, MutableVec3.AsAngle rot) {
        return new QuadRotate(pivot, rot);
    }

    static QuadScale scale(MutableVec3.AsPivot pivot, MutableVec3.AsScale scale) {
        return new QuadScale(pivot, scale);
    }

    static QuadTranslate translate(double x, double y, double z) {
        return new QuadTranslate(x / 16.0, y / 16.0, z / 16.0);
    }

    static QuadSlope slope(Direction face, QuadSlope.QuadSlopeFunction func) {
        return new QuadSlope(face, (a, b) -> func.apply(a * 16.0, b * 16.0) / 16.0);
    }

    static QuadShear shear(Axis axis, Direction direction, double amount) {
        return new QuadShear(axis, direction, amount / 16.0);
    }

    static QuadUVUpdate updateUV(QuadTransform... transforms) {
        return new QuadUVUpdate(transforms);
    }

    static QuadManualCull manualCull(QuadManualCull.CullFaceMapper mapper) {
        return new QuadManualCull(mapper);
    }

    static QuadManualCull manualCull(Direction face1, @Nullable Direction cull1) {
        return new QuadManualCull((lightFace, cullFace) -> face1 == lightFace ? cull1 : cullFace);
    }

    static QuadManualCull manualCull(Direction face1, @Nullable Direction cull1, Direction face2, @Nullable Direction cull2) {
        return new QuadManualCull((lightFace, cullFace) -> {
            if (face1 == lightFace) {
                return cull1;
            } else {
                return face2 == lightFace ? cull2 : cullFace;
            }
        });
    }

    static QuadManualCull manualCull(
        Direction face1, @Nullable Direction cull1, Direction face2, @Nullable Direction cull2, Direction face3, @Nullable Direction cull3
    ) {
        return new QuadManualCull((lightFace, cullFace) -> {
            if (face1 == lightFace) {
                return cull1;
            } else if (face2 == lightFace) {
                return cull2;
            } else {
                return face3 == lightFace ? cull3 : cullFace;
            }
        });
    }

    static QuadManualCull manualCull(
        Direction face1,
        @Nullable Direction cull1,
        Direction face2,
        @Nullable Direction cull2,
        Direction face3,
        @Nullable Direction cull3,
        Direction face4,
        @Nullable Direction cull4
    ) {
        return new QuadManualCull((lightFace, cullFace) -> {
            if (face1 == lightFace) {
                return cull1;
            } else if (face2 == lightFace) {
                return cull2;
            } else if (face3 == lightFace) {
                return cull3;
            } else {
                return face4 == lightFace ? cull4 : cullFace;
            }
        });
    }

    static QuadManualCull manualCull(
        Direction face1,
        @Nullable Direction cull1,
        Direction face2,
        @Nullable Direction cull2,
        Direction face3,
        @Nullable Direction cull3,
        Direction face4,
        @Nullable Direction cull4,
        Direction face5,
        @Nullable Direction cull5
    ) {
        return new QuadManualCull((lightFace, cullFace) -> {
            if (face1 == lightFace) {
                return cull1;
            } else if (face2 == lightFace) {
                return cull2;
            } else if (face3 == lightFace) {
                return cull3;
            } else if (face4 == lightFace) {
                return cull4;
            } else {
                return face5 == lightFace ? cull5 : cullFace;
            }
        });
    }

    static QuadManualCull manualCull(
        Direction face1,
        @Nullable Direction cull1,
        Direction face2,
        @Nullable Direction cull2,
        Direction face3,
        @Nullable Direction cull3,
        Direction face4,
        @Nullable Direction cull4,
        Direction face5,
        @Nullable Direction cull5,
        Direction face6,
        @Nullable Direction cull6
    ) {
        return new QuadManualCull((lightFace, cullFace) -> {
            if (face1 == lightFace) {
                return cull1;
            } else if (face2 == lightFace) {
                return cull2;
            } else if (face3 == lightFace) {
                return cull3;
            } else if (face4 == lightFace) {
                return cull4;
            } else if (face5 == lightFace) {
                return cull5;
            } else {
                return face6 == lightFace ? cull6 : cullFace;
            }
        });
    }

    static QuadManualCull noCull() {
        return manualCull((lightFace, cullFace) -> null);
    }

    static QuadAutoCull autoCull() {
        return QuadAutoCull.BLOCK;
    }

    static QuadAutoCull autoCull(MutableAABB cullingBox) {
        return new QuadAutoCull(cullingBox);
    }

    static QuadUVRotate uvRotate(Direction face, float pivotU, float pivotV, float rotation) {
        return new QuadUVRotate(face, pivotU, pivotV, rotation);
    }

    static QuadUVTranslate uvTranslate(Direction face, float offsetU, float offsetV) {
        return new QuadUVTranslate(face, offsetU, offsetV);
    }

    static QuadUVScale uvScale(Direction face, float pivotU, float pivotV, float scaleU, float scaleV) {
        return new QuadUVScale(face, pivotU, pivotV, scaleU, scaleV);
    }

    @Internal
    public abstract static class Base<Source, Destination> implements CopycatRenderContext {
        private final Source source;
        private final Destination destination;
        private final String key;

        public Base(Source source, Destination destination, String key) {
            this.source = source;
            this.destination = destination;
            this.key = key;
        }

        public Source source() {
            return this.source;
        }

        public Destination destination() {
            return this.destination;
        }

        public String key() {
            return this.key;
        }
    }
}
