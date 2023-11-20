package com.serendipity.seity.dpr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * wiki 단어를 생성할 때 사용되는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateWikiWordRequest {

    private String word;
    private String passage;
    private String link;
}
