package com.spring.user_service.mapper;

import com.spring.user_service.dto.CreatedCustomerResponseDto;
import com.spring.user_service.dto.CustomerCreateRequestDto;
import com.spring.user_service.dto.CustomerResponseDto;
import com.spring.user_service.dto.CustomerUpdateRequestDto;
import com.spring.user_service.entity.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AddressMapper.class, KycDocumentMapper.class})
public interface CustomerMapper {

    CustomerResponseDto toResponseDto(Customer customer);

    CreatedCustomerResponseDto toDto(Customer customer);

    Customer toEntity(CustomerCreateRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CustomerUpdateRequestDto dto, @MappingTarget Customer entity);
}

