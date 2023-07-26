package com.example.demo.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiErrorCode implements ErrorCode {
    EMPTY_USER_ID(HttpStatus.BAD_REQUEST, "ID를 입력해주세요."),
    EMPTY_USER_EMAIL(HttpStatus.BAD_REQUEST, "이메일을 입력해주세요."),
    EMPTY_USER_NAME(HttpStatus.BAD_REQUEST, "이름을 입력해주세요."),
    EMPTY_USER_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호를 입력해주세요."),
    EMPTY_REG_NO(HttpStatus.BAD_REQUEST, "주민등록번호를 입력해주세요."),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "ID는 영어, 숫자로만 이뤄져야합니다."),
    INVALID_USER_NAME(HttpStatus.BAD_REQUEST, "이름은 한글 또는 영어로만 이뤄져야합니다."),
    INVALID_REG_NO(HttpStatus.BAD_REQUEST, "주민등록번호가 형식에 맞지 않습니다."),
    NOT_ALLOWED_SIGNUP_USER(HttpStatus.BAD_REQUEST, "회원가입이 불가능한 유저입니다."),
    WRONG_USER_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    DUPLICATED_USER_ID(HttpStatus.BAD_REQUEST, "이미 존재하는 ID입니다."),
    DUPLICATED_USER_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    NO_MATCHING_USER_INFO(HttpStatus.BAD_REQUEST, "일치하는 회원 정보가 없습니다."),
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "Access Token 값이 잘못됐습니다. 다시 로그인해주세요."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh Token 값이 잘못됐습니다. 다시 로그인해주세요."),
    EMPTY_JWT_TOKEN(HttpStatus.FORBIDDEN, "Access Token 값이 존재하지 않습니다. 다시 로그인해주세요."),
    EMPTY_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "Refresh Token 값이 존재하지 않습니다. 다시 로그인해주세요."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "Refresh Token 이 만료됐습니다. 다시 로그인해주세요.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
