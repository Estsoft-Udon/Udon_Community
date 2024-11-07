package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.dto.request.UsersRequest;
import com.example.estsoft_udon_community.enums.PasswordHint;
import com.example.estsoft_udon_community.repository.LocationRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final LocationRepository locationRepository;
    private final BCryptPasswordEncoder passwordEncoder; // BCryptPasswordEncoder 주입

    // 회원가입
    public Users registerUser(UsersRequest request) {
        // 위치 정보 가져오기
        Location location = locationRepository.findById(request.getLocationId()).orElseThrow();

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Users 객체로 변환 및 비밀번호 설정
        Users user = request.convert(location);
        user.setPassword(encodedPassword);

        // 사용자 저장
        return usersRepository.save(user);
    }

    // 전체조회
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    // 로그인
    public Users loginUser(String loginId, String password) {
        Users users = usersRepository.findByLoginId(loginId);
        String encodedPassword = users.getPassword();

        // 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isMatch = passwordEncoder.matches(password, encodedPassword);

        if (!isMatch) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        // 로그인 시간 추가
        users.updateLastLoginAt();
        usersRepository.save(users);

        return users;
    }

    // 유저 정보 조회
    public Users findUserById(Long id) {
        Users users = usersRepository.findById(id).orElseThrow();

        // location 정보 가져오기
        Location location = users.getLocation();
        users.setLocation(location);

        return users;
    }

    // 유저 정보 수정
    public Users updateUser(Long userId, UsersRequest request) {
        // 예외 처리 바람
        Users user = usersRepository.findById(userId).orElseThrow();

        if (request.getLocationId() != null) {
            Location location = locationRepository.findById(request.getLocationId()).orElseThrow();
            user.setLocation(location);
        }

        return usersRepository.save(request.updateEntity(user));
    }

    // Delete
    public void deleteUser(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow();
        usersRepository.delete(user);
    }

    // 아이디 찾기
    public Users searchId(String name, String email) {
        return usersRepository.findByNameAndEmail(name, email);
    }

    // 비밀번호 찾기
    public Users searchPassword(String loginId, PasswordHint passwordHint, String passwordAnswer) {
        return usersRepository.findByLoginIdAndPasswordHintAndPasswordAnswer(loginId, passwordHint, passwordAnswer);
    }

    // LoginId로 User 찾기
    public Users findByLoginId(String loginId) {
        Users users = usersRepository.findByLoginId(loginId);

        if(users == null) {
            System.out.println("findByLoginId: users is null");
            return null;
        }

        Location location = users.getLocation(); // location 정보 가져오기
        users.setLocation(location);

        return users;
    }

//    // 로그인 유저 지역정보 가져오기
//    public Location getUserLocationByLoginId(String loginId) {
//        Users user = usersRepository.findByLoginId(loginId);
//        if (user == null) {
//            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
//        }
//        return user.getLocation();
//    }
}