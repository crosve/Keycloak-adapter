# Keycloak User Federation Provider

Custom Keycloak user storage provider that integrates with an external REST API.

## Architecture

```
Keycloak → User Federation Provider → External API → MySQL
```

## Build

```bash
mvn clean package
# Output: target/my-provider.jar
```

## Deploy

1. Copy JAR to Keycloak providers:
```bash
cp target/my-provider.jar $KEYCLOAK_HOME/providers/
```

2. Rebuild Keycloak:
```bash
cd $KEYCLOAK_HOME
./bin/kc.sh build
```

3. Configure in Admin UI:
   - Realm Settings → User Federation
   - Create provider: "my-custom-api-provider"
   - Set API Base URL: `http://your-api:3000`

## Implementation Details

### Provider Implements
- **UserStorageProvider** - Base provider interface
- **UserLookupProvider** - User lookups by username/ID/email (called during login)
- **UserRegistrationProvider** - User creation/deletion

### Methods Called by Keycloak During Login
- `getUserByUsername(realm, username)` - Primary login method
- `getUserById(realm, id)` - ID-based lookup
- `getUserByEmail(realm, email)` - Email authentication

### API Endpoints Required
Your external API must provide:
```
GET    /users/{username}      → User object
GET    /users/{userId}        → User object
GET    /users/email/{email}   → User object
POST   /users                 → Create user (return with ID)
PUT    /users/{userId}        → Update user
DELETE /users/{userId}        → Delete user
```

### Expected User Object Format

```json
{
  "userID": 1,
  "username": "john",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "enabled": true,
  "roles": ["user"]
}
```

## Debugging

Check Keycloak logs for:
```
[INFO] getUserByUsername called with username: john
[INFO] Successfully loaded user from API: john
```

## Project Structure

```
src/main/java/com/crosve/keycloak/
├── MyUserProvider.java          # Main provider logic
├── MyUserProviderFactory.java   # Provider factory
├── MyApiClient.java             # External API client
├── MyUserAdapter.java           # Maps API user to Keycloak UserModel
└── model/
    └── MyUser.java              # API user model

src/main/resources/
└── META-INF/services/
    └── org.keycloak.storage.UserStorageProviderFactory  # SPI registration
```

## Key Changes Made

1. ✅ Created service configuration file for provider discovery
2. ✅ Implemented `UserLookupProvider` interface
3. ✅ Added email-based user lookup support
4. ✅ Added comprehensive logging for debugging
5. ✅ JAR successfully builds and includes service file

## Status

✅ Code compiles and builds  
✅ Service configuration file created  
✅ Ready to deploy to Keycloak  

