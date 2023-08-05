package com.jinan.profile.domain.user.constant;

public enum UserRoleEnum {

    MEMBER(Authority.MEMBER),
    PRIVATE_MEMBER(Authority.PRIVATE_MEMBER),
    VIP_MEMBER(Authority.VIP_MEMBER),
    ADMIN(Authority.ADMIN);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String MEMBER = "ROLE_MEMBER";
        public static final String PRIVATE_MEMBER = "ROLE_PRIVATE_MEMBER";
        public static final String VIP_MEMBER = "ROLE_VIP_MEMBER";
        public static final String ADMIN = "ROLE_ADMIN";
    }

    public static UserRoleEnum fromString(String role) {
        return switch (role) {
            case "ROLE_MEMBER" -> MEMBER;
            case "ROLE_PRIVATE_MEMBER" -> PRIVATE_MEMBER;
            case "ROLE_VIP_MEMBER" -> VIP_MEMBER;
            case "ROLE_ADMIN" -> ADMIN;
            default -> null;
        };
    }

}
