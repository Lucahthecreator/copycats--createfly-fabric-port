/*
 * Decompiled with CFR 0.152.
 */
package com.copycatsplus.copycats.foundation.copycat.model.assembly;

public class MutableUV {
    public float u;
    public float v;

    public MutableUV(float u, float v) {
        this.set(u, v);
    }

    public MutableUV set(float u, float v) {
        this.u = u;
        this.v = v;
        return this;
    }

    public boolean isZero() {
        return this.u == 0.0f && this.v == 0.0f;
    }
}

