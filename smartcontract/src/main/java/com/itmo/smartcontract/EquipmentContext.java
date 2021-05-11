package com.itmo.smartcontract;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

class EquipmentContext extends Context {
    public EquipmentContext(ChaincodeStub stub) {
        super(stub);
        this.equipmentList = new EquipmentList(this);
    }

    public EquipmentList equipmentList;
}
