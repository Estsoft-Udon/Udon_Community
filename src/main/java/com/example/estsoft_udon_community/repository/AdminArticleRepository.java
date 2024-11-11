package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Articles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminArticleRepository extends JpaRepository<Articles , Long> {
    // 삭제된 게시글 제외 조회
    Page<Articles> findByIsDeletedFalse(Pageable pageable);

    // 제목에 키워드가 포함되고, 삭제되지 않은 게시글을 찾는 메서드
    Page<Articles> findByTitleContainingAndIsDeletedFalse(String title, Pageable pageable);

}
