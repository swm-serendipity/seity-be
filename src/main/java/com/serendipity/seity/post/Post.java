package com.serendipity.seity.post;

import com.serendipity.seity.common.BaseTimeEntity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 게시글 엔티티 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Document("Posts")
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    private String id;
    private String promptId;
    private int index;  // 몇 번째 질문까지가 공유된 부분인지
    private int views;

    private Post(String promptId, int index, int views) {
        this.promptId = promptId;
        this.index = index;
        this.views = views;
    }

    public static Post createPost(String promptId, int index) {

        return new Post(promptId, index, 0);
    }

    public void increaseViews() {
        views++;
    }
}
