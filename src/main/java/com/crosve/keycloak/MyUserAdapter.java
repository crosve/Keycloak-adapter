package com.crosve.keycloak;

import com.crosve.keycloak.model.MyUser;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

public class MyUserAdapter extends AbstractUserAdapterFederatedStorage {
    private final MyUser user;

    public MyUserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, MyUser user) {
        super(session, realm, model);
        this.user = user;
        this.storageId = new StorageId(storageProviderModel.getId(), String.valueOf(user.getUserID()));
    }

    // ======================
    // GETTERS
    // ======================
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    // ======================
    // SETTERS
    // ======================
    @Override
    public void setUsername(String username) {
        user.setUsername(username);
    }

    @Override
    public void setEmail(String email) {
        user.setEmail(email);
    }

    @Override
    public void setFirstName(String firstName) {
        user.setFirstName(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        user.setLastName(lastName);
    }

    // Getter for MyUser object (for MyUserProvider to call updateUser)
    public MyUser getUser() {
        return user;
    }

}