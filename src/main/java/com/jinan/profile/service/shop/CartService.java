package com.jinan.profile.service.shop;

import com.jinan.profile.domain.shop.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartService {


    public CartItem saveCartItem(String loginId, Long productId) {
        return null;
    }
}
