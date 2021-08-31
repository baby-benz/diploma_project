package com.itmo.accounting.controller.dto.cc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatedOrganizations {
    private String ownerOrg;
    private String operatingOrg;
    private String serviceOrg;
    private String inspectionOrg;
}
