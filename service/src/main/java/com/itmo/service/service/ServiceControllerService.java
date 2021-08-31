package com.itmo.service.service;

import com.itmo.service.equipment.Equipment;
import com.itmo.service.equipment.EquipmentHistory;
import com.itmo.service.equipment.MaintenanceStatus;
import com.itmo.service.service.cc.NetworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceControllerService {
    private final NetworkService networkService;

    public Equipment startMaintenance(Long equipmentId, String serviceOrg, MaintenanceStatus maintenanceStatus) throws AccessException, TimeoutException {
        Contract contract;

        try {
            contract = networkService.accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети", e);
        }

        byte[] response;

        try {
            if (maintenanceStatus.equals(MaintenanceStatus.КОРРЕКТИРУЮЩЕЕ_ОБСЛУЖИВАНИЕ)) {
                response = contract.submitTransaction("startCorrectiveMaintenance", equipmentId.toString(),
                        serviceOrg);
            } else if (maintenanceStatus.equals(MaintenanceStatus.ЗАПЛАНИРОВАННОЕ_ОБСЛУЖИВАНИЕ)) {
                response = contract.submitTransaction("startPlannedMaintenance", equipmentId.toString(),
                        serviceOrg);
            } else if (maintenanceStatus.equals(MaintenanceStatus.ОБСЛУЖИВАНИЕ_ПО_СОСТОЯНИЮ)) {
                response = contract.submitTransaction("startConditionBasedMaintenance", equipmentId.toString(),
                        serviceOrg);
            } else {
                throw new IllegalArgumentException("");
            }
        } catch (ContractException e) {
            String error = String.format("Неверный запрос на совершение транзакции: equipmentId - %s, serviceOrg - %s, maintenanceStatus - %s", equipmentId, serviceOrg, maintenanceStatus);
            log.error(error);
            throw new IllegalArgumentException(error);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        String stringResponse = new String(response, StandardCharsets.UTF_8);

        return Equipment.deserialize(new JSONObject(stringResponse));
    }

    public Equipment processSingleMaintenanceReport(Long equipmentId, String serviceOrg, MultipartFile document) throws AccessException {
        Contract contract;

        try {
            contract = networkService.accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети", e);
        }

        byte[] response;

        try {
            try {
                response = contract.submitTransaction("finishMaintenance", equipmentId.toString(), serviceOrg, Base64.getEncoder().encodeToString(document.getBytes()));
            } catch (IOException e) {
                log.error("Ошибка при попытке сериализовать документ");
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        } catch (ContractException e) {
            String error = String.format("Неверный запрос на совершение транзакции: equipmentId - %s, serviceOrg - %s, maintenanceStatus - %s", equipmentId, serviceOrg, document);
            log.error(error);
            throw new IllegalArgumentException(error);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        String stringResponse = new String(response, StandardCharsets.UTF_8);

        return Equipment.deserialize(new JSONObject(stringResponse));
    }

    public List<Equipment> getAllEquipment() throws AccessException {
        Contract contract;

        try {
            contract = networkService.accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети", e);
        }

        byte[] response;

        try {
            response = contract.evaluateTransaction("queryAllEquipment");
        } catch (ContractException e) {
            String error = "Неверный запрос на получение состояния всего оборудования";
            log.error(error);
            throw new IllegalArgumentException(error);
        }

        String stringResponse = new String(response, StandardCharsets.UTF_8);

        return Equipment.deserializeCollection(new JSONArray(stringResponse));
    }

    public Equipment getEquipmentCurrentState(Long equipmentId) throws AccessException {
        Contract contract;

        try {
            contract = networkService.accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети", e);
        }

        byte[] response;

        try {
            response = contract.evaluateTransaction("queryEquipment", equipmentId.toString());
        } catch (ContractException e) {
            String error = String.format("Неверный запрос на получение текущего состояния оборудования: equipmentId - %s", equipmentId);
            log.error(error);
            throw new IllegalArgumentException(error);
        }

        String stringResponse = new String(response, StandardCharsets.UTF_8);

        return Equipment.deserialize(new JSONObject(stringResponse));
    }

    public List<EquipmentHistory> getEquipmentHistory(Long equipmentId) throws AccessException {
        Contract contract;

        try {
            contract = networkService.accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети", e);
        }

        byte[] response;

        try {
            response = contract.evaluateTransaction("queryEquipmentHistory", equipmentId.toString());
        } catch (ContractException e) {
            String error = String.format("Неверный запрос на получение истории об оборудовании: equipmentId - %s", equipmentId);
            log.error(error);
            throw new IllegalArgumentException(error);
        }

        String stringResponse = new String(response, StandardCharsets.UTF_8);

        return Equipment.deserializeHistory(new JSONArray(stringResponse));
    }
}
