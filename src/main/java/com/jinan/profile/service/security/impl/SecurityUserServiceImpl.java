package com.jinan.profile.service.security.impl;

import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.repository.UserRepository;
import com.jinan.profile.service.security.SecurityUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SecurityUserServiceImpl implements SecurityUserService {

    private final UserRepository userRepository;

    /**
     * 로그인 구현체
     */
    @Override
    public Optional<UserDto> login(UserDto userDto) {
        return userRepository
                .findByUsername(userDto.username())
                .map(UserDto::fromEntity);
    }

}
