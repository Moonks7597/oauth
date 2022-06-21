package io.oauth2.server.config;

import io.oauth2.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import io.oauth2.server.common.jwt.JwtAuthenticationFilter;
import io.oauth2.server.common.jwt.JwtTokenProvider;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String CUSTOM_CONSENT_PAGE_URI = "/consent";

    private static final Logger logger = LogManager.getLogger(SecurityConfig.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer<>();
        authorizationServerConfigurer
                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI));
        RequestMatcher endpointsMatcher = authorizationServerConfigurer
                .getEndpointsMatcher();


//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .requestMatcher(endpointsMatcher)
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin()//.disable()
//                .loginPage("http://localhost:8090/login")
                .loginPage("/loginForm")
                .permitAll()
                .loginProcessingUrl("/loginProcess")
//                .successForwardUrl("/connect/authorize")
//                .loginProcessingUrl("/oauth2/authorize")
//                .defaultSuccessUrl("/oauth2/authorize")
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/errorPage");
//                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
                .and()
                .authorizeRequests()
                .antMatchers("main").authenticated()
                .anyRequest().permitAll()
//                .antMatchers("/user/*").permitAll()
//                .antMatchers("/main").permitAll()
//                .antMatchers("/oauthLogin").permitAll()
//                .antMatchers("/client/*").permitAll()
//                .antMatchers("/favicon.ico", "/errorPage", "/error").permitAll()
//                .antMatchers("/authorization-code").permitAll()
                .and()
                .apply(authorizationServerConfigurer);

        return http.build();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
