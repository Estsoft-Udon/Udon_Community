package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.entity.request.UsersRequest;
import com.example.estsoft_udon_community.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    // 회원가입
    public void registerUser(UsersRequest request) {
        usersRepository.save(request.convert());
    }

    // 로그인 ->
    public Users loginUser(String loginId, String password) {
        Users users = usersRepository.findByLoginId(loginId);
        if (!password.equals(users.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }
        return users;
    }

    // 유저 정보 조회
    public Users findUserById(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    // 유저 정보 수정
    public Users updateUser(Long userId, UsersRequest request) {
        Users user = usersRepository.findById(userId).orElse(null);

        return usersRepository.save(request.updateEntity(user));
    }

    // Delete
    public void deleteUser(Long userId) {
        Users user = findUserById(userId);
        usersRepository.delete(user);
    }

    // 아이디 찾기
    public Users searchId(String name, String email) {
        return usersRepository.findByNameAndEmail(name, email);
    }

    // 비밀번호 찾기
    public Users searchPassword(String loginId, String passwordHint, String passwordAnswer) {
        return usersRepository.findByLoginIdAndPasswordHintAndPasswordAnswer(loginId, passwordHint, passwordAnswer);

    }

}
