package com.example.estsoft_udon_community.service.admin;

import com.example.estsoft_udon_community.dto.request.UpdateArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.repository.AdminArticleRepository;
import com.example.estsoft_udon_community.repository.ArticlesRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import com.example.estsoft_udon_community.service.ArticlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminArticleService {
    private final ArticlesRepository articlesRepository;
    private final AdminArticleRepository repository;

    // 관리자 게시글 조회
    public Page<ArticleResponse> getAdminArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Articles> articles = articlesRepository.findAll(pageable);
        return articles.map(ArticleResponse::new);
    }

    // 관리자 특정 게시글 조회
    public ArticleResponse getArticleById(Long id) {
        Articles article = articlesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        return new ArticleResponse(article);
    }

    // 제목에 키워드가 포함되고, 삭제되지 않은 게시글을 찾는 메서드
    public Page<ArticleResponse> findByTitleContainingAndIsDeleted(String keyword, Pageable pageable) {
        Page<Articles> articles = repository.findByTitleContainingAndIsDeletedFalse(keyword, pageable);
        return articles.map(article -> new ArticleResponse(article)); // ArticleResponse는 Article 엔티티를 DTO로 변환하는 클래스
    }

    // 삭제되지 않은 게시글을 찾는 메서드
    public Page<ArticleResponse> findByIsDeleted(Pageable pageable) {
        Page<Articles> articles = repository.findByIsDeletedFalse(pageable);
        return articles.map(article -> new ArticleResponse(article));
    }

    // 전체 게시글을 찾는 메서드 (삭제되지 않은 게시글만)
    public Page<ArticleResponse> getAdminArticles(Pageable pageable) {
        return findByIsDeleted(pageable); // is_deleted = false인 게시글만 가져오기
    }

    // 공개 비공개 설정
    @Transactional
    public Articles toggleArticleVisibility(Long articleId, String visibility) {
        // 게시물을 찾음
        Articles article = articlesRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // "private"이면 비공개 상태(true), 아니면 공개 상태(false)로 설정
        boolean isBlind = "private".equals(visibility);  // "private"이면 비공개 상태로 처리

        // isBlind가 true이면 비공개, false이면 공개 상태로 설정
        article.setBlind(isBlind);  // boolean 값으로 설정

        // 상태 변경 후 저장
        return articlesRepository.save(article);  // 변경된 엔티티를 저장
    }
}