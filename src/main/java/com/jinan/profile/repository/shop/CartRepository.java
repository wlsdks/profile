package com.jinan.profile.repository.shop;

import com.jinan.profile.domain.shop.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // loginId로 Cart 객체 가져오기
    Optional<Cart> findCartByUser_LoginId(String loginId);
}