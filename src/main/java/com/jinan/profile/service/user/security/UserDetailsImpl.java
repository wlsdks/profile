package com.jinan.profile.service.user.security;

import com.jinan.profile.domain.user.constant.UserRoleEnum;
import com.jinan.profile.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final String loginId; // username 이던데 잘 고려해보자
    private final String password;

    public UserDetailsImpl(User user, String loginId, String password) {
        this.user = user;
        this.loginId = loginId;
        this.password = password;
    }

    public User getUser() {
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } // false -> 사용자 계정의 유요 기간 만료

    @Override
    public boolean isAccountNonLocked() {
        return true;
    } // false -> 사용자 계정 잠금상태

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } // false -> 비밀번호 만료

    @Override
    public boolean isEnabled() {
        return true;
    } // false -> 유효하지 않은 사용자

}
