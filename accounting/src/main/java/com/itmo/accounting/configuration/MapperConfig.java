package com.itmo.accounting.configuration;

import com.itmo.accounting.controller.dto.NewEquipmentOrganizationDto;
import com.itmo.accounting.domain.entity.EquipmentOrganization;
import com.itmo.accounting.domain.entity.EquipmentOrganizationId;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(NewEquipmentOrganizationDto.class, EquipmentOrganization.class).setConverter(converter);
        return modelMapper;
    }

    Converter<NewEquipmentOrganizationDto, EquipmentOrganization> converter = context -> {
        EquipmentOrganization equipmentOrganization = new EquipmentOrganization();
        EquipmentOrganizationId id = new EquipmentOrganizationId();
        id.setOrganizationId(context.getSource().getOrganizationId());
        equipmentOrganization.setId(id);
        equipmentOrganization.setStartDate(context.getSource().getStartDate());
        equipmentOrganization.setEndDate(context.getSource().getEndDate());
        return equipmentOrganization;
    };
}
