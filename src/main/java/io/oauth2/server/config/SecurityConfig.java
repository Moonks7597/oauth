package io.oauth2.server.config;

import io.oauth2.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import io.oauth2.server.common.jwt.JwtAuthenticationFilter;
import io.oauth2.server.common.jwt.JwtTokenProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private static final Logger logger = LogManager.getLogger(SecurityConfig.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin()//.disable()
//                .loginPage("http://localhost:8090/login")
                .loginPage("/oauthLogin")
                .permitAll()
                .loginProcessingUrl("/loginProcess")
                .successForwardUrl("/connect/authorize")
//                .loginProcessingUrl("/oauth2/authorize")
//                .defaultSuccessUrl("/oauth2/authorize")
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request,
                                         HttpServletResponse response, AuthenticationException authException)
                            throws IOException, ServletException {
                        response.sendRedirect("/errorPage");
//                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    }
                })
                .and()
                .authorizeRequests()
                .antMatchers("/user/*").permitAll()
                .antMatchers("/main").permitAll()
                .antMatchers("/oauthLogin").permitAll()
                .antMatchers("/client/*").permitAll()
                .antMatchers("/favicon.ico", "/errorPage", "/error").permitAll()
                .antMatchers("/authorization-code").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
