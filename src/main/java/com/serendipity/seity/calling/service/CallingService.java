package com.serendipity.seity.calling.service;

import com.serendipity.seity.calling.Calling;
import com.serendipity.seity.calling.SseEventName;
import com.serendipity.seity.calling.SseRepositoryKeyRule;
import com.serendipity.seity.calling.dto.callingrequest.*;
import com.serendipity.seity.calling.repository.CallingRepository;
import com.serendipity.seity.calling.repository.SseInMemoryRepository;
import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.detection.PromptDetection;
import com.serendipity.seity.detection.repository.PromptDetectionRepository;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.member.repository.MemberRepository;
import com.serendipity.seity.prompt.Prompt;
import com.serendipity.seity.prompt.repository.PromptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.serendipity.seity.calling.CallingStatus.PENDING;
import static com.serendipity.seity.calling.SseEventName.CALLING_REQUEST;
import static com.serendipity.seity.common.response.BaseResponseStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * 소명에 관련된 서비스 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CallingService {

    @Value("${sse.timeout}")
    private String sseTimeout;

    private static final String UNDER_SCORE = "_";
    private static final String CONNECTED = "CONNECTED";

    private final CallingRepository callingRepository;
    private final SseInMemoryRepository sseRepository;
    private final PromptDetectionRepository promptDetectionRepository;
    private final PromptRepository promptRepository;
    private final MemberRepository memberRepository;

    /**
     * 소명 요청을 하는 메서드입니다.
     * @param promptDetectionId 민감정보 탐지 id
     * @param sender 송신자
     * @return 생성된 소명 요청 엔티티 id
     */
    public CallingRequestResponse createCalling(String promptDetectionId, Member sender) throws BaseException {

        PromptDetection findDetection = promptDetectionRepository.findById(promptDetectionId).orElseThrow(
                () -> new BaseException(INVALID_DETECTION_ID_EXCEPTION));

        Prompt prompt = promptRepository.findById(findDetection.getPromptId()).orElseThrow(
                        () -> new BaseException(INVALID_PROMPT_ID_EXCEPTION));

        Member receiver = memberRepository.findById(prompt.getUserId()).orElseThrow(
                () -> new BaseException(INVALID_USER_ID_EXCEPTION));

        Calling calling =
                callingRepository.save(Calling.createCalling(promptDetectionId, sender.getId(), receiver.getId()));

        // 알람 전송
        sendToClient(receiver, CALLING_REQUEST, CallingRequestAlarmResponse.of(calling, prompt, findDetection.getIndex(), sender));

        return new CallingRequestResponse(calling.getId());
    }

    /**
     * 해당 사용자에게 전송된 소명 요청 리스트를 조회하는 메서드입니다.
     * @param pageNumber 페이지 번호 (0부터 시작)
     * @param pageSize 페이지 크기
     * @param member 현재 로그인한 사용자
     * @return 소명 요청 리스트
     */
    public CallingRequestPagingResponse getCallingList(int pageNumber, int pageSize, Member member) throws BaseException {

        Pageable pageable =
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));

        List<Calling> findCallings = callingRepository.findByReceiverId(member.getId(), pageable);
        List<MultipleCallingRequestResponse> callings = new ArrayList<>();

        for (Calling calling : findCallings) {

            PromptDetection findDetection = promptDetectionRepository.findById(calling.getPromptDetectionId()).orElseThrow(
                    () -> new BaseException(INVALID_DETECTION_ID_EXCEPTION));

            callings.add(MultipleCallingRequestResponse.of(
                    calling,
                    findDetection,
                    promptRepository.findById(findDetection.getPromptId()).orElseThrow(
                            () -> new BaseException(INVALID_PROMPT_ID_EXCEPTION)),
                    member
                    ));
        }

        int totalCallingNumber = callingRepository.countByReceiverId(member.getId());
        return new CallingRequestPagingResponse((totalCallingNumber - 1) / pageSize + 1, totalCallingNumber, callings);
    }

    /**
     * 소명 요청에 대해 소명 메시지를 전송하는 메서드입니다.
     * @param callingId 소명 id
     * @param member 현재 로그인한 사용자
     * @param content 소명 내용
     */
    public void sendCallingContent(String callingId, Member member, String content) throws BaseException {

        Calling findCalling = callingRepository.findById(callingId).orElseThrow(
                () -> new BaseException(INVALID_CALLING_ID_EXCEPTION));

        PromptDetection findDetection = promptDetectionRepository.findById(findCalling.getPromptDetectionId()).orElseThrow(
                () -> new BaseException(INVALID_DETECTION_ID_EXCEPTION));

        if (!findDetection.getUserId().equals(member.getId())) {

            throw new BaseException(INVALID_USER_ACCESS_EXCEPTION);
        }

        findCalling.sendCalling(content);
        callingRepository.save(findCalling);

        // TODO: 보안 담당자에게 알람 전송
        /*sendToClient(
                memberRepository.findById(findCalling.getSenderId()).orElseThrow(
                        () -> new BaseException(INVALID_MEMBER_ID_EXCEPTION)),
                CALLING_SENT,
                CallingSendAlarmResponse.of(
                        findCalling,
                        promptRepository.findById(findDetection.getPromptId()).orElseThrow(
                                () -> new BaseException(INVALID_PROMPT_ID_EXCEPTION)),
                        findDetection.getIndex(),
                        member
                )
        );*/
    }

    /**
     * 자신에게 전송된 단일 소명 요청을 조회하는 메서드입니다.
     * @param callingId 소명 요청 id
     * @param member 현재 로그인한 사용자
     * @return 소명 요청 정보
     */
    public SingleCallingRequestResponse getSingleCalling(String callingId, Member member) throws BaseException {

        Calling findCalling = callingRepository.findById(callingId).orElseThrow(
                () -> new BaseException(INVALID_CALLING_ID_EXCEPTION));

        PromptDetection findDetection = promptDetectionRepository.findById(findCalling.getPromptDetectionId()).orElseThrow(
                () -> new BaseException(INVALID_DETECTION_ID_EXCEPTION));

        if (!findDetection.getUserId().equals(member.getId())) {
            throw new BaseException(INVALID_USER_ACCESS_EXCEPTION);
        }

        return SingleCallingRequestResponse.of(
                findCalling,
                findDetection,
                promptRepository.findById(findDetection.getPromptId()).orElseThrow(
                        () -> new BaseException(INVALID_PROMPT_ID_EXCEPTION))
                );
    }

    /**
     * 클라이언트가 서버에 SSE 방식을 사용하기 위해 구독하는 메서드입니다.
     * TODO: nginx 이슈 해결 후 재배포
     * @param member 현재 로그인한 사용자
     * @param now 현재 시간
     * @return 생성된 SSE emitter
     * @throws BaseException SSE를 return하는 과정에서 예외가 발생한 경우
     */
    public SseEmitter subscribe(Member member, LocalDateTime now) throws BaseException {

        log.info("timeout value: {}", sseTimeout);

        SseEmitter sse = new SseEmitter(Long.parseLong(sseTimeout));
        String key = new SseRepositoryKeyRule(member.getId(), CALLING_REQUEST, now)
                .toCompleteKeyWhichSpecifyOnlyOneValue();

        sse.onCompletion(() -> {
            log.info("onCompletion callback");
            sseRepository.remove(key);
        });
        sse.onTimeout(() -> {
            log.info("onTimeout callback");
            sse.complete();
        });

        sseRepository.put(key, sse);
        try {
            sse.send(SseEmitter.event()
                    .name(CONNECTED)
                    .id(getEventId(member, now, CALLING_REQUEST))
                    .data("subscribe"));
        } catch (IOException e) {
            sseRepository.remove(key);
            log.error("SSE subscribe exception occurred: {}", e.getMessage());
            e.printStackTrace();
            throw new BaseException(SSE_SEND_EXCEPTION);
        }

        // 직원일 경우 소명 요청에 대한 알림 전송
        if (member.getRoles().contains("USER")) {
            List<MultipleCallingRequestResponse> result = new ArrayList<>();
            List<Calling> findCallings = callingRepository.findByReceiverIdAndStatus(member.getId(), PENDING);

            for (Calling calling : findCallings) {
                Optional<PromptDetection> findDetection = promptDetectionRepository.findById(calling.getPromptDetectionId());
                if (findDetection.isPresent()) {
                    Optional<Prompt> findPrompt = promptRepository.findById(findDetection.get().getPromptId());
                    if (findPrompt.isPresent()) {
                        Optional<Member> findSender = memberRepository.findById(calling.getSenderId());
                        findSender.ifPresent(value -> result.add(MultipleCallingRequestResponse.of(
                                calling,
                                findDetection.get(),
                                findPrompt.get(),
                                value
                        )));
                    }
                }
            }
            sendToClient(sse, member, CALLING_REQUEST, result);
        }

        return sse;
    }

    /**
     * 클라이언트에게 알림을 전송하는 메서드입니다.
     * @param receiver 알림을 전송 받으려는 사용자
     * @param data 전송하려는 데이터
     */
    private void sendToClient(Member receiver, SseEventName eventName, Object data) throws BaseException {

        List<SseEmitter> findEmitters = sseRepository.getListByKeyPrefix(receiver.getId());

        if (findEmitters.isEmpty()) {
            log.error("sse emitter가 없습니다");
            return;
        }

        for (SseEmitter sse : findEmitters) {
            try {
                sse.send(SseEmitter.event()
                        .id(getEventId(receiver, LocalDateTime.now(), eventName))
                        .name(eventName.getValue())
                        .data(data, APPLICATION_JSON));
            } catch (IOException e) {
                sseRepository.remove(receiver.getId());
                log.error("SSE send exception occurred: {}", e.getMessage());
                e.printStackTrace();
                throw new BaseException(SSE_SEND_EXCEPTION);
            }
        }
    }

    private void sendToClient(SseEmitter sse, Member receiver, SseEventName eventName, Object data) throws BaseException {

        try {
            sse.send(SseEmitter.event()
                    .id(getEventId(receiver, LocalDateTime.now(), eventName))
                    .name(eventName.getValue())
                    .data(data, APPLICATION_JSON));
        } catch (IOException e) {
            sseRepository.remove(receiver.getId());
            log.error("SSE send exception occurred: {}", e.getMessage());
            e.printStackTrace();
            throw new BaseException(SSE_SEND_EXCEPTION);
        }
    }

    /**
     * 특정 유저의 특정 sse 이벤트에 대한 id를 생성하는 메서드입니다.
     * 위 조건으로 여러개 정의 될 경우 now 로 구분합니다.
     * @param member 현재 로그인한 사용자
     * @param now 현재 시각
     * @param eventName SSE 이벤트의 종류
     * @return sse 이벤트 id
     */
    private String getEventId(Member member, LocalDateTime now, SseEventName eventName) {
        return member.getId() + UNDER_SCORE + eventName.getValue() + UNDER_SCORE + now;
    }
}
