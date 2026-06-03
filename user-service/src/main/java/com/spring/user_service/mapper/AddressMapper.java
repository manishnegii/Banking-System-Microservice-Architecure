package com.spring.user_service.mapper;

import com.spring.user_service.dto.AddressCreateRequestDto;
import com.spring.user_service.dto.AddressResponseDto;
import com.spring.user_service.dto.AddressUpdateRequestDto;
import com.spring.user_service.entity.Address;
import com.spring.user_service.entity.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    @Mapping(target = "customerId", source = "customer.id")
    AddressResponseDto toResponseDto(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", source = "customerId", qualifiedByName = "mapCustomer")
    @Mapping(target = "createdAt", ignore = true)
    Address toEntity(AddressCreateRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(AddressUpdateRequestDto dto, @MappingTarget Address entity);

    @Named("mapCustomer")
    default Customer mapCustomer(Long customerId) {
        if (customerId == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(customerId);
        return customer;
    }
}


