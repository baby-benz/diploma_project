package com.itmo.inspection.service.cc;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.Set;

@Slf4j
@Service
public class WalletService {
    @Value("${contract.username}")
    private String userName;

    private static final String REMOTE_HOST = "77.235.22.134";
    private static final String LOCAL_RELATIVE_PATH = "org2.example.com/ca/";
    private static final String FILE_NAME = "ca.org2.example.com-cert.pem";

    @PostConstruct
    @DependsOn("pemFileService")
    private void initWallet() throws Exception {
        addToWallet();
        registerUser();
    }

    private void addToWallet() throws Exception {
        HFCAClient caClient = createCaClient();

        // Создаем wallet для управления удостоверениями
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

        // Регистрируемся ролью администратора и сохраняем ее удостоверение в wallet
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost(REMOTE_HOST);
        enrollmentRequestTLS.setProfile("tls");

        Enrollment enrollment = caClient.enroll("admin", "adminpw", enrollmentRequestTLS);

        Identity user = Identities.newX509Identity("Org2MSP", enrollment);
        wallet.put("admin", user);

        log.info("Успешно зарегестрировался пользователем \"admin\" и импортировал удостоверение пользователя в wallet");
    }

    private void registerUser() throws Exception {
        HFCAClient caClient = createCaClient();

        // Создаем wallet для управления удостоверениями
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

        // Регистрируем пользователя appUser и сохраняем его удостоверение в wallet
        RegistrationRequest registrationRequest = new RegistrationRequest(userName);
        registrationRequest.setAffiliation("org2.department1");
        registrationRequest.setEnrollmentID(userName);
        registrationRequest.setSecret("12345");

        X509Identity adminIdentity = (X509Identity) wallet.get("admin");

        if (adminIdentity == null) {
            log.error("Пользователь \"admin\" должен быть зарегистрирован и добавлен в wallet для регистрации пользователя appUser");
            return;
        }

        User admin = new User() {
            @Override
            public String getName() {
                return "admin";
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return "org2.department1";
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return Identities.toPemString(adminIdentity.getCertificate());
                    }
                };
            }

            @Override
            public String getMspId() {
                return "Org2MSP";
            }
        };

        String enrollmentSecret;
        Enrollment enrollment;

        try {
            enrollmentSecret = caClient.register(registrationRequest, admin);
            enrollment = caClient.enroll(userName, enrollmentSecret);
        } catch (Exception e) {
            log.info("Пользователь \"appUser\" уже зарегистрирован. Получаю повторно удостоврение...");

            final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
            enrollmentRequestTLS.addHost(REMOTE_HOST);
            enrollmentRequestTLS.setProfile("tls");

            enrollment = caClient.enroll(userName, "12345", enrollmentRequestTLS);
        }

        Identity user = Identities.newX509Identity("Org2MSP", enrollment);
        wallet.put(userName, user);
        log.info("Пользователь \"appUser\" зарегистрирован. Удостоверение импортировано в wallet");
    }

    private HFCAClient createCaClient() throws Exception {
        // Создаем CA-клиент для взаимодействия с CA
        Properties props = new Properties();
        props.put("pemFile", LOCAL_RELATIVE_PATH + FILE_NAME);
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance("https://" + REMOTE_HOST + ":8054", props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);
        return caClient;
    }

    public static Wallet getWallet() throws IOException {
        Path walletPath = Paths.get(".", "wallet");
        return Wallets.newFileSystemWallet(walletPath);
    }
}
