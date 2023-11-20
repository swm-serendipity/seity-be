package com.serendipity.seity.dpr.controller;

import com.serendipity.seity.common.response.BaseResponse;
import com.serendipity.seity.dpr.dto.CreateWikiWordRequest;
import com.serendipity.seity.dpr.dto.DprRequest;
import com.serendipity.seity.dpr.service.DprService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.serendipity.seity.common.response.BaseResponseStatus.SUCCESS;

/**
 * DPR 시연을 위한 테스트 컨트롤러입니다.
 *
 * @author Min Ho CHO
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/dpr")
public class DprController {

    private final DprService dprService;

    @GetMapping("/test1")
    public BaseResponse<?> getDprTest1() {

        return new BaseResponse<>(dprService.createTestResponseCase1());
    }

    /**
     * wiki word 생성 컨트롤러 메서드입니다.
     * @param request wiki word 정보
     * @return 결과
     */
    @PostMapping("/word")
    public BaseResponse<?> createWikiWord(@RequestBody CreateWikiWordRequest request) {

        dprService.createTestResponseCase(request);
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * dpr 컨트롤러 메서드입니다.
     * @param request 질문
     * @return dpr 결과
     */
    @PostMapping
    public BaseResponse<?> dpr(@RequestBody DprRequest request) {

        return new BaseResponse<>(dprService.dpr(request.getQuestion()));
    }

    /**
     * 모든 wiki word를 조회하는 컨트롤러 메서드입니다.
     * @return wiki word 리스트
     */
    @GetMapping("/word")
    public BaseResponse<?> getAllWikiWord() {

        return new BaseResponse<>(dprService.getWikiWords());
    }
}
