package com.example.estsoft_udon_community.enums;

import lombok.Getter;

public enum Grade {
    UDON("우동"),
    UDON_FRIEND("우동 친구"),
    UDON_SHERIFF("우동보안관"),
    UDON_MASTER("우동마스터"),
    UDON_ADMIN("우동관리자");

    private final String displayName;

    Grade(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
