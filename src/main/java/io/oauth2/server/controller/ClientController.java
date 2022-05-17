package io.oauth2.server.controller;

import io.oauth2.server.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.oauth2.server.model.Client;
import io.oauth2.server.service.ClientService;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientRepository clientRepository;
    BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    @GetMapping("/client/join")
    public String clientJoin() {
        return "clientForm";
    }

    @PostMapping("/client/register")
    public ResponseEntity<?> create(@RequestBody Client client){
        return clientService.clientRegister(client);
    }

    @GetMapping("/client/info")
//    @ResponseBody
    public String clientInfo(@RequestBody Client client, Model model) {
        Client byClientName = clientRepository.findByClientName(client.getClientName());
        byClientName.setClientSecret(byClientName.getClientSecret());
        model.addAttribute("client", byClientName);
        return "clientInfo";
    }
}
