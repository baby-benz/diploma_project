package com.itmo.owner.domain.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "sensor")
@TypeDef(
        name = "PGSQL_ENUM",
        typeClass = PostgreSQLEnumType.class
)
public class Sensor {
    /**
     * Поле id - уникальный номер
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Поле rangeMin - минимальное значение, которое может принимать показание с датчика
     */
    @Column(columnDefinition = "INTEGER")
    @NotNull
    private Integer rangeMin;

    /**
     * Поле rangeMax - максимальное значение, которое может принимать показание с датчика
     */
    @Column(columnDefinition = "INTEGER")
    @NotNull
    private Integer rangeMax;

    /**
     * Поле unit - единица измерения показаний датчика
     */
    @Column(columnDefinition = "VARCHAR(15)")
    @NotNull
    private String unit;

    /**
     * Поле sensorType - тип датчика
     */
    @Type(type = "PGSQL_ENUM")
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(columnDefinition = "SENSORTYPE")
    private SensorType sensorType;

    /**
     * Поле equipment - оборудование, на котором установлен датчик
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "equipment_id")
    @NotNull
    private Equipment equipment;

    public Sensor(Sensor another) {
        this.id = another.id;
        this.rangeMin = another.rangeMin;
        this.rangeMax = another.rangeMax;
        this.unit = another.unit;
        this.sensorType = another.sensorType;
        this.equipment = new Equipment(another.equipment);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", rangeMin=" + rangeMin +
                ", rangeMax=" + rangeMax +
                ", unit='" + unit + '\'' +
                ", sensorType=" + sensorType +
                '}';
    }
}
