package com.jinan.profile.repository.shop;

import com.jinan.profile.config.TotalTestSupport;
import com.jinan.profile.domain.shop.Cart;
import com.jinan.profile.domain.shop.CartItem;
import com.jinan.profile.domain.shop.Product;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("장바구니 Repository 테스트")
class CartRepositoryTest extends TotalTestSupport {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private UserRepository userRepository;

    @DisplayName("[happy] - 장바구니 만들기")
    @Test
    void saveCart() {
        //given
        User savedUser = createUser();
        Cart cart = createCart(savedUser, List.of());

        //when
        Cart savedCart = cartRepository.save(cart);

        //then
        assertThat(savedCart).isNotNull();
        assertThat(savedCart).isEqualTo(cart);
        assertThat(savedCart).isInstanceOf(Cart.class);
    }


    private User createUser() {
        User user = User.of(
                "wlsdks123",
                "wlsdks12",
                "wlsdks",
                "wlsdks12@naver.com",
                RoleType.ADMIN,
                UserStatus.Y
        );
        return userRepository.save(user);
    }

    private Cart createCart(User user, List<CartItem> cartItems) {
        return Cart.builder()
                .user(user)
                .cartItems(cartItems)
                .build();
    }

    private CartItem createCartItem(Cart cart, Product product) {
        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(10000000)
                .build();
        return cartItemRepository.save(cartItem);
    }

}