package com.jinan.profile.controller.user.response;

import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.dto.user.UserDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class UserResponse {

    Long userId;
    String loginId;
    String username;
    String password;
    UserStatus status;
    String email;
    RoleType roleType;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Builder
    private UserResponse(Long userId, String loginId, String username, String password, UserStatus status, String email, RoleType roleType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.loginId = loginId;
        this.username = username;
        this.password = password;
        this.status = status;
        this.email = email;
        this.roleType = roleType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserResponse of(Long userId, String loginId, String username, String password, UserStatus status, String email, RoleType roleType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserResponse(
                userId, loginId, username, password, status, email, roleType, createdAt, updatedAt
        );
    }

    // dto -> response 변환 메서드
    public static UserResponse fromDto(UserDto dto) {
        return new UserResponse(
                dto.userId(),
                dto.loginId(),
                dto.username(),
                dto.password(),
                dto.status(),
                dto.email(),
                dto.roleType(),
                dto.createdAt(),
                dto.updatedAt()
        );
    }

}
