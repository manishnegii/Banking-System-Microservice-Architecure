package com.spring.user_service.encryption;

import com.spring.user_service.config.SecurityContext;
import com.spring.user_service.entity.Role;
import com.spring.user_service.utility.MaskingUtils;
import com.spring.user_service.entity.KycDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseMaskingHelper {
    private final AesEncryptionService encryptionService;

    public String resolveDocument(KycDocument entity){
        if(entity == null || entity.getDocumentNumber() == null){
            return null;
        }

    String decrypted = encryptionService.decrypt(entity.getDocumentNumber(),entity.getKeyVersion());

        Role role = SecurityContext.getRole();

        return switch(role) {
            case ADMIN -> decrypted;
            case AUDITOR -> decrypted + "[AUDIT_ACCESS]";
            case USER, CUSTOMER -> MaskingUtils.maskDocument(decrypted,entity.getDocumentType());
        };
    }
}


