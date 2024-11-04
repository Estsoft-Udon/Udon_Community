package com.example.estsoft_udon_community.enums;

public enum UpperLocationEnum {
    서울특별시("서울특별시"),
    부산광역시("부산광역시"),
    대구광역시("대구광역시"),
    인천광역시("인천광역시"),
    광주광역시("광주광역시"),
    대전광역시("대전광역시"),
    울산광역시("울산광역시"),
    세종시("세종시"),
    경기도("경기도"),
    강원도("강원도"),
    충청북도("충청북도"),
    충청남도("충청남도"),
    전라북도("전라북도"),
    전라남도("전라남도"),
    경상북도("경상북도"),
    경상남도("경상남도"),
    제주도("제주도");

    private final String name;

    UpperLocationEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static UpperLocationEnum fromString(String name) {
        for (UpperLocationEnum upperLocation : UpperLocationEnum.values()) {
            if (upperLocation.getName().equalsIgnoreCase(name)) {
                return upperLocation;
            }
        }
        throw new IllegalArgumentException("No constant with name " + name + " found");
    }
}
