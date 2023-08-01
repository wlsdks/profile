package com.jinan.profile.dto.user;

import com.jinan.profile.domain.type.RoleType;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.UserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link User}
 */
public record UserDto(
        Long userId,
        String loginId,
        String username,
        String password,
        UserStatus status,
        String email,
        RoleType roleType,
        List<UserDetailsDto> userDetails, // 계정에서의 참조는 없앤다.
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // factory method of 선언
    public static UserDto of(Long userId, String loginId, String username, String password, UserStatus status, String email, RoleType roleType, List<UserDetailsDto> userDetails, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserDto(userId, loginId, username, password, status, email, roleType, userDetails, createdAt, updatedAt);
    }

    // security에서 사용할 팩토리 메서드
    public static UserDto of(String loginId) {
        return new UserDto(
                null,
                loginId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    // Principal에서 사용할 factory method of 선언
    public static UserDto of(Long userId, String loginId, String username, String password, String email, RoleType roleType) {
        return new UserDto(
                userId,
                loginId,
                username,
                password,
                null,
                email,
                roleType,
                null,
                null,
                null
        );
    }

    // 서비스 레이어에서 entity를 dto로 변환시켜주는 코드
    public static UserDto fromEntity(User entity) {

        // stream으로 데이터를 변환해서 받아준다.
        List<UserDetailsDto> userDetails = entity.getUserDetails()
                .stream()
                .map(UserDetailsDto::fromEntity)
                .collect(Collectors.toList());

        return UserDto.of(
                entity.getId(),
                entity.getLoginId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getUserStatus(),
                entity.getEmail(),
                entity.getRoleType(),
                userDetails,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }


}