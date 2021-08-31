package com.itmo.owner.controller.dto.cc;

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
