package com.jinan.profile.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

//@AllArgsConstructor -> 이걸로는 오류가 난다.
@Getter
public class ProfileApplicationException extends RuntimeException {

    private ErrorCode errorCode;

    // 특별한 로직 [message를 가져오는] 생성자가 필요하기 때문에, 직접 생성자를 정의해야 한다.
    public ProfileApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
