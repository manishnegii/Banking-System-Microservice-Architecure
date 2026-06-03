package com.spring.auth_service.service;

import com.spring.auth_service.dto.AuthUserCreateRequestDto;
import com.spring.auth_service.dto.AuthUserResponseDto;
import com.spring.auth_service.dto.AuthUserUpdateRequestDto;
import com.spring.auth_service.dto.ResetPasswordDto;
import com.spring.auth_service.entity.AuthUser;
import com.spring.auth_service.entity.Role;
import com.spring.auth_service.exception.AuthUserNotFoundException;
import com.spring.auth_service.exception.DuplicateAuthUserException;
import com.spring.auth_service.mapper.AuthUserMapper;
import com.spring.auth_service.repository.AuthUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class AuthUserService {

     private final AuthUserRepository authUserRepository;
     private final AuthUserMapper authUserMapper;
     private final PasswordEncoder passwordEncoder;

     public AuthUserResponseDto createAuthUser(AuthUserCreateRequestDto requestDto) {
         if (authUserRepository.existsByUsername(requestDto.getUsername())) {
             throw new DuplicateAuthUserException();
         }

         if (authUserRepository.existsByEmail(requestDto.getEmail())) {
             throw new DuplicateAuthUserException();
         }

         AuthUser authUser = authUserMapper.toEntity(requestDto);
         authUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
         authUser.setRole(Role.CUSTOMER);

         AuthUser saved = authUserRepository.save(authUser);
         return authUserMapper.toResponseDto(saved);
     }



     public void resetPassword(String email,ResetPasswordDto resetPasswordDto){
         var authUser = authUserRepository.findByEmail(email).orElseThrow(AuthUserNotFoundException::new);

         if(!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getAgainNewPassword())){
             throw new IllegalArgumentException("Password do not match");
         }
             authUser.setPassword(passwordEncoder.encode(resetPasswordDto.getAgainNewPassword()));
             authUser.setPasswordChangedAt(LocalDateTime.now());
             authUserRepository.save(authUser);
     }

     public AuthUserResponseDto getAuthUserById(Long id) {
         AuthUser authUser = authUserRepository.findById(id)
             .orElseThrow(AuthUserNotFoundException::new);
         return authUserMapper.toResponseDto(authUser);
     }

     public AuthUserResponseDto getAuthUserByUsername(String username) {
         AuthUser authUser = authUserRepository.findByUsername(username)
             .orElseThrow(AuthUserNotFoundException::new);
         return authUserMapper.toResponseDto(authUser);
     }

     public AuthUserResponseDto getAuthUserByEmail(String email) {
         AuthUser authUser = authUserRepository.findByEmail(email)
             .orElseThrow(AuthUserNotFoundException::new);
         return authUserMapper.toResponseDto(authUser);
     }

     public List<AuthUserResponseDto> getAllAuthUsers() {
         return authUserRepository.findAll().stream()
             .map(authUserMapper::toResponseDto)
             .collect(Collectors.toList());
     }

     public AuthUserResponseDto updateAuthUser(Long id, AuthUserUpdateRequestDto requestDto) {
         AuthUser authUser = authUserRepository.findById(id)
             .orElseThrow(AuthUserNotFoundException::new);

         // Check for duplicate email if changing
         if (requestDto.getEmail() != null && !authUser.getEmail().equals(requestDto.getEmail())
             && authUserRepository.existsByEmail(requestDto.getEmail())) {
             throw new DuplicateAuthUserException();
         }

         // Update password if provided
         if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
             authUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
             authUser.setPasswordChangedAt(LocalDateTime.now());
         }

         authUserMapper.updateEntityFromDto(requestDto, authUser);
         authUser.setUpdatedAt(LocalDateTime.now());

         AuthUser updated = authUserRepository.save(authUser);
         return authUserMapper.toResponseDto(updated);
     }

     public AuthUserResponseDto deactivateAuthUser(Long id) {
         AuthUser authUser = authUserRepository.findById(id)
             .orElseThrow(AuthUserNotFoundException::new);

         authUser.setIsActive(false);
         authUser.setUpdatedAt(LocalDateTime.now());

         AuthUser updated = authUserRepository.save(authUser);
         return authUserMapper.toResponseDto(updated);
     }

     public AuthUserResponseDto activateAuthUser(Long id) {
         AuthUser authUser = authUserRepository.findById(id)
             .orElseThrow(AuthUserNotFoundException::new);

         authUser.setIsActive(true);
         authUser.setUpdatedAt(LocalDateTime.now());

         AuthUser updated = authUserRepository.save(authUser);
         return authUserMapper.toResponseDto(updated);
     }

     public AuthUserResponseDto lockAuthUser(Long id) {
         AuthUser authUser = authUserRepository.findById(id)
             .orElseThrow(AuthUserNotFoundException::new);

         authUser.setAccountNonLocked(false);
         authUser.setUpdatedAt(LocalDateTime.now());

         AuthUser updated = authUserRepository.save(authUser);
         return authUserMapper.toResponseDto(updated);
     }

     public AuthUserResponseDto unlockAuthUser(Long id) {
         AuthUser authUser = authUserRepository.findById(id)
             .orElseThrow(AuthUserNotFoundException::new);

         authUser.setAccountNonLocked(true);
         authUser.setFailedAttempts(0);
         authUser.setUpdatedAt(LocalDateTime.now());

         AuthUser updated = authUserRepository.save(authUser);
         return authUserMapper.toResponseDto(updated);
     }

     public void deleteAuthUser(Long id) {
         AuthUser authUser = authUserRepository.findById(id)
             .orElseThrow(AuthUserNotFoundException::new);
         authUserRepository.deleteById(id);
     }

     public void recordLoginAttempt(Long id, boolean successful) {
         AuthUser authUser = authUserRepository.findById(id)
             .orElseThrow(AuthUserNotFoundException::new);

         if (successful) {
             authUser.setLastLoginAt(LocalDateTime.now());
             authUser.setFailedAttempts(0);
         } else {
             authUser.setFailedAttempts(authUser.getFailedAttempts() + 1);
             if (authUser.getFailedAttempts() >= 5) {
                 authUser.setAccountNonLocked(false);
             }
         }

         authUser.setUpdatedAt(LocalDateTime.now());
         authUserRepository.save(authUser);
     }
}
