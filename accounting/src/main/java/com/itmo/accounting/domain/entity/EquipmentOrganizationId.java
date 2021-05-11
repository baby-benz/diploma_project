package com.itmo.accounting.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class EquipmentOrganizationId implements Serializable {
    @Column(name = "equipment_id")
    private Long equipmentId;

    @Column(name = "organization_id")
    private Long organizationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        EquipmentOrganizationId that = (EquipmentOrganizationId) o;
        return Objects.equals(equipmentId, that.equipmentId) &&
                Objects.equals(organizationId, that.organizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipmentId, organizationId);
    }
}
