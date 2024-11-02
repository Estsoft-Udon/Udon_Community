package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM Hashtag h WHERE h.id NOT IN (SELECT ah.hashtag.id FROM ArticleHashtagJoin ah)")
    void deleteUnusedHashtags();
}
