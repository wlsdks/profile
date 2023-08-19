package com.jinan.profile.domain.shop;

import com.jinan.profile.domain.shop.constant.ProductType;
import jakarta.persistence.*;
import lombok.*;

@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PRODUCT")
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer price;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProductType productType; // INGREDIENT or TOOL


    @Builder
    private Product (Long id, String name, Integer price, String description, ProductType productType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.productType = productType;
    }

    public static Product of (String name, Integer price, String description, ProductType productType) {
        return new Product(null, name, price, description, productType);
    }
}
