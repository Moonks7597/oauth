package io.oauth2.server.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.oauth2.server.client.domain.Client;

@Repository
public interface ClientRepository  extends JpaRepository<Client, Long> {
    Client findByClientName(String ClientName);
}
