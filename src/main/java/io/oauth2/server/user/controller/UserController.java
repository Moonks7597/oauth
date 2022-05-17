package io.oauth2.server.user.controller;

import io.oauth2.server.user.service.UserService;
import io.oauth2.server.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/join")
    public String userJoin() {
        return "joinForm";
    }

    @PostMapping("/user/signUp")
    public ResponseEntity<?> create(@RequestBody User user, Errors errors){
        return userService.signUp(user);
    }
}
