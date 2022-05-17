package io.oauth2.server.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.oauth2.server.client.domain.Client;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientDto implements Serializable {

    private String clientName;

    @Builder
    public ClientDto(String clientName) {
        this.clientName = clientName;
    }

    public Client toEntity() {
        return Client.builder()
                .clientName(this.clientName)
                .build();
    }
}
