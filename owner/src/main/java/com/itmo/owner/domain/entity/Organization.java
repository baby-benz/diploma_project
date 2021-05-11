package com.itmo.owner.domain.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Entity(name = "organization")
@TypeDef(
        name = "PGSQL_ENUM",
        typeClass = PostgreSQLEnumType.class
)
public class Organization {
    /**
     * Поле id - уникальный номер
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Поле name - название орагиназции
     */
    @Column(columnDefinition = "VARCHAR(40)")
    @NotNull
    private String name;

    /**
     * Поле equipmentType - тип организации (владелец, сервисная, оператор)
     */
    @Type(type = "PGSQL_ENUM")
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(columnDefinition = "ORGANIZATIONTYPE")
    private OrganizationType organizationType;

    /**
     * Поле address - адрес, по которому зарегистрирована организация
     */
    @Column(columnDefinition = "VARCHAR(100)")
    private String address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    private Set<EquipmentOrganization> equipment;
}
