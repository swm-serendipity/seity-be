package com.serendipity.seity.forbiddenword.service;

import com.serendipity.seity.forbiddenword.ForbiddenWord;
import com.serendipity.seity.forbiddenword.dto.CreateForbiddenWordRequest;
import com.serendipity.seity.forbiddenword.dto.ForbiddenWordResponse;
import com.serendipity.seity.forbiddenword.repository.ForbiddenWordRepository;
import com.serendipity.seity.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 금칙어 관련 service 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ForbiddenWordService {

    private final ForbiddenWordRepository forbiddenWordRepository;

    /**
     * 금칙어를 등록하는 메서드입니다.
     * @param request 등록하려는 금칙어
     * @param member 현재 로그인한 사용자
     */
    public void createForbiddenWord(CreateForbiddenWordRequest request, Member member) {

        forbiddenWordRepository.save(ForbiddenWord.createForbiddenWord(member, request.getValue()));
    }

    /**
     * 모든 금칙어 리스트를 조회하는 메서드입니다.
     * @return 금칙어 리스트
     */
    public List<ForbiddenWordResponse> getForbiddenWords() {

        List<ForbiddenWordResponse> result = new ArrayList<>();

        for (ForbiddenWord word : forbiddenWordRepository.findAll()) {

            result.add(ForbiddenWordResponse.of(word));
        }

        return result;
    }

    /**
     * 금칙어를 삭제하는 메서드입니다.
     * TODO: 등록한 사람만 삭제할 수 있는지 등 policy 규정 필요
     * @param id 금칙어 id
     */
    public void deleteForbiddenWord(String id) {

        forbiddenWordRepository.deleteById(id);
    }
}