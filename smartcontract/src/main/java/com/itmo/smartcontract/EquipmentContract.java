package com.itmo.smartcontract;

import com.itmo.smartcontract.equipment.*;
import com.itmo.smartcontract.ledgerapi.State;
import lombok.NoArgsConstructor;
import org.hyperledger.fabric.Logger;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Set;

@Contract(name = "com.itmo.equipment", info = @Info(title = "Смартконтракт КВТО", description = "", version = "0.0.1", license = @License(name = "SPDX-License-Identifier: ", url = ""), contact = @Contact(email = "amazingdude@niuitmo.ru", name = "Никита")))
@Default
@NoArgsConstructor
public class EquipmentContract implements ContractInterface {
    private final static Logger LOG = Logger.getLogger(EquipmentContract.class.getName());

    @Override
    public Context createContext(ChaincodeStub stub) {
        return new EquipmentContext(stub);
    }

    // Первоначальное заполнение данными (если необходимо)
    @Transaction
    public void instantiate(EquipmentContext ctx) {
        //LOG.info("Первоначальное заполнение данными не производилось.");
        String[] equipmentData = {
                "{ \"equipmentId\": \"1\", \"equipmentSerialNumber\": \"ADN123845G\", \"equipmentType\": \"ВИНТОВОЙ_ЗАБОЙНЫЙ_ДВИГАТЕЛЬ\", \"sensors\": [ { \"id\": 1, \"rangeMax\": 1750, \"rangeMin\": 100, \"sensorType\": \"ДАТЧИК_ОБОРОТОВ\", \"unit\": \"об/мин\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазНадзор\", \"serviceOrg\": \"НефтьКазСервис\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"description\": \"\" }",
                "{ \"equipmentId\": \"2\", \"equipmentSerialNumber\": \"DMD150CR\", \"equipmentType\": \"КОМПРЕССОР\", \"sensors\": [ { \"id\": 1, \"rangeMax\": 200, \"rangeMin\": 30, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазНадзор\", \"serviceOrg\": \"НефтьКазСервис\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"description\": \"\" }",
                "{ \"equipmentId\": \"3\", \"equipmentSerialNumber\": \"10740\", \"equipmentType\": \"ВИНТОВОЙ_ЗАБОЙНЫЙ_ДВИГАТЕЛЬ\", \"sensors\": [ { \"id\": 1, \"rangeMax\": 3000, \"rangeMin\": 100, \"sensorType\": \"ДАТЧИК_ОБОРОТОВ\", \"unit\": \"об/мин\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазНадзор\", \"serviceOrg\": \"НефтьКазСервис\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"description\": \"\" }",
                "{ \"equipmentId\": \"4\", \"equipmentSerialNumber\": \"G6424432749T\", \"equipmentType\": \"КОМПРЕССОР\", \"sensors\": [ { \"id\": 1, \"rangeMax\": 600, \"rangeMin\": 60, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазИнспекция\", \"serviceOrg\": \"Каз-Ремонт\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"description\": \"\" }",
                "{ \"equipmentId\": \"5\", \"equipmentSerialNumber\": \"КК27\", \"equipmentType\": \"РВС\", \"sensors\": [ { \"id\": 1, \"rangeMax\": 30000, \"rangeMin\": 3000, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"Па\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазИнспекция\", \"serviceOrg\": \"Каз-Ремонт\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"description\": \"\" }",
                "{ \"equipmentId\": \"6\", \"equipmentSerialNumber\": \"УТ210А\", \"equipmentType\": \"УЧАСТОК_ТРУБОПРОВОДА\", \"sensors\": [ { \"id\": 1, \"rangeMax\": 650, \"rangeMin\": 200, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазИнспекция\", \"serviceOrg\": \"Каз-Ремонт\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"description\": \"\" }",
                "{ \"equipmentId\": \"7\", \"equipmentSerialNumber\": \"УТ211А\", \"equipmentType\": \"УЧАСТОК_ТРУБОПРОВОДА\", \"sensors\": [ { \"id\": 1, \"rangeMax\": 650, \"rangeMin\": 200, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазИнспекция\", \"serviceOrg\": \"Каз-Ремонт\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"description\": \"\" }",
                "{ \"equipmentId\": \"8\", \"equipmentSerialNumber\": \"GP0080Z\", \"equipmentType\": \"КОМПРЕССОР\", \"sensors\": [ { \"id\": 1, \"rangeMax\": 500, \"rangeMin\": 75, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпром Нефть Азия\", \"operatingOrg\": \"Гелиос\", \"inspectionOrg\": \"ГазИнспекция\", \"serviceOrg\": \"ГазпромСнаб\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"description\": \"\" }",
                "{ \"equipmentId\": \"9\", \"equipmentSerialNumber\": \"АГР7403Ж\", \"equipmentType\": \"КОМПРЕССОР\", \"sensors\": [ { \"id\": 1, \"rangeMax\": 220, \"rangeMin\": 25, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпромнефть-Приразломное\", \"operatingOrg\": \"Газпромнефть Марин Бункер\", \"inspectionOrg\": \"Нефть Мор Надзор\", \"serviceOrg\": \"Нелин Сервис\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"description\": \"\" }",
                "{ \"equipmentId\": \"10\", \"equipmentSerialNumber\": \"GP1067Q\", \"equipmentType\": \"КОМПРЕССОР\", \"sensors\": [ { \"id\": 1, \"rangeMax\": 550, \"rangeMin\": 100, \"sensorType\": \"ДАТЧИК_ДАВЛЕНИЯ\", \"unit\": \"бар\" } ], \"equipmentLifecycle\": { \"commissioningDate\": \"2021-05-08\", \"EOLDate\": \"\", \"manufactureDate\": \"2021-05-08\" }, \"relatedOrganizations\": { \"ownerOrg\": \"Газпромнефть-Приразломное\", \"operatingOrg\": \"Газпромнефть Марин Бункер\", \"inspectionOrg\": \"Нефть Мор Надзор\", \"serviceOrg\": \"Нелин Сервис\" }, \"equipmentState\": \"ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ\", \"description\": \"\" }"
        };

        /*Set<Sensor> sensors = new HashSet<>();
        Sensor sensor = new Sensor(0L, 10, 40, "бар", SensorType.ДАТЧИК_ДАВЛЕНИЯ);
        sensors.add(sensor);

        EquipmentLifecycle equipmentLifecycle = new EquipmentLifecycle();
        equipmentLifecycle.setManufactureDate(LocalDate.parse("2018-04-05"));
        equipmentLifecycle.setCommissioningDate(LocalDate.parse("2018-05-05"));

        RelatedOrganizations relatedOrganizations = new RelatedOrganizations();
        relatedOrganizations.setOwnerOrg("");
        relatedOrganizations.setOperatingOrg("");

        Equipment equipment = new Equipment(0L, "ADN123845G",
                EquipmentType.ВИНТОВОЙ_ЗАБОЙНЫЙ_ДВИГАТЕЛЬ, sensors, equipmentLifecycle, relatedOrganizations, "Equipment");*/

        LOG.info("Заполняю первоначальными данными...");

        for (String equipmentDatum : equipmentData) {
            ctx.equipmentList.addEquipment(Equipment.deserialize(equipmentDatum.getBytes(StandardCharsets.UTF_8)));
            LOG.info("Успешно записано: \n" + equipmentDatum);
        }
    }

    @Transaction
    public Equipment queryEquipment(EquipmentContext ctx, Long equipmentId) {
        String key = Equipment.makeKey(new String[]{equipmentId.toString()});

        Equipment equipment = ctx.equipmentList.getEquipment(key);

        if (equipment == null) {
            String errorMessage = String.format("Оборудование с ID %s не существует", key);
            LOG.error(errorMessage);
            throw new ChaincodeException(errorMessage, "ОБОРУДОВАНИЕ_НЕ_НАЙДЕНО");
        }

        return equipment;
    }

    @Transaction
    public Equipment commission(EquipmentContext ctx, Long equipmentId, String equipmentSerialNumber,
                                EquipmentType equipmentType, Set<Sensor> sensors, EquipmentLifecycle equipmentLifecycle,
                                String ownerOrg, String description, LocalDateTime timestamp) {
        RelatedOrganizations relatedOrganizations = new RelatedOrganizations();
        relatedOrganizations.setOwnerOrg(ownerOrg);

        // Создаем экземпляр оборудования
        Equipment equipment = new Equipment(equipmentId, equipmentSerialNumber, equipmentType, sensors, equipmentLifecycle,
                relatedOrganizations, EquipmentState.ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ, description);

        // Добавляем оборудование к списку всех похожих equipment в world state реестра
        ctx.equipmentList.addEquipment(equipment);

        // Возвращаем сериализованный объект приложению, которое вызвало этот метод
        return equipment;
    }

    @Transaction
    public Equipment setOperatingState(EquipmentContext ctx, Long equipmentId, double indication,
                                       LocalDateTime timestamp) {
        // Извлекаем данное оборудование из world state, используя ключ
        String equipmentKey = State.makeKey(new String[]{equipmentId.toString()});
        Equipment equipment = ctx.equipmentList.getEquipment(equipmentKey);

        // Переводим состояние в НОРМАЛЬНАЯ_РАБОТА
        if (equipment.getEquipmentState().equals(EquipmentState.ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ)) {
            equipment.setEquipmentState(EquipmentState.НОРМАЛЬНАЯ_РАБОТА);
        } else {
            throw new IllegalArgumentException("Оборудование не находится в подходящем состоении для перевода в состояние " + EquipmentState.НОРМАЛЬНАЯ_РАБОТА);
        }

        // Обновляем оборудование в world state
        ctx.equipmentList.updateEquipment(equipment);
        return equipment;
    }

    @Transaction
    public Equipment finishMaintenance(EquipmentContext ctx, Long equipmentId, String serviceOrg, byte[] document,
                                       LocalDateTime timestamp) {
        // Извлекаем данное оборудование из world state, используя ключ
        String equipmentKey = State.makeKey(new String[]{equipmentId.toString()});
        Equipment equipment = ctx.equipmentList.getEquipment(equipmentKey);

        if (!equipment.getRelatedOrganizations().getServiceOrg().equals(serviceOrg)) {
            throw new IllegalArgumentException("Оборудование с ID:" + equipmentKey + " не обслуживается организацией " + serviceOrg);
        }

        if (equipment.getEquipmentState().equals(EquipmentState.ОТКЛЮЧЕНО)) {
            equipment.setEquipmentState(EquipmentState.НОРМАЛЬНАЯ_РАБОТА);
        } else {
            throw new IllegalArgumentException("Оборудование не находится в подходящем состоении для перевода в состояние " + EquipmentState.НОРМАЛЬНАЯ_РАБОТА);
        }

        equipment.setMaintenanceStatus(null);

        // Обновляем оборудование в world state
        ctx.equipmentList.updateEquipment(equipment);
        return equipment;
    }

    @Transaction
    public Equipment startCorrectiveMaintenance(EquipmentContext ctx, Long equipmentId, String serviceOrg,
                                                LocalDateTime timestamp) {
        return startMaintenance(ctx, equipmentId, serviceOrg, MaintenanceStatus.КОРРЕКТИРУЮЩЕЕ_ОБСЛУЖИВАНИЕ);
    }

    @Transaction
    public Equipment startConditionBasedMaintenance(EquipmentContext ctx, Long equipmentId, String serviceOrg,
                                                    LocalDateTime timestamp) {
        return startMaintenance(ctx, equipmentId, serviceOrg, MaintenanceStatus.ОБСЛУЖИВАНИЕ_ПО_СОСТОЯНИЮ);
    }

    @Transaction
    public Equipment startPlannedMaintenance(EquipmentContext ctx, Long equipmentId, String serviceOrg,
                                             LocalDateTime timestamp) {
        return startMaintenance(ctx, equipmentId, serviceOrg, MaintenanceStatus.ЗАПЛАНИРОВАННОЕ_ОБСЛУЖИВАНИЕ);
    }

    @Transaction
    public Equipment decommission(EquipmentContext ctx, Long equipmentId, String ownerOrg, LocalDateTime timestamp) {
        String equipmentKey = Equipment.makeKey(new String[]{ equipmentId.toString() });
        Equipment equipment = ctx.equipmentList.getEquipment(equipmentKey);

        // Провреям оборудование на предмет уже совершившегося вывода из эксплуатации
        if (equipment.getEquipmentState().equals(EquipmentState.ВЫВЕДЕНО_ИЗ_ЭКСПЛУАТАЦИИ)) {
            throw new RuntimeException("Оборудование с ID:" + equipmentKey + " уже выведено из эксплуатации");
        }

        // Проверяем, что организация-владелец имеет отношение к данному оборудованию
        if (!equipment.getRelatedOrganizations().getOwnerOrg().equals(ownerOrg)) {
            throw new RuntimeException("Организация " + ownerOrg + " не владеет оборудованием с ID:" + equipmentKey);
        }

        equipment.setEquipmentState(EquipmentState.ВЫВЕДЕНО_ИЗ_ЭКСПЛУАТАЦИИ);

        ctx.equipmentList.updateEquipment(equipment);

        return equipment;
    }

    private Equipment startMaintenance(EquipmentContext ctx, Long equipmentId, String serviceOrg,
                                  MaintenanceStatus maintenanceStatus) {
        // Извлекаем данное оборудование из world state, используя ключ
        String equipmentKey = State.makeKey(new String[]{equipmentId.toString()});
        Equipment equipment = ctx.equipmentList.getEquipment(equipmentKey);

        if (!equipment.getRelatedOrganizations().getServiceOrg().equals(serviceOrg)) {
            throw new IllegalArgumentException("Оборудование с ID:" + equipmentKey + " не обслуживается организацией " + serviceOrg);
        }

        equipment.setEquipmentState(EquipmentState.ОТКЛЮЧЕНО);

        equipment.setMaintenanceStatus(maintenanceStatus);

        // Обновляем оборудование в world state
        ctx.equipmentList.updateEquipment(equipment);

        return equipment;
    }
}
