package com.itmo.smartcontract.ledgerapi;

import com.itmo.smartcontract.ledgerapi.impl.StateListImpl;
import org.hyperledger.fabric.contract.Context;

public interface StateList {
    static StateList getStateList(Context ctx, String listName, StateDeserializer deserializer) {
        return new StateListImpl(ctx, listName, deserializer);
    }

    State getState(String key);

    StateList addState(State state);

    StateList updateState(State state);
}
