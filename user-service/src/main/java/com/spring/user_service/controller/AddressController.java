package com.spring.user_service.controller;

import com.spring.user_service.dto.AddressCreateRequestDto;
import com.spring.user_service.dto.AddressResponseDto;
import com.spring.user_service.dto.AddressUpdateRequestDto;
import com.spring.user_service.entity.AddressType;
import com.spring.user_service.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponseDto> createAddress(
            @Valid @RequestBody AddressCreateRequestDto requestDto,
            UriComponentsBuilder uriComponentsBuilder) {
        AddressResponseDto response = addressService.createAddress(requestDto);
        URI location = uriComponentsBuilder.path("/api/v1/addresses/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponseDto> getAddress(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.getAddressById(addressId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AddressResponseDto>> getAddressesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(addressService.getAddressesByCustomerId(customerId));
    }

    @GetMapping("/customer/{customerId}/type/{type}")
    public ResponseEntity<List<AddressResponseDto>> getAddressesByCustomerAndType(
            @PathVariable Long customerId,
            @PathVariable AddressType type) {
        return ResponseEntity.ok(addressService.getAddressesByCustomerIdAndType(customerId, type));
    }

    @GetMapping("/customer/{customerId}/primary")
    public ResponseEntity<AddressResponseDto> getPrimaryAddress(@PathVariable Long customerId) {
        return ResponseEntity.ok(addressService.getPrimaryAddress(customerId));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDto> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressUpdateRequestDto requestDto) {
        return ResponseEntity.ok(addressService.updateAddress(addressId, requestDto));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}/count")
    public ResponseEntity<Long> getAddressCount(@PathVariable Long customerId) {
        return ResponseEntity.ok(addressService.getAddressCountByCustomerId(customerId));
    }
}
