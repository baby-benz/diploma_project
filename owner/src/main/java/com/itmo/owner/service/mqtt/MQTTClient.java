package com.itmo.owner.service.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
@Slf4j
public final class MQTTClient {
    private static final String MQTT_SERVER_URI = "tcp://77.235.22.134:1883";
    public static final IMqttClient MQTT_CLIENT = createMqttClient();

    private static IMqttClient createMqttClient() {
        try {
            return new MqttClient(MQTT_SERVER_URI, UUID.randomUUID().toString());
        } catch (final MqttException exc) {
            throw new Error(exc);
        }
    }

    @PostConstruct
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        try {
            MQTT_CLIENT.connect(options);
        } catch (MqttSecurityException ex) {
            log.error("Wrong MQTT auth credentials for user: {}", options.getUserName());
        } catch (MqttException exc) {
            log.error("Unable to connect to the MQTT server", exc);
        }
    }
}
