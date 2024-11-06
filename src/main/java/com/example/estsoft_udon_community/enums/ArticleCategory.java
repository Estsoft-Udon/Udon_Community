package com.example.estsoft_udon_community.enums;

public enum ArticleCategory {
    RESTAURANT("맛집"),
    FESTIVITIES("축제"),
    GATHERING("모임"),
    TRAVEL_DESTINATION("여행지"),
    DATE_COURSE("데이트 코스"),
    WALKING_COURSE("산책 코스");

    private final String displayName;

    // 생성자
    ArticleCategory(String displayName) {
        this.displayName = displayName;
    }

    // 디스플레이 이름 반환
    public String getDisplayName() {
        return displayName;
    }

    // 문자열로부터 카테고리 찾기 (대소문자 구분 없음)
    public static ArticleCategory fromString(String text) {
        for (ArticleCategory category : ArticleCategory.values()) {
            if (category.name().equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
