package io.oauth2.server.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.oauth2.server.common.response.ErrorCode;
import io.oauth2.server.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.stereotype.Service;
import io.oauth2.server.common.response.RestResponseHandler;
import io.oauth2.server.common.utils.RandomStringCreateUtils;
import io.oauth2.server.model.Client;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>클라이언트 관련 서비스를 담당하는 class</p>
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final String AUTH_METHOD_CLIENT_SECRET_BASIC = "client_secret_basic";
    private final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    private final String GRANT_TYPE_PASSWORD = "password";
    private final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    private final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    private final String SCOPES_READ = "read";
    private final String SCOPES_WRITE = "write";

    private final ClientRepository clientRepository;
    private final RestResponseHandler response;

    /**
     * 해당 클라이언트의 정보를 받아 등록한다.
     *
     * @param client 등록할 유저의 정보를 담은 객체
     * @return 클라이언트 등록 결과 객체
     */
    public ResponseEntity<?> clientRegister(Client client) {
        RandomStringCreateUtils randomStringCreateUtils = new RandomStringCreateUtils();

        // 클라이언트 중복 검사
        Client byClientId = clientRepository.findByClientName(client.getClientName());
        if (byClientId != null) {
            return response.fail(ErrorCode.DUPLICATED_CLIENT);
        }

        // 클라이언트 정보 주입
        client.setClientId(randomStringCreateUtils.getRandomString(32));
        client.setClientIdIssuedAt(Timestamp.valueOf(LocalDateTime.now()));

        String clientSecret = randomStringCreateUtils.getRandomString(32);
        client.setClientSecret("{noop}" + clientSecret);

        client.setClientAuthenticationMethods(AUTH_METHOD_CLIENT_SECRET_BASIC);
        client.setAuthorizationGrantTypes(
                                GRANT_TYPE_REFRESH_TOKEN + "," +
                                GRANT_TYPE_PASSWORD + "," +
                                GRANT_TYPE_CLIENT_CREDENTIALS + "," +
                                GRANT_TYPE_AUTHORIZATION_CODE);
        client.setScopes(SCOPES_READ + "," + SCOPES_WRITE);

        ClientSettings clientSetting = clientSettings();
        client.setClientSettings(writeMap(clientSetting.getSettings()));
        TokenSettings tokenSetting = tokenSettings();
        client.setTokenSettings(writeMap(tokenSetting.getSettings()));

        clientRepository.save(client);

        return response.success();
    }

    /**
     * Client/Token Setting을 Json 형식으로 변환한다
     *
     * @param data Token/Client Setting
     * @return String(Json) Type Token/Client Setting
     */
    private String writeMap(Map<String, Object> data) {
        ObjectMapper objectMapper = new ObjectMapper();

        // Duration 이용시 Date Type 으로 parsing 할 때 에러가 나지 않도록 설정
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Class 정보를 Json 으로 변환시에 내부 Object 정보를 설정하도록 지정
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        objectMapper.setDefaultTyping(stdTypeResolverBuilder);

        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    /**
     * @return Default Client Setting
     */
    private ClientSettings clientSettings(){
        return ClientSettings.builder()
                .requireProofKey(false)
                .requireAuthorizationConsent(false)
                .build();
    }

    /**
     * @return Default Token Setting
     *         Access Token 유효기간 1시간
     *         Refresh Token 유효기간 30일
     */
    private TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .reuseRefreshTokens(true)
                .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                .accessTokenTimeToLive(Duration.ofHours(1))
                .refreshTokenTimeToLive(Duration.ofDays(30))
                .build();
    }
}
