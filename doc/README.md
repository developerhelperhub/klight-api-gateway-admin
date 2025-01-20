# Spring Boot 3.4.1 Keycloak introspect and method level security

This section outlines the process of implementing the Keycloak introspection flow in Spring Boot version 3.4.1 and demonstrates how to enable method-level security based on roles configured in Keycloak.
## Objective
I am developing a project that provides APIs for creating services for the microservice-based [Klight API gateway](https://github.com/developerhelperhub/klight-api-gateway), which I built using the OpenResty web application. This API gateway delivers high performance compared to a Spring Boot API gateway because the OpenResty framework is built on top of the NGINX server and uses Lua, a language commonly used in game programming.

You might wonder why Kong API Gateway wasn't used. The reason is that the open-source version of Kong does not support OpenID Connect and pay to integrate with OpenID Connect. When building high-performance applications in a microservice architecture, it is crucial to choose an API gateway optimized for performance.
* [Klight API Documentation](https://github.com/developerhelperhub/klight-api-gateway/wiki)
* [Klight Development Board](https://github.com/users/developerhelperhub/projects/4)

## Integration
* Spring Boot 3.4.1
* Java 22
* Keycloak Integration and RBAC implementation
* Spring Oauth2 Resource Server and Method Level Security
* MongoDB NoSQL DB and Auditing

## Installing Dependencies Services
The following services are required dependencies before starting the Spring Boot application.
### Keycloak Installation in Docker
Execute the following commands to start the Keycloak service in a Docker container. The admin username and password are set to "admin" and "admin," respectively.
```shell
docker run -d --name klight-authentication-server -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:26.0 start-dev
```
### MongoDB installation in Docker

Run the following commands to start the MongoDB service in a Docker container. The root username and password are both set to `klight-api-gateway`. After installing MongoDB, you need to create a database named "klight-api-gateway" to enable connection with the Spring Boot application, as this database is referenced in the `application-dev.yaml` file.
```shell
docker run --name klight-api-gateway-mongo -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=klight-api-gateway -e MONGO_INITDB_ROOT_PASSWORD=klight-api-gateway -d amd64/mongo:8.0.3
```

## Spring Security Dependencies
The following dependencies must be configured to enable the `introspection` flow. The admin service is responsible for validating tokens with the Keycloak service. A token must be generated from Keycloak and included in the `Authorization` header of the API request. 
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```
## Keycloak configuration in YAML file
We have to configure the following attributes in the YAML file. This configuration uses in the `SecurityConig.java` class to configure the introspection flow.
```yaml
keycloak:
  introspection-uri: http://127.0.0.1:8080/realms/klight-api-gateway/protocol/openid-connect/token/introspect
  client-id: klight-api-gateway-admin-connect
  client-secret: HDYMxWu2G2VWn5u59MJKwECIBd6VTO7E
```
## Security Configuration
The following attributes must be configured in the YAML file. This configuration is utilized in the `SecurityConfig.java` class to set up the introspection flow.
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Value("${keycloak.introspection-uri}")
    private String introspectionUrl;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Autowired
    private ExceptionAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private ExcetionAuthenticationEntryPoint entryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, OpaqueTokenAuthenticationConverter converter) throws Exception {


        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->
                        auth.anyRequest().authenticated())
                .oauth2ResourceServer(auth->
                        auth.opaqueToken(op->
                                op.introspectionUri(this.introspectionUrl)
                                        .introspectionClientCredentials(
                                                this.clientId,
                                                this.clientSecret)
                                        .authenticationConverter(converter)
                                )

                                .authenticationEntryPoint(entryPoint.handler())
                                .accessDeniedHandler(accessDeniedHandler.handler())
                                )
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(entryPoint.handler())
                                .accessDeniedHandler(accessDeniedHandler.handler()))
        ;

        return httpSecurity.build();
    }

}
```
The key configurations in the above setup are as follows:

* Enable method-level security in the Spring Boot application using `@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)`.
* Implement the `OpaqueTokenAuthenticationConverter` converter to process `realm_access` and `resource_access` from the token and create a list of `GrantedAuthority` based on the roles configured in the Keycloak service.
* Disable `csrf` protection for the REST API service.
* Authenticate all incoming requests using `authorizeHttpRequests`.
* Configure the introspection flow with `oauth2ResourceServer`.
* Handle authentication exceptions using `authenticationEntryPoint`.
* Set up an access denied handler with `accessDeniedHandler`.

## Method Level Configuration
We are securing the functionality of the `ApiService.java` methods using the following configuration. The `@PreAuthorize("hasAuthority('ROLE_ADMIN')")` annotation should be added to service class methods to validate role permissions. We should mention `ROLE_ADMIN` role name in the annotation which we have created in the Keycloak service.
```java
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public void create(ApiServiceRequest request) {
    log.info("Service creating");

    ApiServiceCreate service = createFactory.getObject();

    map(service, request);

    service.create();

}
```
## Keycloak Configuration
Following configurations need to setup in the Keycloak service. We have to login into Keycloak `http://localhost:8080` in the browser with admin user and password.
### Create the Realm `klight-api-gateway`
Keycloak supports multi-tenancy, enabling us to create application-specific configurations through realm setup. For the `Klight API Gateway`, we maintain a dedicated realm. This realm, we are using to maintain the clients, user, group and roles.
* Click on the "Keycloak master" realm in the left corner.
* Click the "Create realm" button.
* Enter the realm name as "klight-api-gateway."
* Enable the realm by toggling the "Enabled" option.
* Click the "Create" button.

**Note**: Ensure that the "klight-api-gateway" realm is selected before configuring anything, as the default realm is set to "master."

### Create the Client `klight-api-gateway-admin-connect`
Following setup to create new client. This client is support OpenID connection where admin service can connect the Keycloak server through this client.

* Click "Clients" in the left sidebar menu.
* Click the "Create Client" button.
* Configure the following "General Settings":
  * Set the client type to "OpenID Connect"
  * Enter the Client ID: `klight-api-gateway-admin-connect`.
  * Enter the Client Name: `Klight API Gateway Admin Connect`
  * Click the "Next" button.
* Configure the following "Capability Configuration":
  * Enable "Client Authentication"
  * Select only the "Direct access grants" flow. This flow allows generating tokens using the "username" and "password" method, which is ideal for trusted clients such as mobile apps or backend services.
  * Click the "Next" button.
* Configure the "Login Settings":
  * No additional configuration is needed here for now.
* Click the "Save" button.
* After the client is created, select the "Credentials" tab.
  * Set the "Client Authenticator" to "Client ID and Secret"
  * Click "Generate" to create a new "Client Secret."
* Select the "Roles" tab
  * Click the "Create Role" button
  * Enter the role name `ROLE_ADMIN`, this is the role name is configuring in the method level
  * Click "Save" button

**Note**: This client id and client secret are configured in the `application-dev.yaml` file

We have to create the `ROLE_ADMIN` in the client `Roles` tab

### Create the User
In this flow, we need to create a new user to authenticate the server.
* Click "Users" in the left sidebar menu.
* Click the "Create new user" button.
* Enable the "Email Verified" option.
* Enter the following "General" information:
  * Username: "api-admin"
  * Email: "my-user@test.com"
  * First Name: "my"
  * Last Name: "user"
* Click the "Create" button.
* Select the user `api-admin' from the user list to configure password and role
* Click the "Credentials" tab.
  * Click the "Set password" button.
  * Enter "test" for both the "Password" and "Password Confirmation" fields.
  * Set "Temporary" to Off.
  * Click the "Save" button.
  * Click the "Save password" button.
* Click the "Role Mapping" tab
  * Click the "Assign Role" button
  * Search the `ROLE_ADMIN` in the search box
  * Select the check box of `ROLE_ADMIN`
  * Click on the `Assign` button

## Testing Endpoint
Once configuration done, we can test the API to test the authentication and authorization

### Get the access token
Run the following command to get the access token from Keycloak
```shell
curl --location 'http://127.0.0.1:8080/realms/klight-api-gateway/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_id=klight-api-gateway-admin-connect' \
--data-urlencode 'client_secret=HDYMxWu2G2VWn5u59MJKwECIBd6VTO7E' \
--data-urlencode 'scope=openid' \
--data-urlencode 'username=api-admin' \
--data-urlencode 'password=test'
```
### Test endpoint of admin service
Run the following command to test the endpoint to create the service
```shell
curl --location 'http://localhost:8082/admin/services' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiI...' \
--header 'Cookie: JSESSIONID=7B6F7EB454EF0392D6EB21658A922900' \
--data '{
    "name": "Test Service",
    "host": "localhost",
    "path": "test-service",
    "protocol": "https"
}'
```

## Other Coding Pattern 
This source contains following coding pattern implemented as well
* Single Responsible of business logic for service `ApiService.java`, this help to maintain the readability, maintainability, reuse this business this class in different functionality, for example 
  * `ApiServiceCreate.java` for creating the service
  * `ApiServiceUpdate.java` for creating the service
* `@EnableMongoAuditing` enabled auditing `MongoDbConfig.java`
  * CreatedDate, LastModifiedDate, CreatedBy, LastModifiedBy
* Added the logs with help `Slf4j` for debugging purpose
* Maintain the unique error code for exception, implemented global exception handling `ExceptionControllerAdvice.java`

## References
* https://spring.io/guides/gs/accessing-data-mongodb
* https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/
* https://howtodoinjava.com/spring-boot/oauth2-login-with-keycloak-and-spring-security/
* https://spring.io/blog/2024/11/24/bootiful-34-security
* https://docs.spring.io/spring-boot/reference/web/spring-security.html
* https://developer.okta.com/blog/2019/06/20/spring-preauthorize
* https://spring.io/blog/2024/11/24/bootiful-34-security
* https://docs.spring.io/spring-security/reference/reactive/oauth2/resource-server/bearer-tokens.html
* https://docs.spring.io/spring-data/mongodb/reference/data-commons/auditing.html

