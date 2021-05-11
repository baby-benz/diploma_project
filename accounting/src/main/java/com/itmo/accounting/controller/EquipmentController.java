package com.itmo.accounting.controller;

import com.itmo.accounting.controller.dto.CreatedEquipmentDto;
import com.itmo.accounting.controller.dto.NewEquipmentDto;
import com.itmo.accounting.controller.dto.EquipmentOrganizationForNewEquipmentDto;
import com.itmo.accounting.domain.entity.Equipment;
import com.itmo.accounting.domain.entity.EquipmentOrganization;
import com.itmo.accounting.service.EquipmentControllerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RequestMapping("/accounting/api/equipment")
@RestController
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentControllerService equipmentControllerService;
    private final ModelMapper modelMapper;

    @PostMapping
    public CreatedEquipmentDto createEquipment(@RequestBody NewEquipmentDto equipment) {
        return modelMapper.map(equipmentControllerService.saveEquipment(modelMapper.map(equipment, Equipment.class)), CreatedEquipmentDto.class);
    }

    @PutMapping("/{id}")
    public CreatedEquipmentDto fullyUpdateEquipment(@PathVariable Long id, @RequestBody NewEquipmentDto equipment) {
        return modelMapper.map(equipmentControllerService.updateEquipment(id, modelMapper.map(equipment, Equipment.class)), CreatedEquipmentDto.class);
    }

    @PatchMapping("/taken-into-account/{id}")
    public CreatedEquipmentDto updateIsTakenIntoAccount(@PathVariable Long id, @RequestParam boolean isTakenIntoAccount) {
        return modelMapper.map(equipmentControllerService.updateIsTakenIntoAccount(id, isTakenIntoAccount), CreatedEquipmentDto.class);
    }

    @PatchMapping("/commissioning-date/{id}")
    public CreatedEquipmentDto updateCommissioningDate(@PathVariable Long id, @RequestParam Date commissioningDate) {
        return modelMapper.map(equipmentControllerService.updateCommissioningDate(id, commissioningDate), CreatedEquipmentDto.class);
    }

    @PatchMapping("/eol-date/{id}")
    public CreatedEquipmentDto updateEOLDate(@PathVariable Long id, @RequestParam Date EOLDate) {
        return modelMapper.map(equipmentControllerService.updateEOLDate(id, EOLDate), CreatedEquipmentDto.class);
    }

    @PatchMapping("/{equipmentId}/{sensorId}")
    public CreatedEquipmentDto updateSensors(@PathVariable Long equipmentId, @PathVariable Long sensorId) {
        return modelMapper.map(equipmentControllerService.updateSensors(equipmentId, sensorId), CreatedEquipmentDto.class);
    }

    @PatchMapping("/organization/{id}")
    public CreatedEquipmentDto updateOrganization(@PathVariable Long id, @RequestParam EquipmentOrganizationForNewEquipmentDto organization) {
        return modelMapper.map(equipmentControllerService.updateOrganizations(id, modelMapper.map(organization, EquipmentOrganization.class)), CreatedEquipmentDto.class);
    }

    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable Long id) {
        equipmentControllerService.deleteEquipment(id);
    }
}
