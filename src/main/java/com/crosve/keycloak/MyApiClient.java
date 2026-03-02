//For api/http requets

package com.crosve.keycloak;

import com.crosve.keycloak.model.MyUser;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.component.ComponentModel;

//Current valid http request for keycloak verion 26.x
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.models.KeycloakSession;

import java.io.IOException;
import java.util.Optional;


@Slf4j
public class MyApiClient {

    private final KeycloakSession session;
    private final String baseUrl;

    public MyApiClient(KeycloakSession session, ComponentModel model) {
        this.session = session;
        // This pulls the "Base URL" setting you define in the Keycloak Admin UI
        this.baseUrl = model.get("userApiBaseUrl"); 
    }

    public Optional<MyUser> fetchUserByUsername(String username) {
        try {
            // Build the request: GET {baseUrl}/users/{username}
            return Optional.of(SimpleHttp.doGet(String.format("%s/users/%s", baseUrl, username), session)
                    .acceptJson()
                    .asJson(MyUser.class)); 
        } catch (IOException e) {
            log.error("Failed to fetch user {} from API: {}", username, e.getMessage());
            return Optional.empty();
        }
    }

    public com.crosve.keycloak.model.MyUser createUser(com.crosve.keycloak.model.MyUser user) {
      
        return user;
    }
}