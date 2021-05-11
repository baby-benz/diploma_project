package com.itmo.service.controller;

import com.itmo.service.equipment.MaintenanceStatus;
import com.itmo.service.service.ServiceControllerService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/service/api/")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceControllerService serviceControllerService;

    @PostMapping("/{equipmentId}/maintenance")
    public String startMaintenance(@PathVariable Long equipmentId, @RequestParam String serviceOrg,
                                 @RequestParam MaintenanceStatus maintenanceStatus) throws AccessException, TimeoutException {
        return serviceControllerService.startMaintenance(equipmentId, serviceOrg, maintenanceStatus);
    }

    @PostMapping("/{equipmentId}/report")
    public String submitMaintenanceReport(@PathVariable Long equipmentId, @RequestParam String serviceOrg,
                                          @RequestBody MultipartFile document) throws AccessException, TimeoutException {
        return serviceControllerService.processMaintenanceReport(equipmentId, serviceOrg, document);
    }
}
