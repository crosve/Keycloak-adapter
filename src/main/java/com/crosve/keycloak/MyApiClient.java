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
            return Optional.ofNullable(SimpleHttp.doGet(String.format("%s/users/%s", baseUrl, username), session)
                    .acceptJson()
                    .asJson(MyUser.class));
        } catch (IOException e) {
            log.error("Failed to fetch user {} from API: {}", username, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<MyUser> fetchUserById(String userId) {
        try {
            return Optional.ofNullable(SimpleHttp.doGet(String.format("%s/users/%s", baseUrl, userId), session)
                    .acceptJson()
                    .asJson(MyUser.class));
        } catch (IOException e) {
            log.error("Failed to fetch user id {} from API: {}", userId, e.getMessage());
            return Optional.empty();
        }
    }

    // CREATE
    public Optional<MyUser> createUser(MyUser user) {
        try {
            MyUser createdUser = SimpleHttp
                    .doPost(String.format("%s/users", baseUrl), session)
                    .json(user)
                    .acceptJson()
                    .asJson(MyUser.class);

            return Optional.ofNullable(createdUser);

        } catch (IOException e) {
            log.error("Failed to create user {}: {}", user.getUsername(), e.getMessage());
            return Optional.empty();
        }
    }

    // UPDATE
    public boolean updateUser(MyUser user) {
        try {
            SimpleHttp
                    .doPut(String.format("%s/users/%d", baseUrl, user.getUserID()), session)
                    .json(user)
                    .asResponse();

            return true;

        } catch (IOException e) {
            log.error("Failed to update user {}: {}", user.getUsername(), e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteUser(int userId) {
        try {
            SimpleHttp
                    .doDelete(String.format("%s/users/%d", baseUrl, userId), session)
                    .asResponse();

            return true;

        } catch (IOException e) {
            log.error("Failed to delete user {}: {}", userId, e.getMessage());
            return false;
        }
    }
}