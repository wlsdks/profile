package com.jinan.profile.controller;

import com.jinan.profile.domain.user.Users;
import com.jinan.profile.dto.message.MessageDto;
import com.jinan.profile.dto.security.Principal;
import com.jinan.profile.dto.user.UsersDto;
import com.jinan.profile.repository.UserRepository;
import com.jinan.profile.service.UserService;
import com.jinan.profile.service.message.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/main")
@Controller
public class ChatMainController {


    @GetMapping("/page")
    public String rootPage() {
        return "mainPage";
    }

}
