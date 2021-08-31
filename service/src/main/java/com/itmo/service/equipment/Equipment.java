package com.itmo.service.equipment;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class Equipment {
    private Long equipmentId;

    private String equipmentSerialNumber;

    private EquipmentType equipmentType;

    private ArrayList<Sensor> sensors;

    private EquipmentLifecycle equipmentLifecycle;

    private RelatedOrganizations relatedOrganizations;

    private EquipmentState equipmentState;

    private MaintenanceStatus maintenanceStatus;

    private String description;

    public Equipment(Long equipmentId, String equipmentSerialNumber, EquipmentType equipmentType, ArrayList<Sensor> sensors,
                     EquipmentLifecycle equipmentLifecycle, RelatedOrganizations relatedOrganizations,
                     EquipmentState equipmentState, String description) {
        this.equipmentId = equipmentId;
        this.equipmentSerialNumber = equipmentSerialNumber;
        this.equipmentType = equipmentType;
        this.sensors = sensors;
        this.equipmentLifecycle = equipmentLifecycle;
        this.relatedOrganizations = relatedOrganizations;
        this.equipmentState = equipmentState;
        this.description = description;
    }

    public static List<EquipmentHistory> deserializeHistory(JSONArray json) {
        List<EquipmentHistory> equipmentHistoryList = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonObject = json.getJSONObject(i);

            String txId = jsonObject.getString("txId");

            JSONObject state = jsonObject.getJSONObject("state");
            Equipment equipment = deserialize(state);

            JSONObject timestampObj = jsonObject.getJSONObject("timestamp");
            Instant timestamp = Instant.ofEpochSecond(timestampObj.getLong("epochSecond"), timestampObj.getLong("nano"));

            String document = state.optString("document");
            if (document.isEmpty()) {
                equipmentHistoryList.add(new EquipmentHistory(txId, equipment, timestamp));
            } else {
                equipmentHistoryList.add(new EquipmentHistoryWithDocument(txId, equipment, timestamp, document));
            }
        }

        return equipmentHistoryList;
    }

    public static List<Equipment> deserializeCollection(JSONArray json) {
        List<Equipment> equipmentList = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonObject = json.getJSONObject(i);

            equipmentList.add(deserialize(jsonObject));
        }

        return equipmentList;
    }

    public static Equipment deserialize(JSONObject json) {
        Long equipmentId = json.getLong("equipmentId");

        String equipmentSerialNumber = json.getString("equipmentSerialNumber");

        EquipmentType equipmentType = json.getEnum(EquipmentType.class, "equipmentType");

        ArrayList<Sensor> sensors = new ArrayList<>();

        JSONArray sensorsJSONArray = json.getJSONArray("sensors");

        for (int i = 0; i < sensorsJSONArray.length(); i++) {
            JSONObject jsonobject = sensorsJSONArray.getJSONObject(i);

            try {
                Double indication = jsonobject.getDouble("indication");

                sensors.add(
                        new SensorWithIndication(
                                jsonobject.getLong("id"),
                                jsonobject.getString("unit"),
                                jsonobject.getEnum(SensorType.class, "sensorType"),
                                indication
                        )
                );
            } catch (JSONException e) {
                sensors.add(
                        new Sensor(
                                jsonobject.getLong("id"),
                                jsonobject.getString("unit"),
                                jsonobject.getEnum(SensorType.class, "sensorType")
                        )
                );
            }
        }

        EquipmentLifecycle equipmentLifecycle = new EquipmentLifecycle();

        JSONObject lifecycleJSONObject = json.getJSONObject("equipmentLifecycle");

        String manufactureDateStr = lifecycleJSONObject.getString("manufactureDate");
        equipmentLifecycle.setManufactureDate(LocalDate.parse(manufactureDateStr));

        String commissioningDateStr = lifecycleJSONObject.getString("commissioningDate");
        equipmentLifecycle.setCommissioningDate(LocalDate.parse(commissioningDateStr));

        String EOLDateStr;
        try {
            EOLDateStr = lifecycleJSONObject.getString("EOLDate");
            if (!EOLDateStr.isEmpty()) {
                equipmentLifecycle.setEOLDate(LocalDate.parse(EOLDateStr));
            }
        } catch (JSONException ignored) {
        }

        RelatedOrganizations relatedOrganizations = new RelatedOrganizations();

        JSONObject organizationsJSONObject = json.getJSONObject("relatedOrganizations");

        relatedOrganizations.setOwnerOrg(organizationsJSONObject.getString("ownerOrg"));
        relatedOrganizations.setOperatingOrg(organizationsJSONObject.getString("operatingOrg"));
        relatedOrganizations.setInspectionOrg(organizationsJSONObject.getString("inspectionOrg"));
        relatedOrganizations.setServiceOrg(organizationsJSONObject.getString("serviceOrg"));

        EquipmentState equipmentState = json.getEnum(EquipmentState.class, "equipmentState");

        String description = json.getString("description");

        Equipment equipment = new Equipment(equipmentId, equipmentSerialNumber, equipmentType, sensors, equipmentLifecycle,
                relatedOrganizations, equipmentState, description);

        try {
            MaintenanceStatus maintenanceStatus = json.getEnum(MaintenanceStatus.class, "maintenanceStatus");
            equipment.setMaintenanceStatus(maintenanceStatus);
        } catch (JSONException ignore) {
        }

        return equipment;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "equipmentId=" + equipmentId +
                ", equipmentSerialNumber='" + equipmentSerialNumber + '\'' +
                ", equipmentType=" + equipmentType +
                ", sensors=" + sensors +
                ", equipmentLifecycle=" + equipmentLifecycle +
                ", relatedOrganizations=" + relatedOrganizations +
                ", equipmentState=" + equipmentState +
                ", maintenanceStatus=" + maintenanceStatus +
                ", description='" + description + '\'' +
                '}';
    }
}
