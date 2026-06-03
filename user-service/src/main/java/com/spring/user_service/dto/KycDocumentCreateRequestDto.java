package com.spring.user_service.dto;

import com.spring.user_service.entity.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KycDocumentCreateRequestDto {

    @NotNull
    private DocumentType documentType;

    @NotBlank
    @Size(min = 5, max = 50)
    private String documentNumber;

    @NotBlank
    private String documentUrl;
}
