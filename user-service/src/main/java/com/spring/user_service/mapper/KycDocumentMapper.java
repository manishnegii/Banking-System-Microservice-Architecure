package com.spring.user_service.mapper;

import com.spring.user_service.dto.KycDocumentCreateRequestDto;
import com.spring.user_service.dto.KycDocumentResponseDto;
import com.spring.user_service.dto.KycDocumentUpdateRequestDto;
import com.spring.user_service.dto.KycStatusResponse;
import com.spring.user_service.encryption.ResponseMaskingHelper;
import com.spring.user_service.entity.KycDocument;
import lombok.AllArgsConstructor;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class KycDocumentMapper {

    @Autowired
    protected ResponseMaskingHelper maskingHelper;

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "maskedDocumentNumber",ignore = true)
    public abstract KycDocumentResponseDto toResponseDto(KycDocument kycDocument);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "verificationStatus", expression = "java(com.spring.user_service.entity.KycStatus.PENDING)")
    @Mapping(target = "verifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract KycDocument toEntity(KycDocumentCreateRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "documentType", ignore = true)
    @Mapping(target = "documentNumber", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract void updateEntityFromDto(KycDocumentUpdateRequestDto dto, @MappingTarget KycDocument entity);

    @Mapping(target = "kycStatus", source = "verificationStatus")
    @Mapping(target = "customerId",source = "customer.id")
    public abstract KycStatusResponse statusResponseDto(KycDocument kycDocument);

    @AfterMapping
    protected void enrich(KycDocument entity,@MappingTarget KycDocumentResponseDto dto){
        dto.setMaskedDocumentNumber(maskingHelper.resolveDocument(entity));
    }
}


