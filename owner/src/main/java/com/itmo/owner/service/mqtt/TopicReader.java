package com.itmo.owner.service.mqtt;

import com.itmo.owner.controller.dto.cc.EquipmentState;
import com.itmo.owner.domain.entity.Equipment;
import com.itmo.owner.domain.entity.Sensor;
import com.itmo.owner.service.cc.NetworkService;
import com.owlike.genson.Genson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicReader {
    //private final IMqttClient mqttClient = MQTTClient.getMqttClient();
    private final NetworkService networkService;
    private final Map<Long, List<com.itmo.owner.controller.dto.cc.Sensor>> listeningSensors = new ConcurrentHashMap<>();
    private final Map<Long, Boolean[]> equipmentDegradationStage = new ConcurrentHashMap<>();
    private final Map<Long, EquipmentState> equipmentState = new ConcurrentHashMap<>();
    private final ModelMapper modelMapper;
    private final Genson genson;

    public void startRetrievingData(List<Equipment> equipmentToRetrieveDataFrom) throws ContractException, IOException, InterruptedException, TimeoutException {
        for (Equipment equipment : equipmentToRetrieveDataFrom) {
            startRetrievingData(equipment);
        }
    }

    public void startRetrievingData(Equipment equipmentToRetrieveDataFrom) throws ContractException, IOException, InterruptedException, TimeoutException {
        addState(equipmentToRetrieveDataFrom.getId());

        List<com.itmo.owner.controller.dto.cc.Sensor> sensorsToListen = new ArrayList<>();

        for(Sensor sensor : equipmentToRetrieveDataFrom.getSensors()) {
            sensorsToListen.add(modelMapper.map(sensor, com.itmo.owner.controller.dto.cc.Sensor.class));
        }

        listeningSensors.put(equipmentToRetrieveDataFrom.getId(), sensorsToListen);
        equipmentDegradationStage.put(equipmentToRetrieveDataFrom.getId(), new Boolean[equipmentToRetrieveDataFrom.getSensors().size()]);

        for (Sensor sensor : equipmentToRetrieveDataFrom.getSensors()) {
            startRetrievingData(sensor);
        }
    }

    private void addState(Long id) throws IOException, ContractException {
        Contract contract = networkService.accessNetwork();

        com.itmo.owner.controller.dto.cc.Equipment equipment = com.itmo.owner.controller.dto.cc.Equipment.deserialize(
                new String(contract.evaluateTransaction("queryEquipment", id.toString()), StandardCharsets.UTF_8)
        );

        equipmentState.put(id, equipment.getEquipmentState());
    }

    private void startRetrievingData(Sensor sensorToRetrieveDataFrom) {
        String topicName = sensorToRetrieveDataFrom.getEquipment().getId() + "/" + sensorToRetrieveDataFrom.getId();

        try {
            //MQTTClient.getMqttClient().subscribe(topicName, (topic, msg) -> {
            //mqttClient.subscribe(topicName, (topic, msg) -> {
            MQTTClient.MQTT_CLIENT.subscribe(topicName, (topic, msg) -> {
                int sensorNumber = 0;

                for (Sensor sensor : sensorToRetrieveDataFrom.getEquipment().getSensors()) {
                    if (sensor.getId() < sensorToRetrieveDataFrom.getId()) {
                        sensorNumber++;
                    }
                }

                double payloadValue = toDouble(msg.getPayload());

                com.itmo.owner.controller.dto.cc.Equipment equipmentToUpdate = modelMapper.map(
                        sensorToRetrieveDataFrom.getEquipment(), com.itmo.owner.controller.dto.cc.Equipment.class
                );

                List<com.itmo.owner.controller.dto.cc.Sensor> equipmentSensors = listeningSensors.get(equipmentToUpdate.getEquipmentId());
                com.itmo.owner.controller.dto.cc.Sensor equipmentSensor = equipmentSensors.get(sensorNumber);
                equipmentSensor.setIndication(payloadValue);
                equipmentSensors.set(sensorNumber, equipmentSensor);
                listeningSensors.put(equipmentToUpdate.getEquipmentId(), equipmentSensors);

                if (payloadValue > sensorToRetrieveDataFrom.getRangeMax()) {
                    log.warn("ВНИМАНИЕ!!! Показание({} {}) {}_{} оборудования {}_{} превысило максимально допустимое значение({} {}).",
                            payloadValue,
                            sensorToRetrieveDataFrom.getUnit(),
                            sensorToRetrieveDataFrom.getSensorType(),
                            sensorToRetrieveDataFrom.getId(),
                            sensorToRetrieveDataFrom.getEquipment().getEquipmentType(),
                            sensorToRetrieveDataFrom.getEquipment().getId(),
                            sensorToRetrieveDataFrom.getRangeMax(),
                            sensorToRetrieveDataFrom.getUnit()
                    );

                    updateStateAndIndications(sensorToRetrieveDataFrom.getEquipment().getId(), sensorNumber);
                } else if (payloadValue < sensorToRetrieveDataFrom.getRangeMin()) {
                    log.warn("ВНИМАНИЕ!!! Показание({} {}) {}_{} оборудования {}_{} упало ниже предельно допуcтимой отметки({} {}).",
                            payloadValue,
                            sensorToRetrieveDataFrom.getUnit(),
                            sensorToRetrieveDataFrom.getSensorType(),
                            sensorToRetrieveDataFrom.getId(),
                            sensorToRetrieveDataFrom.getEquipment().getEquipmentType(),
                            sensorToRetrieveDataFrom.getEquipment().getId(),
                            sensorToRetrieveDataFrom.getRangeMin(),
                            sensorToRetrieveDataFrom.getUnit()
                    );

                    updateStateAndIndications(sensorToRetrieveDataFrom.getEquipment().getId(), sensorNumber);
                }
            });
            log.info("Оформлена подписка на MQTT-топик {}.", topicName);
        } catch (MqttException e) {
            log.error("Ошибка при попытке получения данных с MQTT-топика {}.", topicName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopRetrievingData(String topicName) {
        try {
            MQTTClient.MQTT_CLIENT.unsubscribe(topicName);
            //mqttClient.unsubscribe(topicName);
            //MQTTClient.getMqttClient().unsubscribe(topicName);
            log.info("Успешно отписался от {} MQTT-топика.", topicName);
        } catch (MqttException e) {
            log.error("Ошибка при попытке отписаться от {} MQTT-топика.", topicName);
        }
    }

    private void updateStateAndIndications(Long equipmentId, int sensorNumber) {
        try {
            EquipmentState state = equipmentState.get(equipmentId);

            if (!state.equals(EquipmentState.НЕИСПРАВНО) && !state.equals(EquipmentState.ВЫВЕДЕНО_ИЗ_ЭКСПЛУАТАЦИИ) &&
                    !state.equals(EquipmentState.ОТКЛЮЧЕНО)) {
                List<com.itmo.owner.controller.dto.cc.Sensor> equipmentSensors = listeningSensors.get(equipmentId);

                final boolean[] readyForWriting = {false};

                int indicationsPresent = 0;

                for(com.itmo.owner.controller.dto.cc.Sensor sensor : equipmentSensors) {
                    if (sensor.getIndication() != null) {
                        indicationsPresent++;
                    }

                    if (indicationsPresent == equipmentSensors.size()) {
                        readyForWriting[0] = true;
                        break;
                    }
                }

                if (readyForWriting[0]) {
                    Contract contract = networkService.accessNetwork();

                    if (LocalTime.now().getMinute() == 0) {

                        contract.submitTransaction("updateIndications", equipmentId.toString(), genson.serialize(equipmentSensors));

                        if (state.equals(EquipmentState.ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ)) {
                            equipmentState.put(equipmentId, EquipmentState.НОРМАЛЬНАЯ_РАБОТА);

                            log.info("Оборудование с ID " + equipmentId + " успешно переведено в режим " + EquipmentState.НОРМАЛЬНАЯ_РАБОТА);

                            return;
                        }

                        log.info("Записаны новые показания датчиков оборудования с ID " + equipmentId + " в блокчейн");

                        return;
                    }

                    if (state.equals(EquipmentState.ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ)) {
                        contract.submitTransaction("setOperatingState", equipmentId.toString(), genson.serialize(equipmentSensors));

                        equipmentState.put(equipmentId, EquipmentState.НОРМАЛЬНАЯ_РАБОТА);

                        log.info("Оборудование с ID " + equipmentId + " успешно переведено в режим " + EquipmentState.НОРМАЛЬНАЯ_РАБОТА);

                        return;
                    }

                    Boolean[] degradationStage = equipmentDegradationStage.get(equipmentId);
                    degradationStage[sensorNumber] = true;

                    equipmentDegradationStage.put(equipmentId, degradationStage);

                    int numberOfDegraded = 0;

                    for (Boolean sensorState : degradationStage) {
                        if (sensorState) {
                            numberOfDegraded++;
                        }
                    }

                    if (equipmentSensors.size() == numberOfDegraded) {
                        contract.submitTransaction("updateIndicationsWithFailed", equipmentId.toString(), genson.serialize(equipmentSensors));

                        equipmentState.put(equipmentId, EquipmentState.НЕИСПРАВНО);

                        log.info("Новый статус оборудования: {}", EquipmentState.НЕИСПРАВНО);
                    } else {
                        contract.submitTransaction("updateIndicationsWithDegradedOperating", equipmentId.toString(), genson.serialize(equipmentSensors));

                        equipmentState.put(equipmentId, EquipmentState.РЕЖИМ_ОГРАНИЧЕННОЙ_ФУНКЦИОНАЛЬНОСТИ);

                        log.info("Новый статус оборудования: {}", EquipmentState.РЕЖИМ_ОГРАНИЧЕННОЙ_ФУНКЦИОНАЛЬНОСТИ);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double toDouble(byte[] bytes) {
        return Double.parseDouble(String.format("%.2f", ByteBuffer.wrap(bytes).getDouble()));
    }
}
