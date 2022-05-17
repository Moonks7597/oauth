package io.oauth2.server.client.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "oauth2_registered_client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private String clientId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "client_id_issued_at")
    private LocalDate clientIdIssuedAt;

    @Column(name = "client_secret")
    private String clientSecret;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "client_secret_expires_at")
    private LocalDate clientSecretExpiresAt;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_authentication_methods")
    private String clientAuthenticationMethods;

    @Column(name = "authorization_grant_types")
    private String authorizationGrantTypes;

    @Column(name = "redirect_uris")
    private String redirectUris;

    @Column(name = "scopes")
    private String scopes;

    @Column(name = "client_settings")
    private String clientSettings;

    @Column(name = "token_settings")
    private String tokenSettings;

    @Builder
    public Client(Long id, String clientId, LocalDate clientIdIssuedAt, String clientSecret,
                  LocalDate clientSecretExpiresAt, String clientName, String clientAuthenticationMethods,
                  String authorizationGrantTypes, String redirectUris, String scopes, String clientSettings,
                  String tokenSettings) {
        this.id = id;
        this.clientId = clientId;
        this.clientIdIssuedAt = clientIdIssuedAt;
        this.clientSecret = clientSecret;
        this.clientSecretExpiresAt = clientSecretExpiresAt;
        this.clientName = clientName;
        this.clientAuthenticationMethods = clientAuthenticationMethods;
        this.authorizationGrantTypes = authorizationGrantTypes;
        this.redirectUris = redirectUris;
        this.scopes = scopes;
        this.clientSettings = clientSettings;
        this.tokenSettings = tokenSettings;
    }

    public void setClient(String clientId, LocalDate clientIdIssuedAt, String clientSecret,
                          LocalDate clientSecretExpiresAt, String clientAuthenticationMethods,
                          String authorizationGrantTypes, String redirectUris,
                          String scopes, String clientSettings, String tokenSettings) {
        this.clientId = clientId;
        this.clientIdIssuedAt = clientIdIssuedAt;
        this.clientSecret = clientSecret;
        this.clientSecretExpiresAt = clientSecretExpiresAt;
        this.clientAuthenticationMethods = clientAuthenticationMethods;
        this.authorizationGrantTypes = authorizationGrantTypes;
        this.redirectUris = redirectUris;
        this.scopes = scopes;
        this.clientSettings = clientSettings;
        this.tokenSettings = tokenSettings;
    }
}
