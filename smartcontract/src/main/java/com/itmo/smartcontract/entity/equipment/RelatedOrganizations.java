package com.itmo.smartcontract.entity.equipment;

import com.owlike.genson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@Getter
@Setter
@DataType
@NoArgsConstructor
public class RelatedOrganizations {
    @Property
    private String ownerOrg;
    @Property
    private String operatingOrg;
    @Property
    private String serviceOrg;
    @Property
    private String inspectionOrg;

    public RelatedOrganizations(@JsonProperty("ownerOrg") String ownerOrg, @JsonProperty("operatingOrg") String operatingOrg,
                                @JsonProperty("serviceOrg") String serviceOrg, @JsonProperty("inspectionOrg") String inspectionOrg) {
        this.ownerOrg = ownerOrg;
        this.operatingOrg = operatingOrg;
        this.serviceOrg = serviceOrg;
        this.inspectionOrg = inspectionOrg;
    }

    @Override
    public String toString() {
        return "RelatedOrganizations{" +
                "ownerOrg='" + ownerOrg + '\'' +
                ", operatingOrg='" + operatingOrg + '\'' +
                ", serviceOrg='" + serviceOrg + '\'' +
                ", inspectionOrg='" + inspectionOrg + '\'' +
                '}';
    }
}
