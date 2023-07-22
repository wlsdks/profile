package com.jinan.profile.dto;

import com.jinan.profile.domain.Users;
import com.jinan.profile.domain.type.RoleType;

import java.time.LocalDateTime;

/**
 * DTO for {@link Users}
 */
public record UserDto(
        Long userId,
        String username,
        String email,
        String password,
        RoleType roleType,
//        List<UserDetailsDto> userDetailsDtos, // 계정에서의 참조는 없앤다.
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {


    // factory method 선언
    public UserDto of(Long userId, String username, String email, String password, RoleType roleType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserDto(userId, username, email, password, roleType, createdAt, updatedAt);
    }

    // 서비스 레이어에서 entity를 dto로 변환시켜주는 코드
    public static UserDto from(Users entity) {
        return new UserDto(
                entity.getUserId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRoleType(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }


}