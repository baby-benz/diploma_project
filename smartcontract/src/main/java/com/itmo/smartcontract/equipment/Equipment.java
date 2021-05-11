package com.itmo.smartcontract.equipment;

import com.itmo.smartcontract.ledgerapi.State;
import lombok.Getter;
import lombok.Setter;
import org.hyperledger.fabric.Logger;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@DataType()
public final class Equipment extends State {
    private final static Logger LOG = Logger.getLogger(Equipment.class.getName());

    @Property()
    private Long equipmentId;

    @Property()
    private String equipmentSerialNumber;

    @Property()
    private EquipmentType equipmentType;

    @Property()
    private Set<Sensor> sensors = new HashSet<>();

    @Property()
    private EquipmentLifecycle equipmentLifecycle;

    @Property()
    private RelatedOrganizations relatedOrganizations;

    @Property()
    private EquipmentState equipmentState;

    @Property()
    private MaintenanceStatus maintenanceStatus;

    @Property()
    private String description;

    public Equipment(Long equipmentId, String equipmentSerialNumber, EquipmentType equipmentType, Set<Sensor> sensors,
                     EquipmentLifecycle equipmentLifecycle, RelatedOrganizations relatedOrganizations,
                     EquipmentState equipmentState, String description) {
        this.equipmentId = equipmentId;
        this.key = makeKey(new String[]{this.equipmentId.toString()});
        this.equipmentSerialNumber = equipmentSerialNumber;
        this.equipmentType = equipmentType;
        this.sensors.addAll(sensors);
        this.equipmentLifecycle = equipmentLifecycle;
        this.relatedOrganizations = relatedOrganizations;
        this.equipmentState = equipmentState;
        this.description = description;
    }

    public static Equipment deserialize(byte[] data) {
        JSONObject json = new JSONObject(new String(data, StandardCharsets.UTF_8));

        Long equipmentId = json.getLong("equipmentId");

        String equipmentSerialNumber = json.getString("equipmentSerialNumber");

        EquipmentType equipmentType = json.getEnum(EquipmentType.class, "equipmentType");

        Set<Sensor> sensors = new HashSet<>();

        JSONArray sensorsJSONArray = json.getJSONArray("sensors");

        for (int i = 0; i < sensorsJSONArray.length(); i++) {
            JSONObject jsonobject = sensorsJSONArray.getJSONObject(i);

            sensors.add(
                    new Sensor(
                            jsonobject.getLong("id"),
                            jsonobject.getInt("rangeMin"),
                            jsonobject.getInt("rangeMax"),
                            jsonobject.getString("unit"),
                            jsonobject.getEnum(SensorType.class, "sensorType")
                    )
            );
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
            if(!EOLDateStr.isEmpty()) {
                equipmentLifecycle.setEOLDate(LocalDate.parse(EOLDateStr));
            }
        } catch (JSONException ignored) { }

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
        } catch (JSONException e) { }

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
