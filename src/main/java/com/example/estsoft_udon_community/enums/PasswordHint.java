package com.example.estsoft_udon_community.enums;

public enum PasswordHint {
    FAVORITE_COLOR("좋아하는 색상은?"),
    PET_NAME("애완동물의 이름은?"),
    BIRTH_CITY("태어난 도시 이름은?"),
    MOTHER_MAIDEN_NAME("어머니의 성함은?"),
    FAVORITE_FOOD("가장 좋아하는 음식은?");

    private final String hintMessage;

    PasswordHint(String hintMessage) {
        this.hintMessage = hintMessage;
    }

    public String getHintMessage() {
        return hintMessage;
    }
}
