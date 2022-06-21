package io.oauth2.server.authorization.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/connect/authorize")
    public String authorizeSet(@RequestParam("next") String next) {
        return "redirect:" + next;
    }

    @GetMapping("/error")
    public String errorPage(HttpServletResponse response) {
        return "errorPage";
    }

    @GetMapping("/main")
    @ResponseBody
    public String getIndex(@RequestParam(OAuth2ParameterNames.CODE) String code) {
        return code;
    }

    @GetMapping("/errorPage")
    @ResponseBody
    public String errorPage(@RequestParam(OAuth2ParameterNames.CODE) String code) {
        return code;
    }

    @RequestMapping("authorization-code")
    @ResponseBody
    public String authorizationCodeTest(@RequestParam("code") String code) {
        return code;
    }

}