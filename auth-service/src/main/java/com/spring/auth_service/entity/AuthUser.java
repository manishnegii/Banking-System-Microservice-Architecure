package com.spring.auth_service.entity;

import com.spring.auth_service.entity.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "auth_users",
        indexes = {
                @Index(name = "idx_auth_username", columnList = "username"),
                @Index(name = "idx_auth_email", columnList = "email"),
                @Index(name = "idx_auth_active", columnList = "is_active")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Long id;

    @Column(name = "username",nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email",nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false, length = 20)
    private Role role;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked;

    @Column(name = "failed_attempts")
    private Integer failedAttempts = 0;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = true;
        }
        if (this.emailVerified == null) {
            this.emailVerified = false;
        }
        if (this.accountNonLocked == null) {
            this.accountNonLocked = true;
        }
        if (this.failedAttempts == null) {
            this.failedAttempts = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}


