package com.serendipity.seity.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * 에러 코드 관리를 위한 enum 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 2000 : 요청 성공
     */
    SUCCESS(true, 2000, HttpStatus.OK, "요청에 성공하였습니다."),


    /**
     * 4000 : Request (Client) 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, BAD_REQUEST, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, UNAUTHORIZED, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, UNAUTHORIZED, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003, FORBIDDEN, "권한이 없는 유저의 접근입니다."),
    NO_LOGIN_USER(false,2004, BAD_REQUEST, "로그인된 사용자가 없습니다."),
    INVALID_REFRESH_TOKEN(false,2005, BAD_REQUEST, "refresh token이 유효하지 않습니다."),
    INVALID_ACCESS_TOKEN(false,2006, BAD_REQUEST, "access token이 유효하지 않습니다."),
    WRONG_ACCESS(false,2008, BAD_REQUEST, "잘못된 접근입니다."),

    HTTP_MESSAGE_NOT_READABLE(false,2030, BAD_REQUEST, "잘못된 Request 입니다."),
    MISSING_REQUEST_PARAMETER(false,2031, BAD_REQUEST, "필수 Request Parameter가 누락되었습니다."),

    VALIDATION_EXCEPTION(false,2032, BAD_REQUEST, "Validation 오류가 발생했습니다."),

    INVALID_MEMBER_PART_EXCEPTION(false, 2033, BAD_REQUEST, "part 값이 존재하지 않거나 올바르지 않습니다."),
    INVALID_MEMBER_ROLE_EXCEPTION(false, 2034, BAD_REQUEST, "role 값이 존재하지 않거나 올바르지 않습니다."),

    FILE_MAX_SIZE_EXCEEDED(false, 2041, BAD_REQUEST, "파일 업로드 용량을 초과헀습니다."),

    INVALID_PROMPT_ID_EXCEPTION(false, 2050, BAD_REQUEST, "유효하지 않은 프롬프트 세션 id입니다."),
    INVALID_USER_ACCESS_EXCEPTION(false, 2051, FORBIDDEN, "권한이 없는 유저의 접근입니다."),
    INVALID_POST_ID_EXCEPTION(false, 2052, BAD_REQUEST, "유효하지 않은 게시글 id입니다."),

    /**
     * 5000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 5000, INTERNAL_SERVER_ERROR,"데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 5001, INTERNAL_SERVER_ERROR, "서버와의 연결에 실패하였습니다."),
    UNKNOWN_SERVER_EXCEPTION(false,5002, BAD_REQUEST, "알 수 없는 서버 오류가 발생했습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    private BaseResponseStatus(boolean isSuccess, int code, HttpStatus httpStatus,  String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
