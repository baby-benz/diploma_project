package com.itmo.smartcontract;

import com.itmo.smartcontract.entity.equipment.Equipment;
import com.itmo.smartcontract.ledgerapi.StateHistory;
import com.itmo.smartcontract.ledgerapi.StateList;
import org.hyperledger.fabric.contract.Context;

import java.util.List;

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

    public List<StateHistory> getEquipmentHistory(String equipmentKey) {
        return this.stateList.getStateHistory(equipmentKey);
    }

    public List<Equipment> getAllEquipment() {
        return (List<Equipment>) this.stateList.getAllStates();
    }

    public EquipmentList updateEquipment(Equipment equipment) {
        this.stateList.updateState(equipment);
        return this;
    }

    public void deleteEquipment(String equipmentKey) {
        this.stateList.deleteState(equipmentKey);
    }
}
