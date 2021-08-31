package com.itmo.owner.controller.dto.cc;

import com.itmo.owner.domain.entity.EquipmentType;
import com.itmo.owner.domain.entity.SensorType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Equipment implements Serializable {
    private Long equipmentId;

    private String equipmentSerialNumber;

    private EquipmentType equipmentType;

    private Set<Sensor> sensors;

    private EquipmentLifecycle equipmentLifecycle;

    private RelatedOrganizations relatedOrganizations;

    private EquipmentState equipmentState;

    private String description;

    public Equipment(Long equipmentId, String equipmentSerialNumber, EquipmentType equipmentType, Set<Sensor> sensors,
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

    public static Equipment deserialize(String jsonData) {
        JSONObject json = new JSONObject(jsonData);

        Long equipmentId = json.optLong("equipmentId");

        String equipmentSerialNumber = json.optString("equipmentSerialNumber");

        EquipmentType equipmentType = json.optEnum(EquipmentType.class, "equipmentType");

        JSONArray sensorsJSONArray = json.optJSONArray("sensors");

        Set<Sensor> sensors = new HashSet<>();
        if (sensorsJSONArray != null) {
            for (int i = 0; i < sensorsJSONArray.length(); i++) {
                JSONObject jsonobject = sensorsJSONArray.getJSONObject(i);

                Double indication = jsonobject.optDouble("indication");

                sensors.add(
                        new Sensor(
                                jsonobject.getLong("id"),
                                jsonobject.getString("unit"),
                                jsonobject.getEnum(SensorType.class, "sensorType"),
                                indication
                        )
                );
            }
        }

        EquipmentLifecycle equipmentLifecycle = new EquipmentLifecycle();

        JSONObject lifecycleJSONObject = json.getJSONObject("equipmentLifecycle");

        equipmentLifecycle.setManufactureDate(LocalDate.parse(lifecycleJSONObject.optString("manufactureDate")));
        equipmentLifecycle.setCommissioningDate(LocalDate.parse(lifecycleJSONObject.optString("commissioningDate")));

        RelatedOrganizations relatedOrganizations = new RelatedOrganizations();

        JSONObject organizationsJSONObject = json.getJSONObject("relatedOrganizations");

        relatedOrganizations.setOwnerOrg(organizationsJSONObject.optString("ownerOrg"));
        relatedOrganizations.setOperatingOrg(organizationsJSONObject.optString("operatingOrg"));
        relatedOrganizations.setInspectionOrg(organizationsJSONObject.optString("inspectionOrg"));
        relatedOrganizations.setServiceOrg(organizationsJSONObject.optString("serviceOrg"));

        EquipmentState equipmentState = json.optEnum(EquipmentState.class, "equipmentState");

        String description = json.optString("description");

        return new Equipment(equipmentId, equipmentSerialNumber, equipmentType, sensors, equipmentLifecycle,
                relatedOrganizations, equipmentState, description);
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
                ", description='" + description + '\'' +
                '}';
    }
}
