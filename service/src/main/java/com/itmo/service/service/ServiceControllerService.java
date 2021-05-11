package com.itmo.service.service;

import com.itmo.service.equipment.MaintenanceStatus;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class ServiceControllerService {
    Gateway.Builder builder = Gateway.createBuilder();

    @Value("${contract.name}")
    private String contractName;
    @Value("${contract.username}")
    private String userName;

    public String startMaintenance(Long equipmentId, String serviceOrg, MaintenanceStatus maintenanceStatus) throws AccessException, TimeoutException {
        Contract contract;

        try {
            contract = accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети");
        }

        byte[] response;

        try {
            if (maintenanceStatus.equals(MaintenanceStatus.КОРРЕКТИРУЮЩЕЕ_ОБСЛУЖИВАНИЕ)) {
                response = contract.submitTransaction("startCorrectiveMaintenance", equipmentId.toString(),
                        serviceOrg, LocalDateTime.now().toString());
            } else if (maintenanceStatus.equals(MaintenanceStatus.ЗАПЛАНИРОВАННОЕ_ОБСЛУЖИВАНИЕ)) {
                response = contract.submitTransaction("startPlannedMaintenance", equipmentId.toString(),
                        serviceOrg, LocalDateTime.now().toString());
            } else if (maintenanceStatus.equals(MaintenanceStatus.ОБСЛУЖИВАНИЕ_ПО_СОСТОЯНИЮ)) {
                response = contract.submitTransaction("startConditionBasedMaintenance", equipmentId.toString(),
                        serviceOrg, LocalDateTime.now().toString());
            } else {
                throw new IllegalArgumentException("");
            }
        } catch (ContractException e) {
            String error = String.format("Неверный запрос на совершение транзакции: equipmentId - %s, serviceOrg - %s, maintenanceStatus - %s" ,equipmentId, serviceOrg, maintenanceStatus);
            log.error(error);
            throw new IllegalArgumentException(error);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        return new String(response, StandardCharsets.UTF_8);
    }

    public String processMaintenanceReport(Long equipmentId, String serviceOrg, MultipartFile document) throws AccessException, TimeoutException {
        Contract contract;

        try {
            contract = accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети");
        }

        byte[] response;

        try {
            response = contract.submitTransaction("buy", "MagnetoCorp", "00001", "MagnetoCorp", "DigiBank", "4900000", "2020-05-31");
        }  catch (ContractException e) {
            String error = String.format("Неверный запрос на совершение транзакции: equipmentId - %s, serviceOrg - %s, maintenanceStatus - %s", equipmentId, serviceOrg, document);
            log.error(error);
            throw new IllegalArgumentException(error);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        return new String(response, StandardCharsets.UTF_8);
    }

    private Contract accessNetwork() throws IOException {
        // Wallet хранит коллекцию identity
        Wallet wallet = WalletService.getWallet();

        Path connectionProfile = Paths.get("..", "gateway", "connection-org1.yaml");

        // Устанавливаем опции подключения для строителя шлюза
        builder.identity(wallet, userName).networkConfig(connectionProfile).discovery(false);

        Gateway gateway = builder.connect();

        Network network = gateway.getNetwork("mychannel");

        return network.getContract(contractName, "org.papernet.commercialpaper");
    }
}
