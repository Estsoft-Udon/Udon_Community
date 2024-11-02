package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.ArticleHashtagJoin;
import com.example.estsoft_udon_community.entity.Articles;
import com.example.estsoft_udon_community.entity.Hashtag;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.dto.request.AddArticleRequest;
import com.example.estsoft_udon_community.dto.response.ArticleResponse;
import com.example.estsoft_udon_community.dto.request.UpdateArticleRequest;
import com.example.estsoft_udon_community.repository.*;
import lombok.RequiredArgsConstructor;
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

    // 게시글 등록
    public Articles saveArticle(AddArticleRequest request) {
        Long userId = request.getUserId();
        Users user = usersRepository.findById(userId).orElseThrow();

        List<Hashtag> hashtagList = getOrCreateHashtags(request.getHashtagName());

        Articles articles = new Articles(user, request.getTitle(), request.getContent(), request.getCategory(),
                hashtagList, user.getLocation());
        Articles savedArticle = articlesRepository.save(articles);

        for (Hashtag hashtag : hashtagList) {
            articleHashtagJoinRepository.save(new ArticleHashtagJoin(savedArticle, hashtag));
        }
        return savedArticle;
    }

    // 전체 게시글 조회
    public List<ArticleResponse> findAll() {
        return articlesRepository.findByIsDeletedFalse().stream()
                .map(ArticleResponse::new)
                .toList();
    }

    // 특정 게시글 조회
    public Optional<ArticleResponse> findByArticleId(Long id) {
        return articlesRepository.findByIdAndIsDeletedFalse(id)
                .map(ArticleResponse::new);
    }

    // 특정 게시글 수정
    @Transactional
    public Articles updateArticle(Long id, UpdateArticleRequest request) {
        Articles article = articlesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없음"));

        List<Hashtag> newHashtags = getOrCreateHashtags(request.getHashtagName());

        updateArticleDetails(article, request.getTitle(), request.getContent(), newHashtags);
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

    // 새로운 해시태그를 생성하거나 기존 해시태그를 가져오는 메서드
    private List<Hashtag> getOrCreateHashtags(List<String> hashtagNames) {
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


//    public List<ArticleResponse> findByLocationId(Long locationId) {
//        return articlesRepository.findByLocationId(locationId).stream()
//                .map(ArticleResponse::new)
//                .toList();
//    }
//
//    public List<ArticleResponse> findAllByLikes() {
//        // article에는 직접적으로 Like 관련이 없다.
//        return articlesRepository.findByLikesDesc().stream()
//                .map(ArticleResponse::new)
//                .toList();
//    }
}