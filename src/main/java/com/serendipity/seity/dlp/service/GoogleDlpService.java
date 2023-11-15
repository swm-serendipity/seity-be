package com.serendipity.seity.dlp.service;

import com.serendipity.seity.dlp.CustomInfoType;
import com.serendipity.seity.dlp.InfoType;
import com.serendipity.seity.dlp.NameInfoType;
import com.serendipity.seity.dlp.dto.DeIdentificationResult;
import com.serendipity.seity.dlp.dto.DlpResponse;
import com.serendipity.seity.dlp.dto.api.CharactersToIgnore;
import com.serendipity.seity.dlp.dto.api.Item;
import com.serendipity.seity.dlp.dto.api.request.*;
import com.serendipity.seity.dlp.dto.api.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * DLP 관련 서비스 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Service
@Slf4j
public class GoogleDlpService {

    private static final String EXTERNAL_API_URL =
            "https://dlp.googleapis.com/v2/projects/seity/content:deidentify?key=";

    private final WebClient webClient;

    private static final ExternalApiRequest STATIC_REQUEST;
    private static final ExternalApiRequest STATIC_NAME_REQUEST;

    static {
        // Create the constant parts of the request body
        STATIC_REQUEST = new ExternalApiRequest();

        // Set deidentifyConfig and inspectConfig with constant values
        STATIC_REQUEST.setDeidentifyConfig(new DeidentifyConfig());
        STATIC_REQUEST.setInspectConfig(new InspectConfig());

        // Set deidentifyConfig transformations with constant values
        List<Transformation> transformations = new ArrayList<>();
        Transformation transformation = new Transformation();

        List<com.serendipity.seity.dlp.dto.api.request.InfoType> infoTypes = InfoType.getAllValues();
        infoTypes.addAll(CustomInfoType.getAllValuesToInfoType());

        transformation.setInfoTypes(infoTypes);
        transformation.setPrimitiveTransformation(new PrimitiveTransformation());
        transformation.getPrimitiveTransformation().setCharacterMaskConfig(new CharacterMaskConfig('#',
                false, Collections.singletonList(new CharactersToIgnore(""))));
        transformations.add(transformation);
        STATIC_REQUEST.getDeidentifyConfig().setInfoTypeTransformations(new InfoTypeTransformations(transformations));
        STATIC_REQUEST.getInspectConfig().setInfoTypes(InfoType.getAllValues());
        STATIC_REQUEST.getInspectConfig().setMinLikelihood("LIKELY");   // 비식별화 레벨
        STATIC_REQUEST.getInspectConfig().setCustomInfoTypes(CustomInfoType.getAllValues());    // 커스텀 infotype
    }

    static {
        // Create the constant parts of the request body
        STATIC_NAME_REQUEST = new ExternalApiRequest();

        // Set deidentifyConfig and inspectConfig with constant values
        STATIC_NAME_REQUEST.setDeidentifyConfig(new DeidentifyConfig());
        STATIC_NAME_REQUEST.setInspectConfig(new InspectConfig());

        // Set deidentifyConfig transformations with constant values
        List<Transformation> transformations = new ArrayList<>();
        Transformation transformation = new Transformation();

        List<com.serendipity.seity.dlp.dto.api.request.InfoType> infoTypes = NameInfoType.getAllValues();
        //infoTypes.addAll(CustomInfoType.getAllValuesToInfoType());

        transformation.setInfoTypes(infoTypes);
        transformation.setPrimitiveTransformation(new PrimitiveTransformation());
        transformation.getPrimitiveTransformation().setCharacterMaskConfig(new CharacterMaskConfig('#',
                false, Collections.singletonList(new CharactersToIgnore(""))));
        transformations.add(transformation);
        STATIC_NAME_REQUEST.getDeidentifyConfig().setInfoTypeTransformations(new InfoTypeTransformations(transformations));
        STATIC_NAME_REQUEST.getInspectConfig().setInfoTypes(NameInfoType.getAllValues());
        STATIC_NAME_REQUEST.getInspectConfig().setMinLikelihood("LIKELY");   // 비식별화 레벨
        STATIC_REQUEST.getInspectConfig().setCustomInfoTypes(new ArrayList<>());    // 커스텀 infotype
    }

    public GoogleDlpService(@Value("${google.dlp.key}") String key, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(EXTERNAL_API_URL + key).build();
    }

    /**
     * DLP API를 호출하는 메서드입니다.
     * @param question 질문
     * @return 원본 질문, 변환된 질문, 변환된 부분
     */
    public DlpResponse callDlpApi(String question) {

        STATIC_REQUEST.setItem(new Item(question));     // 질문 세팅

        Mono<ApiResponse> apiResponseMono = webClient.post()
                .bodyValue(STATIC_REQUEST)
                .retrieve()
                .bodyToMono(ApiResponse.class);

        ApiResponse response = apiResponseMono.block();

        return extractConvertedPart(question, response.getItem().getValue());
    }

    /**
     * DLP API를 호출하는 메서드입니다. (이름만 적용)
     * @param question 질문
     * @return 원본 질문, 변환된 질문, 변환된 부분
     */
    public DlpResponse callDlpApiForName(String question) {

        STATIC_NAME_REQUEST.setItem(new Item(question));     // 질문 세팅

        Mono<ApiResponse> apiResponseMono = webClient.post()
                .bodyValue(STATIC_NAME_REQUEST)
                .retrieve()
                .bodyToMono(ApiResponse.class);

        ApiResponse response = apiResponseMono.block();

        return extractConvertedPart(question, response.getItem().getValue());
    }

    /**
     * 변환된 질문에서 변환된 부분이 어느 부분인지를 추출하는 메서드입니다.
     * @param originalQuestion 원본 질문
     * @param convertedQuestion 변환된 질문
     * @return
     */
    private DlpResponse extractConvertedPart(String originalQuestion, String convertedQuestion) {

        DlpResponse response = new DlpResponse(originalQuestion, convertedQuestion);

        int length = originalQuestion.length();
        int startIndex = -1; // 마스킹된 부분 시작 index
        int maskedLength = 0; // 마스킹된 길이

        for (int i = 0; i < length; i++) {
            char charA = originalQuestion.charAt(i);
            char charB = convertedQuestion.charAt(i);

            if (charA != charB) {
                if (startIndex == -1) {
                    // 이전까지 일치하던 부분이 종료되고 새로운 마스킹된 부분이 시작됨
                    startIndex = i;
                }
                maskedLength++;
            } else {
                if (startIndex != -1) {
                    // 마스킹된 부분이 끝남
                    response.getResult().add(new DeIdentificationResult(startIndex, maskedLength));
                    startIndex = -1;
                    maskedLength = 0;
                }
            }
        }

        // 마스킹된 부분이 끝나지 않은 경우 처리
        if (startIndex != -1) {
            response.getResult().add(new DeIdentificationResult(startIndex, maskedLength));
        }

        return response;
    }
}
