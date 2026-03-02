//Creator of the client 

package com.crosve.keycloak;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.List;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.broker.provider.util.SimpleHttp;
//Factory will give keycloak access to the provider 
public class MyUserProviderFactory implements UserStorageProviderFactory<MyUserProvider> {

    public static final String PROVIDER_ID = "my-custom-api-provider";

    @Override
    public MyUserProvider create(KeycloakSession session, ComponentModel model) {
        // Here is where you initialize your API client and pass it to the provider
        MyApiClient client = new MyApiClient(session, model);
        return new MyUserProvider(session, model, client);
    }

    @Override
    public String getId() {
        return PROVIDER_ID; // This name shows up in the Keycloak Admin UI
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return ProviderConfigurationBuilder.create()
        .property("userApiBaseUrl", "API Base URL", "The URL of your external user REST API", 
                ProviderConfigProperty.STRING_TYPE, "http://my-api:3000", null)
        .build();
    }
}