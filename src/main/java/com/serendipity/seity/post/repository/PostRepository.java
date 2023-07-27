package com.serendipity.seity.post.repository;

import com.serendipity.seity.post.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Post repository 클래스입니다.
 *
 * @author Min Ho CHO
 */
public interface PostRepository extends MongoRepository<Post, String> {

    Optional<Post> findByPromptId(String id);
}
