package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.dto.LocationDTO;
import com.example.estsoft_udon_community.dto.response.UsersResponse;
import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.dto.request.UsersRequest;
import com.example.estsoft_udon_community.enums.PasswordHint;
import com.example.estsoft_udon_community.repository.LocationRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final LocationRepository locationRepository;
    private final BCryptPasswordEncoder passwordEncoder; // BCryptPasswordEncoder 주입


//    // 회원가입
//    public Users registerUser(UsersRequest request) {
//        Location location = locationRepository.findById(request.getLocationId()).orElseThrow();
//
//        return usersRepository.save(request.convert(location));
//    }
    // 회원가입
    public Users registerUser(UsersRequest request) {
        // 위치 정보 가져오기
        Location location = locationRepository.findById(request.getLocationId()).orElseThrow();

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword()); // 요청에서 비밀번호 암호화
        // System.out.println("encodedPassword:" + encodedPassword);
        // Users 객체로 변환 및 비밀번호 설정
        Users user = request.convert(location);
        user.setPassword(encodedPassword); // 암호화된 비밀번호 설정

        // 사용자 저장
        return usersRepository.save(user);
    }


    // 전체조회
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    // 로그인 ->
    public Users loginUser(String loginId, String password) {
        Users users = usersRepository.findByLoginId(loginId);
        String encodedPassword = users.getPassword();

        // 암호문과 평문 비교
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isMatch = passwordEncoder.matches(password, encodedPassword);

        if(!isMatch) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다.");
        }

        // 로그인 시간 추가
        users.updateLastLoginAt();
        usersRepository.save(users);

        return users;
    }

//    // 로그인 ->
//    public Users loginUser(String loginId, String password) {
//        Users users = usersRepository.findByLoginId(loginId);
//
//        // 로그인 시간 추가
//        users.updateLastLoginAt();
//        usersRepository.save(users);
//
//        if (!password.equals(users.getPassword())) {
//            throw new IllegalArgumentException("Wrong password");
//        }
//        return users;
//    }

    // 유저 정보 조회
    public Users findUserById(Long id) {
        Users users = usersRepository.findById(id).orElseThrow();

        Location location = users.getLocation(); // location 정보 가져오기
        users.setLocation(location);

        return users;
    }

    // 유저 정보 수정
    public Users updateUser(Long userId, UsersRequest request) {
        Users user = usersRepository.findById(userId).orElse(null);

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

}
