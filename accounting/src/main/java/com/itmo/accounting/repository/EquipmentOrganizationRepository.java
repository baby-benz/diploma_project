package com.itmo.accounting.repository;

import com.itmo.accounting.domain.entity.EquipmentOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EquipmentOrganizationRepository extends JpaRepository<EquipmentOrganization, Long>, JpaSpecificationExecutor<EquipmentOrganization> {
}
