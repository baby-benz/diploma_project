package com.itmo.owner.domain.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity(name = "equipment")
@TypeDef(
        name = "PGSQL_ENUM",
        typeClass = PostgreSQLEnumType.class
)
@NaturalIdCache
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Equipment {
    /**
     * Поле id - уникальный номер
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Поле serialNumber - серийный номер оборудования
     */
    @Column(columnDefinition = "VARCHAR(20)")
    @NotNull
    private String serialNumber;

    /**
     * Поле equipmentType - тип оборудования
     */
    @Type(type = "PGSQL_ENUM")
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(columnDefinition = "EQUIPMENTTYPE")
    private EquipmentType equipmentType;

    /**
     * Поле description - описание/комментарии к оборудованию
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Поле manufactureDate - дата производства оборудования
     */
    @Column(columnDefinition = "DATE")
    @NotNull
    private Date manufactureDate;

    /**
     * Поле commissioningDate - дата ввода в эксплуатацию
     */
    @Column(columnDefinition = "DATE")
    private Date commissioningDate;

    /**
     * Поле EOLDate - дата окончания срока службы
     */
    @Column(columnDefinition = "DATE")
    private Date EOLDate;

    /**
     * Поле sensors - набор датчиков данного оборудования
     */
    @OneToMany(mappedBy = "equipment")
    private Set<Sensor> sensors;

    /**
     * Поле isTakenIntoAccount - булевая переменная, сообщающая является ли оборудование критически важным
     *                           и требует ли проверки показаний с датчиков на предмет сверхнормированных значений
     */
    @Column(columnDefinition = "BOOLEAN")
    private Boolean isTakenIntoAccount;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "equipment")
    @NotNull
    private Set<EquipmentOrganization> organizations;
}
