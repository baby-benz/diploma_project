package com.itmo.service.equipment;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class EquipmentHistoryWithDocument extends EquipmentHistory {
    private String document;

    public EquipmentHistoryWithDocument(String txId, Equipment equipment, Instant timestamp, String document) {
        super(txId, equipment, timestamp);
        this.document = document;
    }
}
