package com.itmo.owner.service.mqtt;

import com.itmo.owner.domain.entity.Equipment;
import com.itmo.owner.domain.entity.Sensor;
import com.itmo.owner.domain.entity.SensorType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
//@Service
//@RequiredArgsConstructor
@UtilityClass
public class SensorEmulator {
    private final Map<Long, ScheduledFuture<?>> emulatorsTaskList = new HashMap<>();
    //private final TopicWriter topicWriter;
    private ScheduledExecutorService executorService;

    private static final double EXCESS_PROBABILITY = 90;
    private static final String FAILURE_MODE = "degraded";

    public void startEmulatingSensors(List<Equipment> equipmentList) {
        executorService = Executors.newScheduledThreadPool(equipmentList.size());

        for (Equipment equipment : equipmentList) {
            startEmulatingSensors(equipment);
        }
    }

    public void startEmulatingSensors(Equipment equipment) {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //emulatorsTaskList.put(equipment, executorService.scheduleAtFixedRate(new IndicationGenerator(equipmentToGenerate, new TopicWriter()),
        emulatorsTaskList.put(equipment.getId(), executorService.scheduleAtFixedRate(new IndicationGenerator(equipment),
                /*generateIndication(equipmentToGenerate),*/ 0, 1, TimeUnit.SECONDS)
        );

        log.info("Запущена эмуляция сенсоров оборудования {}_{}.", equipment.getEquipmentType(), equipment.getId());
    }

    public void stopEmulatingSensors(Equipment equipment) throws NoSuchFieldException {
        for (Sensor sensor : equipment.getSensors()) {
            ScheduledFuture<?> task = emulatorsTaskList.get(sensor.getEquipment().getId());
            if (task != null) {
                emulatorsTaskList.get(sensor.getEquipment().getId()).cancel(true);
            } else {
                String topicName = sensor.getEquipment().getId() + "/" + sensor.getId();
                throw new NoSuchFieldException("Не удалось отписаться от топика " + topicName + " : топик с указанным именем не существует.");
            }
        }
    }

    /*private void generateIndication(Equipment equipment) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        log.info("Equipment " + equipment);

        if (failureMode.equals("degraded")) {
            double randomNumber = random.nextDouble(0.0, 100 + 1e-12);

            //Set<Sensor> sensors = equipment.getSensors();
            List<Sensor> sensors = List.copyOf(equipment.getSensors());

            if (randomNumber <= EXCESS_PROBABILITY) { // Если правда, то генерируем значение ниже или выше диапазона нормальной работы датчика
                for (Sensor sensor : sensors) {
                    String topicName = equipment.getId() + "/" + sensor.getId();

                    writeMessage(topicName, generateOutOfRangeIndication(sensor));
                }
            } else if (randomNumber <= EXCESS_PROBABILITY * 4) { // Если правда, то генерируем значение в верхнем или нижнем диапазоне работы датчика
                for (Sensor sensor : equipment.getSensors()) {
                    String topicName = equipment.getId() + "/" + sensor.getId();

                    writeMessage(topicName, generateBorderIndication(sensor));
                }
            } else { // Во всех остальных случаях генерируем значения в средней части диапазона
                for (Sensor sensor : equipment.getSensors()) {
                    String topicName = equipment.getId() + "/" + sensor.getId();

                    writeMessage(topicName, generateNormalIndication(sensor));
                }
            }
        } else if (failureMode.equals("failed")) {
            for (Sensor sensor : equipment.getSensors()) {
                double randomNumber = random.nextDouble(0.0, 100 + 1e-12);

                String topicName = equipment.getId() + "/" + sensor.getId();

                if (randomNumber <= EXCESS_PROBABILITY) { // Если правда, то генерируем значение ниже или выше диапазона нормальной работы датчика
                    writeMessage(topicName, generateOutOfRangeIndication(sensor));
                } else if (randomNumber <= EXCESS_PROBABILITY * 4) { // Если правда, то генерируем значение в верхнем или нижнем диапазоне работы датчика
                    writeMessage(topicName, generateBorderIndication(sensor));
                } else { // Во всех остальных случаях генерируем значения в средней части диапазона
                    writeMessage(topicName, generateNormalIndication(sensor));
                }
            }
        }
    }*/

    private double generateOutOfRangeIndication(Sensor sensor) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int rangeMin = sensor.getRangeMin();
        int rangeMax = sensor.getRangeMax();

        int belowOrAboveRange = random.nextInt(1, 3);
        if (belowOrAboveRange == 1) {
            double lowerBorder = rangeMin - rangeMax / 4.0;
            if (lowerBorder > 0 || sensor.getSensorType().equals(SensorType.ДАТЧИК_ТЕМПЕРАТУРЫ)) {
                return random.nextDouble(lowerBorder, rangeMin);
            } else if (rangeMin > 0) {
                return random.nextDouble(0.0, rangeMin);
            } else {
                return 0;
            }
        } else {
            if (rangeMax == 0) {
                return random.nextDouble(rangeMax + 1e-12, rangeMax + 15);
            } else {
                return random.nextDouble(rangeMax + 1e-12, rangeMax + rangeMax / 4.0 + 1e-12);
            }
        }
    }

    private double generateBorderIndication(Sensor sensor) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int rangeMin = sensor.getRangeMin();
        int rangeMax = sensor.getRangeMax();

        int lowerOrUpperRange = random.nextInt(1, 3);

        int rangeQuarter = (rangeMax - rangeMin) / 4;
        if (rangeQuarter != 0) {
            if (lowerOrUpperRange == 1) {
                return random.nextDouble(rangeMin, rangeMin + rangeQuarter);
            } else {
                return random.nextDouble(rangeMax - rangeQuarter + 1e-12, rangeMax + 1e-12);
            }
        } else {
            return 0;
        }
    }

    private double generateNormalIndication(Sensor sensor) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int rangeMin = sensor.getRangeMin();
        int rangeMax = sensor.getRangeMax();

        int rangeBorder = (rangeMax - rangeMin) * 49 / 100;
        return random.nextDouble(rangeMin + rangeBorder, rangeMax - rangeBorder + 1e-12);
    }

    private static class IndicationGenerator implements Runnable {
        private final Equipment equipment;
        //private final TopicWriter writer;

        //IndicationGenerator(Equipment equipment, TopicWriter writer) {
        IndicationGenerator(Equipment equipment) {
            this.equipment = equipment;
            //this.writer = writer;
        }

        @Override
        public void run() {
            try {
                ThreadLocalRandom random = ThreadLocalRandom.current();

                if (FAILURE_MODE.equals("degraded")) {
                    double randomNumber = random.nextDouble(0.0, 100 + 1e-12);

                    //Set<Sensor> sensors = equipment.getSensors();
                    //List<Sensor> sensors = List.copyOf(equipment.getSensors());

                    if (randomNumber <= EXCESS_PROBABILITY) { // Если правда, то генерируем значение ниже или выше диапазона нормальной работы датчика
                        //for (Sensor sensor : sensors) {
                        for (Sensor sensor : equipment.getSensors()) {
                            String topicName = equipment.getId() + "/" + sensor.getId();

                            //writeMessage(topicName, generateOutOfRangeIndication(sensor), writer);
                            writeMessage(topicName, generateOutOfRangeIndication(sensor));
                        }
                    } else if (randomNumber <= EXCESS_PROBABILITY * 4) { // Если правда, то генерируем значение в верхнем или нижнем диапазоне работы датчика
                        for (Sensor sensor : equipment.getSensors()) {
                            String topicName = equipment.getId() + "/" + sensor.getId();

                            //writeMessage(topicName, generateBorderIndication(sensor), writer);
                            writeMessage(topicName, generateBorderIndication(sensor));
                        }
                    } else { // Во всех остальных случаях генерируем значения в средней части диапазона
                        for (Sensor sensor : equipment.getSensors()) {
                            String topicName = equipment.getId() + "/" + sensor.getId();

                            //writeMessage(topicName, generateNormalIndication(sensor), writer);
                            writeMessage(topicName, generateNormalIndication(sensor));
                        }
                    }
                } else if (FAILURE_MODE.equals("failed")) {
                    for (Sensor sensor : equipment.getSensors()) {
                        double randomNumber = random.nextDouble(0.0, 100 + 1e-12);

                        String topicName = equipment.getId() + "/" + sensor.getId();

                        if (randomNumber <= EXCESS_PROBABILITY) { // Если правда, то генерируем значение ниже или выше диапазона нормальной работы датчика
                            //writeMessage(topicName, generateOutOfRangeIndication(sensor), writer);
                            writeMessage(topicName, generateOutOfRangeIndication(sensor));
                        } else if (randomNumber <= EXCESS_PROBABILITY * 4) { // Если правда, то генерируем значение в верхнем или нижнем диапазоне работы датчика
                            //writeMessage(topicName, generateBorderIndication(sensor), writer);
                            writeMessage(topicName, generateBorderIndication(sensor));
                        } else { // Во всех остальных случаях генерируем значения в средней части диапазона
                            //writeMessage(topicName, generateNormalIndication(sensor), writer);
                            writeMessage(topicName, generateNormalIndication(sensor));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //private void writeMessage(String topicName, double indication, TopicWriter writer) {
        private void writeMessage(String topicName, double indication) {
            try {
                //writer.publishMessage(topicName, toByteArray(indication));
                TopicWriter.publishMessage(topicName, toByteArray(indication));
            } catch (MqttException e) {
                log.error("Не удалось опубликовать сообщение в топик {}", topicName);
            }
        }

        private byte[] toByteArray(double value) {
            byte[] bytes = new byte[8];
            ByteBuffer.wrap(bytes).putDouble(value);
            return bytes;
        }
    }
}
