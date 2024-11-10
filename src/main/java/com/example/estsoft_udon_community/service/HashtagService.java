package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Hashtag;
import com.example.estsoft_udon_community.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    // 해시태그 이름 가져오기
    public String getHashtagName(Long hashtagId) {
        Optional<Hashtag> hashtag = hashtagRepository.findById(hashtagId);
        return hashtag.map(Hashtag::getName)
                .orElse("해시태그");
    }

    // 상위 5개의 해시태그만 가져오기
    public List<PopularHashtag> getTopUsedHashtags() {

        // PageRequest를 통해 상위 5개의 해시태그만 가져오도록 설정
        PageRequest pageRequest = PageRequest.of(0, 5);
        return hashtagRepository.findTopUsedHashtags(pageRequest).stream()
                .map(result -> new PopularHashtag((Hashtag) result[0], (Long) result[1]))
                .toList();
    }

    public static class PopularHashtag {
        private final Hashtag hashtag;
        private final Long usageCount;

        public PopularHashtag(Hashtag hashtag, Long usageCount) {
            this.hashtag = hashtag;
            this.usageCount = usageCount;
        }

        public Hashtag getHashtag() {
            return hashtag;
        }

        public Long getUsageCount() {
            return usageCount;
        }
    }
}
