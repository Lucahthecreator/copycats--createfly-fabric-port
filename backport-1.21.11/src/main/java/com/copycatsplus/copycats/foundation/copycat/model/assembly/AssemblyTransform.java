/*
 * Decompiled with CFR 0.152.
 */
package com.copycatsplus.copycats.foundation.copycat.model.assembly;

@FunctionalInterface
public interface AssemblyTransform {
    public static final AssemblyTransform IDENTITY = t -> {};

    public void apply(Transformable<?> var1);

    default public AssemblyTransform andThen(AssemblyTransform after) {
        return t -> {
            this.apply(t);
            after.apply(t);
        };
    }

    public static interface Transformable<Self extends Transformable<Self>> {
        public Self rotateX(int var1);

        public Self rotateY(int var1);

        public Self rotateZ(int var1);

        public Self flipX(boolean var1);

        public Self flipY(boolean var1);

        public Self flipZ(boolean var1);
    }
}

