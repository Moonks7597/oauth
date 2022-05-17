package io.oauth2.server.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


public class TokenDto {
    @Builder
    @AllArgsConstructor
    @Data
    public static class TokenInfo {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }
}
