package com.itmo.service.controller;

import com.itmo.service.equipment.Equipment;
import com.itmo.service.equipment.EquipmentHistory;
import com.itmo.service.equipment.MaintenanceStatus;
import com.itmo.service.service.ServiceControllerService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/service/api/")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceControllerService serviceControllerService;

    @PostMapping("/maintenance/{equipmentId}")
    public Equipment startMaintenance(@PathVariable Long equipmentId, @RequestParam String serviceOrg,
                                 @RequestParam MaintenanceStatus maintenanceStatus) throws AccessException, TimeoutException {
        return serviceControllerService.startMaintenance(equipmentId, serviceOrg, maintenanceStatus);
    }

    @PostMapping(value = "/report/{equipmentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Equipment submitMaintenanceReport(@PathVariable Long equipmentId, @RequestParam String serviceOrg,
                                             @RequestBody MultipartFile document) throws AccessException {
        return serviceControllerService.processSingleMaintenanceReport(equipmentId, serviceOrg, document);
    }

    /*@PostMapping(value = "/report/multiple/{equipmentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Equipment submitMaintenanceReport(@PathVariable Long equipmentId, @RequestParam String serviceOrg,
                                             @RequestPart Flux<FilePart> document) throws AccessException, TimeoutException {
        return serviceControllerService.processMultipleMaintenanceReports(equipmentId, serviceOrg, document);
    }*/

    @GetMapping("/equipment")
    public List<Equipment> getAllEquipment() throws AccessException {
        return serviceControllerService.getAllEquipment();
    }

    @GetMapping("/equipment/{equipmentId}")
    public Equipment getEquipmentCurrentState(@PathVariable Long equipmentId) throws AccessException {
        return serviceControllerService.getEquipmentCurrentState(equipmentId);
    }

    @GetMapping("/history/{equipmentId}")
    public List<EquipmentHistory> getEquipmentHistory(@PathVariable Long equipmentId) throws AccessException {
        return serviceControllerService.getEquipmentHistory(equipmentId);
    }
}
