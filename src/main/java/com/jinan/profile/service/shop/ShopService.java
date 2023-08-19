package com.jinan.profile.service.shop;

import com.jinan.profile.repository.shop.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ShopService {

    private final ShopRepository shopRepository;

}
