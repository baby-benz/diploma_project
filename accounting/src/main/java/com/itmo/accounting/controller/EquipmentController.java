package com.itmo.accounting.controller;

import com.itmo.accounting.controller.dto.CreatedEquipmentDto;
import com.itmo.accounting.controller.dto.NewEquipmentDto;
import com.itmo.accounting.controller.dto.NewEquipmentOrganizationDto;
import com.itmo.accounting.domain.entity.Equipment;
import com.itmo.accounting.domain.entity.EquipmentOrganization;
import com.itmo.accounting.domain.entity.EquipmentType;
import com.itmo.accounting.service.EquipmentControllerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.expression.AccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequestMapping("/accounting/api/equipment")
@RestController
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentControllerService equipmentControllerService;
    private final ModelMapper modelMapper;

    @PostMapping
    public CreatedEquipmentDto createEquipment(@RequestBody NewEquipmentDto equipment) throws AccessException {
        return modelMapper.map(equipmentControllerService.saveEquipment(modelMapper.map(equipment, Equipment.class)), CreatedEquipmentDto.class);
    }

    @PutMapping("/{id}")
    public CreatedEquipmentDto fullyUpdateEquipment(@PathVariable Long id, @RequestBody NewEquipmentDto equipment) {
        return modelMapper.map(equipmentControllerService.updateEquipment(id, modelMapper.map(equipment, Equipment.class)), CreatedEquipmentDto.class);
    }

    @PatchMapping("/equipment-type/{id}")
    public CreatedEquipmentDto updateEquipmentType(@PathVariable Long id, @RequestParam EquipmentType equipmentType) {
        return modelMapper.map(equipmentControllerService.updateEquipmentType(id, equipmentType), CreatedEquipmentDto.class);
    }

    @PatchMapping("/taken-into-account/{id}")
    public CreatedEquipmentDto updateIsTakenIntoAccount(@PathVariable Long id, @RequestParam boolean isTakenIntoAccount) {
        return modelMapper.map(equipmentControllerService.updateIsTakenIntoAccount(id, isTakenIntoAccount), CreatedEquipmentDto.class);
    }

    @PatchMapping("/commissioning-date/{id}")
    public CreatedEquipmentDto updateCommissioningDate(@PathVariable Long id, @RequestParam LocalDate commissioningDate) {
        return modelMapper.map(equipmentControllerService.updateCommissioningDate(id, commissioningDate), CreatedEquipmentDto.class);
    }

    @PatchMapping("/eol-date/{id}")
    public CreatedEquipmentDto updateEOLDate(@PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate EOLDate) throws AccessException {
        return modelMapper.map(equipmentControllerService.updateEOLDate(id, EOLDate), CreatedEquipmentDto.class);
    }

    @PatchMapping("/{equipmentId}/{sensorId}")
    public CreatedEquipmentDto addSensor(@PathVariable Long equipmentId, @PathVariable Long sensorId) {
        return modelMapper.map(equipmentControllerService.addSensor(equipmentId, sensorId), CreatedEquipmentDto.class);
    }

    @PatchMapping("/{equipmentId}/organization")
    public CreatedEquipmentDto addOrganization(@PathVariable Long equipmentId, @RequestBody NewEquipmentOrganizationDto organization) {
        return modelMapper.map(equipmentControllerService.addOrganization(equipmentId, modelMapper.map(organization, EquipmentOrganization.class)), CreatedEquipmentDto.class);
    }

    @DeleteMapping("/{id}")
    public String deleteEquipment(@PathVariable Long id) throws AccessException {
        return equipmentControllerService.deleteEquipment(id);
    }
}
