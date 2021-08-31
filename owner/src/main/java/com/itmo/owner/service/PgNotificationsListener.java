package com.itmo.owner.service;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import com.itmo.owner.domain.entity.Sensor;
import com.itmo.owner.repository.EquipmentRepository;
import com.itmo.owner.repository.SensorRepository;
import com.itmo.owner.service.mqtt.TopicReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeoutException;

@Slf4j
//@Service
@RequiredArgsConstructor
public class PgNotificationsListener {
    private final EquipmentRepository equipmentRepository;
    private final SensorRepository sensorRepository;
    private final TopicReader topicReader;

    @Value("${pgjdbc-ng.channel.name.new-critical-equipment}")
    private String newCriticalEquipmentChannelName;
    @Value("${pgjdbc-ng.channel.name.new-critical-sensor}")
    private String newCriticalSensorChannelName;
    @Value("${pgjdbc-ng.channel.name.not-critical-equipment}")
    private String notCriticalEquipmentChannelName;
    @Value("${pgjdbc-ng.channel.name.not-critical-sensor}")
    private String notCriticalSensorChannelName;

    private PGConnection connection;

    // Раскомментировать, если есть необходимость в ручных INSERT и получении уведомлений с сервера базы данных
    // @EventListener
    public void onApplicationLoaded(ContextRefreshedEvent event) throws SQLException {
        connect();
        startListening();
    }

    private void startListening() {
        connection.addNotificationListener(new PGNotificationListener() {
            @Override
            public void notification(int processId, String channelName, String payload) {
                log.info("Received Notification: " + channelName + ", " + payload);

                if (channelName.equals(newCriticalEquipmentChannelName)) {
                    startReadingEquipmentSensors(payload);
                } else if (channelName.equals(newCriticalSensorChannelName)) {
                    startReadingEquipmentSensors(payload);
                } else if (channelName.equals(notCriticalEquipmentChannelName)) {
                    Long equipmentId = Long.parseLong(payload);
                    for (Sensor sensor : sensorRepository.findAllByEquipmentId(equipmentId)) {
                        topicReader.stopRetrievingData(equipmentId + "/" + sensor.getId());
                    }
                } else {
                    topicReader.stopRetrievingData(payload);
                }
            }

            @Override
            public void closed() {
                log.info("Notification listener lost connection to server, trying to reconnect...");
                startListening();
            }
        });

        listen(newCriticalEquipmentChannelName);
        listen(newCriticalSensorChannelName);
        listen(notCriticalEquipmentChannelName);
        listen(notCriticalSensorChannelName);
    }

    private void startReadingEquipmentSensors(String payload) {
        Long equipmentId = Long.parseLong(payload);
        try {
            topicReader.startRetrievingData(equipmentRepository.findById(equipmentId).orElseThrow());
        } catch (ContractException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void listen(String channelName) {
        try {
            Statement statement = connection.createStatement();
            try {
                statement.executeUpdate("LISTEN " + channelName);
                log.info("Started listening to " + channelName + " database notifications channel.");
            } catch (SQLException throwable) {
                log.error("Error accessing the database to listen for notifications.");
            }
        } catch (SQLException throwable) {
            log.error("Error accessing the database to listen for notifications.");
        }
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:pgsql://postgres:AF820300pass@77.235.22.134:5432/postgres").unwrap(PGConnection.class);
        log.info("Successfully connected to the database. Trying to start listening to notifications...");
    }
}
