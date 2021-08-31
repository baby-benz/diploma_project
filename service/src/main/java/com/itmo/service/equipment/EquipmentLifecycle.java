package com.itmo.service.equipment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EquipmentLifecycle {
    private LocalDate manufactureDate;
    private LocalDate commissioningDate;
    private LocalDate EOLDate;
}
