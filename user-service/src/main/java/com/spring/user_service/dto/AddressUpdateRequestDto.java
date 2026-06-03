package com.spring.user_service.dto;

import com.spring.user_service.entity.AddressType;
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
public class AddressUpdateRequestDto {

    private AddressType addressType;

    @Size(min = 5, max = 100)
    private String addressLine1;

    @Size(max = 100)
    private String addressLine2;

    @Size(min = 2, max = 50)
    private String city;

    @Size(min = 2, max = 50)
    private String state;

    @Pattern(regexp = "^[0-9]{5,6}$")
    private String postalCode;

    @Size(min = 2, max = 50)
    private String country;

    private Boolean isPrimary;
}
