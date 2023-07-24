package com.jinan.profile.dto;

import com.jinan.profile.domain.UserDetails;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.jinan.profile.domain.UserDetails}
 * 이렇게 설계하면 entity는 dto를 의존하지 않아서 영향을 받지 않는다.
 */
public record UserDetailsDto(
        Long userDetailId,
        UsersDto userDto,
        String provider,
        String providerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // factory method 선언
    public UserDetailsDto of(Long userDetailId, UsersDto userDto, String provider, String providerId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserDetailsDto(userDetailId, userDto, provider, providerId, createdAt, updatedAt);
    }

    // 서비스 레이어에서 entity를 dto로 변환시켜주는 코드
    public static UserDetailsDto from(UserDetails entity) {
        return new UserDetailsDto(
                entity.getUserDetailId(),
                UsersDto.from(entity.getUsers()), // UserDto의 factory method로 만들어서 가져온다.
                entity.getProvider(),
                entity.getProviderId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}