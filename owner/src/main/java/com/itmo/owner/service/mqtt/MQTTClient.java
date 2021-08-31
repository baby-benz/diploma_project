package com.itmo.owner.service.mqtt;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;

@UtilityClass
@Slf4j
public final class MQTTClient {
    private static final String MQTT_SERVER_URI = "tcp://77.235.22.134:1883";
    public static final IMqttClient MQTT_CLIENT = createMqttClient();

    private static IMqttClient createMqttClient() {
        try {
            IMqttClient mqttClient = new MqttClient(MQTT_SERVER_URI, UUID.randomUUID().toString());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            try {
                mqttClient.connect(options);
            } catch (MqttSecurityException ex) {
                log.error("Wrong MQTT auth credentials for user: {}", options.getUserName());
            } catch (MqttException exc) {
                log.error("Unable to connect to the MQTT server", exc);
            }
            return mqttClient;
        } catch (final MqttException exc) {
            throw new Error(exc);
        }
    }
}
