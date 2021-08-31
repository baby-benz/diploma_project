package com.itmo.smartcontract;

import com.itmo.smartcontract.entity.dto.EquipmentDto;
import com.itmo.smartcontract.entity.equipment.Equipment;
import com.itmo.smartcontract.entity.equipment.EquipmentState;
import com.itmo.smartcontract.entity.equipment.MaintenanceStatus;
import com.itmo.smartcontract.entity.equipment.EquipmentLifecycle;
import com.itmo.smartcontract.entity.equipment.Sensor;
import com.itmo.smartcontract.ledgerapi.State;
import com.itmo.smartcontract.ledgerapi.StateHistory;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import lombok.NoArgsConstructor;
import org.hyperledger.fabric.Logger;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Contract(name = "com.itmo.equipment", info = @Info(title = "Смартконтракт КВТО", description = "", version = "0.0.1", license = @License(name = "SPDX-License-Identifier: ", url = ""), contact = @Contact(email = "amazingdude@niuitmo.ru", name = "Никита")))
@Default
@NoArgsConstructor
public class EquipmentContract implements ContractInterface {
    private final static Logger LOG = Logger.getLogger(EquipmentContract.class.getName());
    private final Genson genson = new GensonBuilder().withConverter(new LocalDateConverter(), LocalDate.class).create();

    @Override
    public Context createContext(ChaincodeStub stub) {
        return new EquipmentContext(stub);
    }

    // Первоначальное заполнение данными (если необходимо)
    @SuppressWarnings("unused")
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void instantiate(EquipmentContext ctx) {
        String[] equipmentData = {
                "{ \"equipmentId\": \"1\", \"equipmentSerialNumber\": \"ADN123845G\", \"equipmentType\": \"ВИНТОВОЙ_ЗАБОЙНЫЙ_ДВИГАТЕЛЬ\", \"sensors\": [ { \"id\": 1, \"sensorType\": \"ДАТЧИК_ОБОРОТОВ\", \"unit\": \"об/мин\", \"indication\": null }, { \"id\": 1, \"sensorType\": \"ДАТЧИК_LFDKTYBZ\", \"unit\": \"об/мин\", \"indication\": null }, { \"id\": 1, \"sensorType\": \"ДАТЧИК_ОБОРОТОВ\", \"unit\": \"об/мин\", \"indication\": null } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": null, \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазНадзор\", \"serviceOrg\": \"НефтьКазСервис\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"document\": null, \"description\": \"\" }",
                "{ \"equipmentId\": \"2\", \"equipmentSerialNumber\": \"DMD150CR\", \"equipmentType\": \"КОМПРЕССОР\", \"sensors\": [ { \"id\": 1, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\", \"indication\": \"\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазНадзор\", \"serviceOrg\": \"НефтьКазСервис\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"document\": \"\", \"description\": \"\" }",
                "{ \"equipmentId\": \"3\", \"equipmentSerialNumber\": \"10740\", \"equipmentType\": \"ВИНТОВОЙ_ЗАБОЙНЫЙ_ДВИГАТЕЛЬ\", \"sensors\": [ { \"id\": 1, \"sensorType\": \"ДАТЧИК_ОБОРОТОВ\", \"unit\": \"об/мин\", \"indication\": \"\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазНадзор\", \"serviceOrg\": \"НефтьКазСервис\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"document\": \"\", \"description\": \"\" }",
                "{ \"equipmentId\": \"4\", \"equipmentSerialNumber\": \"G6424432749T\", \"equipmentType\": \"КОМПРЕССОР\", \"sensors\": [ { \"id\": 1, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\", \"indication\": \"\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазИнспекция\", \"serviceOrg\": \"Каз-Ремонт\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"document\": \"\", \"description\": \"\" }",
                "{ \"equipmentId\": \"5\", \"equipmentSerialNumber\": \"КК27\", \"equipmentType\": \"РВС\", \"sensors\": [ { \"id\": 1, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"Па\", \"indication\": \"\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазИнспекция\", \"serviceOrg\": \"Каз-Ремонт\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"document\": \"\", \"description\": \"\" }",
                "{ \"equipmentId\": \"6\", \"equipmentSerialNumber\": \"УТ210А\", \"equipmentType\": \"УЧАСТОК_ТРУБОПРОВОДА\", \"sensors\": [ { \"id\": 1, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\", \"indication\": \"\" } ],  \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазИнспекция\", \"serviceOrg\": \"Каз-Ремонт\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"document\": \"\", \"description\": \"\" }",
                "{ \"equipmentId\": \"7\", \"equipmentSerialNumber\": \"УТ211А\", \"equipmentType\": \"УЧАСТОК_ТРУБОПРОВОДА\", \"sensors\": [ { \"id\": 1, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\", \"indication\": \"\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазИнспекция\", \"serviceOrg\": \"Каз-Ремонт\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"document\": \"\", \"description\": \"\" }",
                "{ \"equipmentId\": \"8\", \"equipmentSerialNumber\": \"GP0080Z\", \"equipmentType\": \"КОМПРЕССОР\", \"sensors\": [ { \"id\": 1, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\", \"indication\": \"\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазИнспекция\", \"serviceOrg\": \"ГазпромСнаб\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"document\": \"\", \"description\": \"\" }",
                "{ \"equipmentId\": \"9\", \"equipmentSerialNumber\": \"АГР7403Ж\", \"equipmentType\": \"КОМПРЕССОР\", \"sensors\": [ { \"id\": 1, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\", \"indication\": \"\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпромнефть-Приразломное\", \"operatingOrg\": \"Газпромнефть Марин Бункер\", \"inspectionOrg\": \"Нефть Мор Надзор\", \"serviceOrg\": \"Нелин Сервис\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"document\": \"\", \"description\": \"\" }",
                "{ \"equipmentId\": \"10\", \"equipmentSerialNumber\": \"GP1067Q\", \"equipmentType\": \"КОМПРЕССОР\", \"sensors\": [ { \"id\": 1, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\", \"indication\": \"\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпромнефть-Приразломное\", \"operatingOrg\": \"Газпромнефть Марин Бункер\", \"inspectionOrg\": \"Нефть Мор Надзор\", \"serviceOrg\": \"Нелин Сервис\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"document\": \"\", \"description\": \"\" }"
        };

        LOG.info("Заполняю первоначальными данными...");

        for (String equipmentDatum : equipmentData) {
            ctx.equipmentList.addEquipment(Equipment.deserialize(equipmentDatum));
        }
    }

    @SuppressWarnings("unused")
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String queryEquipment(EquipmentContext ctx, Long equipmentId) {
        String key = Equipment.makeKey(new String[]{equipmentId.toString()});

        Equipment equipment = ctx.equipmentList.getEquipment(key);

        if (equipment == null) {
            String errorMessage = String.format("Оборудование с ID %s не существует", key);
            LOG.error(errorMessage);
            throw new ChaincodeException(errorMessage, "ОБОРУДОВАНИЕ_НЕ_НАЙДЕНО");
        }

        return genson.serialize(equipment);
    }

    @SuppressWarnings("unused")
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String queryEquipmentHistory(EquipmentContext ctx, Long equipmentId) {
        String key = Equipment.makeKey(new String[]{equipmentId.toString()});

        List<StateHistory> equipment = ctx.equipmentList.getEquipmentHistory(key);

        if (equipment.isEmpty()) {
            String errorMessage = String.format("Оборудование с ID %s не существует", key);
            LOG.error(errorMessage);
            throw new ChaincodeException(errorMessage, "ОБОРУДОВАНИЕ_НЕ_НАЙДЕНО");
        }

        String serialized = genson.serialize(equipment);

        LOG.info(serialized);

        return genson.serialize(equipment);
    }

    @SuppressWarnings("unused")
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String queryAllEquipment(EquipmentContext ctx) {
        List<Equipment> equipment = ctx.equipmentList.getAllEquipment();

        if (equipment.isEmpty()) {
            String errorMessage = "В реестре нет ни одной записи об оборудовании";
            LOG.error(errorMessage);
            throw new ChaincodeException(errorMessage, "ПУСТОЙ_РЕЕСТР");
        }

        return genson.serialize(equipment);
    }

    @SuppressWarnings("unused")
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String commission(EquipmentContext ctx, EquipmentDto equipment) {
        Equipment equipmentToSave = Equipment.deserialize(genson.serialize(equipment));

        equipmentToSave.setEquipmentState(EquipmentState.ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ);

        // Добавляем оборудование к списку всех equipment в world state реестра
        ctx.equipmentList.addEquipment(equipmentToSave);
        // Возвращаем сериализованный объект приложению, которое вызвало этот метод
        return genson.serialize(equipmentToSave);
    }

    @SuppressWarnings({"unused", "UnusedReturnValue"})
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String setOperatingState(EquipmentContext ctx, Long equipmentId, Sensor[] sensors) {
        // Извлекаем данное оборудование из world state, используя ключ
        String equipmentKey = State.makeKey(new String[]{equipmentId.toString()});
        Equipment equipment = ctx.equipmentList.getEquipment(equipmentKey);

        // Переводим состояние в НОРМАЛЬНАЯ_РАБОТА
        if (Objects.equals(equipment.getEquipmentState(), EquipmentState.ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ) ||
                Objects.equals(equipment.getEquipmentState(), EquipmentState.НОРМАЛЬНАЯ_РАБОТА)) {
            equipment.setEquipmentState(EquipmentState.НОРМАЛЬНАЯ_РАБОТА);
        } else {
            throw new IllegalArgumentException("Оборудование не находится в подходящем состоянии (" +
                    equipment.getEquipmentState() + ") для перевода в состояние " + EquipmentState.НОРМАЛЬНАЯ_РАБОТА);
        }

        equipment.setSensors(sensors);

        // Обновляем оборудование в world state
        ctx.equipmentList.updateEquipment(equipment);

        return genson.serialize(equipment);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String updateIndications(EquipmentContext ctx, Long equipmentId, Sensor[] sensors) {
        return genson.serialize(setIndications(ctx, equipmentId, sensors));
    }

    @SuppressWarnings("unused")
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String updateIndicationsWithDegradedOperating(EquipmentContext ctx, Long equipmentId, Sensor[] sensors) {
        Equipment equipment = setIndications(ctx, equipmentId, sensors);

        equipment.setEquipmentState(EquipmentState.РЕЖИМ_ОГРАНИЧЕННОЙ_ФУНКЦИОНАЛЬНОСТИ);

        // Обновляем оборудование в world state
        ctx.equipmentList.updateEquipment(equipment);

        return genson.serialize(equipment);
    }

    @SuppressWarnings("unused")
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String updateIndicationsWithFailed(EquipmentContext ctx, Long equipmentId, Sensor[] sensors) {
        Equipment equipment = setIndications(ctx, equipmentId, sensors);

        equipment.setEquipmentState(EquipmentState.НЕИСПРАВНО);

        // Обновляем оборудование в world state
        ctx.equipmentList.updateEquipment(equipment);

        return genson.serialize(equipment);
    }

    @SuppressWarnings("unused")
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String startCorrectiveMaintenance(EquipmentContext ctx, Long equipmentId, String serviceOrg) {
        return startMaintenance(ctx, equipmentId, serviceOrg, MaintenanceStatus.КОРРЕКТИРУЮЩЕЕ_ОБСЛУЖИВАНИЕ);
    }

    @SuppressWarnings("unused")
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String startConditionBasedMaintenance(EquipmentContext ctx, Long equipmentId, String serviceOrg) {
        return startMaintenance(ctx, equipmentId, serviceOrg, MaintenanceStatus.ОБСЛУЖИВАНИЕ_ПО_СОСТОЯНИЮ);
    }

    @SuppressWarnings("unused")
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String startPlannedMaintenance(EquipmentContext ctx, Long equipmentId, String serviceOrg) {
        return startMaintenance(ctx, equipmentId, serviceOrg, MaintenanceStatus.ЗАПЛАНИРОВАННОЕ_ОБСЛУЖИВАНИЕ);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    private String startMaintenance(EquipmentContext ctx, Long equipmentId, String serviceOrg, MaintenanceStatus maintenanceStatus) {
        // Извлекаем данное оборудование из world state, используя ключ
        String equipmentKey = State.makeKey(new String[]{equipmentId.toString()});
        Equipment equipment = ctx.equipmentList.getEquipment(equipmentKey);

        if (!equipment.getRelatedOrganizations().getServiceOrg().equals(serviceOrg)) {
            throw new IllegalArgumentException("Оборудование с ID:" + equipmentKey + " не обслуживается организацией " + serviceOrg);
        }

        equipment.setEquipmentState(EquipmentState.ОТКЛЮЧЕНО);

        equipment.setMaintenanceStatus(maintenanceStatus);

        if(equipment.getDocument() != null) {
            equipment.setDocument(null);
        }

        Sensor[] sensors = equipment.getSensors();
        for(Sensor sensor : sensors) {
            sensor.setIndication(null);
        }
        equipment.setSensors(sensors);

        // Обновляем оборудование в world state
        ctx.equipmentList.updateEquipment(equipment);

        return genson.serialize(equipment);
    }

    @SuppressWarnings("unused")
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String finishMaintenance(EquipmentContext ctx, Long equipmentId, String serviceOrg, String document) {
        // Извлекаем данное оборудование из world state, используя ключ
        String equipmentKey = State.makeKey(new String[]{equipmentId.toString()});
        Equipment equipment = ctx.equipmentList.getEquipment(equipmentKey);

        if (equipment.getEquipmentState().equals(EquipmentState.ОТКЛЮЧЕНО)) {
            equipment.setEquipmentState(EquipmentState.НОРМАЛЬНАЯ_РАБОТА);
        } else {
            throw new IllegalArgumentException("Оборудование не находится в подходящем состоении для перевода в состояние " + EquipmentState.НОРМАЛЬНАЯ_РАБОТА);
        }

        equipment.setDocument(document);

        equipment.setMaintenanceStatus(null);

        // Обновляем оборудование в world state
        ctx.equipmentList.updateEquipment(equipment);

        return genson.serialize(equipment);
    }

    @SuppressWarnings("unused")
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String decommission(EquipmentContext ctx, Long equipmentId, String ownerOrg, String EOLDate) {
        String equipmentKey = Equipment.makeKey(new String[]{equipmentId.toString()});
        Equipment equipment = ctx.equipmentList.getEquipment(equipmentKey);

        // Провреям оборудование на предмет уже совершившегося вывода из эксплуатации
        if (Objects.equals(equipment.getEquipmentState(), EquipmentState.ВЫВЕДЕНО_ИЗ_ЭКСПЛУАТАЦИИ)) {
            throw new RuntimeException("Оборудование с ID:" + equipmentKey + " уже выведено из эксплуатации");
        }

        // Проверяем, что организация-владелец имеет отношение к данному оборудованию
        if (!equipment.getRelatedOrganizations().getOwnerOrg().equals(ownerOrg)) {
            throw new RuntimeException("Организация " + ownerOrg + " не владеет оборудованием с ID:" + equipmentKey);
        }

        // Проверяем, что дата не нулевая
        if (EOLDate == null) {
            throw new RuntimeException("Нулевая дата вывода из эксплуатации");
        }

        equipment.setEquipmentState(EquipmentState.ВЫВЕДЕНО_ИЗ_ЭКСПЛУАТАЦИИ);

        EquipmentLifecycle currentLifecycle = equipment.getEquipmentLifecycle();
        currentLifecycle.setEOLDate(LocalDate.parse(EOLDate).toString());
        equipment.setEquipmentLifecycle(currentLifecycle);

        if(equipment.getDocument() != null) {
            equipment.setDocument(null);
        }

        Sensor[] sensors = equipment.getSensors();
        for(Sensor sensor : sensors) {
            sensor.setIndication(null);
        }
        equipment.setSensors(sensors);

        ctx.equipmentList.updateEquipment(equipment);

        return genson.serialize(equipment);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void deleteEquipment(EquipmentContext ctx, Long equipmentId, String ownerOrg) {
        String equipmentKey = Equipment.makeKey(new String[]{equipmentId.toString()});
        Equipment equipment = ctx.equipmentList.getEquipment(equipmentKey);

        // Проверяем, что организация-владелец имеет отношение к данному оборудованию
        if (!equipment.getRelatedOrganizations().getOwnerOrg().equals(ownerOrg)) {
            throw new RuntimeException("Организация " + ownerOrg + " не владеет оборудованием с ID:" + equipmentKey);
        }

        ctx.equipmentList.deleteEquipment(equipmentKey);
    }

    private Equipment setIndications(EquipmentContext ctx, Long equipmentId, Sensor[] sensors) {
        // Извлекаем данное оборудование из world state, используя ключ
        String equipmentKey = State.makeKey(new String[]{equipmentId.toString()});
        Equipment equipment = ctx.equipmentList.getEquipment(equipmentKey);

        if(Objects.equals(equipment.getEquipmentState(), EquipmentState.ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ)) {
            setOperatingState(ctx, equipmentId, sensors);
        }

        if (!Objects.equals(equipment.getEquipmentState(), EquipmentState.НОРМАЛЬНАЯ_РАБОТА) &&
                !Objects.equals(equipment.getEquipmentState(), EquipmentState.РЕЖИМ_ОГРАНИЧЕННОЙ_ФУНКЦИОНАЛЬНОСТИ)) {
            throw new IllegalArgumentException("Оборудование не находится в подходящем состоянии (" +
                    equipment.getEquipmentState() + ") для записи показаний с датчиков");
        }

        if(equipment.getSensors().length != sensors.length) {
            throw new IllegalArgumentException("Количество датчиков для обновления показаний для оборудования с ID " + equipmentId + " не совпадает с количеством переданных показаний");
        }
        List<Boolean> isPresented = new ArrayList<>();

        for(Sensor newSensor : sensors) {
            for(Sensor oldSensor : equipment.getSensors()) {
                if (oldSensor.getId().equals(newSensor.getId())) {
                    isPresented.add(true);
                    break;
                }
            }
        }

        if(isPresented.size() != sensors.length) {
            throw new IllegalArgumentException("Переданные для обновления показаний датчики не соответствуют датчикам оборудования с ID " + equipmentId);
        }

        if(equipment.getDocument() != null) {
            equipment.setDocument(null);
        }

        equipment.setSensors(sensors);

        return equipment;
    }
}
