package io.oauth2.server.controller;

import io.oauth2.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.oauth2.server.model.User;

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
