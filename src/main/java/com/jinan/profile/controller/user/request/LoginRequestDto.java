package com.jinan.profile.controller.user.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class LoginRequestDto {
    private String loginId;
    private String password;
}
