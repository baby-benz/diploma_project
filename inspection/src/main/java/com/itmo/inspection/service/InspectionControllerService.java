package com.itmo.inspection.service;

import com.itmo.inspection.equipment.Equipment;
import com.itmo.inspection.service.cc.NetworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.json.JSONArray;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class InspectionControllerService {
    private final NetworkService networkService;

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

    public Equipment getEquipmentCurrentState(Long equipmentId) throws TimeoutException, AccessException {
        Contract contract;

        try {
            contract = networkService.accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети", e);
        }

        byte[] response;

        try {
            response = contract.submitTransaction("queryEquipment", equipmentId.toString());
        } catch (ContractException e) {
            String error = String.format("Неверный запрос на получение текущего состояния оборудования: equipmentId - %s", equipmentId);
            log.error(error);
            throw new IllegalArgumentException(error);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        return (Equipment) SerializationUtils.deserialize(response);
    }

    public ArrayList<Equipment> getEquipmentHistory(Long equipmentId) throws AccessException, TimeoutException {
        Contract contract;

        try {
            contract = networkService.accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети", e);
        }

        byte[] response;

        try {
            response = contract.submitTransaction("queryEquipmentHistory", equipmentId.toString());
        } catch (ContractException e) {
            String error = String.format("Неверный запрос на получение истории об оборудовании: equipmentId - %s", equipmentId);
            log.error(error);
            throw new IllegalArgumentException(error);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        return (ArrayList<Equipment>) SerializationUtils.deserialize(response);
    }
}
