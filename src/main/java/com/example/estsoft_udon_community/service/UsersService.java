package com.example.estsoft_udon_community.service;

import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.dto.request.UsersRequest;
import com.example.estsoft_udon_community.enums.PasswordHint;
import com.example.estsoft_udon_community.repository.ArticlesLikeRepository;
import com.example.estsoft_udon_community.repository.CommentsLikeRepository;
import com.example.estsoft_udon_community.repository.LocationRepository;
import com.example.estsoft_udon_community.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final LocationRepository locationRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CommentsLikeService commentsLikeService;
    private final ArticlesLikeService articlesLikeService;

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

    // 아이디 삭제
    public void softDelete(Users user) {
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        user.setGrade(null);
    }

    // 아이디 완전 삭제
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

    // 회원가입시 아이디 중복체크
    public boolean isLoginIdDuplicate(String loginId) {
        return usersRepository.existsByLoginIdIgnoreCase(loginId);
    }

    // 비밀번호 변경
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        usersRepository.save(user);
        return true;
    }

    // Hot 한 우동
    public Map<Users, Long> getTopUsersByLikes(int limit) {
        Map<Users, Long> topUsersMaps = new LinkedHashMap<>();

        Map<Long, Long> commentsLikeMap = commentsLikeService.countCommentsLikeGroupByUser();
        Map<Long, Long> articlesLikeMap = articlesLikeService.countArticlesLikeGroupByUser();

        Map<Long, Long> sumMap = new HashMap<>();
        commentsLikeMap.forEach((userId, likeCount) -> sumMap.merge(userId, likeCount, Long::sum));
        articlesLikeMap.forEach((userId, likeCount) -> sumMap.merge(userId, likeCount, Long::sum));

        // sumMap을 값 기준으로 내림차순 정렬하여 상위 5개의 userId를 찾고, 이를 Users 객체와 매핑하여 topUsersMaps에 저장
        sumMap.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue())) // 좋아요 수 기준 내림차순 정렬
                .limit(limit) // 상위 5개만
                .forEach(entry -> {
                    Long userId = entry.getKey();
                    Long likeCount = entry.getValue();
                    Users user = findUserById(userId); // userId로 Users 객체 조회
                    topUsersMaps.put(user, likeCount); // 결과 Map에 Users 객체와 좋아요 수 추가
                });

        return topUsersMaps;
    }

    // 등업 요청 버튼을 누르면 등업 isPromotionRequested 값이 true 로 바뀜
    public Boolean requestPromotion(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getIsPromotionRequested()) {
            return false;
        }
        user.setIsPromotionRequested(true);
        usersRepository.save(user);
        return true;
    }
}