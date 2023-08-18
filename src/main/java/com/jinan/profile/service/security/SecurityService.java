package com.jinan.profile.service.security;

import com.jinan.profile.domain.user.User;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * 일반적인 설계 원칙에 따르면:
 *
 * 1. 컨트롤러는 주로 사용자의 요청을 처리하고 응답을 반환하는 역할에 집중해야 한다.
 * 2. 서비스 계층은 비즈니스 로직 및 데이터 처리를 담당한다.
 * 3. 도메인 모델은 비즈니스 로직의 핵심이며, 데이터와 관련된 규칙 및 연산을 포함한다.
 * 4. 리포지토리는 데이터의 CRUD 작업을 담당한다.
 *
 * 결론:
 * 따라서, SecurityContextHolder를 사용하여 현재 사용자를 가져오는 로직은 서비스 또는 유틸리티 클래스로 추출하는 것이 좋다.
 * 이렇게 하면 컨트롤러는 보안 관련 로직에 대해 알 필요가 없으며, 요청 처리에만 집중할 수 있습니다.
 * 그래서 이렇게 서비스를 따로 만들었다. 이 bean을 가져다 클래스에서 사용하면 된다.
 */
@RequiredArgsConstructor
@Service
public class SecurityService {

    private final UserRepository userRepository;

    /**
     * 지금 접속중인 유저의 이름을 꺼내온다.
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * 유저이름으로 유저를 꺼낸다.
     */
    public User getCurrentUser() {
        String username = getCurrentUsername();

        if (username != null) {
            return userRepository.findUserByLoginId(username)
                    .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));
        }

        return null;
    }
}
