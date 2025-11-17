# Exercise1-Frontend-App-With-OIDC-Integration
This project contains two Spring Boot microservices:

frontend-app → Web UI (OIDC login using Keycloak)

calendar-service → Protected REST API (requires valid OAuth2 token from Keycloak)

Both services integrate with Keycloak using the OpenID Connect (OIDC) protocol.

# Local set up guide

* Install Java 21 and keycloak on your machine
* Active `standalone` profile by setting variable `SPRING_PROFILES_ACTIVE=standalone`

# Implement Features

1. Keycloak Login (Frontend)
   Users authenticate via Keycloak using the Authorization Code flow.

2. Role-Based Access (my-role)
   Only users with Keycloak role: `my-role` can access `/calendar`. Authorization logic is externalized: `app.security.required-role: my-role`

# Keycloak setup
1. create Realm
   Realm name: demo
2. create Users
   username: testuser, set a password in credentials tab
   (create user `mfauser` as well for future mfa flow testing)
3. create Roles
   go to Realm Roles → Create Role, and then create `my-role`. Assign it to user: Users → testuser → Role Mappings → Assign Role → my-role
   (creat `mfa-role` and assign it to user mfauser to test mfa flow)
4. create Client
   Clients → Create, set the client id as `frontend-app`, and then copy the secret keycloak generated to application config.

