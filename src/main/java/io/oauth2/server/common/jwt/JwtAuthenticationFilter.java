package io.oauth2.server.common.jwt;


import io.oauth2.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {  // OncePerRequestFilter

    private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String token = resolveToken((HttpServletRequest) request);
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (jwtTokenProvider.isTokenValid(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");

        String nextUri = httpServletRequest.getParameter("next");

        System.out.println("password = " + password);
        System.out.println("username = " + username);

//        request.setAttribute("redirect_uri", nextUri);

        if (username != null) {
//            User byUsername = userRepository.findByUsername(username);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest)request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        chain.doFilter(request, response);
    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String token = resolveToken(request);
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//        System.out.println("password = " + password);
//        System.out.println("username = " + username);
//
//        if (jwtTokenProvider.isTokenValid(token)) {
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            String name = authentication.getName();
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        filterChain.doFilter(request, response);
//    }

    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(JwtProperties.AUTHORITIES_KEY);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProperties.BEARER_TYPE)) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
