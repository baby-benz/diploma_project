package com.itmo.smartcontract.ledgerapi.impl;

import com.itmo.smartcontract.entity.equipment.Equipment;
import com.itmo.smartcontract.ledgerapi.State;
import com.itmo.smartcontract.ledgerapi.StateDeserializer;
import com.itmo.smartcontract.ledgerapi.StateHistory;
import com.itmo.smartcontract.ledgerapi.StateList;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class StateListImpl implements StateList {
    private final Context ctx;
    private final String name;
    private final StateDeserializer deserializer;

    @Override
    public StateList addState(State state) {
        ChaincodeStub stub = this.ctx.getStub();

        CompositeKey ledgerKey = stub.createCompositeKey(this.name, state.getSplitKey());

        String data = State.GENSON.serialize(state);

        stub.putStringState(ledgerKey.toString(), data);

        return this;
    }

    @Override
    public State getState(String key) {
        ChaincodeStub stub = this.ctx.getStub();

        CompositeKey ledgerKey = stub.createCompositeKey(this.name, State.splitKey(key));

        String data = stub.getStringState(ledgerKey.toString());

        if (data != null) {
            return this.deserializer.deserialize(data);
        } else {
            return null;
        }
    }

    @Override
    public List<? extends State> getAllStates() {
        ChaincodeStub stub = this.ctx.getStub();

        QueryResultsIterator<KeyValue> allStates;

        ArrayList<State> states = new ArrayList<>();

        allStates = stub.getStateByPartialCompositeKey(stub.createCompositeKey(this.name));

        for (KeyValue state : allStates) {
            states.add(this.deserializer.deserialize(state.getStringValue()));
        }

        return states;
    }

    @Override
    public List<StateHistory> getStateHistory(String key) {
        ChaincodeStub stub = this.ctx.getStub();

        CompositeKey ledgerKey = stub.createCompositeKey(this.name, State.splitKey(key));

        QueryResultsIterator<KeyModification> stateHistory;

        List<StateHistory> states = new ArrayList<>();

        stateHistory = stub.getHistoryForKey(ledgerKey.toString());

        stateHistory.forEach((state) -> states.add(
                new StateHistory(
                        state.getTxId(),
                        (Equipment) this.deserializer.deserialize(state.getStringValue()),
                        state.getTimestamp()
                )
        ));

        return states;
    }

    @Override
    public StateList updateState(State state) {
        ChaincodeStub stub = this.ctx.getStub();

        CompositeKey ledgerKey = stub.createCompositeKey(this.name, state.getSplitKey());

        String data = State.GENSON.serialize(state);

        stub.putStringState(ledgerKey.toString(), data);

        return this;
    }

    @Override
    public StateList deleteState(String key) {
        ChaincodeStub stub = this.ctx.getStub();

        CompositeKey ledgerKey = stub.createCompositeKey(this.name, State.splitKey(key));

        stub.delState(ledgerKey.toString());

        return this;
    }
}
