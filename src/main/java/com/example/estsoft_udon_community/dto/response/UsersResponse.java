package com.example.estsoft_udon_community.dto.response;

import com.example.estsoft_udon_community.dto.LocationDTO;
import com.example.estsoft_udon_community.entity.Users;
import com.example.estsoft_udon_community.enums.Grade;
import java.time.LocalDateTime;

import com.example.estsoft_udon_community.util.DateFormatUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {

    // 이걸 추가 했는데 에러가 추가 했으면 오류가 나려나.
    private Long id;
    private String name;
    private String loginId;
    private String email;
    private String nickname;

    private String grade;

    private String displayName;

    private String createdAt;

    private String updatedAt;

    private String lastLoginAt;

    private LocationDTO location;

    private Boolean isPromotionRequested;

    public UsersResponse(Users user) {
        this.id = user.getId();
        this.name = user.getName();
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.grade = user.getGrade().name();
        this.displayName = user.getGrade().getDisplayName();
        this.createdAt = user.getCreatedAt().format(DateFormatUtil.formatter);
        this.updatedAt = user.getUpdatedAt().format(DateFormatUtil.formatter);
        if(user.getLastLoginAt() != null) {
            this.lastLoginAt = user.getLastLoginAt().format(DateFormatUtil.formatter);
        }
        this.location = new LocationDTO(user.getLocation());
        this.isPromotionRequested = user.getIsPromotionRequested();
    }
}
