package io.oauth2.server.user.service;

import io.oauth2.server.common.response.ErrorCode;
import io.oauth2.server.user.repository.UserRepository;
import io.oauth2.server.user.model.PrincipalUserDetails;
import io.oauth2.server.user.model.Roles;
import io.oauth2.server.user.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.oauth2.server.common.response.RestResponseHandler;


/**
 * <p>유저 관련 서비스</p>
 *
 * 회원가입 서비스
 * 등록된 유저 확인 서비스
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RestResponseHandler response;

    /**
     * 해당 유저의 정보를 받아 등록한다.
     *
     * @param user 등록할 유저의 정보를 담은 객체
     * @return 회원가입 결과 응답 객체
     */
    public ResponseEntity<?> signUp(User user) {
        BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
        User byUsername = userRepository.findByUsername(user.getUsername());

        if (byUsername != null) {
            return response.fail(ErrorCode.DUPLICATED_ACCOUNT);
        }

        user.setPassword("{bcrypt}" + pwEncoder.encode(user.getPassword()));
        user.setRole(Roles.ROLE_ADMIN);

        userRepository.save(user);
        return response.success();
    }

    /**
     * 입력된 유저 ID를 통해 해당 유저가 존재하는지 확인한다.
     *
     * @param username 유저의 ID
     * @return 취득한 유저정보의 객체
     * @throws UsernameNotFoundException 미등록 회원 에러
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.debug("등록되지 않은 유저입니다 {userName : " + username + "}", UsernameNotFoundException.class);
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User
                (user.getUsername(), user.getPassword(), new PrincipalUserDetails(user).getAuthorities());
    }
}
