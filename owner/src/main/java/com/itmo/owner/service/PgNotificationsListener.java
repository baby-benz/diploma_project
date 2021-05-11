package com.itmo.owner.service;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import com.itmo.owner.domain.entity.Sensor;
import com.itmo.owner.repository.SensorRepository;
import com.itmo.owner.service.mqtt.TopicReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Service
@RequiredArgsConstructor
public class PgNotificationsListener {
    private final SensorRepository sensorRepository;

    @Value("${spring.datasource.url}")
    private String dbUrl;
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
                    Long equipmentId = Long.parseLong(payload);
                    for (Sensor sensor : sensorRepository.findAllByEquipmentId(equipmentId)) {
                        TopicReader.startRetrievingData(sensor);
                    }
                } else if (channelName.equals(newCriticalSensorChannelName)) {
                    Long equipmentId = Long.parseLong(payload);
                    TopicReader.startRetrievingData(sensorRepository.findById(equipmentId).orElseThrow());
                } else if (channelName.equals(notCriticalEquipmentChannelName)) {
                    Long equipmentId = Long.parseLong(payload);
                    for (Sensor sensor : sensorRepository.findAllByEquipmentId(equipmentId)) {
                        TopicReader.stopRetrievingData(equipmentId + "/" + sensor.getId());
                    }
                } else {
                    TopicReader.stopRetrievingData(payload);
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
