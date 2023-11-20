package com.serendipity.seity.dpr;

import com.serendipity.seity.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * (테스트용) 사내 wiki의 단어를 표현하는 엔티티 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Document("WikiWords")
@Getter
@NoArgsConstructor
public class WikiWord extends BaseTimeEntity {

    @Id
    private String id;
    private String word;
    private String passage;
    private String link;

    private WikiWord(String word, String passage, String link) {
        this.word = word;
        this.passage = passage;
        this.link = link;
    }

    public static WikiWord createWikiWord(String word, String passage, String link) {

        return new WikiWord(word, passage, link);
    }
}
