package com.itmo.owner.service.mqtt;

import com.itmo.owner.domain.entity.Sensor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.nio.ByteBuffer;
import java.util.List;

@Slf4j
@UtilityClass
public class TopicReader {
    public void startRetrievingDataFromSensors(List<Sensor> sensorsToRetrieveDataFrom) {
        for(Sensor sensor : sensorsToRetrieveDataFrom) {
            startRetrievingData(sensor);
        }
    }

    public void startRetrievingData(Sensor sensorToRetrieveDataFrom) {
        String topicName = sensorToRetrieveDataFrom.getEquipment().getId() + "/" + sensorToRetrieveDataFrom.getId();

        try {
            MQTTClient.MQTT_CLIENT.subscribe(topicName, (topic, msg) -> {
                double payloadValue = toDouble(msg.getPayload());

                if (payloadValue > sensorToRetrieveDataFrom.getRangeMax()) {
                    log.warn("ВНИМАНИЕ!!! Показание({} {}) {}_{} оборудования {}_{} превысило максимально допустимое значение({} {}).\nСостояние оборудования: DEGRADED",
                            String.format("%.2f", payloadValue),
                            sensorToRetrieveDataFrom.getUnit(),
                            sensorToRetrieveDataFrom.getSensorType(),
                            sensorToRetrieveDataFrom.getId(),
                            sensorToRetrieveDataFrom.getEquipment().getEquipmentType(),
                            sensorToRetrieveDataFrom.getEquipment().getId(),
                            sensorToRetrieveDataFrom.getRangeMax(),
                            sensorToRetrieveDataFrom.getUnit()
                    );
                } else if (payloadValue < sensorToRetrieveDataFrom.getRangeMin()) {
                    log.warn("ВНИМАНИЕ!!! Показание({} {}) {}_{} оборудования {}_{} упало ниже предельно допуcтимой отметки({} {}).\nСостояние оборудования: DEGRADED",
                            String.format("%.2f", payloadValue),
                            sensorToRetrieveDataFrom.getUnit(),
                            sensorToRetrieveDataFrom.getSensorType(),
                            sensorToRetrieveDataFrom.getId(),
                            sensorToRetrieveDataFrom.getEquipment().getEquipmentType(),
                            sensorToRetrieveDataFrom.getEquipment().getId(),
                            sensorToRetrieveDataFrom.getRangeMin(),
                            sensorToRetrieveDataFrom.getUnit()
                    );
                }
            });
            log.info("Оформлена подписка на MQTT-топик {}.", topicName);
        } catch (MqttException e) {
            log.error("Ошибка при попытке получения данных с MQTT-топика {}.", topicName);
        }
    }

    public void stopRetrievingData(String topicName) {
        try {
            MQTTClient.MQTT_CLIENT.unsubscribe(topicName);
            log.info("Успешно отписался от {} MQTT-топика.", topicName);
        } catch (MqttException e) {
            log.error("Ошибка при попытке отписаться от {} MQTT-топика.", topicName);
        }
    }

    private double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }
}
