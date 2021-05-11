package com.itmo.accounting.repository;

import com.itmo.accounting.domain.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long>, JpaSpecificationExecutor<Sensor> {
    List<Sensor> findAllByEquipmentId(Long id);
}
