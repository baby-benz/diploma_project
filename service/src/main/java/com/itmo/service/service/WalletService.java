package com.itmo.service.service;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Slf4j
@Service
public class WalletService {
    @Value("${contract.username}")
    private String userName;

    @PostConstruct
    private void addToWallet() {
        try {
            // Wallet содержит коллекцию данных пользователей
            Path walletPath = Paths.get(".", "wallet");
            Wallet wallet = Wallets.newFileSystemWallet(walletPath);

            Path credentialPath = Paths.get("..", "..", "..", "..", "test-network", "organizations",
                    "peerOrganizations", "org1.example.com", "users", userName, "msp");
            log.info("credentialPath: " + credentialPath);
            Path certificatePath = credentialPath.resolve(Paths.get("signcerts",
                    userName + "-cert.pem"));
            log.info("certificatePem: " + certificatePath);
            Path privateKeyPath = credentialPath.resolve(Paths.get("keystore",
                    "priv_sk"));

            X509Certificate certificate = readX509Certificate(certificatePath);
            PrivateKey privateKey = getPrivateKey(privateKeyPath);

            Identity identity = Identities.newX509Identity("Org1MSP", certificate, privateKey);

            wallet.put(userName, identity);

            log.info("Запись в Wallet " + walletPath + " прошла успешно");
        } catch (IOException | CertificateException | InvalidKeyException e) {
            log.error("Ошибка при добалении данных пользователя в Wallet");
            e.printStackTrace();
        }
    }

    private static X509Certificate readX509Certificate(final Path certificatePath) throws IOException, CertificateException {
        try (Reader certificateReader = Files.newBufferedReader(certificatePath, StandardCharsets.UTF_8)) {
            return Identities.readX509Certificate(certificateReader);
        }
    }

    private static PrivateKey getPrivateKey(final Path privateKeyPath) throws IOException, InvalidKeyException {
        try (Reader privateKeyReader = Files.newBufferedReader(privateKeyPath, StandardCharsets.UTF_8)) {
            return Identities.readPrivateKey(privateKeyReader);
        }
    }

    public static Wallet getWallet() throws IOException {
        Path walletPath = Paths.get(".", "wallet");
        return Wallets.newFileSystemWallet(walletPath);
    }
}
