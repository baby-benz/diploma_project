package com.itmo.owner.controller.dto.cc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentLifecycle {
    private LocalDate manufactureDate;
    private LocalDate commissioningDate;

    @Override
    public String toString() {
        return "EquipmentLifecycle{" +
                "manufactureDate=" + manufactureDate +
                ", commissioningDate=" + commissioningDate +
                '}';
    }
}
