package com.serendipity.seity.common.exception;

import com.serendipity.seity.common.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *  모든 예외는 서비스 로직에서 처리하고, 예외가 필요할 때는 BaseException으로 처리합니다.
 */
@Getter
@AllArgsConstructor
public class BaseException extends Exception {

    private BaseResponseStatus status;
}
