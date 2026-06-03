package com.spring.auth_service.mapper;

import com.spring.auth_service.admin.AdminDto;
import com.spring.auth_service.dto.AuthUserCreateRequestDto;
import com.spring.auth_service.dto.AuthUserResponseDto;
import com.spring.auth_service.dto.AuthUserUpdateRequestDto;
import com.spring.auth_service.dto.ResetPasswordDto;
import com.spring.auth_service.entity.AuthUser;
import org.mapstruct.*;

import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthUserMapper {

    @Mapping(source = "username",target = "name")
    AdminDto toDtO(AuthUser user);

    AuthUserResponseDto toResponseDto(AuthUser authUser);

    AuthUser toEntity(AuthUserCreateRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AuthUserUpdateRequestDto dto, @MappingTarget AuthUser entity);
}

