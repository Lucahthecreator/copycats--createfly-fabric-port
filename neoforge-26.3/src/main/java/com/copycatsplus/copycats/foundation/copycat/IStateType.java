package com.copycatsplus.copycats.foundation.copycat;

@Deprecated(since = "1.4")
public interface IStateType {
    default StateType stateType() {
        return StateType.SINGULAR;
    }
}
