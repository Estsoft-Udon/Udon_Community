package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Long> {
    List<Articles> findByLocationId(Long locationId);


    // articles - articles_like - like~
}
