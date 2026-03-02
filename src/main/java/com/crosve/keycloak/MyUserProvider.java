//Logic of the provider 

package com.crosve.keycloak;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import java.util.HashMap;
import java.util.Map;

import java.util.UUID;
import org.keycloak.storage.user.UserRegistrationProvider;
import com.crosve.keycloak.model.MyUser;

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
        if (loadedUsers.containsKey(username)) return loadedUsers.get(username);

        // Fetch from API and wrap in our Adapter
        return client.fetchUserByUsername(username)
                .map(user -> {
                    //Wraps around MyUserAdapter in order for keycloak to understand the data 
                    UserModel adapter = new MyUserAdapter(session, realm, model, user);
                    loadedUsers.put(username, adapter);
                    return adapter;
                }).orElse(null);
    }

    @Override
    public UserModel getUserById(RealmModel realm, String id) { 
        return null;
    }

    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        return null; 
    }

    @Override
    public void close() {
        loadedUsers.clear();
    }

    @Override
    public UserModel addUser(RealmModel realm, String username) {
        com.crosve.keycloak.model.MyUser newUser = new com.crosve.keycloak.model.MyUser();
        newUser.setUsername(username);
        
        // CHANGE THIS: Use setUserID, not setId
        newUser.setUserID((int)(Math.random() * 10000)); 

        // This will still error UNTIL you do step 2 below
        com.crosve.keycloak.model.MyUser savedUser = client.createUser(newUser);
        return new MyUserAdapter(session, realm, model, savedUser);
    }


    @Override
    public boolean removeUser(RealmModel realm, UserModel user) {
        return true; 
    }
}