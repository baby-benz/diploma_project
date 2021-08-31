package com.itmo.inspection.controller;

import com.itmo.inspection.equipment.Equipment;
import com.itmo.inspection.service.InspectionControllerService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/inspection/api")
@RequiredArgsConstructor
public class InspectionController {
    private final InspectionControllerService inspectionControllerService;

    @GetMapping("/equipment")
    public List<Equipment> getAllEquipment() throws AccessException {
        return inspectionControllerService.getAllEquipment();
    }

    @GetMapping("/equipment/history/{id}")
    public List<Equipment> getEquipmentHistory(@PathVariable Long id) throws AccessException, TimeoutException {
        return inspectionControllerService.getEquipmentHistory(id);
    }

    @GetMapping("/equipment/{id}")
    public Equipment getEquipmentCurrentState(@PathVariable Long id) throws AccessException, TimeoutException {
        return inspectionControllerService.getEquipmentCurrentState(id);
    }
}
