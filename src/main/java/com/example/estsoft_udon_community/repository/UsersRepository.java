package com.example.estsoft_udon_community.repository;

import com.example.estsoft_udon_community.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    public Users findByNameAndEmail(String name, String email);

    public Users findByLoginIdAndPasswordHintAndPasswordAnswer(String loginId, String passwordHint, String passwordAnswer);

    Users findByLoginId(String loginId);
}