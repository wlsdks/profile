package com.jinan.profile.dto;

import com.jinan.profile.domain.Users;
import com.jinan.profile.domain.type.RoleType;

import java.time.LocalDateTime;

/**
 * DTO for {@link Users}
 */
public record UsersDto(
        Long userId,
        String username,
        String password,
        String email,
        RoleType roleType,
//        List<UserDetailsDto> userDetailsDtos, // 계정에서의 참조는 없앤다.
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {


    // factory method of 선언
    public static UsersDto of(Long userId, String username, String password, String email, RoleType roleType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UsersDto(userId, username, password, email, roleType, createdAt, updatedAt);
    }

    // Principal에서 사용할 factory method  of 선언
    public static UsersDto of(Long userId, String username, String password, String email, RoleType roleType) {
        return new UsersDto(userId, username, email, password, roleType, null, null);
    }

    // 서비스 레이어에서 entity를 dto로 변환시켜주는 코드
    public static UsersDto from(Users entity) {
        return new UsersDto(
                entity.getUserId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getRoleType(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }


}