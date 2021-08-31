package com.itmo.accounting.service;

import com.itmo.accounting.controller.dto.cc.EquipmentLifecycle;
import com.itmo.accounting.controller.dto.cc.RelatedOrganizations;
import com.itmo.accounting.domain.entity.*;
import com.itmo.accounting.repository.EquipmentOrganizationRepository;
import com.itmo.accounting.repository.EquipmentRepository;
import com.itmo.accounting.repository.OrganizationRepository;
import com.itmo.accounting.repository.SensorRepository;
import com.itmo.accounting.service.cc.NetworkService;
import com.owlike.genson.Genson;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class EquipmentControllerService {
    private final EquipmentRepository equipmentRepository;
    private final SensorRepository sensorRepository;
    private final OrganizationRepository organizationRepository;
    private final EquipmentOrganizationRepository equipmentOrganizationRepository;
    private final NetworkService networkService;
    private final Genson genson;

    public Equipment saveEquipment(Equipment equipment) throws AccessException {
        List<EquipmentOrganization> equipmentOrganizations = List.copyOf(equipment.getOrganizations());

        List<Organization> organizations = new ArrayList<>();
        boolean isOwnerPresented = false;

        for (EquipmentOrganization equipmentOrganization : equipmentOrganizations) {
            Organization organization = organizationRepository.findById(equipmentOrganization.getId().getOrganizationId()).orElseThrow();
            organizations.add(organization);

            if(organization.getOrganizationType().equals(OrganizationType.ВЛАДЕЛЕЦ)) {
                isOwnerPresented = true;
            }
        }

        if(!isOwnerPresented) {
            throw new IllegalArgumentException("У добавляемого оборудования должен быть владелец");
        }

        Set<Sensor> sensors = Set.copyOf(equipment.getSensors());

        equipment.setOrganizations(new HashSet<>());
        equipment.setSensors(new HashSet<>());

        Equipment savedEquipment = equipmentRepository.save(equipment);

        Set<EquipmentOrganization> equipmentOrganizationSaved = new HashSet<>();
        EquipmentOrganization tempEquipmentOrganization = new EquipmentOrganization();

        for (int i = 0; i < equipmentOrganizations.size(); i++) {
            tempEquipmentOrganization.setEquipment(savedEquipment);
            tempEquipmentOrganization.setOrganization(organizations.get(i));
            tempEquipmentOrganization.setStartDate(equipmentOrganizations.get(i).getStartDate());
            tempEquipmentOrganization.setEndDate(equipmentOrganizations.get(i).getEndDate());

            equipmentOrganizationSaved.add(equipmentOrganizationRepository.save(tempEquipmentOrganization));
        }

        savedEquipment.setOrganizations(equipmentOrganizationSaved);
        savedEquipment = equipmentRepository.save(savedEquipment);

        Set<Sensor> sensorsSaved = new HashSet<>();

        for (Sensor sensor : sensors) {
            sensor.setEquipment(savedEquipment);
            Sensor sensorSaved = sensorRepository.save(sensor);
            sensorsSaved.add(sensorSaved);
        }

        savedEquipment.setSensors(sensorsSaved);
        savedEquipment = equipmentRepository.save(savedEquipment);

        if (savedEquipment.getIsTakenIntoAccount()) {
            //OwnerOrgRequestUtil.notifyOwner(savedEquipment.getId(), true);
        }

        com.itmo.accounting.controller.dto.cc.Equipment equipmentCCDto = constructEquipmentCCDto(savedEquipment);

        Contract contract;

        try {
            contract = networkService.accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети", e);
        }

        try {
            contract.submitTransaction("commission", genson.serialize(equipmentCCDto));
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (ContractException e) {
            throw new IllegalArgumentException("Неверный запрос на совершение транзакции commissio: equipment - " + equipment);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        return savedEquipment;
    }

    public Equipment updateEquipment(Long id, Equipment equipment) {
        Optional<Equipment> equipmentToUpdate = equipmentRepository.findById(id);
        if (equipmentToUpdate.isPresent()) {
            equipment.setId(id);

            Equipment updatedEquipment = equipmentRepository.save(equipment);

            if (!equipmentToUpdate.get().getIsTakenIntoAccount().equals(updatedEquipment.getIsTakenIntoAccount())) {
                //OwnerOrgRequestUtil.notifyOwner(updatedEquipment.getId(), updatedEquipment.getIsTakenIntoAccount());
            }

            return updatedEquipment;
        }
        return equipmentRepository.save(equipment);
    }

    public Equipment updateEquipmentType(Long id, EquipmentType equipmentType) {
        Equipment equipmentToUpdate = equipmentRepository.findById(id).orElseThrow();
        equipmentToUpdate.setEquipmentType(equipmentType);
        return equipmentRepository.save(equipmentToUpdate);
    }

    public Equipment updateIsTakenIntoAccount(Long id, boolean isTakenIntoAccount) {
        Equipment equipmentToUpdate = equipmentRepository.findById(id).orElseThrow();
        equipmentToUpdate.setIsTakenIntoAccount(isTakenIntoAccount);

        Equipment savedEquipment = equipmentRepository.save(equipmentToUpdate);

        //OwnerOrgRequestUtil.notifyOwner(id, isTakenIntoAccount);

        return savedEquipment;
    }

    public Equipment updateCommissioningDate(Long id, LocalDate commissioningDate) {
        Equipment equipmentToUpdate = equipmentRepository.findById(id).orElseThrow();
        equipmentToUpdate.setCommissioningDate(commissioningDate);
        return equipmentRepository.save(equipmentToUpdate);
    }

    public Equipment updateEOLDate(Long id, LocalDate EOLDate) throws AccessException {
        Equipment equipmentToDecommission = equipmentRepository.findById(id).orElseThrow();

        Contract contract;

        try {
            contract = networkService.accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети", e);
        }

        String ownerOrg = null;
        for(EquipmentOrganization equipmentOrganization : equipmentToDecommission.getOrganizations()) {
            Organization organization = equipmentOrganization.getOrganization();
            if(organization.getOrganizationType().equals(OrganizationType.ВЛАДЕЛЕЦ)) {
                ownerOrg = organization.getName();
                break;
            }
        }

        if(ownerOrg == null) {
            throw new RuntimeException("У оборудования с ID " + id + "не назначен владелец!");
        }

        try {
            contract.submitTransaction("decommission", id.toString(), ownerOrg, EOLDate.toString());
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (ContractException e) {
            throw new IllegalArgumentException("Неверный запрос на совершение транзакции decommission: equipmentId - " + id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        equipmentToDecommission.setEOLDate(EOLDate);
        equipmentToDecommission.setIsTakenIntoAccount(false);


        for (Sensor sensor : equipmentToDecommission.getSensors()) {
            //OwnerOrgRequestUtil.notifyOwner(sensor.getId(), false);
        }

        return equipmentRepository.save(equipmentToDecommission);
    }

    public Equipment addSensor(Long equipmentId, Long sensorId) {
        Equipment equipmentToUpdate = equipmentRepository.findById(equipmentId).orElseThrow();
        Sensor sensorToAdd = sensorRepository.findById(sensorId).orElseThrow();

        Set<Sensor> sensorsToUpdate = equipmentToUpdate.getSensors();
        sensorsToUpdate.add(sensorToAdd);
        equipmentToUpdate.setSensors(sensorsToUpdate);

        return equipmentRepository.save(equipmentToUpdate);
    }

    public Equipment addOrganization(Long equipmentId, EquipmentOrganization organization) {
        Equipment equipmentToUpdate = equipmentRepository.findById(equipmentId).orElseThrow();
        equipmentOrganizationRepository.findById(organization.getId().getOrganizationId()).orElseThrow();

        Set<EquipmentOrganization> organizationsToUpdate = equipmentToUpdate.getOrganizations();
        organization.setEquipment(equipmentToUpdate);
        organizationsToUpdate.add(organization);
        equipmentToUpdate.setOrganizations(organizationsToUpdate);

        return equipmentRepository.save(equipmentToUpdate);
    }

    @Transactional
    public String deleteEquipment(Long id) throws AccessException {
        Equipment equipmentToDelete = equipmentRepository.findById(id).orElseThrow();

        Contract contract;

        try {
            contract = networkService.accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети", e);
        }

        String ownerOrg = null;
        for(EquipmentOrganization equipmentOrganization : equipmentToDelete.getOrganizations()) {
            Organization organization = equipmentOrganization.getOrganization();
            if(organization.getOrganizationType().equals(OrganizationType.ВЛАДЕЛЕЦ)) {
                ownerOrg = organization.getName();
                break;
            }
        }

        if(ownerOrg == null) {
            throw new RuntimeException("У оборудования с ID " + id + " не назначен владелец!");
        }

        try {
            contract.submitTransaction("deleteEquipment", id.toString(), ownerOrg);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (ContractException e) {
            throw new IllegalArgumentException("Неверный запрос на совершение транзакции deleteEquipment: equipmentId - " + id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        equipmentRepository.deleteById(id);
        for (Sensor sensor : equipmentToDelete.getSensors()) {
            //OwnerOrgRequestUtil.notifyOwner(sensor.getId(), false);
        }

        return "Оборудование с ID " + id + " было удалено";
    }

    private com.itmo.accounting.controller.dto.cc.Equipment constructEquipmentCCDto(Equipment equipment) {
        Set<EquipmentOrganization> organizations = equipment.getOrganizations();
        RelatedOrganizations relatedOrganizations = new RelatedOrganizations();

        for (EquipmentOrganization organization : organizations) {
            if (organization.getOrganization().getOrganizationType().equals(OrganizationType.ВЛАДЕЛЕЦ)) {
                relatedOrganizations.setOwnerOrg(organization.getOrganization().getName());
            } else if (organization.getOrganization().getOrganizationType().equals(OrganizationType.НАДЗОР)) {
                relatedOrganizations.setInspectionOrg(organization.getOrganization().getName());
            } else if (organization.getOrganization().getOrganizationType().equals(OrganizationType.СЕРВИС)) {
                relatedOrganizations.setServiceOrg(organization.getOrganization().getName());
            } else if (organization.getOrganization().getOrganizationType().equals(OrganizationType.ЭКСПЛУАТАНТ)) {
                relatedOrganizations.setOperatingOrg(organization.getOrganization().getName());
            } else {
                throw new IllegalArgumentException("Тип организации не определен");
            }
        }

        Set<Sensor> equipmentSensors = equipment.getSensors();
        List<com.itmo.accounting.controller.dto.cc.Sensor> dtoSensors = new ArrayList<>();


        for (Sensor sensor : equipmentSensors) {
            dtoSensors.add(new com.itmo.accounting.controller.dto.cc.Sensor(
                    sensor.getId(),
                    sensor.getUnit(),
                    sensor.getSensorType()
            ));
        }

        return new com.itmo.accounting.controller.dto.cc.Equipment(
                equipment.getId(),
                equipment.getSerialNumber(),
                equipment.getEquipmentType(),
                dtoSensors,
                new EquipmentLifecycle(equipment.getManufactureDate(), equipment.getCommissioningDate()),
                relatedOrganizations,
                equipment.getDescription()
        );
    }
}
