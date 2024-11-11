package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.PasswordHint;
import java.util.List;

import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByNameAndEmail(String name, String email);

    Users findByLoginIdAndPasswordHintAndPasswordAnswer(String loginId, PasswordHint passwordHint,
                                                        String passwordAnswer);

    Users findByLoginId(String loginId);

    boolean existsByLoginIdIgnoreCase(String loginId);

    // 이름에 포함된 문자열로 검색
    Page<Users> findByNameContaining(String search, Pageable pageable);

    boolean existsByNicknameIgnoreCase(String nickname);

    boolean existsByEmailIgnoreCase(String email);
}
