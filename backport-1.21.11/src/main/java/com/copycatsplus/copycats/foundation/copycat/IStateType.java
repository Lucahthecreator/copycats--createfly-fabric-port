/*
 * Decompiled with CFR 0.152.
 */
package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.StateType;

@Deprecated(since="1.4")
public interface IStateType {
    default public StateType stateType() {
        return StateType.SINGULAR;
    }
}

