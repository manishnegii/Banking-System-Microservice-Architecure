package com.spring.auth_service.JWT;


import com.spring.auth_service.entity.AuthUser;
import com.spring.auth_service.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;
//     private final TokenBlacklistRepository blacklistRepository;

    private Jwt generateToken(AuthUser user, long tokenExpiration) {

        var jti = UUID.randomUUID().toString();

        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getUsername())
                .add("role", user.getRole().name())
                .id(jti)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();

        System.out.println(claims);

        return new Jwt(claims, jwtConfig.getSecretKey());

    }

    public Jwt generateAccessToken(AuthUser user){

        return generateToken(user, jwtConfig.getAccessTokenExpiration());

    }

    public Jwt generateRefreshToken(AuthUser user){

        return generateToken(user, jwtConfig.getRefreshTokenExpiration());

    }


    public Jwt parseToken(String token) {
            try {
                var claims = getClaims(token);
                return new Jwt(claims, jwtConfig.getSecretKey());
            } catch (JwtException e) {
                return null;
            }
        }

    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Role getRoleFromToken(String token){
        return Role.valueOf(getClaims(token).get("role",String.class));
    }

    public String getJti(String token){
        return getClaims(token).getId();
    }

    public long getRemainingExpiration(String token){
        var expiration = getClaims(token).getExpiration();

        return expiration.getTime() - System.currentTimeMillis();
    }

    // public void logout(String token){
    //     String jti = getJti(token);

    //     long expiration = getRemainingExpiration(token);
    //     blacklistRepository.blackListToken(jti,expiration);
    // }
}
