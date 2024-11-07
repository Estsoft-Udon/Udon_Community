package com.example.estsoft_udon_community.service.admin;

import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminMemberService {
    private final UsersRepository repository;

    // 회원 전체 정보
    public Page<Users> getAllUser(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    // 선택한 회원의 정보
    public Users getUserById(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
