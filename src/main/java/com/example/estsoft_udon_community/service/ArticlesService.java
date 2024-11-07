package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.dto.response.ArticleDetailResponse;
import com.example.estsoft_udon_community.entity.ArticleHashtagJoin;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Hashtag;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.request.UpdateArticleRequest;
import com.example.estsoft_udon_community.enums.ArticleCategory;
import com.example.estsoft_udon_community.repository.*;
import com.example.estsoft_udon_community.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticlesService {
    private final ArticlesRepository articlesRepository;
    private final HashtagRepository hashtagRepository;
    private final ArticleHashtagJoinRepository articleHashtagJoinRepository;
    private final UsersRepository usersRepository;
    private final CommentsRepository commentsRepository;
    private final ArticlesLikeRepository articlesLikeRepository;
    private final LocationService locationService;

    // 게시글 등록 - api
    public Articles saveArticle(AddArticleRequest request) {
        Long userId = SecurityUtil.getLoggedInUser().getId();
        Users user = usersRepository.findById(userId).orElseThrow();

        List<Hashtag> hashtagList = getOrCreateHashtags(request.getHashtagName());
        Location locationById = locationService.getLocationById(request.getLocationId());

        Articles articles = new Articles(user, request.getTitle(), request.getContent(), request.getCategory(),
                hashtagList, locationById);

        Articles savedArticle = articlesRepository.save(articles);

        for (Hashtag hashtag : hashtagList) {
            articleHashtagJoinRepository.save(new ArticleHashtagJoin(savedArticle, hashtag));
        }
        return savedArticle;
    }

    // 게시글 등록 - boardController
    public void saveArticle(AddArticleRequest request, Long locationId) {
        Long userId = SecurityUtil.getLoggedInUser().getId();
        Users user = usersRepository.findById(userId).orElseThrow();
        List<Hashtag> hashtagList = getOrCreateHashtags(request.getHashtagName());

        // 게시글 생성
        Articles articles = new Articles(user, request.getTitle(), request.getContent(), request.getCategory(),
                hashtagList, locationService.getLocationById(locationId));

        // 데이터베이스에 저장
        Articles savedArticle = articlesRepository.save(articles);

        // 해시태그 저장
        for (Hashtag hashtag : hashtagList) {
            articleHashtagJoinRepository.save(new ArticleHashtagJoin(savedArticle, hashtag));
        }
    }

    // 전체 게시글 조회
    public Page<ArticleDetailResponse> findAll(int page, int size, String sortOption) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));

        Page<Articles> articlesPage = switch (sortOption) {
            case "likeCount" -> articlesRepository.findAllOrderByLikeCount(pageable);
            case "commentCount" -> articlesRepository.findAllOrderByCommentCount(pageable);
            default -> articlesRepository.findByIsDeletedFalse(pageable);
        };

        return articlesPage.map(article -> new ArticleDetailResponse(
                article,
                fetchLikeCount(article),
                fetchCommentCount(article)
        ));
    }

    // 특정 게시글 조회
    public Optional<ArticleResponse> findByArticleId(Long id) {
        return articlesRepository.findByIdAndIsDeletedFalse(id)
                .map(articles -> {
                    articles.incrementViewCount();
                    articlesRepository.save(articles);
                    return new ArticleResponse(articles);
                });
    }

    // 특정 게시글 수정
    @Transactional
    public Articles updateArticle(Long id, UpdateArticleRequest request) {
        Articles article = articlesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없음"));

        List<Hashtag> newHashtags = getOrCreateHashtags(request.getHashtagName());

        updateArticleDetails(article, request.getTitle(), request.getContent(), newHashtags);

        article.setUpdatedAt(LocalDateTime.now());
        removeUnusedHashtags();
        return articlesRepository.save(article);
    }

    // 특정 게시글 삭제
    @Transactional
    public void deleteArticle(Long id) {
        Articles article = articlesRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 게시글"));
        article.setDeleted(true);
        article.setDeletedAt(LocalDateTime.now());
    }

    // 특정 지역 게시글 조회
    public Page<ArticleDetailResponse> findByLocationId(Long locationId, int page, int size, String sortOption) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));

        Page<Articles> articlesPage = switch (sortOption) {
            case "likeCount" -> articlesRepository.findByLocationIdOrderByLikeCount(locationId, pageable);
            case "commentCount" -> articlesRepository.findByLocationIdOrderByCommentCount(locationId, pageable);
            default -> articlesRepository.findByLocationIdAndIsDeletedFalse(locationId, pageable);
        };

        return articlesPage.map(article -> new ArticleDetailResponse(
                article,
                fetchLikeCount(article),
                fetchCommentCount(article)
        ));
    }

    // 해시태그로 게시글 조회
    public Page<ArticleDetailResponse> findByHashtag(Long hashtagId, int page, int size, String sortOption) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));

        Page<Articles> articlesPage = switch (sortOption) {
            case "likeCount" -> hashtagRepository.findArticlesByHashtagIdOrderByLikeCount(hashtagId, pageable);
            case "commentCount" -> hashtagRepository.findArticlesByHashtagIdOrderByCommentCount(hashtagId, pageable);
            default -> hashtagRepository.findArticlesByHashtagIdAndIsDeletedFalse(hashtagId, pageable);
        };

        return articlesPage.map(article -> new ArticleDetailResponse(
                article,
                fetchLikeCount(article),
                fetchCommentCount(article)
        ));
    }

    // 카테고리로 게시글 조회
    public Page<ArticleDetailResponse> findByCategory(String category, int page, int size, String sortOption) {
        ArticleCategory articleCategory = ArticleCategory.valueOf(category.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));

        Page<Articles> articlesPage = switch (sortOption) {
            case "likeCount" -> articlesRepository.findByCategoryOrderByLikeCount(articleCategory, pageable);
            case "commentCount" -> articlesRepository.findByCategoryOrderByCommentCount(articleCategory, pageable);
            default -> articlesRepository.findByCategory(articleCategory, pageable);
        };

        return articlesPage.map(article -> new ArticleDetailResponse(
                article,
                fetchLikeCount(article),
                fetchCommentCount(article)
        ));
    }

    // 제목 검색 기능
    public Page<ArticleDetailResponse> searchByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<Articles> articlesPage = articlesRepository.findByTitleContainingIgnoreCase(title, pageable);
        return articlesPage.map(article -> new ArticleDetailResponse(
                article,
                fetchLikeCount(article),
                fetchCommentCount(article)));
    }

    // 새로운 해시태그를 생성하거나 기존 해시태그를 가져오는 메서드
    private List<Hashtag> getOrCreateHashtags(List<String> hashtagNames) {
        // hashtagNames가 null이면 빈 리스트로 초기화
        if (hashtagNames == null) {
            hashtagNames = List.of();
        }

        return hashtagNames.stream()
                .map(hashtagName -> hashtagRepository.findByName(hashtagName)
                        .orElseGet(() -> hashtagRepository.save(new Hashtag(hashtagName))))
                .toList();
    }

    // 게시글 업데이트
    private void updateArticleDetails(Articles article, String title, String content, List<Hashtag> newHashtags) {
        article.setTitle(title);
        article.setContent(content);
        // 새로운 해시태그로 기존 해시태그 필터링
        List<Hashtag> existingHashtags = article.getHashtags();
        existingHashtags.retainAll(newHashtags);
        // 새로운 해시태그 추가
        for (Hashtag newHashtag : newHashtags) {
            if (!existingHashtags.contains(newHashtag)) {
                existingHashtags.add(newHashtag);
            }
        }
    }

    // 사용안하는 해시태그 제거
    private void removeUnusedHashtags() {
        hashtagRepository.deleteUnusedHashtags();
    }

    public Articles findJustArticle(Long id) {
        return articlesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Article not found"));
    }

    // 좋아요 수 조회
    private Long fetchLikeCount(Articles article) {
        return articlesLikeRepository.countLikesByArticles(article);
    }

    // 댓글 수 조회
    private Long fetchCommentCount(Articles article) {
        return commentsRepository.countNonDeletedCommentsByArticle(article);
    }
}