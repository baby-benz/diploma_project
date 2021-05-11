package com.itmo.smartcontract.ledgerapi.impl;

import com.itmo.smartcontract.ledgerapi.State;
import com.itmo.smartcontract.ledgerapi.StateDeserializer;
import com.itmo.smartcontract.ledgerapi.StateList;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.Logger;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class StateListImpl implements StateList {
    private final static Logger LOG = Logger.getLogger(StateListImpl.class.getName());

    private final Context ctx;
    private final String name;
    private final StateDeserializer deserializer;

    @Override
    public StateList addState(State state) {
        ChaincodeStub stub = this.ctx.getStub();

        CompositeKey ledgerKey = stub.createCompositeKey(this.name, state.getSplitKey());

        byte[] data = State.serialize(state);

        LOG.info("Состояние при записи: " + new String(data, StandardCharsets.UTF_8));

        stub.putState(ledgerKey.toString(), data);

        return this;
    }

    @Override
    public State getState(String key) {
        ChaincodeStub stub = this.ctx.getStub();

        CompositeKey ledgerKey = stub.createCompositeKey(this.name, State.splitKey(key));

        byte[] data = stub.getState(ledgerKey.toString());

        if (data != null) {
            return this.deserializer.deserialize(data);
        } else {
            return null;
        }
    }

    @Override
    public StateList updateState(State state) {
        ChaincodeStub stub = this.ctx.getStub();

        CompositeKey ledgerKey = stub.createCompositeKey(this.name, state.getSplitKey());

        byte[] data = State.serialize(state);

        LOG.info("Состояние при обновлении: " + new String(data, StandardCharsets.UTF_8));

        stub.putState(ledgerKey.toString(), data);

        return this;
    }
}
