package com.jinan.profile.service.security;

import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SecurityUserServiceImpl implements SecurityUserService{

    private final UserRepository userRepository;

    // login하면 Optional<UserDto>를 반환한다.
    @Override
    public Optional<UserDto> login(UserDto userDto) {
        return userRepository.findByLoginId(userDto.loginId())
                .map(UserDto::fromEntity);
    }

}
