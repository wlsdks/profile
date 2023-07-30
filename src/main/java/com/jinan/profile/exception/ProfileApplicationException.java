package com.jinan.profile.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileApplicationException extends RuntimeException {

    private ErrorCode errorCode;
}
