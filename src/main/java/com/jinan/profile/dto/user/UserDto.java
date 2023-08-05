package com.jinan.profile.dto.user;

import com.jinan.profile.domain.user.constant.UserRoleEnum;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.UserStatus;

import java.time.LocalDateTime;

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
        UserRoleEnum role,
//        List<UserDetailsDto> userDetails, // 계정에서의 참조는 없앤다.
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // factory method of 선언
    public static UserDto of(Long userId, String loginId, String username, String password, UserStatus status, String email, UserRoleEnum userRoleEnum, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserDto(userId, loginId, username, password, status, email, userRoleEnum, createdAt, updatedAt);
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
                null
        );
    }

    // Principal에서 사용할 factory method of 선언
    public static UserDto of(Long userId, String loginId, String username, String password, String email, UserRoleEnum userRoleEnum) {
        return new UserDto(
                userId,
                loginId,
                username,
                password,
                null,
                email,
                userRoleEnum,
                null,
                null
        );
    }

    // 서비스 레이어에서 entity를 dto로 변환시켜주는 코드
    public static UserDto fromEntity(User entity) {

//        // stream으로 데이터를 변환해서 받아준다.
//        List<UserDetailsDto> userDetails = entity.getUserDetails()
//                .stream()
//                .map(UserDetailsDto::fromEntity)
//                .collect(Collectors.toList());

        return UserDto.of(
                entity.getId(),
                entity.getLoginId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getUserStatus(),
                entity.getEmail(),
                entity.getRole(),
//                userDetails,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }


}