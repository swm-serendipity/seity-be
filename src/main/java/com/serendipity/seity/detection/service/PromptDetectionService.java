package com.serendipity.seity.detection.service;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.detection.DetectionInfo;
import com.serendipity.seity.detection.DetectionStatus;
import com.serendipity.seity.detection.DetectionWord;
import com.serendipity.seity.detection.PromptDetection;
import com.serendipity.seity.detection.dto.*;
import com.serendipity.seity.detection.repository.PromptDetectionRepository;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.member.repository.MemberRepository;
import com.serendipity.seity.prompt.Prompt;
import com.serendipity.seity.prompt.dto.DetectionRequest;
import com.serendipity.seity.prompt.repository.PromptRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.serendipity.seity.common.response.BaseResponseStatus.*;

/**
 * 중요정보 탐지 관련 서비스 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PromptDetectionService {

    private final PromptDetectionRepository promptDetectionRepository;
    private final PromptRepository promptRepository;
    private final MemberRepository memberRepository;

    /**
     * 민감정보 탐지 엔티티를 생성하는 메서드입니다.
     * @param prompt 프롬프트
     * @param detections 탐지된 민감정보
     * @return 생성된 민감정보 탐지 엔티티 id
     */
    public CreateDetectionResponse createPromptDetection(Prompt prompt, int index, List<DetectionRequest> detections, Member member) {

        if (detections.isEmpty()) {

            return null;
        }

        List<DetectionWord> detectionWords = new ArrayList<>();

        for (DetectionRequest detection : detections) {

            if (detection.getIsDeIdentified()) {
                detectionWords.add(DetectionWord.createDetectionWord(detection.getIndex(), detection.getLength(),
                        DetectionStatus.DE_IDENTIFIED, DetectionInfo.of(detection.getEntity())));
            } else {
                detectionWords.add(DetectionWord.createDetectionWord(detection.getIndex(), detection.getLength(),
                        DetectionStatus.PENDING, DetectionInfo.of(detection.getEntity())));
            }
        }

        return new CreateDetectionResponse(promptDetectionRepository.save(PromptDetection.createPromptDetection(
                prompt,
                member,
                index,
                detectionWords)).getId());
    }

    /**
     * 민감정보 탐지 내역을 날짜순으로 조회하는 메서드입니다.
     * @param member 현재 로그인한 사용자
     * @return 민감정보 탐지 내역
     */
    public DetectionPagingResponse getDetectionList(Member member, int pageNumber, int pageSize) {

        Pageable pageable =
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));

        List<PromptDetection> findDetectionList
                = promptDetectionRepository.findPromptDetectionByIsDetectedAndPartOrderByCreateTimeDesc(
                        true, member.getPart().getValue(), pageable);

        List<MultipleDetectionResponse> result = new ArrayList<>();

        for (PromptDetection detection : findDetectionList) {

            result.add(MultipleDetectionResponse.of(
                    promptRepository.findById(detection.getPromptId()),
                    memberRepository.findById(detection.getUserId()),
                    detection));
        }

        return pagingDetections(
                findDetectionList,
                promptDetectionRepository.findPromptDetectionByIsDetectedAndPartOrderByCreateTimeDesc(true, member.getPart().getValue()).size(),
                pageSize,
                pageNumber);
    }

    /**
     * 민감정보 탐지 내역 1개를 조회하는 메서드입니다.
     * @param id 탐지 내역 id
     * @return 민감정보 탐지 내역 정보
     */
    public SingleDetectionResponse getSingleDetection(String id, Member member) throws BaseException {

        PromptDetection findDetection
                = promptDetectionRepository.findById(id).orElseThrow(
                        () -> new BaseException(INVALID_PROMPT_DETECTION_ID_EXCEPTION));

        if (!findDetection.getPart().equals(member.getPart().getValue())) {

            throw new BaseException(INVALID_USER_ACCESS_EXCEPTION);
        }

        return SingleDetectionResponse.of(
                promptRepository.findById(findDetection.getPromptId()).orElseThrow(
                        () -> new BaseException(INVALID_PROMPT_ID_EXCEPTION)),
                memberRepository.findById(findDetection.getUserId()).orElseThrow(
                        () -> new BaseException(INVALID_USER_ID_EXCEPTION)),
                findDetection
        );
    }

    /**
     * 1개의 민감정보 탐지 내역에 대해 해결된 것으로 갱신하는 메서드입니다.
     * @param id PromptDetection id
     * @param index 대상 탐지 내역의 index
     */
    public void solvePromptDetection(String id, int index, Member member) throws BaseException {

        PromptDetection findDetection = promptDetectionRepository.findById(id).orElseThrow(
                () -> new BaseException(INVALID_DETECTION_ID_EXCEPTION));

        Prompt findPrompt = promptRepository.findById(findDetection.getPromptId()).orElseThrow(
                () -> new BaseException(INVALID_PROMPT_ID_EXCEPTION));

        Member findMember = memberRepository.findById(findPrompt.getUserId()).orElseThrow(
                () -> new BaseException(INVALID_MEMBER_ID_EXCEPTION));

        if (!findMember.getPart().equals(member.getPart())) {   // 자신이 아닌 다른 파트의 직원 소명을 해결하려 하는 경우

            throw new BaseException(INVALID_USER_ACCESS_EXCEPTION);
        }

        findDetection.solveDetection(index);
        promptDetectionRepository.save(findDetection);
    }

    /**
     * 1개의 소명 엔티티를 삭제하는 메서드입니다.
     * @param id 소명 엔티티 id
     */
    public void deletePromptDetection(String id, Member member) throws BaseException {

        PromptDetection findDetection
                = promptDetectionRepository.findById(id).orElseThrow(
                        () -> new BaseException(INVALID_PROMPT_DETECTION_ID_EXCEPTION));

        if (!findDetection.getPart().equals(member.getPart().getValue())) {

            throw new BaseException(INVALID_USER_ACCESS_EXCEPTION);
        }

        promptDetectionRepository.deleteById(id);
    }

    private DetectionPagingResponse pagingDetections(List<PromptDetection> detections, int totalDetectionNumber, int pageSize, int page) {

        List<MultipleDetectionResponse> result = new ArrayList<>();

        for (PromptDetection detection : detections) {

            result.add(MultipleDetectionResponse.of(
                    promptRepository.findById(detection.getPromptId()),
                    memberRepository.findById(detection.getUserId()),
                    detection));
        }

        return new DetectionPagingResponse((totalDetectionNumber - 1) / pageSize + 1, totalDetectionNumber, page, result);
    }

}
