package com.spring.auth_service.service;

import com.spring.auth_service.JWT.JwtConfig;
import com.spring.auth_service.JWT.JwtService;
import com.spring.auth_service.dto.LoginRequest;
import com.spring.auth_service.dto.LoginResponse;
import com.spring.auth_service.entity.AuthUser;
import com.spring.auth_service.exception.AccountLockedException;
import com.spring.auth_service.exception.InvalidLoginException;
import com.spring.auth_service.repository.AuthUserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@AllArgsConstructor
public class LoginAuthService {

    private final AuthUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request, HttpServletResponse response){

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,""));

        validateAccountLock(user);

        try{

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        }catch(BadCredentialsException ex){
            int attempts = handleFailedLogin(user);

            throw new InvalidLoginException("Invalid Credentials",attempts);
        }

        handleSuccessFullLogin(user);

        var accessToken = jwtService.generateAccessToken(user);

        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken",refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        //write true for production
        cookie.setSecure(false);
        response.addCookie(cookie);

        return new LoginResponse(accessToken.toString());
    }


    private void validateAccountLock(AuthUser user){
        if(user.getAccountLockedUntil() == null ) {
            return;
        }

        if(user.getAccountLockedUntil().isAfter(LocalDateTime.now())) {
            throw new AccountLockedException();
        }


        user.setAccountNonLocked(true);
        user.setAccountLockedUntil(null);
        user.setFailedAttempts(0);
        userRepository.save(user);

    }

    private int handleFailedLogin(AuthUser user){
        int attempts = Optional.ofNullable(user.getFailedAttempts()).orElse(0) + 1;

        user.setFailedAttempts(attempts);

        if(attempts >= 3){
            user.setAccountNonLocked(false);
            var lockTime = LocalDateTime.now().plusMinutes(1);
            user.setAccountLockedUntil(lockTime);
        }
        userRepository.save(user);

        return attempts;
    }

    private void handleSuccessFullLogin(AuthUser user){
        user.setFailedAttempts(0);
        user.setAccountNonLocked(true);
        user.setAccountLockedUntil(null);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
