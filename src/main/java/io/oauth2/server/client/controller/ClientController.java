package io.oauth2.server.client.controller;

import io.oauth2.server.client.dto.ClientDto;
import io.oauth2.server.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.oauth2.server.client.domain.Client;
import io.oauth2.server.client.service.ClientService;

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
    public ResponseEntity<?> create(@ModelAttribute ClientDto clientDto){
        Client client = clientDto.toEntity();
        return clientService.clientRegister(client);
    }

    @GetMapping("/client/info")
    public String clientInfo(@RequestBody ClientDto clientDto, Model model) {
        Client byClientName = clientRepository.findByClientName(clientDto.getClientName());
        model.addAttribute("client", byClientName);
        return "clientInfo";
    }
}
