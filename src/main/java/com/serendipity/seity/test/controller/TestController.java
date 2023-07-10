package com.serendipity.seity.test.controller;

import com.serendipity.seity.test.TestEntity;
import com.serendipity.seity.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 테스트 컨트롤러입니다.
 *
 * @author Min Ho CHO
 */
@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {

    private final TestRepository testRepository;

    @GetMapping()
    public String test() {
        return "success";
    }

    @GetMapping("/db-test")
    public String dbTest() {

        // 1. 테스트 엔티티 저장
        TestEntity entity = testRepository.save(TestEntity.createTestEntity());
        testRepository.flush();

        // 2. 테스트 엔티티 불러오기
        if (testRepository.findById(entity.getId()).isEmpty()) {
            return "error";
        } else {
            return testRepository.findById(entity.getId()).get().getName();
        }
    }

    /**
     * security 테스트를 위한 메서드입니다.
     */
    @GetMapping("/security")
    public String securityTest() {
        return "success";
    }
}
