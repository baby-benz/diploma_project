package com.itmo.owner.service;

import com.itmo.owner.domain.entity.Sensor;
import com.itmo.owner.repository.SensorRepository;
import com.itmo.owner.service.mqtt.SensorEmulator;
import com.itmo.owner.service.mqtt.TopicReader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MQTTManager {
    private final SensorEmulator sensorEmulator;
    private final SensorRepository sensorRepository;

    @Value("${mqtt.emulation}")
    private boolean toEmulateSensors;

    @EventListener
    public void onApplicationLoaded(ContextRefreshedEvent event) {
        List<Sensor> takenIntoAccountSensors = sensorRepository.findAllByEquipmentIsTakenIntoAccount(true);

        TopicReader.startRetrievingDataFromSensors(takenIntoAccountSensors);

        if(toEmulateSensors) {
            sensorEmulator.startEmulatingSensors(takenIntoAccountSensors);
        }
    }

    public String subscribeOrUnsubscribe(@PathVariable Long sensorId, @RequestParam boolean toSubscribe) throws NoSuchFieldException {
        Sensor sensorToSubscribe = sensorRepository.findById(sensorId).orElseThrow();

        String topicName = sensorToSubscribe.getEquipment().getId() + "/" + sensorId;

        if(toSubscribe) {
            TopicReader.startRetrievingData(sensorToSubscribe);

            if(toEmulateSensors) {
                sensorEmulator.startEmulatingSensor(sensorToSubscribe);
            }

            return "Успешно оформлена подписка на топик " + topicName + ".";
        } else {
            TopicReader.stopRetrievingData(topicName);

            if(toEmulateSensors) {
                sensorEmulator.stopEmulatingSensor(sensorToSubscribe);
            }

            return "Успешно прекращена подписка на топик " + topicName + ".";
        }
    }
}
