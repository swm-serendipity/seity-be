package com.serendipity.seity.forbiddenword.repository;

import com.serendipity.seity.forbiddenword.ForbiddenWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 금칙어 repository 클레스입니다.
 *
 * @author Min Ho CHO
 */
public interface ForbiddenWordRepository extends JpaRepository<ForbiddenWord, String> {

    Optional<ForbiddenWord> findByValue(String value);
}
