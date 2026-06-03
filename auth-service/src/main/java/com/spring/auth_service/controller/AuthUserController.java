package com.spring.auth_service.controller;

import com.spring.auth_service.dto.AuthUserCreateRequestDto;
import com.spring.auth_service.dto.AuthUserResponseDto;
import com.spring.auth_service.dto.AuthUserUpdateRequestDto;
import com.spring.auth_service.dto.ResetPasswordDto;
import com.spring.auth_service.service.AuthUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth-users")
@RequiredArgsConstructor
public class AuthUserController {

    private final AuthUserService authUserService;

    @PostMapping("/register")
    public ResponseEntity<AuthUserResponseDto> createAuthUser(
            @Valid @RequestBody AuthUserCreateRequestDto requestDto,
            UriComponentsBuilder uriComponentsBuilder) {
        AuthUserResponseDto response = authUserService.createAuthUser(requestDto);
        URI location = uriComponentsBuilder.path("/api/v1/auth-users/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto){
        authUserService.resetPassword(resetPasswordDto.getEmail(), resetPasswordDto);
        return ResponseEntity.ok("Password reset Successfully");
    }



    @GetMapping("/{userId}")
    public ResponseEntity<AuthUserResponseDto> getAuthUser(@PathVariable Long userId) {
        return ResponseEntity.ok(authUserService.getAuthUserById(userId));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AuthUserResponseDto> getAuthUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(authUserService.getAuthUserByUsername(username));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AuthUserResponseDto> getAuthUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(authUserService.getAuthUserByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<AuthUserResponseDto>> getAllAuthUsers() {
        return ResponseEntity.ok(authUserService.getAllAuthUsers());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<AuthUserResponseDto> updateAuthUser(
            @PathVariable Long userId,
            @Valid @RequestBody AuthUserUpdateRequestDto requestDto) {
        return ResponseEntity.ok(authUserService.updateAuthUser(userId, requestDto));
    }

    @PostMapping("/{userId}/deactivate")
    public ResponseEntity<AuthUserResponseDto> deactivateAuthUser(@PathVariable Long userId) {
        return ResponseEntity.ok(authUserService.deactivateAuthUser(userId));
    }

    @PostMapping("/{userId}/activate")
    public ResponseEntity<AuthUserResponseDto> activateAuthUser(@PathVariable Long userId) {
        return ResponseEntity.ok(authUserService.activateAuthUser(userId));
    }

    @PostMapping("/{userId}/lock")
    public ResponseEntity<AuthUserResponseDto> lockAuthUser(@PathVariable Long userId) {
        return ResponseEntity.ok(authUserService.lockAuthUser(userId));
    }

    @PostMapping("/{userId}/unlock")
    public ResponseEntity<AuthUserResponseDto> unlockAuthUser(@PathVariable Long userId) {
        return ResponseEntity.ok(authUserService.unlockAuthUser(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteAuthUser(@PathVariable Long userId) {
        authUserService.deleteAuthUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/login-success")
    public ResponseEntity<Void> recordSuccessfulLogin(@PathVariable Long userId) {
        authUserService.recordLoginAttempt(userId, true);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/login-failure")
    public ResponseEntity<Void> recordFailedLogin(@PathVariable Long userId) {
        authUserService.recordLoginAttempt(userId, false);
        return ResponseEntity.ok().build();
    }
}

