package com.jinan.profile.service.security.impl;

import com.jinan.profile.dto.security.SecurityUserDetailsDto;
import com.jinan.profile.dto.user.UserDto;
import com.jinan.profile.service.security.SecurityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Spring Security를 사용하기 위한 UserDetailsService의 구현체
 */
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SecurityUserService securityUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = UserDto.of(username);

        // 사용자 정보가 존재하지 않는 경우
        if (username == null || username.equals("")) {
            return securityUserService.login(userDto)
                    .map(user -> new SecurityUserDetailsDto(
                            user,
                            Collections.singleton(new SimpleGrantedAuthority(user.username()))))
                    .orElseThrow(() -> new AuthenticationServiceException(username));
        }
        // 비밀번호가 틀린 경우
        else {
            return securityUserService.login(userDto)
                    .map(user -> new SecurityUserDetailsDto(
                            user,
                            Collections.singleton(new SimpleGrantedAuthority(user.username()))))
                    .orElseThrow(() -> new BadCredentialsException(username));
        }
    }

}
