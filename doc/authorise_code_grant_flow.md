## Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

## Authorization configuration
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().authenticated()
                )
                .oauth2Login((login) -> login
                        .redirectionEndpoint((endpoint) -> endpoint
                                .baseUri("/login/oauth2/callback")
                        ));

        return  httpSecurity.build();
    }
}
```
## YAML configuration
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-id: klight-api-gateway-admin-connect
            client-secret: HDYMxWu2G2VWn5u59MJKwECIBd6VTO7E
            scope: openid
            authorization-grant-type: authorization_code
            redirect-uri: http://localhost:8082/login/oauth2/callback
            issuer-uri: http://localhost:8080/realms/klight-api-gateway
        provider:
          keycloak:
            authorization-uri: "http://localhost:8080/realms/klight-api-gateway/protocol/openid-connect/auth"
            token-uri: "http://localhost:8080/realms/klight-api-gateway/protocol/openid-connect/token"
            user-info-uri: "http://localhost:8080/realms/klight-api-gateway/protocol/openid-connect/userinfo"
            user-info-authentication-method: "header"
            jwk-set-uri: "http://127.0.0.1:8080/realms/klight-api-gateway/protocol/openid-connect/certs"
            user-name-attribute: "name"
```

## User information
```java
@RestController
@RequestMapping("/admin/users")
public class UserController {

    @RequestMapping("/info")
    @ResponseBody
    public String oauthUserInfo(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                                @AuthenticationPrincipal OAuth2User oauth2User) {
        return
                "User Name: " + oauth2User.getName() + "<br/>" +
                        "User Authorities: " + oauth2User.getAuthorities() + "<br/>" +
                        "Client Name: " + authorizedClient.getClientRegistration().getClientName() + "<br/>" +
                        this.prettyPrintAttributes(oauth2User.getAttributes());
    }

    private String prettyPrintAttributes(Map<String, Object> attributes) {
        StringBuilder acc = new StringBuilder("User Attributes: <br/><div style='padding-left:20px'>");
        for (String key : attributes.keySet()) {
            Object value = attributes.get(key);
            acc.append(String.format("<div>%s:&nbsp%s</div>", key, value.toString()));
        }
        return acc.append("</div>").toString();
    }
}
```