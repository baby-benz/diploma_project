package com.itmo.service.equipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class EquipmentHistory {
    private String txId;
    private Equipment equipment;
    private Instant timestamp;
}
