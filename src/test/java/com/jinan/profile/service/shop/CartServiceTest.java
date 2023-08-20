package com.jinan.profile.service.shop;

import com.jinan.profile.config.TotalTestSupport;
import com.jinan.profile.domain.shop.Cart;
import com.jinan.profile.domain.shop.CartItem;
import com.jinan.profile.domain.shop.Product;
import com.jinan.profile.domain.user.User;
import com.jinan.profile.domain.user.constant.RoleType;
import com.jinan.profile.domain.user.constant.UserStatus;
import com.jinan.profile.exception.ProfileApplicationException;
import com.jinan.profile.repository.shop.CartRepository;
import com.jinan.profile.repository.shop.ProductRepository;
import com.jinan.profile.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("장바구니 Service 테스트")
class CartServiceTest extends TotalTestSupport {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartService cartService;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    @DisplayName("[해피] - 회원이 상품을 선택하면 장바구니에 저장된다.")
    @Test
    void saveProductToCart() {
        //given
        Product savedProduct = createProduct();
        User savedUser = createUser();
        Cart cart = createCart(savedUser, List.of());
        String loginId = savedUser.getLoginId();
        Long productId = savedProduct.getId();
        Integer quantity = 2;

        //when
        CartItem savedCartItem = cartService.saveCartItem(loginId, productId, quantity);


        //then
        assertThat(savedCartItem).isNotNull();
        assertThat(savedCartItem).isInstanceOf(CartItem.class);
        assertThat(savedCartItem).extracting("product").isEqualTo(savedProduct);
    }

    @DisplayName("[bad] - 존재하지 않는 회원이 상품을 장바구니에 저장하면 예외발생.")
    @Test
    void saveProductToCartExceptionOne() {
        //given
        Product savedProduct = createProduct();
        User savedUser = createUser();
        Cart cart = createCart(savedUser, List.of());
        String loginId = savedUser.getLoginId();
        Long productId = savedProduct.getId();
        Integer quantity = 2;

        //when & then
        assertThatThrownBy(() -> cartService.saveCartItem("test", productId, quantity)).isInstanceOf(ProfileApplicationException.class);

    }


    @DisplayName("[bad] - 회원이 존재하지 않는 상품을 장바구니에 저장하면 예외발생.")
    @Test
    void saveProductToCartExceptionTwo() {
        //given
        Product savedProduct = createProduct();
        User savedUser = createUser();
        Cart cart = createCart(savedUser, List.of());
        String loginId = savedUser.getLoginId();
        Long productId = savedProduct.getId();
        Integer quantity = 2;

        //when & then
        assertThatThrownBy(() -> cartService.saveCartItem(loginId, 34L, quantity)).isInstanceOf(ProfileApplicationException.class);
    }



    private Product createProduct() {
        Product product = Product.builder()
                .name("product name")
                .price(20000)
                .description("description")
                .build();
        return productRepository.save(product);
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

}