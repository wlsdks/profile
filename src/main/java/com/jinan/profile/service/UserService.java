package com.jinan.profile.service;

import com.jinan.profile.dto.UsersDto;
import com.jinan.profile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * repository에서 가져오는 entity정보는 service 외부로는 나가지 않도록 dto를 사용한다.(격리를 시킨다.)
     * 이 메소드에서 마지막에 orElse 예외처리를 해주지 않은것은 이 service 레이어에서 에러처리를 전담하지않기 위함이다.(상위로 전파)
     */
    public Optional<UsersDto> searchUser(String username) {
        return userRepository.findByUsername(username)
                .map(UsersDto::from);
    }



}
