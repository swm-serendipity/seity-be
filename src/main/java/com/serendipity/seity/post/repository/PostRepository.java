package com.serendipity.seity.post.repository;

import com.serendipity.seity.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Post repository 클래스입니다.
 *
 * @author Min Ho CHO
 */
public interface PostRepository extends MongoRepository<Post, String> {

    Optional<Post> findByPromptId(String id);
    List<Post> findTopNByCreateTimeAfterOrderByLikesDescCreateTimeDesc(LocalDateTime date, int n);
    List<Post> findByOrderByCreateTimeDesc(Pageable pageable);
    List<Post> findByOrderByLikesDesc(Pageable pageable);
    @Query(value = "{ 'createTime' : { $gte: ?0 } }")
    List<Post> findTopNByOrderByLikeNumberDesc(LocalDateTime time, Pageable pageable);
    @Query(value = "{ 'part': ?0, 'createTime' : { $gte: ?1 } }")
    List<Post> findByPartOrderByLikeNumberDesc(String part, LocalDateTime time, Pageable pageable);
}
