package com.serendipity.seity.dlp.controller;

import com.serendipity.seity.common.response.BaseResponse;
import com.serendipity.seity.dlp.dto.DlpRequest;
import com.serendipity.seity.dlp.service.DlpService;
import com.serendipity.seity.dlp.service.GoogleDlpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * DLP 컨트롤러입니다.
 *
 * @author Min Ho CHO
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dlp")
@Slf4j
public class DlpController {

    private final GoogleDlpService googleDlpService;
    private final DlpService dlpService;

    /**
     * DLP를 이용한 비식별화 메서드입니다.
     * @param request 질문
     * @return 원본 질문, 변환된 질문, 변환된 부분
     */
    @PostMapping("/de-identification")
    public BaseResponse<?> deIdentification(@RequestBody DlpRequest request) {

        return new BaseResponse<>(googleDlpService.callDlpApi(request.getQuestion()));
    }

    @PostMapping("/de-identification/name")
    public BaseResponse<?> deIdentificationWithName(@RequestBody DlpRequest request) {

        return new BaseResponse<>(googleDlpService.callDlpApiForName(request.getQuestion()));
    }

    /**
     * 자체 개발된 dlp 서버로의 비식별화 메서드입니다.
     * @return dlp 서버로부터의 http response
     */
    @PostMapping("/v2/de-identification")
    public ResponseEntity<String> deIdentificationV2(@RequestBody DlpRequest request) {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dlpService.sendRequest(request.getQuestion()));
    }
}
