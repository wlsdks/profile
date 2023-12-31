package com.jinan.profile.controller.shop;


import com.jinan.profile.service.security.SecurityService;
import com.jinan.profile.service.shop.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/shop")
@Slf4j
@RequiredArgsConstructor
@Controller
public class CartController {

    private final CartService cartService;
    private final SecurityService securityService;



}
