package com.itmo.accounting.controller.dto.cc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@AllArgsConstructor
public class EquipmentLifecycle {
    @Getter
    private LocalDate manufactureDate;
    @Getter
    private LocalDate commissioningDate;
}
