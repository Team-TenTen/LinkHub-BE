package com.tenten.linkhub.global.response;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //global
    INTERNAL_SERVER_ERROR("G001", "Internal Server Error"),
    UNAUTHENTICATED_ERROR("G002", "Requires Authentication"),
    NOT_SUPPORTED_HTTP_MEDIA_TYPE("G003", "요청된 MediaType은 지원하지 않습니다."),
    INVALID_USER_INPUT("G004", "클라이언트로 부터 지원하지 않는 파라미터 타입이 들어왔습니다."),

    //s3 - imageFile
    FAIL_TO_UPLOAD_IMAGES("I001", "이미지를 업로드 할 수 없습니다."),
    FAIL_TO_DELETE_IMAGE("I002", "이미지를 삭제할 수 없습니다."),

    //member
    DUPLICATE_NEWS_EMAIL("M001", "중복된 이메일입니다."),
    DUPLICATE_FOLLOWING("M002", "중복된 팔로잉입니다."),

    //space
    SPACE_OWNER_LEAVE_ATTEMPT("S001", "스페이스의 주인은 스페이스를 나갈 수 없습니다. 나가기 대신 스페이스 삭제를 진행해야 합니다."),
    DUPLICATE_SPACE_MEMBER("S002", "해당 멤버는 이미 스페이스의 멤버입니다."),
    SPACE_SCRAP_LIMIT("S003", "한 스페이스에 대한 가져오기는 1회만 가능합니다."),
    LINK_COUNT_LIMIT_FOR_SCRAP("S004", "가져오기는 200개 이하의 Link를 가진 스페이스만 가능합니다."),

    //favorite
    DUPLICATE_FAVORITE("F001", "중복된 즐겨찾기 등록입니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
