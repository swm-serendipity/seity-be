package com.serendipity.seity.dlp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * DLP에 적용할 개인정보를 나타내는 enum 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
public enum InfoType {

    EMAIL_ADDRESS("EMAIL_ADDRESS"),
    DATE_OF_BIRTH("DATE_OF_BIRTH"),
    CREDIT_CARD_NUMBER("CREDIT_CARD_NUMBER"),
    IMEI_HARDWARE_ID("IMEI_HARDWARE_ID"),
    //PERSON_NAME("PERSON_NAME"),
    PHONE_NUMBER("PHONE_NUMBER"),
    STREET_ADDRESS("STREET_ADDRESS"),
    KOREA_PASSPORT("KOREA_PASSPORT"),
    KOREA_RRN("KOREA_RRN");

    private final String value;

    public static List<com.serendipity.seity.dlp.dto.api.request.InfoType> getAllValues() {

        List<com.serendipity.seity.dlp.dto.api.request.InfoType> result = new ArrayList<>();

        for (InfoType infoType : InfoType.values()) {
            result.add(new com.serendipity.seity.dlp.dto.api.request.InfoType(infoType.value));
        }

        return result;
    }
}
