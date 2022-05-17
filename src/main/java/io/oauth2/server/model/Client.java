package io.oauth2.server.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "oauth2_registered_client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "client_id")
    private String clientId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "client_id_issued_at")
    private java.util.Date clientIdIssuedAt;

    @Column(name = "client_secret")
    private String clientSecret;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "client_secret_expires_at")
    private java.util.Date clientSecretExpiresAt;

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

}
