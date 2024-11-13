package com.example.estsoft_udon_community.dto.request;

import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.Grade;
import com.example.estsoft_udon_community.enums.PasswordHint;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsersRequest {
    private String name;

    private String loginId;

    private String password;
    // 비밀번호 확인 입력 박스
    private PasswordHint passwordHint;
    // 비밀번호 찾기용 문답 입력박스
    private String passwordAnswer;

    private String nickname;

    private String email;

    private Long locationId;

    // 추가된 필드
    private String upperLocation; // UpperLocation 정보

    private String locationName; // Location 이름

    private Boolean isDeleted;
    private LocalDateTime deletedAt;

    // UserRequest -> Users
    public Users convert(Location location) {
        return new Users(loginId,
                password,
                name,
                nickname,
                email,
                Grade.UDON,
                passwordHint,
                passwordAnswer, location);
    }

    public Users updateEntity(Users user) {
        if (loginId != null) {
            user.setLoginId(loginId);
        }
        if (password != null) {
            user.setPassword(password); // Consider encoding the password in UsersService
        }
        if (name != null) {
            user.setName(name);
        }
        if (nickname != null) {
            user.setNickname(nickname);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (passwordHint != null) {
            user.setPasswordHint(passwordHint);
        }
        if (passwordAnswer != null) {
            user.setPasswordAnswer(passwordAnswer);
        }
        if(isDeleted != null) {
            user.setIsDeleted(isDeleted);
        }
        if(deletedAt != null) {
            user.setDeletedAt(deletedAt);
        }

        return user;
    }
}
