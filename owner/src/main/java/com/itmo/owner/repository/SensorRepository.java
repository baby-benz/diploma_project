package com.itmo.owner.repository;

import com.itmo.owner.domain.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long>, JpaSpecificationExecutor<Sensor> {
    List<Sensor> findAllByEquipmentId(Long id);
    List<Sensor> findAllByEquipmentIsTakenIntoAccount(boolean IsTakenIntoAccount);
}
