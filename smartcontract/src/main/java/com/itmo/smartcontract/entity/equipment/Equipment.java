package com.itmo.smartcontract.entity.equipment;

import com.itmo.smartcontract.ledgerapi.State;
import com.owlike.genson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hyperledger.fabric.Logger;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
@DataType
public final class Equipment extends State {
    private final static Logger LOG = Logger.getLogger(Equipment.class.getName());

    @Property
    private Long equipmentId;

    @Property
    private String equipmentSerialNumber;

    @Property(schema = {"enum", "ЦЕНТРОБЕЖНЫЙ_НАСОС,ПОРШНЕВОЙ_НАСОС,КОМПРЕССОР,ВИНТОВОЙ_ЗАБОЙНЫЙ_ДВИГАТЕЛЬ,СЕПАРАТОР,УЧАСТОК_ТРУБОПРОВОДА,РВС"})
    private String equipmentType;

    @Property
    private Sensor[] sensors;

    @Property
    private EquipmentLifecycle equipmentLifecycle;

    @Property
    private RelatedOrganizations relatedOrganizations;

    @Property(schema = {"enum", "ВВЕДЕНО_В_ЭКСПЛУАТАЦИЮ,НОРМАЛЬНАЯ_РАБОТА,РЕЖИМ_ОГРАНИЧЕННОЙ_ФУНКЦИОНАЛЬНОСТИ,НЕИСПРАВНО,ОТКЛЮЧЕНО,ВЫВЕДЕНО_ИЗ_ЭКСПЛУАТАЦИИ"})
    private String equipmentState;

    @Property(schema = {"enum", "КОРРЕКТИРУЮЩЕЕ_ОБСЛУЖИВАНИЕ,ЗАПЛАНИРОВАННОЕ_ОБСЛУЖИВАНИЕ,ОБСЛУЖИВАНИЕ_ПО_СОСТОЯНИЮ"})
    private String maintenanceStatus;

    @Property
    private String document;

    @Property
    private String description;

    public EquipmentState getEquipmentState() {
        if (this.equipmentState != null) {
            return EquipmentState.valueOf(this.equipmentState);
        } else return null;
    }

    public MaintenanceStatus getMaintenanceStatus() {
        if (this.maintenanceStatus != null) {
            return MaintenanceStatus.valueOf(this.maintenanceStatus);
        } else return null;
    }

    public EquipmentType getEquipmentType() {
        if (this.equipmentType != null) {
            return EquipmentType.valueOf(this.equipmentType);
        } else return null;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        if (equipmentType != null) {
            this.equipmentType = equipmentType.toString();
        } else {
            this.equipmentType = null;
        }
    }

    public void setEquipmentState(EquipmentState equipmentState) {
        if (equipmentState != null) {
            this.equipmentState = equipmentState.toString();
        } else {
            this.equipmentState = null;
        }
    }

    public void setMaintenanceStatus(MaintenanceStatus maintenanceStatus) {
        if (maintenanceStatus != null) {
            this.maintenanceStatus = maintenanceStatus.toString();
        } else {
            this.maintenanceStatus = null;
        }
    }

    public Equipment(@JsonProperty("equipmentID") Long equipmentId,
                     @JsonProperty("equipmentSerialNumber") String equipmentSerialNumber,
                     @JsonProperty("equipmentType") EquipmentType equipmentType,
                     @JsonProperty("sensors") Sensor[] sensors,
                     @JsonProperty("equipmentLifecycle") EquipmentLifecycle equipmentLifecycle,
                     @JsonProperty("relatedOrganizations") RelatedOrganizations relatedOrganizations,
                     @JsonProperty("equipmentState") EquipmentState equipmentState,
                     @JsonProperty("maintenanceStatus") MaintenanceStatus maintenanceStatus,
                     @JsonProperty("description") String description,
                     @JsonProperty("document") String document) {
        this.equipmentId = equipmentId;
        this.key = makeKey(new String[]{this.equipmentId.toString()});
        this.equipmentSerialNumber = equipmentSerialNumber;
        if (equipmentType != null) {
            this.equipmentType = equipmentType.toString();
        } else {
            this.equipmentType = null;
        }
        this.sensors = sensors;
        this.equipmentLifecycle = equipmentLifecycle;
        this.relatedOrganizations = relatedOrganizations;
        if (equipmentState != null) {
            this.equipmentState = equipmentState.toString();
        } else {
            this.equipmentState = null;
        }
        if (maintenanceStatus != null) {
            this.maintenanceStatus = maintenanceStatus.toString();
        } else {
            this.maintenanceStatus = null;
        }
        this.description = description;
        this.document = document;
    }

    public static Equipment deserialize(String jsonData) {
        JSONObject json = new JSONObject(jsonData);

        LOG.info(json.toString());

        Long equipmentId = json.optLong("equipmentId");

        String equipmentSerialNumber = json.optString("equipmentSerialNumber");

        EquipmentType equipmentType = json.optEnum(EquipmentType.class, "equipmentType");

        JSONArray sensorsJSONArray = json.optJSONArray("sensors");

        Sensor[] sensors = new Sensor[0];
        if (sensorsJSONArray != null) {
            sensors = new Sensor[sensorsJSONArray.length()];

            for (int i = 0; i < sensorsJSONArray.length(); i++) {
                JSONObject jsonobject = sensorsJSONArray.getJSONObject(i);

                Double indication = jsonobject.optDouble("indication");

                sensors[i] =
                        new Sensor(
                                jsonobject.getLong("id"),
                                jsonobject.getString("unit"),
                                jsonobject.getEnum(SensorType.class, "sensorType"),
                                indication
                        );
            }
        }

        EquipmentLifecycle equipmentLifecycle = new EquipmentLifecycle();

        JSONObject lifecycleJSONObject = json.getJSONObject("equipmentLifecycle");

        equipmentLifecycle.setManufactureDate(lifecycleJSONObject.optString("manufactureDate"));
        equipmentLifecycle.setCommissioningDate(lifecycleJSONObject.optString("commissioningDate"));
        equipmentLifecycle.setEOLDate(lifecycleJSONObject.optString("EOLDate"));

        RelatedOrganizations relatedOrganizations = new RelatedOrganizations();

        JSONObject organizationsJSONObject = json.getJSONObject("relatedOrganizations");

        relatedOrganizations.setOwnerOrg(organizationsJSONObject.optString("ownerOrg"));
        relatedOrganizations.setOperatingOrg(organizationsJSONObject.optString("operatingOrg"));
        relatedOrganizations.setInspectionOrg(organizationsJSONObject.optString("inspectionOrg"));
        relatedOrganizations.setServiceOrg(organizationsJSONObject.optString("serviceOrg"));

        EquipmentState equipmentState = json.optEnum(EquipmentState.class, "equipmentState");

        MaintenanceStatus maintenanceStatus = json.optEnum(MaintenanceStatus.class, "maintenanceStatus");

        String description = json.optString("description");

        String document = json.optString("document");

        return new Equipment(equipmentId, equipmentSerialNumber, equipmentType, sensors, equipmentLifecycle,
                relatedOrganizations, equipmentState, maintenanceStatus, description, document);
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "equipmentId=" + equipmentId +
                ", equipmentSerialNumber='" + equipmentSerialNumber + '\'' +
                ", equipmentType=" + equipmentType +
                ", sensors=" + Arrays.toString(sensors) +
                ", equipmentLifecycle=" + equipmentLifecycle +
                ", relatedOrganizations=" + relatedOrganizations +
                ", equipmentState=" + equipmentState +
                ", maintenanceStatus=" + maintenanceStatus +
                ", document='" + document + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
