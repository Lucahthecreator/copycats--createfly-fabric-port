package com.copycatsplus.copycats.content.copycat.base;

/**
 * An interface to identify a copycats type.
 *
 * @deprecated To identify copycats, check that the block class is an instanceof {@link ICopycatBlock}. To identify multi-state copycats, check that the block class is an instanceof {@link com.copycatsplus.copycats.content.copycat.base.multistate.IMultiStateCopycatBlock}.
 */
@Deprecated(since = "1.4")
public interface IStateType {

    /*
     * Used to identify a copycats type.
     */
    default StateType stateType() {
        return StateType.SINGULAR;
    }
}
