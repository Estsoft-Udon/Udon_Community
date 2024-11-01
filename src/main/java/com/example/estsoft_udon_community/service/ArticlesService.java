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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticlesService {
    private final ArticlesRepository articlesRepository;
    private final ArticlesLikeRepository articlesLikeRepository;
    private final UsersService usersService;
    private final HashtagRepository hashtagRepository;
    private final ArticleHashtagJoinRepository articleHashtagJoinRepository;
    private final UsersRepository usersRepository;

    public Articles saveArticle(AddArticleRequest request) {
        Long userId = request.getUserId();
        Users user = usersRepository.findById(userId).orElseThrow();

        List<Hashtag> hashtagList = new ArrayList<>();
        for(String hashtag : request.getHashtagName()) {
            Hashtag newHashtag = hashtagRepository.findByName(hashtag)
                    .orElseGet(() -> hashtagRepository.save(new Hashtag(hashtag)));

            hashtagList.add(newHashtag);
        }
        Articles articles = new Articles(user, request.getTitle(), request.getContent(), request.getCategory(),
                hashtagList, user.getLocation());

        Articles savedArticle = articlesRepository.save(articles);

        for(Hashtag hashtag : hashtagList) {
            articleHashtagJoinRepository.save(new ArticleHashtagJoin(savedArticle, hashtag));
        }

        return savedArticle;
    }

    public List<ArticleResponse> findAll() {
        return articlesRepository.findAll().stream()
                .map(ArticleResponse::new)
                .toList();
    }

    public Optional<ArticleResponse> findByArticleId(Long id) {
        return articlesRepository.findById(id)
                .map(ArticleResponse::new);
    }

    @Transactional
    public Articles updateArticle(Long id, UpdateArticleRequest request) {
        Articles article = articlesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        article.update(request.getTitle(), request.getContent(), request.getHashtags());
        return articlesRepository.save(article);
    }

    public void deleteArticle(Long id) {
        articlesRepository.deleteById(id);
    }

    public List<ArticleResponse> findByLocationId(Long locationId) {
        return articlesRepository.findByLocationId(locationId).stream()
                .map(ArticleResponse::new)
                .toList();
    }

//    public List<ArticleResponse> findAllByLikes() {
//        // article에는 직접적으로 Like 관련이 없다.
//        return articlesRepository.findByLikesDesc().stream()
//                .map(ArticleResponse::new)
//                .toList();
//    }

}