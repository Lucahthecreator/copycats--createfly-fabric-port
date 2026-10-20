package com.copycatsplus.copycats.foundation.copycat.model.assembly;

@FunctionalInterface
public interface AssemblyTransform {
    AssemblyTransform IDENTITY = t -> {};

    void apply(AssemblyTransform.Transformable<?> var1);

    default AssemblyTransform andThen(AssemblyTransform after) {
        return t -> {
            this.apply(t);
            after.apply(t);
        };
    }

    public interface Transformable<Self extends AssemblyTransform.Transformable<Self>> {
        Self rotateX(int var1);

        Self rotateY(int var1);

        Self rotateZ(int var1);

        Self flipX(boolean var1);

        Self flipY(boolean var1);

        Self flipZ(boolean var1);
    }
}
