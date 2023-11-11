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
    // success
    SUCCESS(true, 2000, HttpStatus.OK, "요청에 성공하였습니다."),


    /**
     * 4000 : Request (Client) 오류
     */
    // member
    NO_LOGIN_USER(false,4000, BAD_REQUEST, "로그인된 사용자가 없습니다."),
    INVALID_USER_ACCESS_EXCEPTION(false, 4001, FORBIDDEN, "권한이 없는 유저의 접근입니다."),
    INVALID_REFRESH_TOKEN_USER_ID_EXCEPTION(false, 4002, INTERNAL_SERVER_ERROR, "refresh token 내부에 사용자 id 정보가 존재하지 않습니다."),

    // exception handler
    HTTP_MESSAGE_NOT_READABLE(false,4010, BAD_REQUEST, "잘못된 Request 입니다."),
    MISSING_REQUEST_PARAMETER(false,4011, BAD_REQUEST, "필수 Request Parameter가 누락되었습니다."),
    VALIDATION_EXCEPTION(false,4012, BAD_REQUEST, "Validation 오류가 발생했습니다."),
    FILE_MAX_SIZE_EXCEEDED(false, 4013, BAD_REQUEST, "파일 업로드 용량을 초과헀습니다."),

    // sign up
    ALREADY_LOGIN_ID_EXIST(false, 4020, BAD_REQUEST, "이미 존재하는 로그인 ID입니다."),
    ALREADY_EMAIL_EXIST(false, 4021, BAD_REQUEST, "이미 존재하는 이메일입니다."),
    INVALID_MEMBER_PART_EXCEPTION(false, 4022, BAD_REQUEST, "part 값이 존재하지 않거나 올바르지 않습니다."),
    INVALID_MEMBER_ROLE_EXCEPTION(false, 4023, BAD_REQUEST, "role 값이 존재하지 않거나 올바르지 않습니다."),

    // login
    INVALID_REFRESH_TOKEN(false,4030, BAD_REQUEST, "refresh token이 유효하지 않습니다."),
    INVALID_LOGIN_ID_OR_PASSWORD(false, 4031, BAD_REQUEST, "아이디 또는 비밀번호가 올바르지 않습니다."),

    // prompt
    INVALID_PROMPT_ID_EXCEPTION(false, 4040, BAD_REQUEST, "유효하지 않은 프롬프트 세션 id입니다."),

    // post
    INVALID_POST_ID_EXCEPTION(false, 4050, BAD_REQUEST, "유효하지 않은 게시글 id입니다."),

    // detection
    INVALID_DETECTION_INFO_EXCEPTION(false, 4060, BAD_REQUEST, "유효하지 않은 민감정보 종류입니다."),
    INVALID_DETECTION_ID_EXCEPTION(false, 4061, BAD_REQUEST, "유효하지 않은 민감정보 탐지 id입니다."),
    INVALID_MEMBER_ID_EXCEPTION(false, 4062, BAD_REQUEST, "프롬프트의 user id가 유효하지 않습니다. " +
            "(프롬프트 작성자가 회원 탈퇴를 했을 가능성 존재)"),
    INVALID_USER_ID_EXCEPTION(false, 4063, BAD_REQUEST, "사용자의 id가 존재하지 않습니다."),
    INVALID_PROMPT_DETECTION_ID_EXCEPTION(false, 4064, BAD_REQUEST, "민감정보 검출 내역 id가 유효하지 않습니다."),

    // calling
    INVALID_CALLING_ID_EXCEPTION(false, 4070, BAD_REQUEST, "소명 요청 id가 유효하지 않습니다."),

    /**
     * 5000 : Database, Server 오류
     */
    // exception handler
    UNKNOWN_SERVER_EXCEPTION(false,5000, INTERNAL_SERVER_ERROR, "알 수 없는 서버 오류가 발생했습니다."),

    // prompt
    CHAT_GPT_EXCEPTION(false, 5010, INTERNAL_SERVER_ERROR, "ChatGPT API에서 오류가 발생했습니다."),

    // calling
    SSE_SEND_EXCEPTION(false, 5020, INTERNAL_SERVER_ERROR, "SSE 전송 과정 중 오류가 발생했습니다.");

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
