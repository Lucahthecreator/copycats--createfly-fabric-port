/*
 * Decompiled with CFR 0.152.
 */
package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableUV;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVec3;

public class MutableVertex {
    public MutableVec3 xyz;
    public MutableUV uv;

    public MutableVertex(MutableVec3 xyz, MutableUV uv) {
        this.set(xyz, uv);
    }

    public MutableVertex set(MutableVec3 xyz, MutableUV uv) {
        this.xyz = xyz;
        this.uv = uv;
        return this;
    }
}

