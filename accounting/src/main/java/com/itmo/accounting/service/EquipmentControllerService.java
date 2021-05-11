package com.itmo.accounting.service;

import com.itmo.accounting.domain.entity.Equipment;
import com.itmo.accounting.domain.entity.EquipmentOrganization;
import com.itmo.accounting.domain.entity.Sensor;
import com.itmo.accounting.repository.EquipmentOrganizationRepository;
import com.itmo.accounting.repository.EquipmentRepository;
import com.itmo.accounting.repository.SensorRepository;
import com.itmo.accounting.util.OwnerOrgRequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquipmentControllerService {
    private final EquipmentRepository equipmentRepository;
    private final SensorRepository sensorRepository;
    private final EquipmentOrganizationRepository equipmentOrganizationRepository;

    public Equipment saveEquipment(Equipment equipment) {
        Equipment savedEquipment = equipmentRepository.save(equipment);

        EquipmentOrganization equipmentOrganizationToSave = new EquipmentOrganization();

        for (EquipmentOrganization organization : equipment.getOrganizations()) {
            equipmentOrganizationToSave.setEquipment(savedEquipment);
            equipmentOrganizationToSave.setOrganization(organization.getOrganization());
            equipmentOrganizationToSave.setStartDate(organization.getStartDate());

            Date endDate = organization.getEndDate();
            if (endDate != null) {
                equipmentOrganizationToSave.setEndDate(endDate);
            }

            equipmentOrganizationRepository.save(equipmentOrganizationToSave);
        }

        for (Sensor sensor : equipment.getSensors()) {
            sensor.setEquipment(savedEquipment);
            Sensor savedSensor = sensorRepository.save(sensor);

            if (savedEquipment.getIsTakenIntoAccount()) {
                OwnerOrgRequestUtil.notifyOwnerAboutTerminationOfSubscription(savedSensor.getId(), true);
            }
        }

        return savedEquipment;
    }

    public Equipment updateEquipment(Long id, Equipment equipment) {
        Optional<Equipment> equipmentToUpdate = equipmentRepository.findById(id);
        if (equipmentToUpdate.isPresent()) {
            equipment.setId(id);

            Equipment updatedEquipment = equipmentRepository.save(equipment);

            if (!equipmentToUpdate.get().getIsTakenIntoAccount().equals(updatedEquipment.getIsTakenIntoAccount())) {
                for (Sensor sensor : updatedEquipment.getSensors()) {
                    OwnerOrgRequestUtil.notifyOwnerAboutTerminationOfSubscription(sensor.getId(), updatedEquipment.getIsTakenIntoAccount());
                }
            }

            return updatedEquipment;
        }
        return equipmentRepository.save(equipment);
    }

    public Equipment updateIsTakenIntoAccount(Long id, boolean isTakenIntoAccount) {
        Equipment equipmentToUpdate = equipmentRepository.findById(id).orElseThrow();
        equipmentToUpdate.setIsTakenIntoAccount(isTakenIntoAccount);

        Equipment savedEquipment = equipmentRepository.save(equipmentToUpdate);

        for (Sensor sensor : savedEquipment.getSensors()) {
            OwnerOrgRequestUtil.notifyOwnerAboutTerminationOfSubscription(sensor.getId(), isTakenIntoAccount);
        }

        return savedEquipment;
    }

    public Equipment updateCommissioningDate(Long id, Date commissioningDate) {
        Equipment equipmentToUpdate = equipmentRepository.findById(id).orElseThrow();
        equipmentToUpdate.setCommissioningDate(commissioningDate);
        return equipmentRepository.save(equipmentToUpdate);
    }

    public Equipment updateEOLDate(Long id, Date EOLDate) {
        Equipment equipmentToUpdate = equipmentRepository.findById(id).orElseThrow();
        equipmentToUpdate.setEOLDate(EOLDate);
        return equipmentRepository.save(equipmentToUpdate);
    }

    public Equipment updateSensors(Long equipmentId, Long sensorId) {
        Equipment equipmentToUpdate = equipmentRepository.findById(equipmentId).orElseThrow();
        Sensor sensorToAdd = sensorRepository.findById(sensorId).orElseThrow();

        Set<Sensor> sensorsToUpdate = equipmentToUpdate.getSensors();
        sensorsToUpdate.add(sensorToAdd);

        equipmentToUpdate.setSensors(sensorsToUpdate);
        return equipmentRepository.save(equipmentToUpdate);
    }

    public Equipment updateOrganizations(Long id, EquipmentOrganization organization) {
        Equipment equipmentToUpdate = equipmentRepository.findById(id).orElseThrow();
        equipmentRepository.findById(organization.getEquipment().getId());

        Set<EquipmentOrganization> organizationsToUpdate = equipmentToUpdate.getOrganizations();
        organizationsToUpdate.add(organization);

        equipmentToUpdate.setOrganizations(organizationsToUpdate);
        return equipmentRepository.save(equipmentToUpdate);
    }

    public void deleteEquipment(Long id) {
        Equipment equipmentToDelete = equipmentRepository.findById(id).orElseThrow();
        equipmentRepository.delete(equipmentToDelete);
        for (Sensor sensor : equipmentToDelete.getSensors()) {
            OwnerOrgRequestUtil.notifyOwnerAboutTerminationOfSubscription(sensor.getId(), false);
        }
    }
}
