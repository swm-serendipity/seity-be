package com.serendipity.seity.common.exception;

import com.serendipity.seity.common.response.BaseResponse;
import com.serendipity.seity.common.response.BaseResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 공통 예외 처리 메서드입니다.
 *
 * @author Min Ho CHO
 */
@RestControllerAdvice
public class CustomRestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<BaseResponseStatus>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        // 아예 잘못된 형식으로 request 를 요청할 경우 예외 발생
        ex.printStackTrace();
        return BaseResponse.toResponseEntity(new BaseException(BaseResponseStatus.HTTP_MESSAGE_NOT_READABLE));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<BaseResponseStatus>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        // 필수 request parameter가 없을 때 발생
        ex.printStackTrace();
        return BaseResponse.toResponseEntity(new BaseException(BaseResponseStatus.MISSING_REQUEST_PARAMETER));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<BaseResponseStatus>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        // Validation 오류일 때 발생
        ex.printStackTrace();
        return BaseResponse.toResponseEntity(new BaseException(BaseResponseStatus.VALIDATION_EXCEPTION));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public final ResponseEntity<BaseResponse<BaseResponseStatus>> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex) {

        // 파일 업로드 용량 초과시 발생
        ex.printStackTrace();
        return BaseResponse.toResponseEntity(new BaseException(BaseResponseStatus.FILE_MAX_SIZE_EXCEEDED));

    }

    @ExceptionHandler(BaseException.class)
    public final ResponseEntity<BaseResponse<BaseResponseStatus>> handleBaseException(BaseException exception) {

        // throws BaseException을 처리
        return BaseResponse.toResponseEntity(exception);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<BaseResponse<BaseResponseStatus>> handleAllExceptions(Exception ex) {
        // 따로 핸들링하지 않은 기타 모든 예외를 처리
        ex.printStackTrace();

        return BaseResponse.toResponseEntity(new BaseException(BaseResponseStatus.UNKNOWN_SERVER_EXCEPTION));
    }

}
