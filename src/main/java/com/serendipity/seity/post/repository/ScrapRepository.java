package com.serendipity.seity.post.repository;

import com.serendipity.seity.post.Scrap;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Scrap 관련 repository 클래스입니다.
 *
 * @author Min Ho CHO
 */
public interface ScrapRepository extends MongoRepository<Scrap, String> {

    Optional<Scrap> findByUserId(String userId);
}
