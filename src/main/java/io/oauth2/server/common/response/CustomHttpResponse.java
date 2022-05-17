//package sngy.oauth.authServer.common.response;
//
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.configurers.openid.OpenIDLoginConfigurer;
//import org.springframework.security.oauth2.server.authorization.web.*;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.access.ExceptionTranslationFilter;
//import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
//import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
//import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
//import org.springframework.security.web.authentication.RememberMeServices;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.logout.LogoutFilter;
//import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
//import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
//import org.springframework.security.web.context.SecurityContextPersistenceFilter;
//import org.springframework.security.web.context.SecurityContextRepository;
//import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
//import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
//import org.springframework.security.web.session.SessionManagementFilter;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//
//public class CustomHttpResponse {
//    AuthenticationManager authenticationManage;
//    RememberMeServices rememberMeServices;
//    String data;
//    SecurityContextRepository securityContextRepository;
//    AuthenticationEntryPoint authenticationEntryPoint;
//    AuthenticationManager authenticationManager;
//    RequestMatcher requestMatcher;
//
//    // Spring Security filter chain 관련 필터
//    SecurityContextPersistenceFilter securityContextPersistenceFilter =
//            new SecurityContextPersistenceFilter();
//    UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter =
//            new UsernamePasswordAuthenticationFilter();
//    DefaultLoginPageGeneratingFilter defaultLoginPageGeneratingFilter =
//            new DefaultLoginPageGeneratingFilter();
//    WebAsyncManagerIntegrationFilter webAsyncManagerIntegrationFilter =
//            new WebAsyncManagerIntegrationFilter();
//    LogoutFilter logoutFilter = new LogoutFilter();
//    RememberMeAuthenticationFilter rememberMeAuthenticationFilter =
//            new RememberMeAuthenticationFilter(authenticationManage, rememberMeServices);
//    SecurityContextHolderAwareRequestFilter securityContextHolderAwareRequestFilter =
//            new SecurityContextHolderAwareRequestFilter();
//    AnonymousAuthenticationFilter anonymousAuthenticationFilter =
//            new AnonymousAuthenticationFilter(data);
//    SessionManagementFilter sessionManagementFilter =
//            new SessionManagementFilter(securityContextRepository);
//    ExceptionTranslationFilter exceptionTranslationFilter =
//            new ExceptionTranslationFilter(authenticationEntryPoint);
//    FilterSecurityInterceptor filterSecurityInterceptor =
//            new FilterSecurityInterceptor();
//    JWKSource<SecurityContext> jwkSource;
//
//    // OAuth2 관련 filter
//    OAuth2AuthorizationEndpointFilter oAuth2AuthorizationEndpointFilter =
//            new OAuth2AuthorizationEndpointFilter(authenticationManager);
//    NimbusJwkSetEndpointFilter nimbusJwkSetEndpointFilter =
//            new NimbusJwkSetEndpointFilter(jwkSource);
//    OAuth2ClientAuthenticationFilter oAuth2ClientAuthenticationFilter =
//            new OAuth2ClientAuthenticationFilter(authenticationManager, requestMatcher);
//    OAuth2TokenEndpointFilter oAuth2TokenEndpointFilter =
//            new OAuth2TokenEndpointFilter(authenticationManager);
//    OAuth2TokenIntrospectionEndpointFilter oAuth2TokenIntrospectionEndpointFilter =
//            new OAuth2TokenIntrospectionEndpointFilter(authenticationManager);
//    OAuth2TokenRevocationEndpointFilter oAuth2TokenRevocationEndpointFilter =
//            new OAuth2TokenRevocationEndpointFilter(authenticationManager);
//
////    LoginUrlAuthenticationEntryPoint
////    OpenIDLoginConfigurer
//
//}
