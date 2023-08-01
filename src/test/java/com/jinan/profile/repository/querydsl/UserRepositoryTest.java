package com.jinan.profile.repository.querydsl;

import com.jinan.profile.config.QuerydslConfig;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("[Repository] 유저 테스트")
@DataJpaTest // 단위테스트
@Import({QuerydslConfig.class}) // slice 테스트 시 JPAQueryFactory 주입이 안되므로 직접 config를 넣어준다.
class UserRepositoryTest {

    private UserRepository userRepository;

    public UserRepositoryTest(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void selectTest() {
        //given

        //when
        User user = userRepository.findByUserId(22L);


        //then
    }

}