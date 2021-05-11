package com.itmo.smartcontract.equipment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatedOrganizations {
    private String ownerOrg;
    private String operatingOrg;
    private String serviceOrg;
    private String inspectionOrg;
}
