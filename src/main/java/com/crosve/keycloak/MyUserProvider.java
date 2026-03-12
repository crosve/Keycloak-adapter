//Logic of the provider 

package com.crosve.keycloak;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import java.util.HashMap;
import java.util.Map;

import java.util.UUID;
import org.keycloak.storage.user.UserRegistrationProvider;
import com.crosve.keycloak.model.MyUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyUserProvider implements UserStorageProvider, UserLookupProvider, UserRegistrationProvider {

    private final KeycloakSession session;
    private final ComponentModel model;
    private final MyApiClient client;
    private final Map<String, UserModel> loadedUsers = new HashMap<>();

    public MyUserProvider(KeycloakSession session, ComponentModel model, MyApiClient client) {
        this.session = session;
        this.model = model;
        this.client = client;
    }

    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
        log.info("getUserByUsername called with username: {}", username);
        if (loadedUsers.containsKey(username)) return loadedUsers.get(username);

        // Fetch from API and wrap in our Adapter
        return client.fetchUserByUsername(username)
                .map(user -> {
                    //Wraps around MyUserAdapter in order for keycloak to understand the data 
                    UserModel adapter = new MyUserAdapter(session, realm, model, user);
                    loadedUsers.put(username, adapter);
                    log.info("Successfully loaded user from API: {}", username);
                    return adapter;
                }).orElse(null);
    }

    @Override
    public UserModel getUserById(RealmModel realm, String id) {
        log.info("getUserById called with id: {}", id);
        StorageId storageId = new StorageId(id);
        String externalId = storageId.getExternalId(); // your userID as string

        return client.fetchUserById(externalId)
                .map(u -> {
                    UserModel adapter = new MyUserAdapter(session, realm, model, u);
                    log.info("Successfully loaded user by id from API: {}", externalId);
                    return adapter;
                })
                .orElse(null);
    }

    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        log.info("getUserByEmail called with email: {}", email);
        return client.fetchUserByEmail(email)
                .map(u -> {
                    UserModel adapter = new MyUserAdapter(session, realm, model, u);
                    log.info("Successfully loaded user by email from API: {}", email);
                    return adapter;
                })
                .orElse(null);
    }

    @Override
    public void close() {
        loadedUsers.clear();
    }

    @Override
    public UserModel addUser(RealmModel realm, String username) {
        log.info("addUser called with username: {}", username);
        com.crosve.keycloak.model.MyUser newUser = new com.crosve.keycloak.model.MyUser();
        newUser.setUsername(username);
        
        // CHANGE THIS: Use setUserID, not setId
        newUser.setUserID((int)(Math.random() * 10000)); 

        // createUser returns Optional<MyUser> -> unwrap it
        com.crosve.keycloak.model.MyUser savedUser = client.createUser(newUser).orElse(null);
        if (savedUser == null) {
            log.error("Failed to create user in API: {}", username);
            return null;
        }

        log.info("Successfully created user in API: {}", username);
        return new MyUserAdapter(session, realm, model, savedUser);
    }

    @Override
    public boolean removeUser(RealmModel realm, UserModel user) {
        return true; 
    }
}