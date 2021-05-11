package com.itmo.owner.service.mqtt;

import com.itmo.owner.domain.entity.Sensor;
import com.itmo.owner.domain.entity.SensorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorEmulator {
    private final Map<String, ScheduledFuture<?>> emulatorsTaskList;

    private ScheduledExecutorService executorService;

    @Value("${mqtt.sensor-emulator.excess-probability}")
    private double excessProbability;

    public void startEmulatingSensors(List<Sensor> sensors) {
        executorService = Executors.newScheduledThreadPool(sensors.size());

        for (Sensor sensor : sensors) {
            startEmulatingSensor(sensor);
        }
    }

    public void startEmulatingSensor(Sensor sensor) {
        String topicName = sensor.getEquipment().getId() + "/" + sensor.getId();

        emulatorsTaskList.put(topicName, executorService.scheduleAtFixedRate(() -> {
            double indication = generateIndication(sensor);

            try {
                TopicWriter.publishMessage(topicName, toByteArray(indication));
            } catch (MqttException e) {
                log.error("Не удалось опубликовать сообщение в топик {}", topicName);
            }
        }, 0, 1, TimeUnit.SECONDS));

        log.info("Запущена эмуляция сенсора {}_{} оборудования {}_{}.", sensor.getSensorType(), sensor.getId(), sensor.getEquipment().getEquipmentType(), sensor.getEquipment().getId());
    }

    public void stopEmulatingSensor(Sensor sensor) throws NoSuchFieldException {
        String topicName = sensor.getEquipment().getId() + "/" + sensor.getId();

        ScheduledFuture<?> task = emulatorsTaskList.get(topicName);
        if (task != null) {
            emulatorsTaskList.get(topicName).cancel(true);
        } else {
            throw new NoSuchFieldException("Не удалось отписаться от топика {}: топик с указанным именем не существует.");
        }
    }

    private double generateIndication(Sensor sensor) {
        int rangeMin = sensor.getRangeMin();
        int rangeMax = sensor.getRangeMax();

        ThreadLocalRandom random = ThreadLocalRandom.current();
        double randomNumber = random.nextDouble(0.0, 100+1e-12);

        if (randomNumber <= excessProbability) { // Если правда, то генерируем значение ниже или выше диапазона нормальной работы датчика
            int belowOrAboveRange = random.nextInt(1, 3);
            if (belowOrAboveRange == 1) {
                double lowerBorder = rangeMin - rangeMax / 4.0;
                if(lowerBorder > 0 || sensor.getSensorType().equals(SensorType.ДАТЧИК_ТЕМПЕРАТУРЫ)) {
                    return random.nextDouble(lowerBorder, rangeMin);
                } else {
                    return random.nextDouble(0.0, rangeMin);
                }
            } else {
                return random.nextDouble(rangeMax + 1e-12, rangeMax + rangeMax / 4.0 + 1e-12);
            }
        } else if (randomNumber <= excessProbability * 8) { // Если правда, то генерируем значение в верхнем или нижнем диапазоне работы датчика
            int lowerOrUpperRange = random.nextInt(1, 3);
            int rangeQuarter = (rangeMax - rangeMin) / 4;
            if (lowerOrUpperRange == 1) {
                return random.nextDouble(rangeMin, rangeMin + rangeQuarter);
            } else {
                return random.nextDouble(rangeMax - rangeQuarter + 1e-12, rangeMax + 1e-12);
            }
        } else { // Во всех остальных случаях генерируем значения в средней части диапазона
            int rangeQuarter = (rangeMax - rangeMin) / 4;
            return random.nextDouble(rangeMin + rangeQuarter, rangeMax - rangeQuarter + 1e-12);
        }
    }

    private byte[] toByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }
}
