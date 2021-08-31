package com.itmo.service.service.cc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Wallet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NetworkService {
    @Value("${contract.username}")
    private String userName;
    @Value("${contract.name}")
    private String contractName;
   // private final WalletService walletService;

    public Contract accessNetwork() throws IOException {
        Wallet wallet = WalletService.getWallet();

        // Загружаем файл конфигурации сети
        Path networkConfigPath = Paths.get("org3.example.com", "connection-org3.yaml");

        Gateway.Builder builder = Gateway.createBuilder();

        builder.identity(wallet, userName).networkConfig(networkConfigPath).discovery(false);

        Gateway gateway = builder.connect();

        return gateway.getNetwork("mychannel").getContract(contractName, "com.itmo.equipment");
    }

    //@PostConstruct
    @DependsOn("walletService")
    public void initLedger() throws AccessException, TimeoutException {
        Contract contract;

        try {
            contract = accessNetwork();
        } catch (IOException e) {
            throw new AccessException("Не удалось получить доступ к блокчейн-сети", e);
        }

        try {
            contract.submitTransaction("instantiate");
        } catch (ContractException e) {
            throw new IllegalArgumentException("Ошибка в запросе на совершение транзакции instantiate", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
