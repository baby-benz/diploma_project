package com.itmo.accounting.service;

import com.itmo.accounting.domain.entity.EquipmentOrganization;
import com.itmo.accounting.domain.entity.Organization;
import com.itmo.accounting.domain.entity.OrganizationType;
import com.itmo.accounting.repository.EquipmentRepository;
import com.itmo.accounting.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrganizationControllerService {
    private final OrganizationRepository organizationRepository;
    private final EquipmentRepository equipmentRepository;

    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public Organization fullyUpdateOrganization(Long id, Organization organization) {
        Optional<Organization> organizationToUpdate = organizationRepository.findById(id);
        if (organizationToUpdate.isPresent()) {
            organization.setId(id);
            return organizationRepository.save(organization);
        }
        return organizationRepository.save(organization);
    }

    public Organization updateName(Long id, String name) {
        Organization organizationToUpdate = organizationRepository.findById(id).orElseThrow();
        organizationToUpdate.setName(name);
        return organizationRepository.save(organizationToUpdate);
    }

    public Organization updateOrganizationType(Long id, OrganizationType organizationType) {
       Organization organizationToUpdate = organizationRepository.findById(id).orElseThrow();
        organizationToUpdate.setOrganizationType(organizationType);
        return organizationRepository.save(organizationToUpdate);
    }

    public Organization updateAddress(Long id, String address) {
        Organization organizationToUpdate = organizationRepository.findById(id).orElseThrow();
        organizationToUpdate.setAddress(address);
        return organizationRepository.save(organizationToUpdate);
    }

    public Organization addEquipment(Long organizationId, EquipmentOrganization equipment) {
        Organization organizationToUpdate = organizationRepository.findById(organizationId).orElseThrow();
        equipmentRepository.findById(equipment.getEquipment().getId()).orElseThrow();

        Set<EquipmentOrganization> equipmentToUpdate = organizationToUpdate.getEquipment();
        equipment.setOrganization(organizationToUpdate);
        equipmentToUpdate.add(equipment);
        organizationToUpdate.setEquipment(equipmentToUpdate);

        return organizationRepository.save(organizationToUpdate);
    }

    @Transactional
    public void deleteOrganization(Long id) {
        Organization organizationToDelete = organizationRepository.findById(id).orElseThrow();
        organizationRepository.delete(organizationToDelete);
    }
}
