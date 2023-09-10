package com.serendipity.seity.dlp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum NameInfoType {

    PERSON_NAME("PERSON_NAME");

    private final String value;

    public static List<com.serendipity.seity.dlp.dto.api.request.InfoType> getAllValues() {

        List<com.serendipity.seity.dlp.dto.api.request.InfoType> result = new ArrayList<>();

        for (NameInfoType infoType : NameInfoType.values()) {
            result.add(new com.serendipity.seity.dlp.dto.api.request.InfoType(infoType.value));
        }

        return result;
    }
}
