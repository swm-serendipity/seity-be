package com.serendipity.seity.post;

import com.serendipity.seity.common.BaseTimeEntity;
import com.serendipity.seity.member.Member;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 엔티티 클래스입니다.
 *
 * @author Min Ho CHO
 * TODO: member의 직무 바꿨을 때 모든 post 수정 필요
 * TODO: 지금 member 다 따로 조회하고 있는데, 그냥 member 정보를 post에 넣고 member이 수정될 때마다 post를 바꿀지에 대한 고려 필요
 */
@Document("Posts")
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    private String id;
    private String promptId;
    private int index;  // 몇 번째 질문까지가 공유된 부분인지
    private String part;    // 어느 부서의 사용자가 작성한 게시글인지
    private String title;
    private int views;
    private List<Like> likes;
    private int likeNumber;
    private List<String> mentionMemberList;

    private Post(String promptId, int index, String part, String title, int views, List<String> mentionMemberList) {
        this.promptId = promptId;
        this.index = index;
        this.part = part;
        this.title = title;
        this.views = views;
        this.likes = new ArrayList<>();
        this.likeNumber = 0;
        this.mentionMemberList = mentionMemberList;
    }

    public static Post createPost(String promptId, int index, String part, String title, List<String> mentionMemberList) {

        return new Post(promptId, index, part, title, 0, mentionMemberList);
    }

    public void increaseViews() {
        views++;
    }

    public void like(Member member) {

        likes.add(new Like(member.getId()));
        likeNumber++;
    }

    public void unlike(Member member) {

        for (Like like : likes) {

            if (like.getUserId().equals(member.getId())) {

                likes.remove(like);
                likeNumber--;
                return;
            }
        }
    }
}
