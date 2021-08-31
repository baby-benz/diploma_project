package com.itmo.owner.service.mqtt;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.ByteBuffer;

@Slf4j
@UtilityClass
class TopicWriter {
    public void publishMessage(String topicName, byte[] payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload);

        message.setQos(0);
        message.setRetained(true);

        MQTTClient.MQTT_CLIENT.publish(topicName, message);
        log.info("Опубликовано показание {} в топик {}", String.format("%.2f", ByteBuffer.wrap(payload).getDouble()), topicName);
    }
}
