package com.tenten.linkhub.domain.space.model.category;

public enum Category {
    ENTER_ART("엔터테인먼트-예술"),
    LIFE_KNOWHOW_SHOPPING("생활-노하우-쇼핑"),
    HOBBY_LEISURE_TRAVEL("취미-여가-여행"),
    KNOWLEDGE_ISSUE_CAREER("지식-이슈-커리어"),
    ETC("기타");

    private final String description;

    Category(String description) {
        this.description = description;
    }

}
