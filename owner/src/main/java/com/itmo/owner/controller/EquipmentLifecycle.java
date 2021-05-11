package com.itmo.owner.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EquipmentLifecycle {
    private Date manufactureDate;
    private Date commissioningDate;
    private Date EOLDate;
}
