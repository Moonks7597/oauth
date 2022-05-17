package io.oauth2.server.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import io.oauth2.server.common.utils.JwksUtils;

@Configuration(proxyBeanMethods = false)
//@EnableWebSecurity
public class AuthorizationConfig {
    private static final Logger logger = LogManager.getLogger(AuthorizationConfig.class);
//    private static final String CUSTOM_CONSENT_PAGE_URI = "/consent";

    /**
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
//                new OAuth2AuthorizationServerConfigurer<>();
//        authorizationServerConfigurer
//                .authorizationEndpoint(authorizationEndpoint ->
//                        authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI));
//
//        RequestMatcher endpointsMatcher = authorizationServerConfigurer
//                .getEndpointsMatcher();
//
//        http
//                .requestMatcher(endpointsMatcher)
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests.anyRequest().authenticated()
//                )
//                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
//                .apply(authorizationServerConfigurer);
        return http.formLogin(Customizer.withDefaults()).build();
    }

//    @Bean
//    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
//        RegisteredClient registeredClient = RegisteredClient.withId("sngy")
//                .clientId("sngy")
//                .clientSecret("{noop}sngy")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
////                .redirectUri("http://localhost:8080/authorization-code")
//                .redirectUri("http://localhost:8090/main")
////                .scope(OidcScopes.OPENID)
//                .scope("read")
//                .scope("write")
//                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(1))
//                        .refreshTokenTimeToLive(Duration.ofDays(30))
//                        .reuseRefreshTokens(true).build())
////                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//                .build();
//        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
//
//        RegisteredClient dbRegisteredClient = registeredClientRepository.findByClientId("sngy");
//        if (dbRegisteredClient == null) {
//            registeredClientRepository.save(registeredClient);
//        }
//
////        return new InMemoryRegisteredClientRepository(registeredClient);
//        return registeredClientRepository;
//    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
        return registeredClientRepository;
    }

    /**
     *
     * @return
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = JwksUtils.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    /**
     *
     * @param jdbcTemplate
     * @param registeredClientRepository
     * @return
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    /**
     *
     * @param jdbcTemplate
     * @param registeredClientRepository
     * @return
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

//    @Bean
//    public ProviderSettings providerSettings() {
//        return ProviderSettings.builder().issuer("http://localhost:8080").build();
//    }
}
