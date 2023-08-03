//package com.jinan.profile.dto.security;
//
//import com.jinan.profile.domain.type.RoleType;
//import com.jinan.profile.dto.user.UserDto;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
///**
// * 1. Spring Security 인증 전용 클래스 -> UserDetails를 implements(구현)해서 인증정보라는것을 만들어서 사용했다.
// *
// */
//public record Principal(
//        Long userId,
//        String loginId,
//        String username,
//        String password,
//        String email,
//        RoleType roleType
//) implements UserDetails {
//
//    // OAuth2와 관련없는 생성자 factory 메소드 oAuth2Attributes 필드를 사용하지 않는 코드를 위함
//    public static Principal of(Long userId, String loginId, String username, String password, String email, RoleType roleType) {
//        return new Principal(userId, loginId, username, password, email, roleType);
//    }
//
//    // dto를 받아서 인증정보 데이터타입인 BoardPrincipal로 변환해주는 factory 메소드
//    public static Principal from(UserDto dto) {
//        return Principal.of(
//                dto.userId(),
//                dto.loginId(),
//                dto.username(),
//                dto.password(),
//                dto.email(),
//                dto.roleType()
//        );
//    }
//
//    public UserDto toDto() {
//        return UserDto.of(
//                userId,
//                loginId,
//                username,
//                password,
//                email,
//                roleType
//        );
//    }
//
//
//    /**
//     * Spring Security 기능 사용을 위한 구현
//     */
//    @Override public String getUsername() { return username; }
//
//    // 이건 어떻게 해야할지 알아보자
//    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return null; }
//
//    @Override public String getPassword() { return password; }
//    @Override public boolean isAccountNonExpired() { return true; }
//    @Override public boolean isAccountNonLocked() { return true; }
//    @Override public boolean isCredentialsNonExpired() { return true; }
//    @Override public boolean isEnabled() { return true; }
//
//}
