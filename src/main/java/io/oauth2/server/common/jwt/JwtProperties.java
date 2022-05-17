package io.oauth2.server.common.jwt;

public interface JwtProperties {

    // JWT의 발급자 URL
    final String TOKEN_ISS = "https://sngy.io";

    // JWT의 발급 대상자
    final String TOKEN_AUD = "https://localhost:8080/main";

    // Access Token 유효 기간 (30분)
    final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 30;

    // Refresh Token 유효 기간 (1개월)
    final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 30;

    // Client ID
    final String CLIENT_ID = "sngy";

    // Client Secret
    final String CLIENT_SECRET = "sngy";

    // Header에 보관되어 있는 토큰 name
    final String HEADER_STRING = "Authorization";

    // 토큰 발급에 사용되는 비밀키
    final String TOKEN_SECRET = "sngySecret123456789012345678901234567890";

    final String AUTHORITIES_KEY = "auth";

    final String BEARER_TYPE = "Bearer";
}
