package com.spring.account_service.mapper;

import com.spring.account_service.dto.AccountResponseDto;
import com.spring.account_service.dto.CreateAccountRequestDto;
import com.spring.account_service.dto.UpdateAccountRequestDto;
import com.spring.account_service.entity.Account;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    AccountResponseDto toResponseDto(Account account);

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "status", expression = "java(com.spring.account_service.entity.AccountStatus.ACTIVE)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Account toEntity(CreateAccountRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateAccountRequestDto dto, @MappingTarget Account entity);
}

