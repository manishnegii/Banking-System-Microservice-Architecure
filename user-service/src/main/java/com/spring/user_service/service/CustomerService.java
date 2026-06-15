package com.spring.user_service.service;

import com.rexchord.common_security.util.SecurityUtils;
import com.spring.user_service.config.SecurityContext;
import com.spring.user_service.dto.CreatedCustomerResponseDto;
import com.spring.user_service.dto.CustomerCreateRequestDto;
import com.spring.user_service.dto.CustomerResponseDto;
import com.spring.user_service.dto.CustomerUpdateRequestDto;
import com.spring.user_service.entity.Customer;
import com.spring.user_service.exception.CustomerNotFoundException;
import com.spring.user_service.exception.DuplicateCustomerException;
import com.spring.user_service.mapper.CustomerMapper;
import com.spring.user_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressService addressService;
    private final KycDocumentService kycDocumentService;

    public CreatedCustomerResponseDto createCustomer(CustomerCreateRequestDto requestDto) {
        if (customerRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateCustomerException();
        }

        if (customerRepository.existsByMobileNumber(requestDto.getMobileNumber())) {
            throw new DuplicateCustomerException();
        }

        var customer = customerMapper.toEntity(requestDto);
        customer.setAuthId(SecurityContext.getCurrentUserId());
        customer.setRole((SecurityContext.getRole()).name());
        var saved = customerRepository.save(customer);
        return customerMapper.toDto(saved);
    }



    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(CustomerNotFoundException::new);
        System.out.println(customer);
        return customerMapper.toResponseDto(customer);
    }



//    public CustomerResponseDto getCustomerByAuthId(Long authId) {
//        Customer customer = customerRepository.findByAuthUserId(authId)
//            .orElseThrow(CustomerNotFoundException::new);
//        return customerMapper.toResponseDto(customer);
//    }

    public Long getCustomerId(Long authId){
        var customerId = customerRepository.findCustomerIdByAuthId(authId);
        System.out.println(customerId);
        if(customerId == null){
            throw new CustomerNotFoundException();
        }
        return customerId;
    }

    public CustomerResponseDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
            .orElseThrow(CustomerNotFoundException::new);
        return customerMapper.toResponseDto(customer);
    }

//    public CustomerResponseDto getCustomerByMobileNumber(String mobileNumber) {
//        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
//            .orElseThrow(CustomerNotFoundException::new);
//        return customerMapper.toResponseDto(customer);
//    }

    public List<CustomerResponseDto> searchCustomersByName(String name) {
        return customerRepository.findByFullNameContainingIgnoreCase(name).stream()
            .map(customerMapper::toResponseDto)
            .collect(Collectors.toList());
    }


    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream()
            .map(customerMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    public CustomerResponseDto updateCustomer(Long id, CustomerUpdateRequestDto requestDto) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(CustomerNotFoundException::new);

        // Check for duplicate email if changing
        if (requestDto.getEmail() != null && !customer.getEmail().equals(requestDto.getEmail())
            && customerRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateCustomerException();
        }

        // Check for duplicate mobile if changing
        if (requestDto.getMobileNumber() != null && !customer.getMobileNumber().equals(requestDto.getMobileNumber())
            && customerRepository.existsByMobileNumber(requestDto.getMobileNumber())) {
            throw new DuplicateCustomerException();
        }

        customerMapper.updateEntityFromDto(requestDto, customer);
        customer.setUpdatedAt(LocalDateTime.now());

        Customer updated = customerRepository.save(customer);
        return customerMapper.toResponseDto(updated);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(CustomerNotFoundException::new);

        // Delete associated addresses and KYC documents
        addressService.deleteAddressesByCustomerId(id);
        kycDocumentService.deleteKycDocumentsByCustomerId(id);

        customerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getTotalCustomerCount() {
        return customerRepository.count();
    }
}
