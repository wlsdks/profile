package com.jinan.profile.controller.user.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class SignUpRequestDto {

    private String loginId;
    private String password;
    private String role;

}