package com.serendipity.seity.post;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 스크랩한 게시글을 나타내는 엔티티입니다.
 *
 * @author Min Ho CHO
 */
@Document("Scraps")
@Getter
@NoArgsConstructor
public class Scrap {

    @Id
    private String id;
    private String userId;
    private List<ScrapPost> scrapPostList;

    private Scrap(String userId, List<ScrapPost> scrapPostList) {

        this.userId = userId;
        this.scrapPostList = scrapPostList;
    }

    public static Scrap createScrap(String userId, String postId, LocalDateTime createdAt) {

        List<ScrapPost> scrapPostList = new ArrayList<>();
        scrapPostList.add(new ScrapPost(postId, createdAt));

        return new Scrap(userId, scrapPostList);
    }

    public void add(String postId, LocalDateTime createdAt) {

        for (ScrapPost scrapPost : scrapPostList) {
            if (scrapPost.getPostId().equals(postId)) {
                return;
            }
        }

        scrapPostList.add(new ScrapPost(postId, createdAt));
    }

    public void delete(String postId) {

        for (ScrapPost scrapPost : scrapPostList) {

            if (scrapPost.getPostId().equals(postId)) {
                scrapPostList.remove(scrapPost);
                return;
            }
        }
    }
}
