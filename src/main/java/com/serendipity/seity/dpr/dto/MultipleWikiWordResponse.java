package com.serendipity.seity.dpr.dto;

import com.serendipity.seity.dpr.WikiWord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * wiki word에 대해 표현하는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MultipleWikiWordResponse {

    private String word;
    private String passage;
    private String link;

    public static MultipleWikiWordResponse of(WikiWord word) {

        return new MultipleWikiWordResponse(word.getWord(), word.getPassage(), word.getLink());
    }
}
