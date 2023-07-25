package com.jinan.profile.dto.user;

import com.jinan.profile.domain.type.RoleType;
import com.jinan.profile.domain.user.Users;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link Users}
 */
public record UsersDto(
        Long userId,
        String username,
        String password,
        String email,
        RoleType roleType,
        List<UserDetailsDto> userDetails, // 계정에서의 참조는 없앤다.
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // factory method of 선언
    public static UsersDto of(Long userId, String username, String password, String email, RoleType roleType, List<UserDetailsDto> userDetails, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UsersDto(userId, username, password, email, roleType, userDetails, createdAt, updatedAt);
    }

    // Principal에서 사용할 factory method of 선언
    public static UsersDto of(Long userId, String username, String password, String email, RoleType roleType) {
        return new UsersDto(
                userId,
                username,
                email,
                password,
                roleType,
                null, // 여기를 어똫게 처리할지 고민이다.
                null,
                null
        );
    }

    // 서비스 레이어에서 entity를 dto로 변환시켜주는 코드
    public static UsersDto fromEntity(Users entity) {

        // stream으로 데이터를 변환해서 받아준다.
        List<UserDetailsDto> userDetails = entity.getUserDetails()
                .stream()
                .map(UserDetailsDto::fromEntity)
                .collect(Collectors.toList());

        return UsersDto.of(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getRoleType(),
                userDetails,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }


}