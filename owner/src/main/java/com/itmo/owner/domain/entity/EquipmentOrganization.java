package com.itmo.owner.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@Entity(name = "equipment_organization")
public class EquipmentOrganization {
    @EmbeddedId
    private EquipmentOrganizationId id = new EquipmentOrganizationId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("equipmentId")
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("organizationId")
    private Organization organization;

    /**
     * Поле startDate - дата начала действия договора
     */
    @Column(columnDefinition = "DATE")
    @NotNull
    private Date startDate;

    /**
     * Поле endDate - дата окончания действия договора
     */
    @Column(columnDefinition = "DATE")
    private Date endDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof EquipmentOrganization))
            return false;

        EquipmentOrganization that = (EquipmentOrganization) o;
        return Objects.equals(equipment, that.equipment) &&
                Objects.equals(organization, that.organization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipment, organization);
    }
}
