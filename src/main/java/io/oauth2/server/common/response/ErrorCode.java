package io.oauth2.server.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // SUCCESS
    PROCESS_SUCCESS(200, "유효하지 않은 입력값입니다.", "000"),

    // Error
    INVALID_INPUT_VALUE(400, "유효하지 않은 입력값입니다.", "E001"),
    DATA_NOT_FOUND(400, "데이터가 존재하지 않습니다.", "E002"),
    INVALID_INPUT_ID(400, "등록되지 않은 아이디 입니다.", "E003"),
    INVALID_INPUT_PW(400, "올바르지 않은 비밀번호 입니다.", "E004"),
    DUPLICATED_ACCOUNT(400, "이미 등록된 ID 입니다.", "E005"),
    DUPLICATED_CLIENT(400, "이미 등록된 클라이언트 입니다.", "E006"),
    AUTH_URI_NOT_FOUND(400, "올바르지 않은 접속 경로입니다.", "E101"),
    ACCESS_DENIED(403, "접근이 거부 되었습니다.", "E102"),
    SITE_NOT_FOUND(404, "해당 사이트를 찾을 수 업습니다.", "E103"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", "E900"),
    EXIT_ERROR(500, "Internal Server Error", "E900");

    private final int state;
    private final String message;
    private final String code;

}
