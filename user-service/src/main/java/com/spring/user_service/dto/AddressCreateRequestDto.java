package com.spring.user_service.dto;

import com.spring.user_service.entity.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressCreateRequestDto {

    @NotNull
    private Long customerId;

    @NotNull
    private AddressType addressType;

    @NotBlank
    @Size(min = 5, max = 100)
    private String addressLine1;

    @Size(max = 100)
    private String addressLine2;

    @NotBlank
    @Size(min = 2, max = 50)
    private String city;

    @NotBlank
    @Size(min = 2, max = 50)
    private String state;

    @NotBlank
    @Pattern(regexp = "^[0-9]{5,6}$")
    private String postalCode;

    @NotBlank
    @Size(min = 2, max = 50)
    private String country;

    private Boolean isPrimary;
}
