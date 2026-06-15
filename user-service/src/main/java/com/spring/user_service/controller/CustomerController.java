package com.spring.user_service.controller;

import com.rexchord.common_security.util.SecurityUtils;
import com.spring.user_service.dto.*;
import com.spring.user_service.service.CustomerService;
import com.spring.user_service.service.KycDocumentService;
import com.spring.user_service.utility.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final UserContext userContext;
    private final KycDocumentService documentService;

    @GetMapping("/debug")
    public Object debug(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/auth/{authId}/customer-id")
    public Long getCustomerId(@PathVariable Long authId){
        return customerService.getCustomerId(authId);
    }

    @PostMapping
    public ResponseEntity<CreatedCustomerResponseDto> createCustomer(
            @Valid @RequestBody CustomerCreateRequestDto requestDto,
            UriComponentsBuilder uriComponentsBuilder) {

        var response = customerService.createCustomer(requestDto);
        URI location = uriComponentsBuilder.path("/api/v1/customers/{id}")
                .buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(location).body(response);
    }



    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }



//    @GetMapping("/auth/{authId}")
//    public ResponseEntity<CustomerResponseDto> getCustomerByAuthId(@PathVariable Long authId) {
//        return ResponseEntity.ok(customerService.getCustomerByAuthId(authId));
//    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerResponseDto> getCustomerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

//    @GetMapping("/mobile/{mobileNumber}")
//    public ResponseEntity<CustomerResponseDto> getCustomerByMobileNumber(@PathVariable String mobileNumber) {
//        return ResponseEntity.ok(customerService.getCustomerByMobileNumber(mobileNumber));
//    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponseDto>> searchCustomers(
            @RequestParam String name) {
        return ResponseEntity.ok(customerService.searchCustomersByName(name));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerUpdateRequestDto requestDto) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, requestDto));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCustomerCount() {
        return ResponseEntity.ok(customerService.getTotalCustomerCount());
    }
}
