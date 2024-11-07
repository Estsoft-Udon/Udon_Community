package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.PasswordHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByNameAndEmail(String name, String email);

    Users findByLoginIdAndPasswordHintAndPasswordAnswer(String loginId, PasswordHint passwordHint,
                                                               String passwordAnswer);
    Users findByLoginId(String loginId);

    boolean existsByLoginIdIgnoreCase(String loginId);
}
