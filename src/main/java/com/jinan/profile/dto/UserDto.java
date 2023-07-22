package com.jinan.profile.dto;

import com.jinan.profile.domain.User;
import com.jinan.profile.domain.type.RoleType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.jinan.profile.domain.User}
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
    public static UserDto from(User entity) {
        new UserDto(
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