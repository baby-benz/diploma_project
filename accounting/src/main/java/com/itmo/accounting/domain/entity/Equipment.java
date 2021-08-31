package com.itmo.accounting.domain.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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
    private LocalDate manufactureDate;

    /**
     * Поле commissioningDate - дата ввода в эксплуатацию
     */
    @Column(columnDefinition = "DATE")
    private LocalDate commissioningDate;

    /**
     * Поле EOLDate - дата окончания срока службы
     */
    @Column(columnDefinition = "DATE")
    private LocalDate EOLDate;

    /**
     * Поле sensors - набор датчиков данного оборудования
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipment")
    private Set<Sensor> sensors;

    /**
     * Поле isTakenIntoAccount - булевая переменная, сообщающая является ли оборудование критически важным
     *                           и требует ли проверки показаний с датчиков на предмет сверхнормированных значений
     */
    @Column(columnDefinition = "BOOLEAN")
    private Boolean isTakenIntoAccount;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    @NotNull
    private Set<EquipmentOrganization> organizations;

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", serialNumber='" + serialNumber + '\'' +
                ", equipmentType=" + equipmentType +
                ", description='" + description + '\'' +
                ", manufactureDate=" + manufactureDate +
                ", commissioningDate=" + commissioningDate +
                ", EOLDate=" + EOLDate +
                ", sensors=" + sensors +
                ", isTakenIntoAccount=" + isTakenIntoAccount +
                ", organizations=" + organizations +
                '}';
    }
}
