package com.itmo.smartcontract;

import com.itmo.smartcontract.equipment.Equipment;
import com.itmo.smartcontract.ledgerapi.StateList;
import org.hyperledger.fabric.contract.Context;

public class EquipmentList {
    private final StateList stateList;

    public EquipmentList(Context ctx) {
        this.stateList = StateList.getStateList(ctx, EquipmentList.class.getSimpleName(), Equipment::deserialize);
    }

    public EquipmentList addEquipment(Equipment equipment) {
        stateList.addState(equipment);
        return this;
    }

    public Equipment getEquipment(String equipmentKey) {
        return (Equipment) this.stateList.getState(equipmentKey);
    }

    public EquipmentList updateEquipment(Equipment equipment) {
        this.stateList.updateState(equipment);
        return this;
    }
}
