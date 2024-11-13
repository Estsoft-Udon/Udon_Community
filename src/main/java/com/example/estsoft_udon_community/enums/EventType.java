package com.example.estsoft_udon_community.enums;

public enum EventType {
    FESTIVAL("축제"),
    GATHERING("소모임");

    private final String displayName;

    EventType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
