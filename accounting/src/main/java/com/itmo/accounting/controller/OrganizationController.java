package com.itmo.accounting.controller;

import com.itmo.accounting.controller.dto.CreatedOrganizationDto;
import com.itmo.accounting.controller.dto.NewEquipmentOrganizationDto;
import com.itmo.accounting.controller.dto.NewOrganizationDto;
import com.itmo.accounting.domain.entity.EquipmentOrganization;
import com.itmo.accounting.domain.entity.Organization;
import com.itmo.accounting.domain.entity.OrganizationType;
import com.itmo.accounting.service.OrganizationControllerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/accounting/api/organization")
@RestController
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationControllerService organizationControllerService;
    private final ModelMapper modelMapper;

    @PostMapping
    public CreatedOrganizationDto createOrganization(@RequestBody NewOrganizationDto organization) {
        return modelMapper.map(organizationControllerService.createOrganization(modelMapper.map(organization, Organization.class)), CreatedOrganizationDto.class);
    }

    @PutMapping("/{id}")
    public CreatedOrganizationDto fullyUpdateOrganization(@PathVariable Long id, @RequestBody NewOrganizationDto organization) {
        return modelMapper.map(organizationControllerService.fullyUpdateOrganization(id, modelMapper.map(organization, Organization.class)), CreatedOrganizationDto.class);
    }

    @PatchMapping("/name/{id}")
    public CreatedOrganizationDto updateName(@PathVariable Long id, @RequestParam String name) {
        return modelMapper.map(organizationControllerService.updateName(id, name), CreatedOrganizationDto.class);
    }

    @PatchMapping("/organization-type/{id}")
    public CreatedOrganizationDto updateOrganizationType(@PathVariable Long id, @RequestParam OrganizationType organizationType) {
        return modelMapper.map(organizationControllerService.updateOrganizationType(id, organizationType), CreatedOrganizationDto.class);
    }

    @PatchMapping("/address/{id}")
    public CreatedOrganizationDto updateAddress(@PathVariable Long id, @RequestParam String address) {
        return modelMapper.map(organizationControllerService.updateAddress(id, address), CreatedOrganizationDto.class);
    }

    @PatchMapping("/{organizationId}/equipment")
    public CreatedOrganizationDto addEquipment(@PathVariable Long organizationId, @RequestBody NewEquipmentOrganizationDto organization) {
        return modelMapper.map(organizationControllerService.addEquipment(organizationId, modelMapper.map(organization, EquipmentOrganization.class)), CreatedOrganizationDto.class);
    }

    @DeleteMapping("/{id}")
    public void deleteOrganization(@PathVariable Long id) {
        organizationControllerService.deleteOrganization(id);
    }
}
