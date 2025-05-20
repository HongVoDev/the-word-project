package com.journal.backendJournal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BibleKeywordVerseRepository extends JpaRepository<BibleKeywordVerseEntity, Long> {

	List findByKeyword(String keyword);
	
	@Query(value = "SELECT * FROM bible_keyword_verses WHERE keyword = :keyword ORDER BY RAND() LIMIT 3", nativeQuery = true)
	List<BibleKeywordVerseEntity> findThreeByKeyword(@Param("keyword") String keyword);
}
