package com.itmo.owner.service.mqtt;

import lombok.experimental.UtilityClass;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@UtilityClass
public class TopicWriter {
    public void publishMessage(String topicName, byte[] payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload);

        message.setQos(0);
        message.setRetained(true);

        MQTTClient.MQTT_CLIENT.publish(topicName, message);
    }
}
