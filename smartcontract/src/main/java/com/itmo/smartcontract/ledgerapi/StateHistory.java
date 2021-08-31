package com.itmo.smartcontract.ledgerapi;

import com.itmo.smartcontract.entity.equipment.Equipment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
public class StateHistory {
    private String txId;
    private Equipment state;
    private Instant timestamp;

    @Override
    public String toString() {
        return "StateHistory{" +
                "txId='" + txId + '\'' +
                ", state=" + state +
                ", timestamp=" + timestamp +
                '}';
    }
}
