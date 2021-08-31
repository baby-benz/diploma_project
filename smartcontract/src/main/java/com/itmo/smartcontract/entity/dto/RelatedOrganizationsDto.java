package com.itmo.smartcontract.entity.dto;

import com.owlike.genson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@Getter
@Setter
@NoArgsConstructor
@DataType
public class RelatedOrganizationsDto {
    @Property()
    private String ownerOrg;
    @Property()
    private String operatingOrg;
    @Property()
    private String serviceOrg;
    @Property()
    private String inspectionOrg;

    public RelatedOrganizationsDto(@JsonProperty("ownerOrg") String ownerOrg, @JsonProperty("operatingOrg") String operatingOrg,
                                   @JsonProperty("serviceOrg") String serviceOrg, @JsonProperty("inspectionOrg") String inspectionOrg) {
        this.ownerOrg = ownerOrg;
        this.operatingOrg = operatingOrg;
        this.serviceOrg = serviceOrg;
        this.inspectionOrg = inspectionOrg;
    }
}
