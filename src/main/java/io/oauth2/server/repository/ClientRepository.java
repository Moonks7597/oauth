package io.oauth2.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.oauth2.server.model.Client;

@Repository
public interface ClientRepository  extends JpaRepository<Client, Long> {
//    public Client findByClientId(String clientId);
    public Client findByClientName(String ClientName);
}
