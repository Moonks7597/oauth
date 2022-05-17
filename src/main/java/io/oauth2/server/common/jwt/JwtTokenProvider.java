package io.oauth2.server.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.oauth2.server.common.dto.TokenDto;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LogManager.getLogger(JwtTokenProvider.class);

    private Key secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(JwtProperties.TOKEN_SECRET.getBytes());
    }

    public TokenDto.TokenInfo generateToken(Authentication authentication) {
        // 권한 취득
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();

        // Access Token 작성
        Date accessExpiration = new Date(now.getTime() + JwtProperties.ACCESS_TOKEN_VALID_TIME);
        String accessToken =  Jwts.builder()
                .setSubject(authentication.getName())
                .claim(JwtProperties.AUTHORITIES_KEY, authorities)
                .setExpiration(accessExpiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 작성
        Date refreshExpiration = new Date(now.getTime() + JwtProperties.REFRESH_TOKEN_VALID_TIME);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshExpiration)
                .signWith(secretKey,  SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.TokenInfo.builder()
                .grantType(JwtProperties.BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(JwtProperties.REFRESH_TOKEN_VALID_TIME)
                .build();
    }

    public boolean isTokenValid(String jwtToken) {
        try {
            // 토큰 검증 처리
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명입니다.");
            return false;
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰입니다.");
            return false;
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다.");
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못되었습니다.");
            return false;
        }
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(JwtProperties.AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(JwtProperties.AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey)
                        .build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(secretKey)
                            .build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
