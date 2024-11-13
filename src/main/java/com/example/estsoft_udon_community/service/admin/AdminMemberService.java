package com.example.estsoft_udon_community.service.admin;

import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminMemberService {
    private final UsersRepository repository;

    // 선택한 회원의 정보
    public Users getUserById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    // 회원 이름으로 검색 (동명이인 가능), 이름에 포함된 글자도 출력가능
    public Page<Users> getUsersSearchName(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByNameContaining(search, pageable);
    }

    // 검색 검색 및 select 구현
    public Page<Users> getFilteredUsers(int page, int size, String sortOption, String keyword) {
        Pageable pageable = PageRequest.of(page, size, determineSortOrder(sortOption));

        if (keyword != null && !keyword.isEmpty()) {
            return repository.findByNameContaining(keyword, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

    private Sort determineSortOrder(String sortOption) {
        return switch (sortOption) {
            case "recent" -> Sort.by("createdAt").descending();
            case "grade" -> Sort.by("grade").ascending();
            case "promotionRequested" -> Sort.by("isPromotionRequested").descending();
            default -> Sort.unsorted();
        };
    }
}
