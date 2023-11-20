package com.serendipity.seity.dpr.service;

import com.serendipity.seity.dpr.DprQuestion;
import com.serendipity.seity.dpr.WikiWord;
import com.serendipity.seity.dpr.dto.CreateWikiWordRequest;
import com.serendipity.seity.dpr.dto.DprDetectionResponse;
import com.serendipity.seity.dpr.dto.DprResponse;
import com.serendipity.seity.dpr.dto.MultipleWikiWordResponse;
import com.serendipity.seity.dpr.repository.DprQuestionRepository;
import com.serendipity.seity.dpr.repository.WikiWordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;


import java.util.*;

/**
 * DPR Test를 위한 service 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DprService {

    private final WikiWordRepository wikiWordRepository;
    private final DprQuestionRepository dprQuestionRepository;
    private final Random random = new Random();

    public DprResponse createTestResponseCase1() {

        List<DprDetectionResponse> detections = new ArrayList<>();
        detections.add(new DprDetectionResponse(
                "탄소 나노튜브 wiki",
                "탄소 나노튜브를 활용한 혁신적인 반도체 기술\n" +
                "\n" +
                "최근의 반도체 산업에서 주목받고 있는 신기술 중 하나는 탄소 나노튜브를 이용한 혁신적인 반도체 제조 기술입니다. 탄소 나노튜브는 매우 작은 크기와 우수한 전기 전도성을 가지고 있어, 기존의 실리콘 소자보다 더 뛰어난 성능을 제공합니다.\n" +
                "\n" +
                "이러한 나노튜브를 사용하면 소자의 크기를 미세화하면서도 전력 소모를 줄일 수 있어, 효율적이고 고성능의 반도체를 만들 수 있게 됩니다. 또한, 나노튜브는 실리콘과는 다른 물리적 특성을 가지고 있어 열 효율성도 향상시키는 데 도움이 됩니다.\n" +
                "\n" +
                "이러한 혁신적인 반도체 기술은 모바일 디바이스부터 데이터 센터까지 다양한 응용 분야에서 성능 향상과 에너지 효율성을 동시에 실현할 수 있어, 산업 전반에 긍정적인 영향을 미치고 있습니다.\n",
                "",
                95));
        detections.add(new DprDetectionResponse(
                "탄소 나노튜브 wiki",
                "플렉서블 전자기기를 위한 탄소 나노튜브 소재\n" +
                "\n" +
                "탄소 나노튜브는 그 특이한 물리적 특성으로 플렉서블 전자기기의 핵심 소재로 주목받고 있습니다. 이 나노튜브는 우수한 기계적 유연성을 제공하여, 전자기기의 디자인과 형태를 더 자유롭게 구현할 수 있게 합니다.\n" +
                "\n" +
                "또한, 탄소 나노튜브는 뛰어난 전기 전도성을 가지고 있어 전자 신호를 빠르게 전달할 수 있습니다. 이는 플렉서블 전자기기에서도 안정적이고 빠른 성능을 제공할 수 있게 합니다.\n" +
                "\n" +
                "이러한 특성으로 탄소 나노튜브는 스마트웨어, 휘어지는 디스플레이, 그리고 바이오의료 응용 등 다양한 분야에서 혁신적인 소재로 활용되고 있습니다.",
                "",
                92
        ));
        detections.add(new DprDetectionResponse(
                "플랙서블 나노튜브 wiki",
                "플렉서블 전자기기를 위한 탄소 나노튜브 소재\n" +
                        "\n" +
                        "탄소 나노튜브는 그 특이한 물리적 특성으로 플렉서블 전자기기의 핵심 소재로 주목받고 있습니다. 이 나노튜브는 우수한 기계적 유연성을 제공하여, 전자기기의 디자인과 형태를 더 자유롭게 구현할 수 있게 합니다.\n" +
                        "\n" +
                        "또한, 탄소 나노튜브는 뛰어난 전기 전도성을 가지고 있어 전자 신호를 빠르게 전달할 수 있습니다. 이는 플렉서블 전자기기에서도 안정적이고 빠른 성능을 제공할 수 있게 합니다.\n" +
                        "\n" +
                        "이러한 특성으로 탄소 나노튜브는 스마트웨어, 휘어지는 디스플레이, 그리고 바이오의료 응용 등 다양한 분야에서 혁신적인 소재로 활용되고 있습니다.",
                "",
                92
        ));
        detections.add(new DprDetectionResponse(
                "나노컨덴서 wiki",
                "에너지 저장을 위한 탄소 나노튜브 나노컨덴서\n" +
                        "\n" +
                        "탄소 나노튜브는 뛰어난 전기 전도성 뿐만 아니라 큰 표면적을 가지고 있어, 나노컨덴서(ultracapacitors)에 활용되어 에너지 저장 분야에서 혁신을 이끌고 있습니다. 나노컨덴서는 전통적인 전지보다 빠른 충전 및 방전 속도와 더 높은 에너지 밀도를 제공하며, 이는 고속 충전이 필요한 응용 분야에서 특히 중요합니다.\n" +
                        "\n" +
                        "탄소 나노튜브를 사용한 나노컨덴서는 긴 수명과 안정적인 성능을 제공하여, 전기 자동차나 신재생 에너지 저장 시스템과 같은 분야에서 효과적으로 활용되고 있습니다. 이를 통해 에너지 효율성과 지속 가능성 측면에서 새로운 발전을 이루고 있습니다.",
                "",
                85
        ));


        return new DprResponse("어떻게 탄소 나노튜브를 사용한 반도체 기술이 기존 실리콘 소자보다 성능 향상과 전력 소모 감소를 달성할 수 있는지에 대해 자세히 설명할 수 있을까요?", detections);
    }

    /**
     * wiki word를 생성하는 메서드입니다.
     * @param request wiki word 정보
     */
    public void createTestResponseCase(CreateWikiWordRequest request) {

        if (!wikiWordRepository.existsByWord(request.getWord())) {

            wikiWordRepository.save(WikiWord.createWikiWord(request.getWord(), request.getPassage(), request.getLink()));
        }

    }

    /**
     * dpr 메서드입니다.
     * @param question 질문
     * @return dpr 결과
     */
    public DprResponse dpr(String question) {

        // 1. 기존에 했던 질문과 같은 질문일 경우 같은 결과를 반환
        List<DprQuestion> allDprQuestions = dprQuestionRepository.findAll();
        DprQuestion resultQuestion = null;
        int currentThreshold = -1;
        for (DprQuestion dprQuestion : allDprQuestions) {

            int levenshteinDistance = StringUtils.getLevenshteinDistance(dprQuestion.getQuestion(), question);
            if (levenshteinDistance <= 2) {     // 값이 작을수록 유사

                if (resultQuestion == null || levenshteinDistance < currentThreshold) {

                    resultQuestion = dprQuestion;
                }
            }
        }

        if (resultQuestion != null) {

            return new DprResponse(question, resultQuestion.getAnswers());
        }

        // 2. 처음 하는 질문일 경우
        List<WikiWord> allWikiWords = wikiWordRepository.findAll();
        List<DprDetectionResponse> detections = new ArrayList<>();

        for (WikiWord word : allWikiWords) {

            if (question.contains(word.getWord())) {

                // 검출
                detections.add(new DprDetectionResponse(
                        word.getWord() + " wiki",
                        word.getPassage(),
                        word.getLink(),
                        random.nextInt(26) + 70));
            }
        }

        if (!detections.isEmpty()) {
            detections.sort(Comparator.comparingInt(DprDetectionResponse::getSimilarity).reversed());
        }

        // 질문 리스트에 저장
        dprQuestionRepository.save(DprQuestion.createDprQuestion(question, detections));

        return new DprResponse(question, detections);
    }

    /**
     * 모든 wiki word를 조회하는 메서드입니다.
     * @return wiki word
     */
    public List<MultipleWikiWordResponse> getWikiWords() {

        List<WikiWord> wikiWords = wikiWordRepository.findAll();

        List<MultipleWikiWordResponse> result = new ArrayList<>();
        for (WikiWord word : wikiWords) {

            result.add(MultipleWikiWordResponse.of(word));
        }

        return result;
    }
}
