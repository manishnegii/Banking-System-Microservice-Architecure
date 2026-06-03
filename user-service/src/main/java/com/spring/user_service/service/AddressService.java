package com.spring.user_service.service;

import com.spring.user_service.dto.AddressCreateRequestDto;
import com.spring.user_service.dto.AddressResponseDto;
import com.spring.user_service.dto.AddressUpdateRequestDto;
import com.spring.user_service.entity.Address;
import com.spring.user_service.entity.AddressType;
import com.spring.user_service.exception.AddressNotFoundException;
import com.spring.user_service.exception.CustomerNotFoundException;
import com.spring.user_service.mapper.AddressMapper;
import com.spring.user_service.repository.AddressRepository;
import com.spring.user_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final AddressMapper addressMapper;

    public AddressResponseDto createAddress(AddressCreateRequestDto requestDto) {
        // Validate customer exists
        customerRepository.findById(requestDto.getCustomerId())
            .orElseThrow(CustomerNotFoundException::new);

        Address address = addressMapper.toEntity(requestDto);

        // If marking as primary, unmark other primary addresses for this customer
        if (Boolean.TRUE.equals(requestDto.getIsPrimary())) {
            addressRepository.findByCustomerIdAndIsPrimaryTrue(requestDto.getCustomerId())
                .ifPresent(primary -> {
                    primary.setIsPrimary(false);
                    addressRepository.save(primary);
                });
        }

        Address saved = addressRepository.save(address);
        return addressMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public AddressResponseDto getAddressById(Long id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(AddressNotFoundException::new);
        return addressMapper.toResponseDto(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponseDto> getAddressesByCustomerId(Long customerId) {
        // Validate customer exists
        customerRepository.findById(customerId)
            .orElseThrow(CustomerNotFoundException::new);

        return addressRepository.findByCustomerId(customerId).stream()
            .map(addressMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AddressResponseDto> getAddressesByCustomerIdAndType(Long customerId, AddressType type) {
        // Validate customer exists
        customerRepository.findById(customerId)
            .orElseThrow(CustomerNotFoundException::new);

        return addressRepository.findByCustomerIdAndAddressType(customerId, type).stream()
            .map(addressMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AddressResponseDto getPrimaryAddress(Long customerId) {
        Address address = addressRepository.findByCustomerIdAndIsPrimaryTrue(customerId)
            .orElseThrow(AddressNotFoundException::new);
        return addressMapper.toResponseDto(address);
    }

    public AddressResponseDto updateAddress(Long id, AddressUpdateRequestDto requestDto) {
        Address address = addressRepository.findById(id)
            .orElseThrow(AddressNotFoundException::new);

        Long customerId = address.getCustomer().getId();

        // If marking as primary, unmark other primary addresses for this customer
        if (Boolean.TRUE.equals(requestDto.getIsPrimary()) && !address.getIsPrimary()) {
            addressRepository.findByCustomerIdAndIsPrimaryTrue(customerId)
                .ifPresent(primary -> {
                    primary.setIsPrimary(false);
                    addressRepository.save(primary);
                });
        }

        addressMapper.updateEntityFromDto(requestDto, address);
        Address updated = addressRepository.save(address);
        return addressMapper.toResponseDto(updated);
    }

    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(AddressNotFoundException::new);
        addressRepository.deleteById(id);
    }

    public void deleteAddressesByCustomerId(Long customerId) {
        addressRepository.deleteByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public long getAddressCountByCustomerId(Long customerId) {
        return addressRepository.countByCustomerId(customerId);
    }
}
