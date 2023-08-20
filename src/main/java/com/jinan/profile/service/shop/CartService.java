package com.jinan.profile.service.shop;

import com.jinan.profile.domain.shop.Cart;
import com.jinan.profile.domain.shop.CartItem;
import com.jinan.profile.domain.shop.Product;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.exception.ErrorCode;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.shop.CartItemRepository;
import com.jinan.profile.repository.shop.CartRepository;
import com.jinan.profile.repository.shop.ProductRepository;
import com.jinan.profile.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    /**
     * loginId, productId, quantity로 CartItem 저장
     */
    @Transactional
    public CartItem saveCartItem(String loginId, Long productId, Integer quantity) {
        // 로그인 한 유저 객체 가져오기
        User user = userRepository.findUserByLoginId(loginId)
                .orElseThrow(() -> new ProfileApplicationException(ErrorCode.USER_NOT_FOUND));

        // 해당 유저에게 장바구니가 있는지 확인
        Cart cart = cartRepository.findCartByUser_LoginId(loginId).orElseGet(
                () -> {
                    // 장바구니가 없으면 장바구니 생성
                    return cartRepository.save(Cart.of(user, List.of()));
                }
        );

        // productId로 product 객체 가져오기
        Product product = productRepository.findProductById(productId).orElseThrow(() -> new ProfileApplicationException(ErrorCode.PRODUCT_NOT_FOUND));

        // cartItem에 product 객체와 quantity 저장하기
        CartItem cartItem = CartItem.of(cart, product, quantity);

        // cartItem 저장
        return cartItemRepository.save(cartItem);
    }
}
