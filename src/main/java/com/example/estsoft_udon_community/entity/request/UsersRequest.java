package com.example.estsoft_udon_community.entity.request;

import com.example.estsoft_udon_community.entity.Location;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.Grade;
import com.example.estsoft_udon_community.enums.PasswordHint;
import lombok.Data;

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

    private Location location;

    public Users convert() {
        return new Users(loginId, password, name, nickname, email, Grade.UDON, passwordHint, passwordAnswer, location);
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
        return user;
    }
}
