//package com.spring.account_service.client;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.server.ResponseStatusException;
//
//@Service
//@RequiredArgsConstructor
//public class StubUserValidationClient implements UserValidationClient {
//
//    private final RestTemplate restTemplate;
//    private final WebClient webClient;
//
//    // Uncomment for reactive client usage:
//    // private final WebClient webClient;
//
//    @Value("${services.user-service.base-url:http://localhost:8081}")
//    private String userServiceBaseUrl;
//
//    @Override
//    public void validateUserExists(Long userId) {
//        if (userId == null || userId <= 0) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid userId");
//        }
//
//        String url = userServiceBaseUrl + "/api/v1/users/" + userId;
//        try {
//            restTemplate.getForEntity(url, Object.class);
//        } catch (RestClientException ex) {
//            throw new ResponseStatusException(
//                HttpStatus.BAD_REQUEST,
//                "User validation failed for userId: " + userId,
//                ex
//            );
//        }
//
//        /*
//        // WebClient version (commented as requested):
//        try {
//            webClient.get()
//                .uri(url)
//                .retrieve()
//                .toBodilessEntity()
//                .block();
//        } catch (Exception ex) {
//            throw new ResponseStatusException(
//                HttpStatus.BAD_REQUEST,
//                "User validation failed for userId: " + userId,
//                ex
//            );
//        }
//        */
//    }
//}
//
