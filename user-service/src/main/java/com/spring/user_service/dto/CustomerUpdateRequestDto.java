package com.spring.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateRequestDto {

    @Size(min = 2, max = 100)
    private String fullName;

    @Email
    private String email;

    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(M|F|O)$")
    private String gender;

    @Pattern(regexp = "^[0-9]{10}$")
    private String mobileNumber;
}
