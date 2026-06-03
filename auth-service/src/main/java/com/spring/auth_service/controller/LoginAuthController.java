package com.spring.auth_service.controller;

import com.spring.auth_service.dto.LoginRequest;
import com.spring.auth_service.dto.LoginResponse;
import com.spring.auth_service.service.LoginAuthService;
import com.spring.auth_service.service.LoginAuthServices;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class LoginAuthController {

//    private final JwtService jwtService;
//    private final AuthUserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
    private final LoginAuthService authService;
    private final LoginAuthServices authServices;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {

        return ResponseEntity.ok(authService.login(request,response));
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        var accessToken = authServices.refreshAccessToken(refreshToken);
        return new LoginResponse(accessToken.toString());
    }

//    @GetMapping("/me")
//    public ResponseEntity<AuthUserResponseDto> me(){
//        var user = auth.getCurrentUser();
//        var userDto = userMapper.toDto(user);
//
//        return ResponseEntity.ok(userDto);
//    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request){
//        var authHeader = request.getHeader("Authorization");
//        var token = authHeader.replace("Bearer ", "");
//        jwtService.logout(token);
//        return ResponseEntity.ok("Logged Out Successfully");
//    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
