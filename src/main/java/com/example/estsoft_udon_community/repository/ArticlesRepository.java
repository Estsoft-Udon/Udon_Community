package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Long> {
    List<Articles> findByLocationId(Long locationId);

    Collection<Articles> findByIsDeletedFalse();

    Optional<Articles> findByIdAndIsDeletedFalse(Long id);

    Long countByHashtagsContains(Hashtag hashtag);

    // articles - articles_like - like~
}